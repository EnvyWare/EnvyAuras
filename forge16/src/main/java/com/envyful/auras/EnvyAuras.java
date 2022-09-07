package com.envyful.auras;

import com.envyful.api.command.sender.SenderTypeFactory;
import com.envyful.api.concurrency.UtilLogger;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.gui.factory.ForgeGuiFactory;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.auras.command.ForgeEnvyPlayerSenderType;
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

    private static EnvyAuras instance;

    private Logger logger = LogManager.getLogger(MOD_ID);

    private ForgePlayerManager playerManager = new ForgePlayerManager();
    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private EnvyAurasConfig config;
    private EnvyAurasGraphics graphics;

    public EnvyAuras() {
        instance = this;
        UtilLogger.setLogger(this.logger);
        MinecraftForge.EVENT_BUS.register(this);

        GuiFactory.setPlatformFactory(new ForgeGuiFactory());
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        this.reloadConfig();
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        SenderTypeFactory.register(new ForgeEnvyPlayerSenderType());

    }

    public void reloadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(EnvyAurasConfig.class);
            this.graphics = YamlConfigFactory.getInstance(EnvyAurasGraphics.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EnvyAuras getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public ForgePlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public ForgeCommandFactory getCommandFactory() {
        return this.commandFactory;
    }

    public EnvyAurasConfig getConfig() {
        return this.config;
    }

    public EnvyAurasGraphics getGraphics() {
        return this.graphics;
    }
}
