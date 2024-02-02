package com.envyful.auras.task;

import com.envyful.auras.EnvyAuras;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.concurrent.atomic.AtomicInteger;

public class AuraTask implements Runnable {

    public static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    public void run() {
        if (ServerLifecycleHooks.getCurrentServer() == null || ServerLifecycleHooks.getCurrentServer().getAllLevels() == null) {
            return;
        }

        COUNTER.incrementAndGet();

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

                try {
                    if (!aura.shouldDisplay(pixelmon)) {
                        continue;
                    }

                    aura.type().display(allLevel, pixelmon);
                } catch (Exception e) { // This has to exist because for some reason setting the uncaught exception handler still isn't logging my errors -_-
                    EnvyAuras.getLogger().error("Failed to display aura for pokemon with uuid {}", pokemon.getUUID(), e);
                }
            }
        }
    }
}
