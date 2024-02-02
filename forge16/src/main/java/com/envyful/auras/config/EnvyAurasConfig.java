package com.envyful.auras.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.config.yaml.DefaultConfig;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.jexl.config.CalculationConfig;
import com.envyful.auras.particle.type.SimpleParticlesAuraType;
import com.envyful.auras.particle.type.PositionedSphereAuraType;
import com.envyful.auras.particle.type.SpiralAuraType;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.IOException;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/EnvyAuras/config.yml")
public class EnvyAurasConfig extends AbstractYamlConfig {

    private transient List<Aura> auras = Lists.newArrayList();

    public EnvyAurasConfig() throws IOException {
        super();

        this.auras = YamlConfigFactory.getInstances(Aura.class, "config/EnvyAuras/auras",
                DefaultConfig.onlyNew("default/example.yml", Aura.builder()
                                .id("example")
                                .displayName("&a&lExample!")
                                .displayItem(ConfigItem.builder()
                                        .type("minecraft:stone")
                                        .name("&a&lExample!")
                                        .amount(1)
                                        .build())
                                .aura(new SimpleParticlesAuraType("flame", 10, 0.15F))
                                .displayCalculation("tick % 5 == 0")
                        .build()),
                DefaultConfig.onlyNew("default/example2.yml", Aura.builder()
                        .id("example2")
                        .displayName("&a&lExample 2!")
                        .displayItem(ConfigItem.builder()
                                .type("minecraft:stone")
                                .name("&a&lExample 2!")
                                .amount(1)
                                .build())
                        .aura(new PositionedSphereAuraType("flame",
                                new CalculationConfig("entityX"),
                                new CalculationConfig("entityY + entityHeight"),
                                new CalculationConfig("entityZ"),
                                0.25, 100, true, true))
                        .displayCalculation("tick % 10 == 0")
                        .build()),
                DefaultConfig.onlyNew("default/example3.yml", Aura.builder()
                        .id("example3")
                        .displayName("&a&lExample 3!")
                        .displayItem(ConfigItem.builder()
                                .type("minecraft:stone")
                                .name("&a&lExample 3!")
                                .amount(1)
                                .build())
                        .aura(new SpiralAuraType("flame",
                                new CalculationConfig("entityX"),
                                new CalculationConfig("entityY"),
                                new CalculationConfig("entityZ"),
                                1.25, 3, 1000))
                        .displayCalculation("tick % 10 == 0")
                        .build()));
    }

    public List<Aura> getAuras() {
        return this.auras;
    }

    public Aura auraFromId(String id) {
        for (var aura : this.auras) {
            if (aura.id().equalsIgnoreCase(id)) {
                return aura;
            }
        }

        return null;
    }

    public Aura auraFromItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == Items.AIR) {
            return null;
        }

        var nbt = itemStack.getTag();

        if (!nbt.contains("aura")) {
            return null;
        }

        return this.auraFromId(nbt.getString("aura"));
    }

}
