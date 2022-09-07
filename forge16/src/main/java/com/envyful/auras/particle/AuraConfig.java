package com.envyful.auras.particle;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Locale;

@ConfigSerializable
public abstract class AuraConfig {

    protected String type;

    public AuraConfig() {}

    public String getTypeId() {
        return this.type.toUpperCase(Locale.ROOT);
    }

}
