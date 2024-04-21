package com.envyful.auras.particle.type;

import com.envyful.api.jexl.config.CalculationConfig;
import com.envyful.api.jexl.context.SuppliableContext;
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
import org.apache.commons.jexl3.JexlContext;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@AuraData("wings")
@ConfigSerializable
public class WingsAuraType extends IdentifiableAuraType {

    @Comment("This sets the particle type to use")
    private String particleType;
    private transient ParticleOptions particleCache;

    @Comment("This sets the x coordinate for the center of the sphere. Use 'entityX' to use the entity's x coordinate")
    private CalculationConfig xCenterCalculation;

    @Comment("This sets the z coordinate for the center of the sphere. Use 'entityY' to use the entity's Y coordinate, 'random' to use a random number (0-1), and 'entityHeight' to use the entity's height")
    private CalculationConfig yCenterCalculation;

    @Comment("This sets the z coordinate for the center of the sphere. Use 'entityZ' to use the entity's z coordinate")
    private CalculationConfig zCenterCalculation;

    @Comment("This sets the size of the wings")
    private double size;

    @Comment("This sets the amount of particles to generate")
    private double angleDelta;


    public WingsAuraType() {
        super();
    }

    public WingsAuraType(String particleType,
                         CalculationConfig xCenterCalculation,
                         CalculationConfig yCenterCalculation,
                         CalculationConfig zCenterCalculation,
                         double size,
                         double angleDelta) {
        super();

        this.particleType = particleType;
        this.xCenterCalculation = xCenterCalculation;
        this.yCenterCalculation = yCenterCalculation;
        this.zCenterCalculation = zCenterCalculation;
        this.size = size;
        this.angleDelta = angleDelta;
    }

    @Override
    public void display(ServerLevel world, PixelmonEntity entity) {
        var particles = this.getParticles();

        if (particles == null) {
            EnvyAuras.getLogger().error("Invalid particle type provided: {}", this.particleType);
            return;
        }

        var context = this.getContext(entity);

        var posX = (double) this.xCenterCalculation.getExpression().evaluate(context);
        var posY = (double) this.yCenterCalculation.getExpression().evaluate(context);
        var posZ = (double) this.zCenterCalculation.getExpression().evaluate(context);
        var center = new Vector3d(posX, posY, posZ);
        double t = 0;

        while (t <= Math.PI * 2) {
            var x = this.size * (Math.sin(t) * (Math.exp(Math.cos(t)) - 2 * Math.cos(4 * t) - Math.pow(Math.sin(t / 12), 5)));
            var y = this.size * (Math.cos(t) * (Math.exp(Math.cos(t)) - 2 * Math.cos(4 * t) - Math.pow(Math.sin(t / 12), 5)));
            var yawRad = (float) Math.toRadians(entity.getRotationVector().y);
            var vec = new Vector2f((float) x, 0);
            var newX = vec.x * (float) Math.cos(yawRad) - vec.y * (float) Math.sin(yawRad);
            var newZ = vec.x * (float) Math.sin(yawRad) + vec.y * (float) Math.cos(yawRad);
            var facing = entity.getDirection().getOpposite();

            var packet = new ClientboundLevelParticlesPacket(particles, true,
                    center.x() + newX + facing.getStepX() / 2.0, center.y() + y, center.z() + newZ + facing.getStepZ() / 2.0,
                    0.0F, 0.0F, 0.0F, 0.0F, 1);

            PacketDistributor.TRACKING_ENTITY.with(entity).send(packet);

            t += this.angleDelta;
        }
    }

    private ParticleOptions getParticles() {
        if (this.particleCache == null) {
            this.particleCache = (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(ResourceLocationHelper.of(this.particleType));
        }

        return this.particleCache;
    }

    private JexlContext getContext(PixelmonEntity entity) {
        var context = new SuppliableContext();

        context.set("entityX", entity.getX());
        context.set("entityY", entity.getY());
        context.set("entityZ", entity.getZ());
        context.set("random", Math::random);
        context.set("entityHeight", entity.getBbHeight());

        return context;
    }
}
