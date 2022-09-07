package com.envyful.auras.particle;

import com.envyful.auras.config.EnvyAurasConfig;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;

public interface AuraType {

    String getId();

    EnvyAurasConfig.Aura asConfig();

    void display(ServerWorld world, Entity entity) throws Exception;

}
