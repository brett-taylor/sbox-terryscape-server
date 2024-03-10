package com.terryscape.cache.sound;

public class SoundDefinitionImpl implements SoundDefinition {

    private String id;

    @Override
    public String getId() {
        return id;
    }

    public SoundDefinitionImpl setId(String id) {
        this.id = id;
        return this;
    }
}
