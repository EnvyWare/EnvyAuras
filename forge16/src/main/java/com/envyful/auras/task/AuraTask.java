package com.envyful.auras.task;

import com.envyful.auras.EnvyAuras;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class AuraTask implements Runnable {
    @Override
    public void run() {
        if (ServerLifecycleHooks.getCurrentServer() == null || ServerLifecycleHooks.getCurrentServer().getAllLevels() == null) {
            return;
        }

        for (var allLevel : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
            for (var allEntity : allLevel.getAllEntities()) {
                if (!(allEntity instanceof PixelmonEntity)) {
                    continue;
                }

                var pixelmon = (PixelmonEntity) allEntity;
                var pokemon = pixelmon.getPokemon();

                if (!pokemon.getPersistentData().contains("ENVY_AURAS")) {
                    continue;
                }

                var aura = EnvyAuras.getConfig().auraFromId(pokemon.getPersistentData().getString("ENVY_AURAS"));

                if (aura == null) {
                    EnvyAuras.getLogger().error("Invalid aura type {} for pokemon with uuid {}", pokemon.getPersistentData().getString("ENVY_AURAS"), pokemon.getUUID());
                    continue;
                }

                if (!aura.shouldDisplay(pixelmon)) {
                    continue;
                }

                aura.type().display(allLevel, pixelmon);
            }
        }
    }
}
