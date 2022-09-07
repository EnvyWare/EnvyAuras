package com.envyful.auras.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.auras.particle.AuraConfig;
import com.envyful.auras.particle.type.SimpleType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Map;

@ConfigSerializable
@ConfigPath("config/EnvyAuras/config.yml")
public class EnvyAurasConfig extends AbstractYamlConfig {

    private Map<String, Aura> auras = ImmutableMap.of(
            "one", new Aura(
                    "example", "Example Aura", new SimpleType.SimpleConfig(
                            "minecraft:flame", "flame"
            ), new ConfigItem(
                            "minecraft:stone", 1, "SIMPLE!", Lists.newArrayList("NO!")
            )
            )
    );

    public EnvyAurasConfig() {
        super();
    }

    public List<Aura> getAuras() {
        return Lists.newArrayList(this.auras.values());
    }

    @ConfigSerializable
    public static class Aura {

        private String id;
        private String displayName;
        private AuraConfig typeConfig;
        private ConfigItem auraItem;

        public Aura(String id, String displayName, AuraConfig typeConfig, ConfigItem auraItem) {
            this.id = id;
            this.displayName = displayName;
            this.typeConfig = typeConfig;
            this.auraItem = auraItem;
        }

        public Aura() {
        }

        public String getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public AuraConfig getTypeConfig() {
            return this.typeConfig; //TODO:
        }

        public ItemStack getAuraItem() {
            return UtilConfigItem.fromConfigItem(this.auraItem);
        }
    }
}
