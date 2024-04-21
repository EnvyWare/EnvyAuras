package com.envyful.auras.particle.type;

import com.envyful.auras.EnvyAuras;
import com.envyful.auras.particle.AuraData;
import com.envyful.auras.particle.IdentifiableAuraType;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@AuraData("simple-particles")
@ConfigSerializable
public class SimpleParticlesAuraType extends IdentifiableAuraType {

    private String particleType;
    private int amount;
    private float particleSpeed;

    private transient ParticleOptions particleCache;

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
    public void display(ServerLevel world, PixelmonEntity entity) {
        var particles = this.getParticles();

        if (particles == null) {
            EnvyAuras.getLogger().error("Invalid particle type provided: {}", this.particleType);
            return;
        }

        var packet = new ClientboundLevelParticlesPacket(particles, true,
                entity.getX(), entity.getY(), entity.getZ(),
                entity.level().random.nextFloat() - 0.5F,
                entity.level().random.nextFloat() - 0.5F,
                entity.level().random.nextFloat() - 0.5F, this.particleSpeed, this.amount);

        PacketDistributor.TRACKING_ENTITY.with(entity).send(packet);
    }

    private ParticleOptions getParticles() {
        if (this.particleCache == null) {
            this.particleCache = (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(ResourceLocationHelper.of(this.particleType));
        }

        return this.particleCache;
    }
}
