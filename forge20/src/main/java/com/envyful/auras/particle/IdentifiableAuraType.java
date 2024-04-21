package com.envyful.auras.particle;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public abstract class IdentifiableAuraType implements AuraType {

    private String id;

    protected IdentifiableAuraType() {
        var data = this.getClass().getAnnotation(AuraData.class);

        if (data == null) {
            throw new IllegalArgumentException("AuraType must have AuraData annotation");
        }

        this.id = data.value();
    }

    @Override
    public String id() {
        return this.id;
    }
}
