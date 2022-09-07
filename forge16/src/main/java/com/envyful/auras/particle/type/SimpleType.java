package com.envyful.auras.particle.type;

import com.envyful.auras.particle.AuraConfig;
import com.envyful.auras.particle.AuraType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Locale;

public class SimpleType extends AbstractAuraType<SimpleType.SimpleConfig> {

    public SimpleType() {
        super("simple");
    }

    public SimpleType(AuraConfig config) {
        this();

        if (!(config instanceof SimpleConfig)) {
            throw new UnsupportedOperationException("Incorrect config type");
        }

        this.config = (SimpleConfig) config;
    }

    @Override
    public void display(ServerWorld world, Entity entity) throws Exception {
        ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(this.config.getParticle());
        IParticleData particle = readParticle(new StringReader(this.config.getParticleData()), particleType);
        world.sendParticles(particle, entity.getX(), entity.getY(), entity.getZ(), 10, 2, 2, 2, 0.5f);
    }

    private static <T extends IParticleData> T readParticle(StringReader p_199816_0_, ParticleType<T> p_199816_1_) throws CommandSyntaxException {
        return p_199816_1_.getDeserializer().fromCommand(p_199816_1_, p_199816_0_);
    }

    @ConfigSerializable
    public static class SimpleConfig extends AuraConfig {

        private String particleType;
        private transient ResourceLocation particle = null;

        private String particleData;

        public SimpleConfig() {
            super();
        }

        public SimpleConfig(String particleType, String particleData) {
            this.type = "simple";
            this.particleType = particleType;
            this.particleData = particleData;
        }

        public ResourceLocation getParticle() {
            if (this.particle == null) {
                this.particle = new ResourceLocation(this.particleType.toLowerCase(Locale.ROOT));
            }

            return this.particle;
        }

        public String getParticleData() {
            return this.particleData;
        }
    }
}
