package fr.euphyllia.energie.model;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface SchedulerTaskInter {

    @NotNull Plugin getPlugin();

    boolean isCancelled();

    void cancel();

    int getTaskId();

    boolean isSync();
}
