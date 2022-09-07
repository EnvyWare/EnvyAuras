package com.envyful.auras.particle.type;

import com.envyful.auras.config.EnvyAurasConfig;
import com.envyful.auras.particle.AuraConfig;
import com.envyful.auras.particle.AuraType;

public abstract class AbstractAuraType<T extends AuraConfig> implements AuraType {

    private final String id;
    protected EnvyAurasConfig.Aura config;
    protected T particleConfig;

    protected AbstractAuraType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public EnvyAurasConfig.Aura asConfig() {
        return this.config;
    }
}
