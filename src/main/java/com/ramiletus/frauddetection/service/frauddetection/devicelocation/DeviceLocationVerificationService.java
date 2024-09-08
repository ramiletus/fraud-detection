package com.ramiletus.frauddetection.service.frauddetection.devicelocation;

public interface DeviceLocationVerificationService {
    int verifyDeviceLocation(String phoneNumber, Double newLongitude, Double newLatitude, String ip);
}
