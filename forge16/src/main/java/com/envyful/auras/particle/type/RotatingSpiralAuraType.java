package com.envyful.auras.particle.type;

import com.envyful.api.jexl.config.CalculationConfig;
import com.envyful.api.jexl.context.SuppliableContext;
import com.envyful.auras.EnvyAuras;
import com.envyful.auras.particle.AuraData;
import com.envyful.auras.particle.IdentifiableAuraType;
import com.envyful.auras.task.AuraTask;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.jexl3.JexlContext;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@AuraData("rotating-spiral")
@ConfigSerializable
public class RotatingSpiralAuraType extends IdentifiableAuraType {

    @Comment("This sets the particle type to use")
    private String particleType;
    private transient IParticleData particleCache;

    @Comment("This sets the x coordinate for the center of the sphere. Use 'entityX' to use the entity's x coordinate")
    private CalculationConfig xCenterCalculation;

    @Comment("This sets the z coordinate for the center of the sphere. Use 'entityY' to use the entity's Y coordinate, 'random' to use a random number (0-1), and 'entityHeight' to use the entity's height")
    private CalculationConfig yCenterCalculation;

    @Comment("This sets the z coordinate for the center of the sphere. Use 'entityZ' to use the entity's z coordinate")
    private CalculationConfig zCenterCalculation;

    @Comment("This sets the radius of the sphere")
    private double radius;

    @Comment("This sets the amount of particles to generate")
    private int particleCount;

    @Comment("This sets the number of times the spiral wraps around the center")
    private int turns;

    public RotatingSpiralAuraType() {
        super();
    }

    public RotatingSpiralAuraType(String particleType,
                                  CalculationConfig xCenterCalculation,
                                  CalculationConfig yCenterCalculation,
                                  CalculationConfig zCenterCalculation,
                                  double radius,
                                  int turns,
                                  int particleCount) {
        super();

        this.particleType = particleType;
        this.xCenterCalculation = xCenterCalculation;
        this.yCenterCalculation = yCenterCalculation;
        this.zCenterCalculation = zCenterCalculation;
        this.radius = radius;
        this.turns = turns;
        this.particleCount = particleCount;
    }

    @Override
    public void display(ServerWorld world, PixelmonEntity entity) {
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
        var tick = AuraTask.COUNTER.get();

        for (int i = 0; i < particleCount; ++i) {
            double t = (i + 0.000) / particleCount;
            double r = t * radius;
            double theta = (2 * Math.PI * t * turns) + tick;

            double x = r * Math.cos(theta);
            double z = r * Math.sin(theta);

            var packet = new SSpawnParticlePacket(particles, true,
                    center.x() + x, center.y() + ((1 - (i + 0.000) / particleCount)) * entity.getBbHeight(), center.z() + z,
                    0.0F, 0.0F, 0.0F, 0.0F, 1);

            PacketDistributor.TRACKING_ENTITY.with(() -> entity).send(packet);
        }
    }

    private IParticleData getParticles() {
        if (this.particleCache == null) {
            this.particleCache = (IParticleData) Registry.PARTICLE_TYPE.get(ResourceLocationHelper.of(this.particleType));
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
