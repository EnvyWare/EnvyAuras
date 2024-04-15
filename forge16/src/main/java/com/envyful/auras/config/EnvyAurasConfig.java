package com.envyful.auras.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.config.yaml.DefaultConfig;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.jexl.config.CalculationConfig;
import com.envyful.auras.particle.type.*;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.IOException;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/EnvyAuras/config.yml")
public class EnvyAurasConfig extends AbstractYamlConfig {

    private transient List<Aura> auras;

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
                        .build()),
                DefaultConfig.onlyNew("default/example4.yml", Aura.builder()
                        .id("example4")
                        .displayName("&a&lExample 4!")
                        .displayItem(ConfigItem.builder()
                                .type("minecraft:stone")
                                .name("&a&lExample 4!")
                                .amount(1)
                                .build())
                        .aura(new WingsAuraType("flame",
                                new CalculationConfig("entityX"),
                                new CalculationConfig("entityY + (entityHeight / 2)"),
                                new CalculationConfig("entityZ"),
                                0.5, 0.01))
                        .displayCalculation("tick % 10 == 0")
                        .build()),
                DefaultConfig.onlyNew("default/example5.yml", Aura.builder()
                        .id("example5")
                        .displayName("&a&lExample 5!")
                        .displayItem(ConfigItem.builder()
                                .type("minecraft:stone")
                                .name("&a&lExample 5!")
                                .amount(1)
                                .build())
                        .aura(new HaloAuraType("flame",
                                new CalculationConfig("entityX"),
                                new CalculationConfig("entityY + entityHeight"),
                                new CalculationConfig("entityZ"),
                                100, 0.5))
                        .displayCalculation("tick % 10 == 0")
                        .build()),
                DefaultConfig.onlyNew("default/example6.yml", Aura.builder()
                        .id("example6")
                        .displayName("&a&lExample 6!")
                        .displayItem(ConfigItem.builder()
                                .type("minecraft:stone")
                                .name("&a&lExample 6!")
                                .amount(1)
                                .build())
                        .aura(new RotatingSpiralAuraType("flame",
                                new CalculationConfig("entityX"),
                                new CalculationConfig("entityY"),
                                new CalculationConfig("entityZ"),
                                1.25, 3, 1000))
                        .displayCalculation("tick % 10 == 0")
                        .build()),
                DefaultConfig.onlyNew("default/none.yml", Aura.builder()
                        .id("none")
                        .displayName("&a&lNone!")
                        .displayItem(ConfigItem.builder()
                                .type("minecraft:stone")
                                .name("&a&lNone!")
                                .amount(1)
                                .build())
                        .aura(new HaloAuraType("flame",
                                new CalculationConfig("entityX"),
                                new CalculationConfig("entityY + entityHeight"),
                                new CalculationConfig("entityZ"),
                                0, 0))
                        .displayCalculation("1 == 2")
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

        if (nbt == null) {
            return null;
        }

        if (!nbt.contains("aura")) {
            return null;
        }

        return this.auraFromId(nbt.getString("aura"));
    }

    public Aura auraFromPokemon(PixelmonEntity pixelmon) {
        return this.auraFromPokemon(pixelmon.getPokemon());
    }

    public Aura auraFromPokemon(Pokemon pokemon) {
        var nbt = pokemon.getPersistentData();

        if (!nbt.contains("ENVY_AURAS")) {
            return null;
        }

        return this.auraFromId(nbt.getString("ENVY_AURAS"));
    }

}
