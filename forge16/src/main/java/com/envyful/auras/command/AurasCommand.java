package com.envyful.auras.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.auras.EnvyAuras;
import net.minecraft.command.ICommandSource;

import java.util.List;

@Command(
        value = "auras"
)
@Permissible("com.envyful.auras.command.auras")
@SubCommands(GiveCommand.class)
public class AurasCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSource sender, String[] args) {
        EnvyAuras.getInstance().reloadConfig();
        PlatformProxy.sendMessage(sender, List.of("&a&lReloaded config!"));
    }
}
