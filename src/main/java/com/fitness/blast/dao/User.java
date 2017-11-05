package com.fitness.blast.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    public User(UUID id, String name, String latitude, String longitude, String location, long rewardPoints, Boolean isAvailable, Boolean winner) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.rewardPoints = rewardPoints;
        this.isAvailable = isAvailable;
        this.winner = winner;
        this.lastLatitude = latitude;
        this.lastLongitude = longitude;
    }

    private UUID id;
    private String name;
    private String latitude;
    private String longitude;
    private String location;
    private long rewardPoints = 0;
    private boolean isAvailable = true;
    private boolean winner;

    private String lastLatitude;
    private String lastLongitude;

}
