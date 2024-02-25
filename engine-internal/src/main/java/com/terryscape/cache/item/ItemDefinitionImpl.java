package com.terryscape.cache.item;

public class ItemDefinitionImpl implements ItemDefinition {

    private String id;

    private String name;

    private String description;

    private boolean stackable;

    private String animationMainHandAttack;

    private String animationOffHandAttack;

    @Override
    public String getId() {
        return id;
    }

    public ItemDefinitionImpl setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public ItemDefinitionImpl setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public ItemDefinitionImpl setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean isStackable() {
        return stackable;
    }

    public ItemDefinitionImpl setStackable(boolean stackable) {
        this.stackable = stackable;
        return this;
    }

    @Override
    public String getAnimationMainHandAttack() {
        return animationMainHandAttack;
    }

    public ItemDefinitionImpl setAnimationMainHandAttack(String animationMainHandAttack) {
        this.animationMainHandAttack = animationMainHandAttack;
        return this;
    }

    @Override
    public String getAnimationOffHandAttack() {
        return animationOffHandAttack;
    }

    public ItemDefinitionImpl setAnimationOffHandAttack(String animationOffHandAttack) {
        this.animationOffHandAttack = animationOffHandAttack;
        return this;
    }

    @Override
    public String toString() {
        return "ItemDefinition(id=%s)".formatted(id);
    }
}
