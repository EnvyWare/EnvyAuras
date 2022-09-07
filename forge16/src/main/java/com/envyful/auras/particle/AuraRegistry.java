package com.envyful.auras.particle;

import com.envyful.auras.EnvyAuras;
import com.envyful.auras.config.EnvyAurasConfig;
import com.google.common.collect.Maps;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Map;

public class AuraRegistry {

    private static final Map<String, AuraData> REGISTERED_AURAS = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public static <T extends AuraType> T createInstance(EnvyAurasConfig.Aura config) {
        if (!REGISTERED_AURAS.containsKey(config.getTypeConfig().getTypeId())) {
            return null;
        }

        return (T) REGISTERED_AURAS.get(config.getTypeConfig().getTypeId()).createInstance(config);
    }

    public static <T extends AuraType> T register(T t) {
        REGISTERED_AURAS.put(t.getId().toLowerCase(Locale.ROOT), new AuraData(t));
        return t;
    }

    public static class AuraData {

        private AuraType basicInstance;
        private Constructor<? extends AuraType> configConstructor;

        public AuraData(AuraType basicInstance) {
            this.basicInstance = basicInstance;

            try {
                this.configConstructor = basicInstance.getClass().getConstructor(EnvyAurasConfig.Aura.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        public String getId() {
            return this.basicInstance.getId().toUpperCase(Locale.ROOT);
        }

        public AuraType createInstance(EnvyAurasConfig.Aura config) {
            try {
                return this.configConstructor.newInstance(config);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
