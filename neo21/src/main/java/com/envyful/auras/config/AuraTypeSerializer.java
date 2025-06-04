package com.envyful.auras.config;

import com.envyful.auras.EnvyAuras;
import com.envyful.auras.particle.AuraRegistry;
import com.envyful.auras.particle.AuraType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class AuraTypeSerializer implements TypeSerializer<AuraType> {

    @Override
    public AuraType deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.raw() == null) {
            return null;
        }

        var id = node.node("id").getString();
        var auraType = AuraRegistry.getAuraClass(id);

        if (auraType == null) {
            EnvyAuras.getLogger().error("Invalid aura type provided: {}", id);
            return null;
        }

        return ObjectMapper.factory().get(auraType).load(node);
    }

    @Override
    public void serialize(Type type, @Nullable AuraType obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node("id").set(obj.id());
        this.save(obj, node);
    }

    @SuppressWarnings("unchecked")
    private <T extends AuraType> void save(T value, ConfigurationNode target) throws SerializationException {
        Class<T> clazz = (Class<T>) value.getClass();
        ObjectMapper.factory().get(clazz).save(value, target);
    }
}
