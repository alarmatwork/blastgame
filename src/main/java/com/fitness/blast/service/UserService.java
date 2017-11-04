package com.fitness.blast.service;

import com.fitness.blast.dao.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private HashMap<UUID, User> registeredUsers = new HashMap<>();

    public void register(User user) {
        registeredUsers.put(user.getId(), user);
        log.info("Registered user: " + user + " Registered users count: " + registeredUsers.size());
    }

    public User getOpponent(String id) {
        User opponent = null;
        UUID myId = UUID.fromString(id);
        int i = 0;

        Iterator it = registeredUsers.entrySet().iterator();


        while (it.hasNext()) {
            Map.Entry<UUID, User> candidate = (Map.Entry) it.next();

            if (!candidate.getKey().equals(myId)       // It is not myself
                    && candidate.getValue().isAvailable() // Player is not playing at the moment
                    ) {

                opponent = candidate.getValue();
                opponent.setAvailable(false);
                registeredUsers.get(myId).setAvailable(false); // Mark myself also as a part of a game

                log.info("Returning opponent:" + opponent + " for the user: " + id);
                return opponent;
            }
        }
        log.info("Waiting for oponent for: " + id);


        log.error("No oppponents available. Should wait");

        return opponent;
    }

    public User findUser(String userId) {
        log.info("Currently Registered Users: " + registeredUsers);
        return registeredUsers.get(UUID.fromString(userId));
    }

    public void makeAMove(String id, int x, int y) {

    }


    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
            log.error("Exception happened", ex);

        }
    }

}
