package com.envyful.auras.command;

import com.envyful.api.command.sender.SenderType;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ForgeEnvyPlayerSenderType implements SenderType<ICommandSource, ForgeEnvyPlayer> {
    @Override
    public Class<?> getType() {
        return ForgeEnvyPlayer.class;
    }

    @Override
    public boolean isAccepted(ICommandSource sender) {
        return sender instanceof ServerPlayerEntity;
    }

    @Override
    public ForgeEnvyPlayer getInstance(ICommandSource sender) {
        return com.envyful.auras.EnvyAuras.getInstance().getPlayerManager().getPlayer((ServerPlayerEntity) sender);
    }
}
