package com.fitness.blast.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class User {

    private UUID id;
    private String name;
    private String latitude;
    private String longitude;
    private String location;
    private long rewardPoints = 0;
    private boolean isAvailable = true;
    private boolean winner;
}
