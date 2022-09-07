
package com.envyful.auras.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@ConfigPath("config/EnvyAuras/guis.yml")
public class EnvyAurasGraphics extends AbstractYamlConfig {

    public EnvyAurasGraphics() {
        super();
    }

}
