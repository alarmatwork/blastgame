package com.fitness.blast.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private User user1;

    @NonNull
    @JsonIgnore
    private User user2;

    @NonNull
    @JsonIgnore
    private Set<Point> user1Points;
    @NonNull
    @JsonIgnore
    private Set<Point> user2Points;
    @NonNull
    private boolean gameOver;

    private User currentOwner;

    public Game setSortingOwner(User sortingOwner){
        this.currentOwner = sortingOwner;
        return this;
    }

    public User getMe(){
        return currentOwner;
    }

    public User getOpponent(){

        return getOpponentUser(currentOwner);
    }

    public User getOpponentUser(User user) {
        if (user1 != null && user1.equals(user)){
            return user2;
        } else {
            return user1;
        }
    }

    public Set<Point> getMyPoints(){
        if (user1 != null && user1.equals(currentOwner)){
            return user1Points;
        } else {
            return user2Points;
        }
    }

    public Set<Point> getOpponentPoints(){
        if (user1 != null && user1.equals(currentOwner)){
            return user2Points;
        } else {
            return user1Points;
        }
    }

    public Game(){
        //empty game with:
        //waiting == true
    }
    public boolean isWaiting(){
        return user1 == null || user2 == null;
    }

    @JsonIgnore
    public String getUsersIdentifier(){
        if (isWaiting()) {
            return "...";
        }
        return user1.getId().toString() + user2.getId().toString();
    }
}
