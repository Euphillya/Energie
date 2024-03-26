package fr.euphyllia.energie.executors;

import fr.euphyllia.energie.model.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ExecutorsScheduler implements Scheduler {

    private final Plugin plugin;
    private final ConcurrentHashMap<Integer, SchedulerTaskInter> mapSchedulerTask = new ConcurrentHashMap<>();

    public ExecutorsScheduler(Plugin pluginBukkit) {
        this.plugin = pluginBukkit;
    }


    @Override
    public @Nullable <T> Future<T> callSyncMethod(SchedulerCallBack callBack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        if (!schedulerType.equals(SchedulerType.ASYNC)) {
            throw new UnsupportedOperationException();
        }
        final AtomicReference<ExecutorsSchedulerTask> executorsSchedulerTaskRef = new AtomicReference<>();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        TaskRunnable taskRunnable = (executorsScheduler) -> {
            mapSchedulerTask.put(executorsScheduler.getTaskId(), executorsScheduler);
            callBack.run(executorsScheduler);
        };

        executorsSchedulerTaskRef.set(new ExecutorsSchedulerTask(this.plugin, executorService));

        executorService.scheduleAtFixedRate(() -> {
            taskRunnable.run(executorsSchedulerTaskRef.get());
        }, initialDelayTicks * 50, periodTicks * 50, TimeUnit.MILLISECONDS);

        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return executorsSchedulerTaskRef.get();
    }

    @Override
    public @Nullable SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
    }

    @Override
    public @Nullable SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
    }

    @Override
    public @Nullable SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
        return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
    }

    @Override
    public @Nullable SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delayTicks) {
        if (!schedulerType.equals(SchedulerType.ASYNC)) {
            throw new UnsupportedOperationException();
        }
        final AtomicReference<ExecutorsSchedulerTask> executorsSchedulerTaskRef = new AtomicReference<>();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        TaskRunnable taskRunnable = (executorsScheduler) -> {
            mapSchedulerTask.put(executorsScheduler.getTaskId(), executorsScheduler);
            callBack.run(executorsScheduler);
        };

        executorsSchedulerTaskRef.set(new ExecutorsSchedulerTask(this.plugin, executorService));

        executorService.schedule(() -> {
            taskRunnable.run(executorsSchedulerTaskRef.get());
        }, delayTicks * 50, TimeUnit.MILLISECONDS);

        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return executorsSchedulerTaskRef.get();
    }

    @Override
    public @Nullable SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delayTicks) {
        return this.runDelayed(schedulerType, callBack, delayTicks);
    }

    @Override
    public @Nullable SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delayTicks) {
        return this.runDelayed(schedulerType, callBack, delayTicks);
    }

    @Override
    public @Nullable SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delayTicks) {
        return this.runDelayed(schedulerType, callBack, delayTicks);
    }

    @Override
    public @Nullable SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        if (!schedulerType.equals(SchedulerType.ASYNC)) {
            throw new UnsupportedOperationException();
        }
        final AtomicReference<ExecutorsSchedulerTask> executorsSchedulerTaskRef = new AtomicReference<>();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        TaskRunnable taskRunnable = (executorsScheduler) -> {
            mapSchedulerTask.put(executorsScheduler.getTaskId(), executorsScheduler);
            callBack.run(executorsScheduler);
        };

        executorsSchedulerTaskRef.set(new ExecutorsSchedulerTask(this.plugin, executorService));

        executorService.execute(() -> {
            taskRunnable.run(executorsSchedulerTaskRef.get());
        });

        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return executorsSchedulerTaskRef.get();
    }

    @Override
    public @Nullable SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack) {
        return this.runTask(schedulerType, callBack);
    }

    @Override
    public @Nullable SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack) {
        return this.runTask(schedulerType, callBack);
    }

    @Override
    public @Nullable SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired) {
        return this.runTask(schedulerType, callBack);
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delay) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delay) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delay) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delay) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delay, long period) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delay, long period) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delay, long period) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delay, long period) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SchedulerTaskInter> getPendingTasks() {
        return new ArrayList<>(mapSchedulerTask.values());
    }

    @Override
    public void cancelAllTask() {
        for (Map.Entry<Integer, SchedulerTaskInter> entry : mapSchedulerTask.entrySet()) {
            SchedulerTaskInter schedulerTaskInter = entry.getValue();
            schedulerTaskInter.cancel();
        }
    }

    @Override
    public void cancelTask(int taskId) {
        SchedulerTaskInter schedulerTask = this.mapSchedulerTask.get(taskId);
        if (schedulerTask == null || schedulerTask.isCancelled()) return;
        schedulerTask.cancel();
    }
}
