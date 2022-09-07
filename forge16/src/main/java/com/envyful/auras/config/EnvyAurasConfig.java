package com.envyful.auras.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@ConfigPath("config/EnvyAuras/config.yml")
public class EnvyAurasConfig extends AbstractYamlConfig {

    public EnvyAurasConfig() {
        super();
    }
}
