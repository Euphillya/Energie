package fr.euphyllia.energie.folia;

import fr.euphyllia.energie.model.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FoliaScheduler implements Scheduler {

    private final Plugin plugin;
    private final ConcurrentHashMap<Integer, SchedulerTaskInter> mapSchedulerTask = new ConcurrentHashMap<>();

    public FoliaScheduler(Plugin pluginBukkit) {
        this.plugin = pluginBukkit;
    }


    @Override
    public @Nullable <T> Future<T> callSyncMethod(SchedulerCallBack callBack) {
        Bukkit.getGlobalRegionScheduler().execute(this.plugin, () -> {
            callBack.run(null);
        });
        return null;
    }

    @Override
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks <= 0) {
            initialDelayTicks = 1;
        }
        if (periodTicks <= 0) {
            periodTicks = 1;
        }
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            Bukkit.getAsyncScheduler().runAtFixedRate(this.plugin, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, initialDelayTicks * 50, periodTicks * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(this.plugin, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, initialDelayTicks, periodTicks);
        }
    }

    @Override
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        } else {
            if (initialDelayTicks <= 0) {
                initialDelayTicks = 1;
            }
            if (periodTicks <= 0) {
                periodTicks = 1;
            }
            Bukkit.getRegionScheduler().runAtFixedRate(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, initialDelayTicks, periodTicks);
        }
    }

    @Override
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        } else {
            if (initialDelayTicks <= 0) {
                initialDelayTicks = 1;
            }
            if (periodTicks <= 0) {
                periodTicks = 1;
            }
            Bukkit.getRegionScheduler().runAtFixedRate(this.plugin, location, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, initialDelayTicks, periodTicks);
        }
    }

    @Override
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        } else {
            if (initialDelayTicks <= 0) {
                initialDelayTicks = 1;
            }
            if (periodTicks <= 0) {
                periodTicks = 1;
            }
            entity.getScheduler().runAtFixedRate(this.plugin, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, retired, initialDelayTicks, periodTicks);
        }
    }

    @Override
    public void runDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delayTicks) {
        if (delayTicks <= 0) {
            delayTicks = 1;
        }
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            Bukkit.getAsyncScheduler().runDelayed(this.plugin, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, delayTicks * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getGlobalRegionScheduler().runDelayed(this.plugin, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, delayTicks);
        }
    }

    @Override
    public void runDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delayTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            this.runDelayed(schedulerType, callBack, delayTicks);
        } else {
            if (delayTicks <= 0) {
                delayTicks = 1;
            }
            Bukkit.getRegionScheduler().runDelayed(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, delayTicks);
        }
    }

    @Override
    public void runDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delayTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            this.runDelayed(schedulerType, callBack, delayTicks);
        } else {
            if (delayTicks <= 0) {
                delayTicks = 1;
            }
            Bukkit.getRegionScheduler().runDelayed(this.plugin, location, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, delayTicks);
        }
    }

    @Override
    public void runDelayed(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delayTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            this.runDelayed(schedulerType, callBack, delayTicks);
        } else {
            if (delayTicks <= 0) {
                delayTicks = 1;
            }
            entity.getScheduler().runDelayed(this.plugin, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, retired, delayTicks);
        }
    }

    @Override
    public void runTask(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            Bukkit.getAsyncScheduler().runNow(this.plugin, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            });
        } else {
            Bukkit.getGlobalRegionScheduler().run(this.plugin, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            });
        }
    }

    @Override
    public void runTask(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            this.runTask(schedulerType, callBack);
        } else {
            Bukkit.getRegionScheduler().run(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            });
        }
    }

    @Override
    public void runTask(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            this.runTask(schedulerType, callBack);
        } else {
            Bukkit.getRegionScheduler().run(this.plugin, location, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            });
        }
    }

    @Override
    public void runTask(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            this.runTask(schedulerType, callBack);
        } else {
            entity.getScheduler().run(this.plugin, task -> {
                SchedulerTaskInter schedulerTask = new FoliaSchedulerTask(task);
                mapSchedulerTask.put(schedulerTask.getTaskId(), schedulerTask);
                callBack.run(schedulerTask);
            }, retired);
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                this.runTask(schedulerType, schedulerTask -> {
                    completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                    callBack.run(schedulerTask);
                });
            } else {
                Bukkit.getGlobalRegionScheduler().execute(this.plugin, () -> {
                    completableFuture.complete(0);
                    callBack.run(null);
                });
            }
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                this.runTask(schedulerType, worldChunk, schedulerTask -> {
                    completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                    callBack.run(schedulerTask);
                });
            } else {
                Bukkit.getRegionScheduler().execute(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), () -> {
                    completableFuture.complete(0);
                    callBack.run(null);
                });
            }
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                this.runTask(schedulerType, location, schedulerTask -> {
                    completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                    callBack.run(schedulerTask);
                });
            } else {
                Bukkit.getRegionScheduler().execute(this.plugin, location, () -> {
                    completableFuture.complete(0);
                    callBack.run(null);
                });
            }
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delay) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            this.runDelayed(schedulerType, schedulerTask -> {
                completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                callBack.run(schedulerTask);
            }, delay);
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delay) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            this.runDelayed(schedulerType, worldChunk, schedulerTask -> {
                completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                callBack.run(schedulerTask);
            }, delay);
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delay) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            this.runDelayed(schedulerType, location, schedulerTask -> {
                completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                callBack.run(schedulerTask);
            }, delay);
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();

    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delay) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                this.runDelayed(schedulerType, entity, schedulerTask -> {
                    completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                    callBack.run(schedulerTask);
                }, retired, delay);
            } else {
                entity.getScheduler().execute(this.plugin, () -> {
                    completableFuture.complete(0);
                    callBack.run(null);
                }, retired, delay);
            }
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delay, long period) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            this.runAtFixedRate(schedulerType, schedulerTask -> {
                completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                callBack.run(schedulerTask);
            }, delay, period);
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delay, long period) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            this.runAtFixedRate(schedulerType, worldChunk, schedulerTask -> {
                completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                callBack.run(schedulerTask);
            }, delay, period);
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delay, long period) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            this.runAtFixedRate(schedulerType, location, schedulerTask -> {
                completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                callBack.run(schedulerTask);
            }, delay, period);
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delay, long period) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        try {
            this.runAtFixedRate(schedulerType, entity, schedulerTask -> {
                completableFuture.complete(schedulerTask != null ? schedulerTask.getTaskId() : 0);
                callBack.run(schedulerTask);
            }, retired, delay, period);
        } catch (Exception ignored) {
            completableFuture.complete(-1);
        }
        return completableFuture.join();
    }

    @Override @Deprecated
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, long initialDelayTicks, long periodTicks, SchedulerCallBack callBack) {
        this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
    }

    @Override @Deprecated
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLoc, long initialDelayTicks, long periodTicks, SchedulerCallBack callBack) {
        if (chunkOrLoc instanceof Location location) {
            this.runAtFixedRate(schedulerType, location, callBack, initialDelayTicks, periodTicks);
        } else if (chunkOrLoc instanceof MultipleRecords.WorldChunk worldChunk) {
            this.runAtFixedRate(schedulerType, worldChunk, callBack, initialDelayTicks, periodTicks);
        } else if (chunkOrLoc instanceof Chunk chunk) {
            MultipleRecords.WorldChunk worldChunk = new MultipleRecords.WorldChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
            this.runAtFixedRate(schedulerType, worldChunk, callBack, initialDelayTicks, periodTicks);
        } else {
            this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        }
    }

    @Override @Deprecated
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLocOrEntity, @Nullable Runnable retired, long initialDelayTicks, long periodTicks, SchedulerCallBack callBack) {
        if (chunkOrLocOrEntity instanceof Entity entity) {
            this.runAtFixedRate(schedulerType, entity, callBack, retired, initialDelayTicks, periodTicks);
        } else {
            this.runAtFixedRate(schedulerType, chunkOrLocOrEntity, initialDelayTicks, periodTicks, callBack);
        }
    }

    @Override @Deprecated
    public void runDelayed(@NotNull SchedulerType schedulerType, long delayTicks, SchedulerCallBack callBack) {
        this.runDelayed(schedulerType, callBack, delayTicks);
    }

    @Override @Deprecated
    public void runDelayed(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLoc, long delayTicks, SchedulerCallBack callBack) {
        if (chunkOrLoc instanceof Location location) {
            this.runDelayed(schedulerType, location, callBack, delayTicks);
        } else if (chunkOrLoc instanceof MultipleRecords.WorldChunk worldChunk) {
            this.runDelayed(schedulerType, worldChunk, callBack, delayTicks);
        } else if (chunkOrLoc instanceof Chunk chunk) {
            MultipleRecords.WorldChunk worldChunk = new MultipleRecords.WorldChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
            this.runDelayed(schedulerType, worldChunk, callBack, delayTicks);
        } else {
            this.runDelayed(schedulerType, callBack, delayTicks);
        }
    }

    @Override @Deprecated
    public void runDelayed(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLocOrEntity, @Nullable Runnable retired, long delayTicks, SchedulerCallBack callBack) {
        if (chunkOrLocOrEntity instanceof Entity entity) {
            this.runDelayed(schedulerType, entity, callBack, retired, delayTicks);
        } else {
            this.runDelayed(schedulerType, chunkOrLocOrEntity, delayTicks, callBack);
        }
    }

    @Override @Deprecated
    public void execute(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        this.scheduleSyncDelayed(schedulerType, callBack);
    }

    @Override @Deprecated
    public void execute(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLoc, SchedulerCallBack callBack) {
        if (chunkOrLoc instanceof Location location) {
            this.scheduleSyncDelayed(schedulerType, location, callBack);
        } else if (chunkOrLoc instanceof MultipleRecords.WorldChunk worldChunk) {
            this.scheduleSyncDelayed(schedulerType, worldChunk, callBack);
        } else if (chunkOrLoc instanceof Chunk chunk) {
            MultipleRecords.WorldChunk worldChunk = new MultipleRecords.WorldChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
            this.scheduleSyncDelayed(schedulerType, worldChunk, callBack);
        } else {
            this.scheduleSyncDelayed(schedulerType, callBack);
        }
    }

    @Override @Deprecated
    public void execute(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLocOrEntity, @Nullable Runnable retired, SchedulerCallBack callBack) {
        if (chunkOrLocOrEntity instanceof Entity entity) {
            this.execute(schedulerType, entity, retired, callBack);
        } else {
            this.execute(schedulerType, chunkOrLocOrEntity, callBack);
        }
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
