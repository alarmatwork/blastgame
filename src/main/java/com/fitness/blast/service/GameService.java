package com.fitness.blast.service;

import com.fitness.blast.dao.Game;
import com.fitness.blast.dao.Point;
import com.fitness.blast.dao.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GameService {
    public static final int RADIUS_FROM_POINT = 100;
    Set<Game> activeGames = new HashSet<>();


    public Game registerGame(User user1, User user2) {

        Optional<Game> existingGame = findGameBetweenUsers(user1, user2);
        if (existingGame.isPresent()) {
            log.info("Returning existing game" + existingGame.get());
            return existingGame.get();
        }

        Game newGame = new Game(UUID.randomUUID(), user1, user2, new HashSet<>(), new HashSet<>(), false);
        activeGames.add(newGame);
        log.info("Registered  new game: " + newGame);
        return newGame;
    }

    public Game ping(User user, Double lat, Double lon) {
        Optional<Game> gameOfUser1 = findGameForUser1(user);
        Optional<Game> gameOfUser2 = findGameForUser2(user);

        if (gameOfUser1.isPresent()) {
            log.info("Updating User1 rewardPoints");
            Set<Point> user1Points = gameOfUser1.get().getUser1Points();
            isPointCollected(lat, lon, user1Points);
            isGameOver(gameOfUser1.get());
            return gameOfUser1.get();
        } else if (gameOfUser2.isPresent()) {
            log.info("Updating User2 rewardPoints");
            isPointCollected(lat, lon, gameOfUser2.get().getUser2Points());
            isGameOver(gameOfUser2.get());

            return gameOfUser2.get();
        } else {
            return new Game(); // Special game placholder with Waiting==true
        }
    }

    private void isGameOver(Game game) {
        if (game.getUser1Points().size() < 3 || game.getUser2Points().size() <3){
            return;
        }

        if (game.getUser1Points().stream().filter(point -> point.isCollected()).count() == game.getUser1Points().size()){
            game.setGameOver(true);
            game.getUser1().setWinner(true);
        }

        if (game.getUser2Points().stream().filter(point -> point.isCollected()).count() == game.getUser2Points().size()){
            game.setGameOver(true);
            game.getUser2().setWinner(true);
        }
    }

    private Game findUserGame(User user){
        Optional<Game> gameOfUser1 = findGameForUser1(user);
        if (gameOfUser1.isPresent()){
            return gameOfUser1.get();
        }

        Optional<Game> gameOfUser2 = findGameForUser2(user);
        if (gameOfUser2.isPresent()){
            return  gameOfUser2.get();
        }

        throw new RuntimeException("Game not found for user: "+user);

    }



    public Game addPointToOpponentMap(User pointGiver, Point point) {

        Game game = findUserGame(pointGiver);
        game.setCurrentOwner(pointGiver);

        User opponent = game.getOpponentUser(pointGiver);
        Set<Point> opponentPoints = game.getOpponentPoints();

        point.setOwner(opponent);
        opponentPoints.add(point);
//TODO:
//       if (opponentPoints.size() == 3) {
//          opponentPoints.add(generateRandomPoint(gameOfUser1.get().getUser2(), opponentPoints));
//       }

        return game;
    }

    private Optional<Game> findGameForUser1(User user) {
        return activeGames.stream().filter(game -> {
            return game.getUser1().equals(user);
        }).findFirst();
    }

    private Optional<Game> findGameForUser2(User user) {
        return activeGames.stream().filter(game ->
                {
                    return game.getUser2().equals(user);

                }
        ).findFirst();
    }

    public Optional<Game> findGameBetweenUsers(User user1, User user2) {

        Optional<Game> result = activeGames.stream().filter(game -> {
            String uid = game.getUsersIdentifier();
            return uid.contains(user1.getId().toString()) && uid.contains(user2.getId().toString());
        }).findFirst();

        return result;

    }

    private Point generateRandomPoint(User owner, Set<Point> userPoints) {
        //TODO: Get to japan

        Point generatedPoint = new Point(owner, 100, 100);

        return generatedPoint;

    }

    private void isPointCollected(Double lat, Double lon, Set<Point> points) {
        points.stream().forEach(point -> {
                    checkPointInRadius(point, lat, lon);
                }
        );
    }

    private void checkPointInRadius(Point point, Double lat, Double lon) {

        float dist = distFrom(point.getLatitude(), point.getLongitude(), lat, lon);
        log.info("Distance between: " + dist);
        if (dist < RADIUS_FROM_POINT) {
            point.setCollected(true);
        }

    }

    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

}
