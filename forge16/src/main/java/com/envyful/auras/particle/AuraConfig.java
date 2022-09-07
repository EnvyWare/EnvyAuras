package com.envyful.auras.particle;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public abstract class AuraConfig {

    protected String type;
    private transient AuraType auraType = null;

    public AuraConfig() {}

    public AuraType getType() {
        if (this.auraType == null) {
            this.auraType = AuraRegistry.get(this.type).orElse(null);
        }

        return this.auraType;
    }
}
