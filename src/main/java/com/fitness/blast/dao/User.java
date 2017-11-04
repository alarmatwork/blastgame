package com.fitness.blast.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class User {

    private UUID id;
    private String name;
    private String gpsLatitude;
    private String gpsLongitude;
    private String location;
    private long points = 0;
    private boolean isAvailable = true;
}
