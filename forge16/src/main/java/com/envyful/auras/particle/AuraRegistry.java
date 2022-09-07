package com.envyful.auras.particle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class AuraRegistry {

    private static final Map<String, AuraType> REGISTERED_AURAS = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public static <T extends AuraType> Optional<T> get(String id) {
        return (Optional<T>) Optional.ofNullable(REGISTERED_AURAS.get(id.toLowerCase(Locale.ROOT)));
    }

    public static <T extends AuraType> T register(T t) {
        REGISTERED_AURAS.put(t.getId().toLowerCase(Locale.ROOT), t);
        return t;
    }

    public static List<AuraType> getAll() {
        return Lists.newArrayList(REGISTERED_AURAS.values());
    }
}
