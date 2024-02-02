package com.envyful.auras.gui;

import com.envyful.api.config.type.ConfigInterface;
import com.envyful.api.config.type.ConfigItem;
import com.envyful.api.config.type.ExtendedConfigItem;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.config.UtilConfigInterface;
import com.envyful.api.forge.config.UtilConfigItem;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.gui.factory.GuiFactory;
import com.envyful.api.gui.pane.Pane;
import com.envyful.api.type.Pair;
import com.envyful.auras.EnvyAuras;
import com.envyful.auras.config.Aura;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class OverwriteWarningUI {

    private ConfigInterface guiSettings = ConfigInterface.builder()
            .fillerItem(ConfigItem.builder()
                    .type("minecraft:black_stained_glass_pane")
                    .amount(1)
                    .name(" ")
                    .build())
            .title("&4&lOverwrite Warning")
            .height(3)
            .fillType(ConfigInterface.FillType.BLOCK)
            .build();
    
    private ExtendedConfigItem confirmButton = ExtendedConfigItem.builder()
            .type("minecraft:lime_stained_glass_pane")
            .amount(1)
            .name("&a&lConfirm")
            .positions(Pair.of(6, 1))
            .lore("&7Click to confirm the overwrite")
            .build();

    private ExtendedConfigItem cancelButton = ExtendedConfigItem.builder()
            .type("minecraft:red_stained_glass_pane")
            .amount(1)
            .name("&c&lCancel")
            .positions(Pair.of(2, 1))
            .lore("&7Click to cancel the overwrite")
            .build();

    private Pair<Integer, Integer> itemPosition = Pair.of(4, 1);

    public OverwriteWarningUI() {
    }

    public void open(ForgeEnvyPlayer player, Aura aura, PixelmonEntity pixelmon) {
        Pane pane = GuiFactory.paneBuilder()
                .topLeftY(0)
                .topLeftX(0)
                .height(guiSettings.getHeight())
                .width(9)
                .build();

        UtilConfigInterface.fillBackground(pane, guiSettings);

        pane.set(this.itemPosition.getX(), this.itemPosition.getY(),
                GuiFactory.displayable(EnvyAuras.getConfig().auraFromPokemon(pixelmon).getItem(1)));

        UtilConfigItem.builder()
                .singleClick()
                .asyncClick(false)
                .clickHandler((envyPlayer, clickType) -> {
                    player.getParent().closeContainer();
                    pixelmon.getPokemon().getPersistentData().putString("ENVY_AURAS", aura.id());
                    //TODO: message
                })
                .extendedConfigItem(player, pane, this.confirmButton);

        UtilConfigItem.builder()
                .singleClick()
                .asyncClick(false)
                .clickHandler((envyPlayer, clickType) -> {
                    player.getParent().closeContainer();
                    player.getParent().inventory.add(aura.getItem(1));
                    //TODO: message
                })
                .extendedConfigItem(player, pane, this.cancelButton);


        GuiFactory.guiBuilder()
                .height(this.guiSettings.getHeight())
                .title(UtilChatColour.colour(this.guiSettings.getTitle()))
                .addPane(pane)
                .setPlayerManager(EnvyAuras.getPlayerManager())
                .build().open(player);
    }
}
