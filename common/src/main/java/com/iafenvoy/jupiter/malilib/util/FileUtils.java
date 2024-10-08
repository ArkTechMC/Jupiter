package com.iafenvoy.jupiter.malilib.util;

import com.google.common.collect.ImmutableSet;
import com.iafenvoy.jupiter.Jupiter;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

public class FileUtils {
    private static final Set<Character> ILLEGAL_CHARACTERS = ImmutableSet.of('/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':');

    public static File getConfigDirectory() {
        return new File(MinecraftClient.getInstance().runDirectory, "config");
    }

    public static File getMinecraftDirectory() {
        return MinecraftClient.getInstance().runDirectory;
    }

    /**
     * Checks that the target directory exists, and the file either doesn't exist,
     * or the canOverwrite argument is true and the file is writable
     *
     */
    public static boolean canWriteToFile(File dir, String fileName, boolean canOverwrite) {
        if (dir.exists() && dir.isDirectory()) {
            File file = new File(dir, fileName);
            return !file.exists() || (canOverwrite && file.isFile() && file.canWrite());
        }

        return false;
    }

    public static File getCanonicalFileIfPossible(File file) {
        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
            Jupiter.LOGGER.error("Failed to get file", e);
        }

        return file;
    }

    public static String getJoinedTrailingPathElements(File file, File rootPath, int maxStringLength, String separator) {
        StringBuilder path = new StringBuilder();

        if (maxStringLength <= 0) {
            return "...";
        }

        while (file != null) {
            String name = file.getName();

            if (!path.isEmpty()) {
                path.insert(0, name + separator);
            } else {
                path = new StringBuilder(name);
            }

            int len = path.length();

            if (len > maxStringLength) {
                path = new StringBuilder("... " + path.substring(len - maxStringLength, len));
                break;
            }

            if (file.equals(rootPath)) {
                break;
            }

            file = file.getParentFile();
        }

        return path.toString();
    }

    public static String getNameWithoutExtension(String name) {
        int i = name.lastIndexOf(".");
        return i != -1 ? name.substring(0, i) : name;
    }

    public static String generateSimpleSafeFileName(String name) {
        return name.toLowerCase(Locale.US).replaceAll("\\W", "_");
    }

    public static String generateSafeFileName(String name) {
        StringBuilder sb = new StringBuilder(name.length());

        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);

            if (!ILLEGAL_CHARACTERS.contains(c)) {
                sb.append(c);
            }
        }

        // Some weird reserved windows keywords apparently... FFS >_>
        return sb.toString().replaceAll("COM", "").replaceAll("PRN", "");
    }
}
