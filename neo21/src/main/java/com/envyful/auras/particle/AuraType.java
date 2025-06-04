package com.envyful.auras.particle;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public interface AuraType {

    String id();

    void display(ServerLevel world, PixelmonEntity entity);

}
