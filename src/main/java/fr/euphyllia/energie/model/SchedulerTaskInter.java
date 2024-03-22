package fr.euphyllia.energie.model;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SchedulerTaskInter {

    @NotNull Plugin getPlugin();

    boolean isCancelled();

    void cancel();

    int getTaskId();

    boolean isSync();

    void setSchedulerTask(@Nullable ScheduledTask interTask);
}
