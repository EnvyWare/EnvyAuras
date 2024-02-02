package com.envyful.auras.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
@ConfigPath("config/EnvyAuras/locale.yml")
public class EnvyAurasLocale extends AbstractYamlConfig {

    private List<String> receivedAura = Lists.newArrayList("&e&l(!) &eYou were given %amount% x %aura%");
    private List<String> givenAura = Lists.newArrayList("&e&l(!) &eYou have given %player% %amount% x %aura%");
    private List<String> cancelOverwrite = Lists.newArrayList("&c&l(!) &cYou cancelled the aura overwrite");
    private List<String> overwritten = Lists.newArrayList("&e&l(!) &eYou overwrote the %old_aura% to %new_aura% for %pokemon%");
    private List<String> auraSet = Lists.newArrayList("&e&l(!) &eYou set %pokemon%'s aura to %aura%");
    private List<String> cannotSetOthersAuras = Lists.newArrayList("&c&l(!) &cYou cannot set the aura for other players' Pokemon!");

    public EnvyAurasLocale() {
        super();
    }

    public List<String> getReceivedAura() {
        return this.receivedAura;
    }

    public List<String> getGivenAura() {
        return this.givenAura;
    }

    public List<String> getCancelOverwrite() {
        return this.cancelOverwrite;
    }

    public List<String> getOverwritten() {
        return this.overwritten;
    }

    public List<String> getAuraSet() {
        return this.auraSet;
    }

    public List<String> getCannotSetOthersAuras() {
        return this.cannotSetOthersAuras;
    }
}
