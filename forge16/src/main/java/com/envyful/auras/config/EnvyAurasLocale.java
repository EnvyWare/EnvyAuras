package com.envyful.auras.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.config.yaml.DefaultConfig;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.auras.particle.type.SimpleParticlesAuraType;
import com.google.common.collect.Lists;
import net.minecraft.particles.ParticleTypes;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.IOException;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/EnvyAuras/locale.yml")
public class EnvyAurasLocale extends AbstractYamlConfig {

    private List<String> receivedAura = Lists.newArrayList("&e&l(!) &eYou were given %amount% x %aura%");
    private List<String> givenAura = Lists.newArrayList("&e&l(!) &eYou have given %player% %amount% x %aura%");

    public EnvyAurasLocale() {
        super();
    }

    public List<String> getReceivedAura() {
        return this.receivedAura;
    }

    public List<String> getGivenAura() {
        return this.givenAura;
    }
}
