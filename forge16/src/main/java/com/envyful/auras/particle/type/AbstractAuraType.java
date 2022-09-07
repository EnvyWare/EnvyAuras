package com.envyful.auras.particle.type;

import com.envyful.auras.particle.AuraConfig;
import com.envyful.auras.particle.AuraType;

public abstract class AbstractAuraType<T extends AuraConfig> implements AuraType {

    private final String id;
    protected T config;

    protected AbstractAuraType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
