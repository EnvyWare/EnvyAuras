package com.envyful.auras.particle.type;

import com.envyful.auras.EnvyAuras;
import com.envyful.auras.particle.AuraData;
import com.envyful.auras.particle.IdentifiableAuraType;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDispatcher;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@AuraData("simple-particles")
@ConfigSerializable
public class SimpleParticlesAuraType extends IdentifiableAuraType {

    private String particleType;
    private int amount;
    private float particleSpeed;

    private transient IParticleData particleCache;

    public SimpleParticlesAuraType() {
        super();
    }

    public SimpleParticlesAuraType(String particleType, int amount, float particleSpeed) {
        super();

        this.particleType = particleType;
        this.amount = amount;
        this.particleSpeed = particleSpeed;
    }

    @Override
    public void display(ServerWorld world, PixelmonEntity entity) {
        var particles = this.getParticles();

        if (particles == null) {
            EnvyAuras.getLogger().error("Invalid particle type provided: {}", this.particleType);
            return;
        }

        var packet = new SSpawnParticlePacket(particles, true,
                entity.getX(), entity.getY(), entity.getZ(),
                entity.level.random.nextFloat() - 0.5F,
                entity.level.random.nextFloat() - 0.5F,
                entity.level.random.nextFloat() - 0.5F, this.particleSpeed, this.amount);

        PacketDistributor.TRACKING_ENTITY.with(() -> entity).send(packet);
    }

    private IParticleData getParticles() {
        if (this.particleCache == null) {
            this.particleCache = (IParticleData) Registry.PARTICLE_TYPE.get(ResourceLocationHelper.of(this.particleType));
        }

        return this.particleCache;
    }
}
