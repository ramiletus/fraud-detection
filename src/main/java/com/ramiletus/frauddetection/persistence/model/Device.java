package com.ramiletus.frauddetection.persistence.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Objects;
import java.util.UUID;

@Node
public class Device {

    @Id
    @GeneratedValue
    private UUID id;

    @Getter
    @Setter
    private String browser;

    @Getter
    @Setter
    private String operativeSystem;

    public Device() {
    }

    public Device(String browser, String operativeSystem) {
        this.browser = browser;
        this.operativeSystem = operativeSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id) && Objects.equals(browser, device.browser) && Objects.equals(operativeSystem, device.operativeSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, browser, operativeSystem);
    }
}
