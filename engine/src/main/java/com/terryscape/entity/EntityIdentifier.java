package com.terryscape.entity;

import java.util.UUID;

public class EntityIdentifier {

    public static EntityIdentifier randomIdentifier() {
        return new EntityIdentifier(UUID.randomUUID());
    }

    public static EntityIdentifier fromString(String identifier) {
        return new EntityIdentifier(UUID.fromString(identifier));
    }

    private final UUID identifier;

    private EntityIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getValue() {
        return identifier.toString();
    }

    @Override
    public String toString() {
        return "EntityIdentifier(%s)".formatted(getValue());
    }

    @Override
    public boolean equals(Object o) {
        return identifier.equals(o);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
