package com.envyful.auras.particle;

import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;

public interface AuraType {

    String getId();

    void display(ServerWorld world, Entity entity);

}
