
package com.envyful.auras.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.auras.gui.OverwriteWarningUI;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@ConfigPath("config/EnvyAuras/guis.yml")
public class EnvyAurasGraphics extends AbstractYamlConfig {

    private OverwriteWarningUI overwriteWarningUI = new OverwriteWarningUI();

    public EnvyAurasGraphics() {
        super();
    }

    public OverwriteWarningUI getOverwriteWarningUI() {
        return this.overwriteWarningUI;
    }

}
