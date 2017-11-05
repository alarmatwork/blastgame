package com.fitness.blast.service

import com.fitness.blast.dao.User
import spock.lang.Specification

class UserServiceSpec extends Specification {

    UserService service = new UserService();



        def "GetOpponent for regular case of two registered users"() {
            given:
                User user1 = new User(UUID.randomUUID(), "First User","59.1", "57.34","Estonia, Tart", 0, true, false)
                User user2 = new User(UUID.randomUUID(), "Second User","52.1", "17.34","Estonia, Tart", 0, true, false)
                service.register(user1)
                service.register(user2)

            when:
                User opponent = service.getOpponent(user1.getId().toString())

            then:
                opponent == user2

        }

    def "GetOpponent with only one user"() {
        given:
            User user1 = new User(UUID.randomUUID(), "First User", "59.1", "57.34", "Estonia, Tart", 0, true, false)
            service.register(user1)

        when:
            User opponent = service.getOpponent(user1.getId().toString())

        then:
            opponent == null

    }



    def "GetOpponent when two users fregistered and if we get opponent then we should mark both as not available"() {
        given:
            User user1 = new User(UUID.randomUUID(), "Peeter","59.1", "57.34","Estonia, Tart", 0, true, false)
            User user2 = new User(UUID.randomUUID(), "Mait","52.1", "17.34","Estonia, Tart", 0, true, false)
            service.register(user1)
            service.register(user2)

        when:
            service.getOpponent(user1.getId().toString())

        then:
            user2.isAvailable() == false
            user1.isAvailable() == false

    }

}
