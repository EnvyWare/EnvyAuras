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
import org.joml.Vector3d;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@AuraData("positioned-sphere")
@ConfigSerializable
public class PositionedSphereAuraType extends IdentifiableAuraType {

    @Comment("This sets the particle type to use")
    private String particleType;
    private transient ParticleOptions particleCache;

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

    @Comment("This sets if the particles can generate only on the surface of the sphere, or both on the surface and inside the sphere")
    private boolean hollow;

    @Comment("This sets if the particles should be uniformly distributed across the sphere or randomly distributed")
    private boolean uniform;

    public PositionedSphereAuraType() {
        super();
    }

    public PositionedSphereAuraType(String particleType,
                                    CalculationConfig xCenterCalculation,
                                    CalculationConfig yCenterCalculation,
                                    CalculationConfig zCenterCalculation,
                                    double radius,
                                    int particleCount, boolean hollow, boolean uniform) {
        super();

        this.particleType = particleType;
        this.xCenterCalculation = xCenterCalculation;
        this.yCenterCalculation = yCenterCalculation;
        this.zCenterCalculation = zCenterCalculation;
        this.radius = radius;
        this.particleCount = particleCount;
        this.hollow = hollow;
        this.uniform = uniform;
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

        for (int i = 0; i < particleCount; ++i) {
            double x;
            double y;
            double z;

            if (uniform) {
                double goldenRatio = (Math.sqrt(5) - 1) / 2;
                double longitude = i * 2 * Math.PI * goldenRatio;
                double latitude = 2 * Math.asin(-1.00 + 2.00 * (i + 0.00) / (particleCount - 1));
                var u = Math.random();
                var r = (hollow ? 1 : Math.cbrt(u)) * radius;

                x = r * Math.cos(longitude) * Math.sin(latitude);
                y = r * Math.sin(longitude) * Math.sin(latitude);
                z = r * Math.cos(latitude);
            } else {
                var phi = Math.random() * 2 * Math.PI;
                var cosTheta = Math.random() * 2 - 1;
                var u = Math.random();
                var theta = Math.acos(cosTheta);
                var r = (hollow ? 1 : Math.cbrt(u)) * radius;

                x = r * Math.sin(theta) * Math.cos(phi);
                y = r * Math.sin(theta) * Math.sin(phi);
                z = r * Math.cos(theta);
            }

            var packet = new ClientboundLevelParticlesPacket(particles, true,
                    center.x() + x, center.y() + y, center.z() + z,
                    0.0F, 0.0F, 0.0F, 0.0F, 1);

            PacketDistributor.TRACKING_ENTITY.with(entity).send(packet);
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
