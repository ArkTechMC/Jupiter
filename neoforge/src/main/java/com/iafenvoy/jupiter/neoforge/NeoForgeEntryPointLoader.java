package com.iafenvoy.jupiter.neoforge;

import com.iafenvoy.jupiter.EntryPointLoader;
import com.iafenvoy.jupiter.api.JupiterConfig;
import com.iafenvoy.jupiter.api.JupiterConfigEntry;
import com.iafenvoy.jupiter.util.ReflectUtil;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NeoForgeEntryPointLoader extends EntryPointLoader {
    public static final NeoForgeEntryPointLoader INSTANCE = new NeoForgeEntryPointLoader();

    private static <T> List<T> combine(List<T> l1, List<T> l2) {
        l1.addAll(l2);
        return l1;
    }

    @Override
    protected List<JupiterConfigEntry> loadEntries() {
        final Type configAnnotation = Type.getType(JupiterConfig.class);
        return ModList.get()
                .getAllScanData()
                .stream()
                .map(scanData -> scanData.getAnnotations()
                        .stream()
                        .filter(x -> x.annotationType().equals(configAnnotation))
                        .map(ModFileScanData.AnnotationData::memberName)
                        .map(ReflectUtil::getClassUnsafely)
                        .filter(Objects::nonNull)
                        .map(ReflectUtil::constructUnsafely)
                        .filter(x -> x instanceof JupiterConfigEntry)
                        .map(x -> (JupiterConfigEntry) x)
                        .toList())
                .reduce(new ArrayList<>(), NeoForgeEntryPointLoader::combine);
    }
}
