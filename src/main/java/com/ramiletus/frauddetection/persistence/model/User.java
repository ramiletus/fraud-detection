package com.ramiletus.frauddetection.persistence.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Node
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    private String name;

    @Setter
    private String email;

    @Setter
    @Relationship(type = "PHONENUMBERS")
    private Set<PhoneNumber> phoneNumbers;

    @Setter
    @Relationship(type = "USES_DEVICE")
    private Set<Device> devices;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(phoneNumbers, user.phoneNumbers) && Objects.equals(devices, user.devices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phoneNumbers, devices);
    }
}
