package com.envyful.auras.particle;

import com.envyful.auras.EnvyAuras;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class AuraRegistry {

    private static final Map<String, Class<? extends AuraType>> REGISTERED_AURAS = Maps.newHashMap();
    private static final Type DATA_ANNOTATION = Type.getType(AuraData.class);

    public static void init() {
        ModList.get().getAllScanData().stream()
                .map(ModFileScanData::getAnnotations)
                .flatMap(Collection::stream)
                .filter(a -> DATA_ANNOTATION.equals(a.annotationType()))
                .forEach(AuraRegistry::register);
    }

    @SuppressWarnings("unchecked")
    private static void register(ModFileScanData.AnnotationData annotationData) {
        try {
            var typeClass = (Class<? extends AuraType>) Class.forName(annotationData.clazz().getClassName(), true, EnvyAuras.class.getClassLoader());
            var id = annotationData.annotationData().get("value").toString();

            if (id.isEmpty()) {
                EnvyAuras.getLogger().error("AuraType must have AuraData annotation");
                return;
            }

            register(id, typeClass);
        } catch (ClassNotFoundException e) {
            EnvyAuras.getLogger().error("Failed to load aura type class", e);
        }
    }

    public static <T extends AuraType> void register(String id, Class<T> auraClass) {
        REGISTERED_AURAS.put(id.toLowerCase(Locale.ROOT), auraClass);
    }

    public static <T extends AuraType> Class<T> getAuraClass(String id) {
        return (Class<T>) REGISTERED_AURAS.get(id.toLowerCase(Locale.ROOT));
    }
}
