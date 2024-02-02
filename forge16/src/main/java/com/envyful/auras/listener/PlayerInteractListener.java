package com.envyful.auras.listener;

import com.envyful.api.platform.PlatformProxy;
import com.envyful.api.text.Placeholder;
import com.envyful.auras.EnvyAuras;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerInteractListener {

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getTarget() instanceof PixelmonEntity)) {
            return;
        }

        var pixelmon = (PixelmonEntity) event.getTarget();
        var player = (ServerPlayerEntity) event.getPlayer();
        var itemInHand = player.getItemInHand(Hand.MAIN_HAND);
        var aura = EnvyAuras.getConfig().auraFromItem(itemInHand);

        if (aura == null) {
            return;
        }

        event.setCanceled(true);

        if (!pixelmon.isOwnedBy(player)) {
            PlatformProxy.sendMessage(player, EnvyAuras.getLocale().getCannotSetOthersAuras());
            return;
        }

        if (pixelmon.getPokemon().getPersistentData().contains("ENVY_AURAS")) {
            itemInHand.shrink(1);
            EnvyAuras.getGraphics().getOverwriteWarningUI().open(EnvyAuras.getPlayerManager().getPlayer(player), aura, pixelmon);
            return;
        }

        pixelmon.getPokemon().getPersistentData().putString("ENVY_AURAS", aura.id());
        PlatformProxy.sendMessage(player, EnvyAuras.getLocale().getAuraSet(),
                Placeholder.simple("%aura%", aura.displayName()),
                Placeholder.simple("%pokemon%", pixelmon.getDisplayName().getString()));
        itemInHand.shrink(1);
    }

}
