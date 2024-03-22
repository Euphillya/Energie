package fr.euphyllia.energie.legacy;

import fr.euphyllia.energie.model.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class LegacyScheduler implements Scheduler {
    private final Plugin plugin;

    public LegacyScheduler(Plugin pluginBukkit) {
        this.plugin = pluginBukkit;
    }

    @Override
    public @Nullable <T> Future<T> callSyncMethod(SchedulerCallBack callBack) {
        return Bukkit.getScheduler().callSyncMethod(this.plugin, () -> {
            callBack.run(null);
            return null;
        });
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        AtomicReference<LegacySchedulerTask> schedulerTaskRef = new AtomicReference<>(null);
        Runnable taskRunnable = () -> callBack.run(schedulerTaskRef.get());

        if (schedulerType.equals(SchedulerType.ASYNC)) {
            schedulerTaskRef.set(new LegacySchedulerTask(Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, taskRunnable, initialDelayTicks, periodTicks)));
        } else {
            schedulerTaskRef.set(new LegacySchedulerTask(Bukkit.getScheduler().runTaskTimer(this.plugin, taskRunnable, initialDelayTicks, periodTicks)));
        }

        return schedulerTaskRef.get();
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
        return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delayTicks) {
        AtomicReference<LegacySchedulerTask> schedulerTaskRef = new AtomicReference<>(null);
        Runnable taskRunnable = () -> callBack.run(schedulerTaskRef.get());

        if (schedulerType.equals(SchedulerType.ASYNC)) {
            schedulerTaskRef.set(new LegacySchedulerTask(Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, taskRunnable, delayTicks)));
        } else {
            schedulerTaskRef.set(new LegacySchedulerTask(Bukkit.getScheduler().runTaskLater(this.plugin, taskRunnable, delayTicks)));
        }

        return schedulerTaskRef.get();
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delayTicks) {
        return this.runDelayed(schedulerType, callBack, delayTicks);
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delayTicks) {
        return this.runDelayed(schedulerType, callBack, delayTicks);
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delayTicks) {
        return this.runDelayed(schedulerType, callBack, delayTicks);
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        AtomicReference<LegacySchedulerTask> schedulerTaskRef = new AtomicReference<>(null);
        Runnable taskRunnable = () -> callBack.run(schedulerTaskRef.get());

        if (schedulerType.equals(SchedulerType.ASYNC)) {
            schedulerTaskRef.set(new LegacySchedulerTask(Bukkit.getScheduler().runTaskAsynchronously(this.plugin, taskRunnable)));
        } else {
            schedulerTaskRef.set(new LegacySchedulerTask(Bukkit.getScheduler().runTask(this.plugin, taskRunnable)));
        }

        return schedulerTaskRef.get();
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack) {
        return this.runTask(schedulerType, callBack);
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack) {
        return this.runTask(schedulerType, callBack);
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired) {
        return this.runTask(schedulerType, callBack);
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return Bukkit.getScheduler().scheduleAsyncDelayedTask(this.plugin, () -> callBack.run(null));
        } else {
            return Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> callBack.run(null));
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack) {
        return this.scheduleSyncDelayed(schedulerType, callBack);
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack) {
        return this.scheduleSyncDelayed(schedulerType, callBack);
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delay) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return Bukkit.getScheduler().scheduleAsyncDelayedTask(this.plugin, () -> callBack.run(null), delay);
        } else {
            return Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> callBack.run(null), delay);
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delay) {
        return this.scheduleSyncDelayed(schedulerType, callBack, delay);
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delay) {
        return this.scheduleSyncDelayed(schedulerType, callBack, delay);
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delay) {
        return this.scheduleSyncDelayed(schedulerType, callBack, delay);
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delay, long period) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return Bukkit.getScheduler().scheduleAsyncRepeatingTask(this.plugin, () -> callBack.run(null), delay, period);
        } else {
            return Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> callBack.run(null), delay, period);
        }
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delay, long period) {
        return this.scheduleSyncRepeating(schedulerType, callBack, delay, period);
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delay, long period) {
        return this.scheduleSyncRepeating(schedulerType, callBack, delay, period);
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delay, long period) {
        return this.scheduleSyncRepeating(schedulerType, callBack, delay, period);
    }

    @Override
    public List<SchedulerTaskInter> getPendingTasks() {
        return Bukkit.getScheduler().getPendingTasks().stream().map(LegacySchedulerTask::new).collect(Collectors.toList());
    }

    @Override
    public void cancelAllTask() {
        Bukkit.getScheduler().cancelTasks(this.plugin);
    }

    @Override
    public void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
