package com.envyful.auras.listener;

import com.envyful.api.platform.PlatformProxy;
import com.envyful.api.text.Placeholder;
import com.envyful.auras.EnvyAuras;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerInteractListener {

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getTarget() instanceof PixelmonEntity)) {
            return;
        }

        if(event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        var pixelmon = (PixelmonEntity) event.getTarget();
        var player = (ServerPlayer) event.getEntity();
        var itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
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
