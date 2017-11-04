package com.fitness.blast.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Game {

    private UUID id;
    private User user1;
    private User user2;

    private Set<Point> user1Points;
    private Set<Point> user2Points;

    public boolean isWaiting(){
        return user1 == null || user2 == null;
    }

    public String getUsersIdentifier(){
        return user1.getId().toString() + getUser2().getId().toString();

    }

}
