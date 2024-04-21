package com.envyful.auras.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Completable;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.forge.command.completion.player.PlayerTabCompleter;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.api.text.Placeholder;
import com.envyful.auras.EnvyAuras;
import com.envyful.auras.config.Aura;
import net.minecraft.commands.CommandSource;

@Command(
        value = "give"
)
@Permissible("com.envyful.auras.command.give")
public class GiveCommand {

    @CommandProcessor
    public void onCommand(@Sender CommandSource sender,
                          @Completable(PlayerTabCompleter.class) @Argument ForgeEnvyPlayer target,
                          @Completable(AuraTabCompleter.class) @Argument Aura aura,
                          @Argument(defaultValue = "1") int amount) {
        var item = aura.getItem(amount);
        target.getParent().getInventory().add(item);

        PlatformProxy.sendMessage(target, EnvyAuras.getLocale().getReceivedAura(),
                Placeholder.simple("%amount%", String.valueOf(amount)),
                Placeholder.simple("%aura%", aura.displayName()));

        PlatformProxy.sendMessage(sender, EnvyAuras.getLocale().getGivenAura(),
                Placeholder.simple("%player%", target.getName()),
                Placeholder.simple("%amount%", String.valueOf(amount)),
                Placeholder.simple("%aura%", aura.displayName()));
    }
}
