package com.envyful.auras.requirement;

import com.envyful.auras.EnvyAuras;
import com.envyful.auras.config.Aura;
import com.google.common.collect.Sets;
import com.pixelmonmod.api.pokemon.requirement.AbstractStringPokemonRequirement;
import com.pixelmonmod.api.requirement.Requirement;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.Set;

public class AuraRequirement extends AbstractStringPokemonRequirement {

    private static final Set<String> KEYS = Sets.newHashSet("aura");
    public static final String DEFAULT_VALUE = "none";


    public AuraRequirement() { super(KEYS, DEFAULT_VALUE); }

    public AuraRequirement(String value) { super(KEYS, DEFAULT_VALUE, value); }

    @Override
    public Requirement<Pokemon, PixelmonEntity, String> createInstance(String value) {
        return new AuraRequirement(value);
    }

    @Override
    public boolean isDataMatch(Pokemon pokemon) {
        var aura = EnvyAuras.getConfig().auraFromPokemon(pokemon);

        if (aura == null) {
            return false;
        }

        return aura.id().equalsIgnoreCase(value);
    }

    @Override
    public void applyData(Pokemon pokemon) {
        if(EnvyAuras.getConfig().auraFromId(value) == null) {
            return;
        }

        pokemon.getPersistentData().putString("ENVY_AURAS", value);
    }
}
