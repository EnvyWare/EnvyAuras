package com.envyful.auras;

import com.envyful.api.concurrency.UtilLogger;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.gui.factory.ForgeGuiFactory;
import com.envyful.api.forge.platform.ForgePlatformHandler;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.auras.config.EnvyAurasConfig;
import com.envyful.auras.config.EnvyAurasGraphics;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(EnvyAuras.MOD_ID)
public class EnvyAuras {

    public static final String MOD_ID = "envyauras";

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static EnvyAuras instance;

    private ForgePlayerManager playerManager = new ForgePlayerManager();
    private ForgeCommandFactory commandFactory = new ForgeCommandFactory(playerManager);

    private EnvyAurasConfig config;
    private EnvyAurasGraphics graphics;

    public EnvyAuras() {
        instance = this;
        UtilLogger.setLogger(this.LOGGER);
        PlatformProxy.setPlayerManager(this.playerManager);
        PlatformProxy.setHandler(ForgePlatformHandler.getInstance());

        MinecraftForge.EVENT_BUS.register(this);

        GuiFactory.setPlatformFactory(new ForgeGuiFactory());
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        this.reloadConfig();
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {

    }

    public void reloadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(EnvyAurasConfig.class);
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

    public static final EnvyAurasGraphics getGraphics() {
        return instance.graphics;
    }
}
