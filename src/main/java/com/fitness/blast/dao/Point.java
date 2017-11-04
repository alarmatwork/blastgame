package com.fitness.blast.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Point {

    public Point(User user, double latitude, double longitude){
        this.id = UUID.randomUUID();
        this.owner = user;
        this.latitude = latitude;
        this.longitude = longitude;

        this.isCollected = false;
        this.reward = 100;
    }

    private User owner;
    private UUID id;

    private Double longitude;
    private Double latitude;

    private String message;
    private int reward;
    private boolean isCollected;


}
