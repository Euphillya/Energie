package fr.euphyllia.energie;

import fr.euphyllia.energie.executors.ExecutorsScheduler;
import fr.euphyllia.energie.folia.FoliaScheduler;
import fr.euphyllia.energie.legacy.LegacyScheduler;
import fr.euphyllia.energie.model.Scheduler;
import org.bukkit.plugin.Plugin;

public class Energie {

    private final Plugin plugin;
    private final ExecutorsScheduler executorsScheduler;
    private final LegacyScheduler legacyScheduler;
    private final FoliaScheduler foliaScheduler;

    public Energie(Plugin javaPlugin) {
        this.plugin = javaPlugin;
        this.executorsScheduler = new ExecutorsScheduler(this.plugin);
        this.legacyScheduler = new LegacyScheduler(this.plugin);
        this.foliaScheduler = new FoliaScheduler(this.plugin);
    }

    public static boolean isFolia() {
        return hasClass("io.papermc.paper.threadedregions.RegionizedServer");
    }

    public static boolean isPaper() {
        return hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration");
    }

    public Scheduler getScheduler(SchedulerSoft schedulerSoft) {
        if (schedulerSoft == SchedulerSoft.NATIVE) {
            return this.executorsScheduler;
        } else if (schedulerSoft == SchedulerSoft.MINECRAFT) {
            if (isFolia()) {
                return this.foliaScheduler;
            }
            return this.legacyScheduler;
        }
        throw new UnsupportedOperationException();
    }

    public enum SchedulerSoft {
        NATIVE, MINECRAFT
    }
    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }
}