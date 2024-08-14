package com.iafenvoy.jupiter;

import com.iafenvoy.jupiter.api.JupiterConfigEntry;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Collectors;

public abstract class EntryPointLoader {
    private List<JupiterConfigEntry> entries;

    public List<JupiterConfigEntry> getEntries() {
        if (entries == null) {
            entries = loadEntries();
            Jupiter.LOGGER.info("{} Jupiter entrypoint(s) detected: {}", entries.size(), entries.stream().map(JupiterConfigEntry::getId).map(Identifier::toString).collect(Collectors.joining(", ")));
        }
        return entries;
    }

    protected abstract List<JupiterConfigEntry> loadEntries();
}
