package com.envyful.auras;

import com.envyful.api.concurrency.UtilLogger;
import com.envyful.api.config.ConfigTypeSerializer;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeTaskBuilder;
import com.envyful.api.forge.gui.factory.ForgeGuiFactory;
import com.envyful.api.forge.platform.ForgePlatformHandler;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.auras.command.AuraTabCompleter;
import com.envyful.auras.command.AurasCommand;
import com.envyful.auras.config.*;
import com.envyful.auras.listener.PlayerInteractListener;
import com.envyful.auras.particle.AuraRegistry;
import com.envyful.auras.particle.AuraType;
import com.envyful.auras.requirement.AuraRequirement;
import com.envyful.auras.task.AuraTask;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.init.PixelmonInitEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

@Mod(EnvyAuras.MOD_ID)
public class EnvyAuras {

    public static final String MOD_ID = "envyauras";

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static EnvyAuras instance;

    private ForgePlayerManager playerManager = new ForgePlayerManager();
    private ForgeCommandFactory commandFactory = new ForgeCommandFactory(playerManager);

    private EnvyAurasConfig config;
    private EnvyAurasLocale locale;
    private EnvyAurasGraphics graphics;

    public EnvyAuras() {
        instance = this;

        UtilLogger.setLogger(LOGGER);
        PlatformProxy.setPlayerManager(this.playerManager);
        PlatformProxy.setHandler(ForgePlatformHandler.getInstance());
        GuiFactory.setPlatformFactory(new ForgeGuiFactory());
        GuiFactory.setPlayerManager(this.playerManager);

        ConfigTypeSerializer.register(new AuraTypeSerializer(), AuraType.class);

        AuraRegistry.init();

        MinecraftForge.EVENT_BUS.register(this);
        Pixelmon.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new PlayerInteractListener());

        new ForgeTaskBuilder().task(new AuraTask())
                .delay(100)
                .async(false)
                .interval(1)
                .start();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        this.reloadConfig();
    }

    @SubscribeEvent
    public void onSetupEvent(PixelmonInitEvent event){
        PokemonSpecificationProxy.register(new AuraRequirement());
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        this.commandFactory.registerCompleter(new AuraTabCompleter());
        this.commandFactory.registerInjector(Aura.class, (sender, args) -> {
            var aura = this.config.auraFromId(args[0]);

            if (aura == null) {
                PlatformProxy.sendMessage(sender, List.of("&c&l(!) &cNo aura found with that ID!"));
            }

            return aura;
        });

        this.commandFactory.registerCommand(event.getDispatcher(), this.commandFactory.parseCommand(new AurasCommand()));
    }

    public void reloadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(EnvyAurasConfig.class);
            this.locale = YamlConfigFactory.getInstance(EnvyAurasLocale.class);
            this.graphics = YamlConfigFactory.getInstance(EnvyAurasGraphics.class);
        } catch (IOException e) {
            LOGGER.error("Error loading configs", e);
        }
    }

    public static EnvyAuras getInstance() {
        return instance;
    }

    public static final Logger getLogger() {
        return LOGGER;
    }

    public static final ForgePlayerManager getPlayerManager() {
        return instance.playerManager;
    }

    public static final ForgeCommandFactory getCommandFactory() {
        return instance.commandFactory;
    }

    public static final EnvyAurasConfig getConfig() {
        return instance.config;
    }

    public static final EnvyAurasLocale getLocale() {
        return instance.locale;
    }

    public static final EnvyAurasGraphics getGraphics() {
        return instance.graphics;
    }
}
