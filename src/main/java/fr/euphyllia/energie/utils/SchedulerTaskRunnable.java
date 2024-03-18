package fr.euphyllia.energie.utils;

import fr.euphyllia.energie.Energie;
import fr.euphyllia.energie.folia.FoliaSchedulerTask;
import fr.euphyllia.energie.legacy.LegacySchedulerTask;
import fr.euphyllia.energie.model.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public abstract class SchedulerTaskRunnable extends AbstractSchedulerTaskRunnable implements SchedulerRunnable {

    @Override
    public void cancel() throws IllegalStateException {
        task.cancel();
    }

    @Override
    public @NotNull SchedulerTaskInter runTask(@NotNull Plugin plugin, SchedulerType schedulerType) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        if (Energie.isFolia()) {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return setupTask(new FoliaSchedulerTask(Bukkit.getAsyncScheduler().runNow(plugin, task1 -> this.run()), false));
            } else {
                return setupTask(new FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().run(plugin, task1 -> this.run()), true));
            }
        } else {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return setupTask(new LegacySchedulerTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, this::run)));
            } else {
                return setupTask(new LegacySchedulerTask(Bukkit.getScheduler().runTask(plugin, this::run)));
            }
        }
    }

    @Override
    public @NotNull SchedulerTaskInter runTask(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk) {
        if (!Energie.isFolia() || schedulerType.equals(SchedulerType.ASYNC)) {
            return runTask(plugin, schedulerType);
        }
        checkNotYetScheduled();
        return setupTask(new FoliaSchedulerTask(Bukkit.getRegionScheduler().run(plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task1 -> this.run()), true));
    }

    @Override
    public @NotNull SchedulerTaskInter runTask(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Location location) {
        if (!Energie.isFolia() || schedulerType.equals(SchedulerType.ASYNC)) {
            return runTask(plugin, schedulerType);
        }
        checkNotYetScheduled();
        return setupTask(new FoliaSchedulerTask(Bukkit.getRegionScheduler().run(plugin, location, task1 -> this.run()), true));
    }

    @Override
    public @NotNull SchedulerTaskInter runTask(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Entity entity, @Nullable Runnable retired) {
        if (!Energie.isFolia() || schedulerType.equals(SchedulerType.ASYNC)) {
            return runTask(plugin, schedulerType);
        }
        checkNotYetScheduled();
        return setupTask(new FoliaSchedulerTask(entity.getScheduler().run(plugin, task1 -> this.run(), retired), true));
    }

    @Override
    public @NotNull SchedulerTaskInter runDelayed(@NotNull Plugin plugin, SchedulerType schedulerType, long delay) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        if (Energie.isFolia()) {
            delay = Math.max(1, delay);
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return setupTask(new FoliaSchedulerTask(Bukkit.getAsyncScheduler().runDelayed(plugin, task1 -> this.run(), delay * 50, TimeUnit.MILLISECONDS), false));
            } else {
                return setupTask(new FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, task1 -> this.run(), delay), true));
            }
        } else {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return setupTask(new LegacySchedulerTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this::run, delay)));
            } else {
                return setupTask(new LegacySchedulerTask(Bukkit.getScheduler().runTaskLater(plugin, this::run, delay)));
            }
        }
    }

    @Override
    public @NotNull SchedulerTaskInter runDelayed(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, long delayTicks) {
        if (!Energie.isFolia() || schedulerType.equals(SchedulerType.ASYNC)) {
            return runTask(plugin, schedulerType);
        }
        checkNotYetScheduled();
        delayTicks = Math.max(1, delayTicks);
        return setupTask(new FoliaSchedulerTask(Bukkit.getRegionScheduler().runDelayed(plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task1 -> this.run(), delayTicks), true));
    }

    @Override
    public @NotNull SchedulerTaskInter runDelayed(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Location location, long delayTicks) {
        if (!Energie.isFolia() || schedulerType.equals(SchedulerType.ASYNC)) {
            return runTask(plugin, schedulerType);
        }
        checkNotYetScheduled();
        delayTicks = Math.max(1, delayTicks);
        return setupTask(new FoliaSchedulerTask(Bukkit.getRegionScheduler().runDelayed(plugin, location, task1 -> this.run(), delayTicks), true));
    }

    @Override
    public @NotNull SchedulerTaskInter runDelayed(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Entity entity, @Nullable Runnable retired, long delayTicks) {
        checkNotYetScheduled();
        if (!Energie.isFolia() || schedulerType.equals(SchedulerType.ASYNC)) {
            return runTask(plugin, schedulerType);
        }
        checkNotYetScheduled();
        delayTicks = Math.max(1, delayTicks);
        return setupTask(new FoliaSchedulerTask(entity.getScheduler().runDelayed(plugin, task1 -> this.run(), retired, delayTicks), true));
    }

    @Override
    public @NotNull SchedulerTaskInter runAtFixedRate(@NotNull Plugin plugin, SchedulerType schedulerType, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        if (Energie.isFolia()) {
            delay = Math.max(1, delay);
            period = Math.max(1, period);
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return setupTask(new FoliaSchedulerTask(Bukkit.getAsyncScheduler().runAtFixedRate(plugin, task1 -> this.run(), delay * 50, period * 50, TimeUnit.MILLISECONDS), false));
            } else {
                return setupTask(new FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, task1 -> this.run(), delay, period), true));
            }
        } else {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return setupTask(new LegacySchedulerTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::run, delay, period)));
            } else {
                return setupTask(new LegacySchedulerTask(Bukkit.getScheduler().runTaskTimer(plugin, this::run, delay, period)));
            }
        }
    }

    @Override
    public @NotNull SchedulerTaskInter runAtFixedRate(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, long delay, long period) {
        if (!Energie.isFolia() || schedulerType.equals(SchedulerType.ASYNC)) {
            return runTask(plugin, schedulerType);
        }
        checkNotYetScheduled();
        delay = Math.max(1, delay);
        period = Math.max(1, period);
        return setupTask(new FoliaSchedulerTask(Bukkit.getRegionScheduler().runAtFixedRate(plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task1 -> this.run(), delay, period), true));
    }

    @Override
    public @NotNull SchedulerTaskInter runAtFixedRate(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Location location, long delay, long period) {
        if (!Energie.isFolia() || schedulerType.equals(SchedulerType.ASYNC)) {
            return runTask(plugin, schedulerType);
        }
        checkNotYetScheduled();
        delay = Math.max(1, delay);
        period = Math.max(1, period);
        return setupTask(new FoliaSchedulerTask(Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, task1 -> this.run(), delay, period), true));
    }

    @Override
    public @NotNull SchedulerTaskInter runAtFixedRate(@NotNull Plugin plugin, @NotNull SchedulerType schedulerType, Entity entity, @Nullable Runnable retired, long delay, long period) {
        checkNotYetScheduled();
        if (!Energie.isFolia() || schedulerType.equals(SchedulerType.ASYNC)) {
            return runTask(plugin, schedulerType);
        }
        checkNotYetScheduled();
        delay = Math.max(1, delay);
        period = Math.max(1, period);
        return setupTask(new FoliaSchedulerTask(entity.getScheduler().runAtFixedRate(plugin, task1 -> this.run(), retired, delay, period), true));
    }
}
