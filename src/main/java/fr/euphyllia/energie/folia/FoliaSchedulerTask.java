package fr.euphyllia.energie.folia;

import fr.euphyllia.energie.model.SchedulerTaskInter;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FoliaSchedulerTask implements SchedulerTaskInter {

    private final ScheduledTask schedulerTask;
    private boolean isSynchronous = false;

    public FoliaSchedulerTask(ScheduledTask schedulerTask, boolean sync) {
        this.schedulerTask = schedulerTask;
        this.isSynchronous = sync;
    }


    @Override
    public @NotNull Plugin getPlugin() {
        return this.schedulerTask.getOwningPlugin();
    }

    @Override
    public boolean isCancelled() {
        return this.schedulerTask.isCancelled();
    }

    @Override
    public void cancel() {
        this.schedulerTask.cancel();
    }

    @Override
    public int getTaskId() {
        return this.schedulerTask.hashCode();
    }

    @Override
    public boolean isSync() {
        return this.isSynchronous;
    }
}
