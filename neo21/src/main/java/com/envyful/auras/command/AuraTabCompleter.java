package com.envyful.auras.command;

import com.envyful.api.command.injector.TabCompleter;
import com.envyful.auras.EnvyAuras;
import com.envyful.auras.config.Aura;
import net.minecraft.commands.CommandSource;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class AuraTabCompleter implements TabCompleter<CommandSource> {
    @Override
    public List<String> getCompletions(CommandSource iCommandSource, String[] strings, Annotation... annotations) {
        return EnvyAuras.getConfig().getAuras().stream().map(Aura::id).collect(Collectors.toList());
    }
}
