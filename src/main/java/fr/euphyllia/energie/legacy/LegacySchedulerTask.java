package fr.euphyllia.energie.legacy;

import fr.euphyllia.energie.model.SchedulerTaskInter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class LegacySchedulerTask implements SchedulerTaskInter {

    private final BukkitTask task;

    public LegacySchedulerTask(BukkitTask bukkitRunnable) {
        this.task = bukkitRunnable;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.task.getOwner();
    }

    @Override
    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }

    @Override
    public int getTaskId() {
        return this.task.getTaskId();
    }

    @Override
    public boolean isSync() {
        return this.task.isSync();
    }
}
