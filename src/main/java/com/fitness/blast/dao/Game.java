package com.fitness.blast.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Game {

    @NonNull
    private UUID id;

    @NonNull
    private User user1;
    @NonNull
    private User user2;
    @NonNull
    private Set<Point> user1Points;
    @NonNull
    private Set<Point> user2Points;
    @NonNull
    private boolean gameOver;

    private User currentOwner;

    public Game setSortingOwner(User sortingOwner){
        this.currentOwner = sortingOwner;
        return this;
    }

    public Map<User, Set<Point>> getOrderedPoints(){
        if (currentOwner == null){
            return null;
        }

        LinkedHashMap<User, Set<Point>> userSetLinkedHashMap = new LinkedHashMap<>();

        if(currentOwner.equals(user1)){
            userSetLinkedHashMap.put(user1, user1Points);
            userSetLinkedHashMap.put(user2, user2Points);
        } else {
            userSetLinkedHashMap.put(user2, user2Points);
            userSetLinkedHashMap.put(user1, user1Points);

        }
            return userSetLinkedHashMap;
    }

    public Game(){
        //empty game with:
        //waiting == true
    }
    public boolean isWaiting(){
        return user1 == null || user2 == null;
    }

    public String getUsersIdentifier(){
        if (isWaiting()) {
            return "...";
        }
        return user1.getId().toString() + getUser2().getId().toString();

    }
}
