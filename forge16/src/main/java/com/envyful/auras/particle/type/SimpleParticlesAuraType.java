package com.envyful.auras.particle.type;

import com.envyful.auras.EnvyAuras;
import com.envyful.auras.particle.AuraData;
import com.envyful.auras.particle.IdentifiableAuraType;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@AuraData("simple-particles")
@ConfigSerializable
public class SimpleParticlesAuraType extends IdentifiableAuraType {

    private String particleType;
    private int amount;

    private transient IParticleData particleCache;

    public SimpleParticlesAuraType() {
        super();
    }

    public SimpleParticlesAuraType(String particleType, int amount) {
        super();

        this.particleType = particleType;
        this.amount = amount;
    }

    @Override
    public void display(ServerWorld world, Entity entity) {
        var particles = this.getParticles();

        if (particles == null) {
            EnvyAuras.getLogger().error("Invalid particle type provided: {}", this.particleType);
            return;
        }

        System.out.println("Displaying particles");
        world.sendParticles(particles, entity.getX(), entity.getY(), entity.getZ(), this.amount,
                entity.level.random.nextDouble() - 0.5,
                entity.level.random.nextDouble() - 0.5,
                entity.level.random.nextDouble() - 0.5, 1.0D);
    }

    private IParticleData getParticles() {
        if (this.particleCache == null) {
            this.particleCache = (IParticleData) Registry.PARTICLE_TYPE.get(ResourceLocationHelper.of(this.particleType));
        }

        return this.particleCache;
    }
}
