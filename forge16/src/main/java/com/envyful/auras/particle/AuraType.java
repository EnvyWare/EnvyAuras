package com.envyful.auras.particle;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public interface AuraType {

    String id();

    void display(ServerWorld world, PixelmonEntity entity);

}
