package com.ramiletus.frauddetection.service.frauddetection.devicelocation;

import com.ramiletus.apigenerator.openapi.api.model.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import com.ramiletus.apigenerator.openapi.api.VerifyApi;
import java.util.Random;

@Service
@ConditionalOnProperty(
        value = "location.verification.api.call",
        havingValue = "true")
public class DeviceLocationVerificationServiceImpl implements DeviceLocationVerificationService {

    private final VerifyApi verifyApi = new VerifyApi();

    public int verifyDeviceLocation(String phoneNumber, Double newLongitude, Double newLatitude, String ip) {
        VerifyLocationResponseDTO response =
                verifyApi.verifyLocation(String.valueOf(new Random().nextInt()), // Using random xcorrelator, we don't care
                        new VerifyLocationRequestDTO.VerifyLocationRequestDTOBuilder()
                                .area(new CircleDTO.CircleDTOBuilder()
                                        .areaType("CIRCLE")
                                        .center(new PointDTO.PointDTOBuilder()
                                                .latitude(newLatitude)
                                                .longitude(newLongitude)
                                                .build())
                                        .radius(2000)
                                        .build())
                                .device(new DeviceDTO.DeviceDTOBuilder()
                                        .phoneNumber(phoneNumber)
                                        .ipv4Address(new DeviceIpv4AddrDTO.DeviceIpv4AddrDTOBuilder()
                                                .publicAddress(ip)
                                                .build())
                                        .build())
                                .build());

        int fraudScore = 1;

        switch (response.getVerificationResult()) {
            case TRUE:
                fraudScore += 0;
                break;
            case FALSE:
                fraudScore = 99;
                break;
            case PARTIAL:
                fraudScore += response.getMatchRate();
                break;
            case UNKNOWN:
                fraudScore += 50;
                break;
        }

        return fraudScore;
    }
}
