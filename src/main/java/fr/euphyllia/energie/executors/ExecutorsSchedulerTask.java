package fr.euphyllia.energie.executors;

import fr.euphyllia.energie.model.SchedulerTaskInter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;

public class ExecutorsSchedulerTask implements SchedulerTaskInter {

    private final ExecutorService executors;
    private final Plugin plugin;

    public ExecutorsSchedulerTask(Plugin plugin, ExecutorService executors) {
        this.plugin = plugin;
        this.executors = executors;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean isCancelled() {
        return this.executors.isShutdown();
    }

    @Override
    public void cancel() {
        this.executors.shutdown();
    }

    @Override
    public int getTaskId() {
        return this.executors.hashCode();
    }
}
