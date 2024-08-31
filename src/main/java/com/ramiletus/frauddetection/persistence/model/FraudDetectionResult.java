package com.ramiletus.frauddetection.persistence.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FraudDetectionResult {

    private User user;
    private Device device;
    private Location location;
    private double distance;
    private long timeDiff;
    private double speed;
}
