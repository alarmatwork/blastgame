package com.fitness.blast.service

import com.fitness.blast.dao.Game
import com.fitness.blast.dao.Point
import com.fitness.blast.dao.User
import spock.lang.Specification

class GameServiceSpec extends Specification {
    UserService service = new UserService()
    GameService gameService = new GameService()

    def "Ping test"() {
        given:
        User user1 = new User(UUID.randomUUID(), "Tasku User", "58.377625", "26.729006", "Estonia, Tartu, Tasku", 0, true,false)
        User user2 = new User(UUID.randomUUID(), "Tamme Staadion", "58.366816", "26.713064", "Estonia, Tart", 0, true, false)

        service.register(user1)
        service.register(user2)

        service.getOpponent(user1.getId().toString())
        gameService.registerGame(user1, user2)

        Point taskuPoint = new Point(user1, 100, 200)
        taskuPoint.setLatitude(58.377624)
        taskuPoint.setLongitude(26.729004)
        taskuPoint.message = "Taskupoint"

        gameService.addPointToOpponentMap(user2, taskuPoint)

        Point taskuPointNotCollected = new Point(user1, 100, 200)
        taskuPointNotCollected.setLatitude(58.347600)
        taskuPointNotCollected.setLongitude(26.749066)

        gameService.addPointToOpponentMap(user2, taskuPointNotCollected)

        Point tammeStaadioni = new Point(user2, 100, 200)
        tammeStaadioni.setLatitude(58.366669)
        tammeStaadioni.setLongitude(26.713686)
        gameService.addPointToOpponentMap(user1, tammeStaadioni)


        Point tammeStaadioniNotCollected = new Point(user2, 100, 200)
        tammeStaadioniNotCollected.setLatitude(58.376669)
        tammeStaadioniNotCollected.setLongitude(26.723686)

        gameService.addPointToOpponentMap(user1, tammeStaadioniNotCollected)



        when:
            gameService.ping(user1, 58.377625, 26.729006)

        then:
            taskuPoint.isCollected() == true
            taskuPointNotCollected.isCollected() == false


        when:
            gameService.ping(user2, 58.366007, 26.713684)

        then:
            tammeStaadioni.isCollected() == true
            tammeStaadioniNotCollected.isCollected() == false

    }


    def "Winner detection"() {
        given:
        User user1 = new User(UUID.randomUUID(), "Tasku User Winnder", "58.377625", "26.729006", "Estonia, Tartu, Tasku", 0, true,false)
        User user2 = new User(UUID.randomUUID(), "Tamme Staadion", "58.366816", "26.713064", "Estonia, Tart", 0, true, false)

        service.register(user1)
        service.register(user2)

        service.getOpponent(user1?.getId().toString())
        Game game = gameService.registerGame(user1, user2)

        Point taskuPoint1 = new Point(user1, 58.377624, 26.729004)
        Point taskuPoint2 = new Point(user1, 58.377624, 26.729004)
        Point taskuPoint3 = new Point(user1, 58.377624, 26.729004)

        gameService.addPointToOpponentMap(user2, taskuPoint1)
        gameService.addPointToOpponentMap(user2, taskuPoint2)
        gameService.addPointToOpponentMap(user2, taskuPoint3)

        Point tammeStaadioni1 = new Point(user2, 58.366669, 26.713686)
        Point tammeStaadioni2 = new Point(user2, 58.366669, 26.713686)
        Point tammeStaadioni3 = new Point(user2, 58.366669, 26.713686)

        gameService.addPointToOpponentMap(user1, tammeStaadioni1)
        gameService.addPointToOpponentMap(user1, tammeStaadioni2)
        gameService.addPointToOpponentMap(user1, tammeStaadioni3)



        when:
            gameService.ping(user2, 58.366669, 26.713686)

        then:

            tammeStaadioni1.isCollected() == true
            tammeStaadioni2.isCollected() == true
            tammeStaadioni3.isCollected() == true

            user2.isWinner() == true
            game.gameOver == true


    }
}
