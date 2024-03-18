package fr.euphyllia.energie;

import fr.euphyllia.energie.executors.ExecutorsScheduler;
import fr.euphyllia.energie.folia.FoliaScheduler;
import fr.euphyllia.energie.legacy.LegacyScheduler;
import fr.euphyllia.energie.model.Scheduler;
import org.bukkit.plugin.Plugin;

public class Energie {

    private final ExecutorsScheduler executorsScheduler;
    private final LegacyScheduler legacyScheduler;
    private final FoliaScheduler foliaScheduler;

    public Energie(Plugin javaPlugin) {
        this.executorsScheduler = new ExecutorsScheduler(javaPlugin);
        this.legacyScheduler = new LegacyScheduler(javaPlugin);
        this.foliaScheduler = new FoliaScheduler(javaPlugin);
    }

    public static boolean isFolia() {
        return hasClass("io.papermc.paper.threadedregions.RegionizedServer");
    }

    public static boolean isPaper() {
        return hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration");
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    public Scheduler getNativeScheduler() {
        return this.executorsScheduler;
    }

    public Scheduler getMinecraftScheduler() {
        if (isFolia()) {
            return this.foliaScheduler;
        }
        return this.legacyScheduler;
    }

    @Deprecated
    public Scheduler getScheduler(SchedulerSoft schedulerSoft) {
        if (schedulerSoft == SchedulerSoft.NATIVE) {
            return this.getNativeScheduler();
        } else if (schedulerSoft == SchedulerSoft.MINECRAFT) {
            return this.getMinecraftScheduler();
        }
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public enum SchedulerSoft {
        NATIVE, MINECRAFT
    }
}