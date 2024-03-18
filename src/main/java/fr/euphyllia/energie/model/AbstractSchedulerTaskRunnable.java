package fr.euphyllia.energie.model;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSchedulerTaskRunnable {
    protected SchedulerTaskInter task;

    /**
     * Returns true if this task has been cancelled.
     *
     * @return true if the task has been cancelled
     * @throws IllegalStateException if task was not scheduled yet
     */
    protected synchronized boolean isCancelled() throws IllegalStateException {
        checkScheduled();
        return task.isCancelled();
    }

    /**
     * Attempts to cancel this task.
     *
     * @throws IllegalStateException if task was not scheduled yet
     */
    public abstract void cancel() throws IllegalStateException;

    @NotNull
    public abstract SchedulerTaskInter runTask(@NotNull Plugin plugin, SchedulerType schedulerType) throws IllegalArgumentException, IllegalStateException;

    public abstract @NotNull SchedulerTaskInter runTask(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk);

    public abstract @NotNull SchedulerTaskInter runTask(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Location location);

    public abstract @NotNull SchedulerTaskInter runTask(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Entity entity, @Nullable Runnable retired);

    @NotNull
    public abstract SchedulerTaskInter runDelayed(@NotNull Plugin plugin, SchedulerType schedulerType, long delay) throws IllegalArgumentException, IllegalStateException;

    public abstract @NotNull SchedulerTaskInter runDelayed(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, long delayTicks);

    public abstract @NotNull SchedulerTaskInter runDelayed(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Location location, long delayTicks);

    public abstract @NotNull SchedulerTaskInter runDelayed(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Entity entity, @Nullable Runnable retired, long delayTicks);

    @NotNull
    public abstract SchedulerTaskInter runAtFixedRate(@NotNull Plugin plugin, SchedulerType schedulerType, long delay, long period) throws IllegalArgumentException, IllegalStateException;

    public abstract @NotNull SchedulerTaskInter runAtFixedRate(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, long initialDelayTicks, long periodTicks);

    public abstract @NotNull SchedulerTaskInter runAtFixedRate(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Location location, long initialDelayTicks, long periodTicks);

    public abstract @NotNull SchedulerTaskInter runAtFixedRate(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Entity entity, @Nullable Runnable retired, long initialDelayTicks, long periodTicks);

    /**
     * Gets the task id for this runnable.
     *
     * @return the task id that this runnable was scheduled as
     * @throws IllegalStateException if task was not scheduled yet
     */
    protected synchronized int getTaskId() throws IllegalStateException {
        checkScheduled();
        return task.getTaskId();
    }

    protected void checkScheduled() {
        if (task == null) {
            throw new IllegalStateException("Not scheduled yet");
        }
    }

    protected void checkNotYetScheduled() {
        if (task != null) {
            throw new IllegalStateException("Already scheduled as " + task.getTaskId());
        }
    }

    @NotNull
    protected SchedulerTaskInter setupTask(@NotNull final SchedulerTaskInter task) {
        this.task = task;
        return task;
    }
}
