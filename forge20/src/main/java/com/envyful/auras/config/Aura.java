package com.envyful.auras.config;

import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.jexl.config.CalculationConfig;
import com.envyful.auras.particle.AuraType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.jexl3.MapContext;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Aura extends AbstractYamlConfig {

    private String id;
    private String displayName;
    private ConfigItem displayItem;
    private AuraType aura;
    private CalculationConfig displayCalculation;

    protected Aura(Builder builder) {
        this();

        this.id = builder.id;
        this.displayName = builder.displayName;
        this.displayItem = builder.displayItem;
        this.aura = builder.aura;
        this.displayCalculation = new CalculationConfig(builder.displayCalculation);
    }

    public Aura() {
        super();
    }

    public String id() {
        return this.id;
    }

    public String displayName() {
        return this.displayName;
    }

    public ItemStack getItem(int amount) {
        var item = UtilConfigItem.fromConfigItem(this.displayItem);
        var nbt = item.getOrCreateTag();

        nbt.putString("aura", this.id);
        item.setTag(nbt);
        item.setCount(amount);
        return item;
    }

    public boolean shouldDisplay(PixelmonEntity entity) {
        var expression = this.displayCalculation.getExpression();

        if (expression == null) {
            return true;
        }

        var context = new MapContext();

        context.set("uuid", entity.getUUID().toString());
        context.set("level", entity.getPokemon().getPokemonLevel());
        context.set("shiny", entity.getPokemon().isShiny());
        context.set("tick", entity.tickCount);

        return (boolean) expression.evaluate(context);
    }

    public AuraType type() {
        return this.aura;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String displayName;
        private ConfigItem displayItem;
        private AuraType aura;
        private String displayCalculation;

        private Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder displayItem(ConfigItem displayItem) {
            this.displayItem = displayItem;
            return this;
        }

        public Builder aura(AuraType aura) {
            this.aura = aura;
            return this;
        }

        public Builder displayCalculation(String displayCalculation) {
            this.displayCalculation = displayCalculation;
            return this;
        }

        public Aura build() {
            return new Aura(this);
        }
    }

}
