package com.fitness.blast.service;

import com.fitness.blast.dao.Game;
import com.fitness.blast.dao.Point;
import com.fitness.blast.dao.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

        Game newGame = new Game(UUID.randomUUID(), user1, user2, new HashSet<>(), new HashSet<>());
        activeGames.add(newGame);
        log.info("Registered  new game: " + newGame);
        return newGame;
    }

    public Game ping(User user, Double lat, Double lon) {
        Optional<Game> gameOfUser1 = findGameForUser1(user);
        Optional<Game> gameOfUser2 = findGameForUser2(user);

        if (gameOfUser1.isPresent()) {
            log.info("Updating User1 points");
            isPointCollected(lat, lon, gameOfUser1.get().getUser1Points());
        } else if (gameOfUser2.isPresent()) {
            log.info("Updating User2 points");
            isPointCollected(lat, lon, gameOfUser2.get().getUser2Points());

            return gameOfUser2.get();
        } else {
            throw new RuntimeException("Game not found for user: " + user);
        }
        return gameOfUser1.get();

    }


    public void addPointToMap(User user, Point point) {
        Optional<Game> gameOfUser1 = findGameForUser1(user);
        Optional<Game> gameOfUser2 = findGameForUser2(user);

        if (gameOfUser1.isPresent()) {
            Set<Point> opponentPoints = gameOfUser1.get().getUser2Points();
            opponentPoints.add(point);

            if (opponentPoints.size() == 3) {
                opponentPoints.add(generateRandomPoint(gameOfUser1.get().getUser2(), opponentPoints));
            }

        } else if (gameOfUser2.isPresent()) {
            Set<Point> opponentPoints = gameOfUser2.get().getUser1Points();
            opponentPoints.add(point);

            if (opponentPoints.size() == 3) {
                opponentPoints.add(generateRandomPoint(gameOfUser2.get().getUser1(), opponentPoints));
            }

        } else {
            throw new RuntimeException("Can not find game for the user: " + user);
        }

    }

    private Optional<Game> findGameForUser1(User user) {
        return activeGames.stream().filter(game -> game.getUser1().equals(user)).findFirst();
    }

    private Optional<Game> findGameForUser2(User user) {
        return activeGames.stream().filter(game -> game.getUser2().equals(user)).findFirst();
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
