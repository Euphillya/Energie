package fr.euphyllia.energie.folia;

import fr.euphyllia.energie.model.*;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class FoliaScheduler implements Scheduler {

    private final Plugin plugin;
    private final ConcurrentHashMap<Integer, SchedulerTaskInter> mapSchedulerTask = new ConcurrentHashMap<>();

    public FoliaScheduler(Plugin pluginBukkit) {
        this.plugin = pluginBukkit;
    }


    @Override
    public @Nullable <T> Future<T> callSyncMethod(SchedulerCallBack callBack) {
        Bukkit.getGlobalRegionScheduler().execute(this.plugin, () -> callBack.run(null));
        return null;
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        initialDelayTicks = Math.max(1, initialDelayTicks);
        periodTicks = Math.max(1, periodTicks);

        FoliaSchedulerTask inter;
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            inter = new FoliaSchedulerTask(null, false);
            ScheduledTask interTask = Bukkit.getAsyncScheduler().runAtFixedRate(this.plugin, task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, initialDelayTicks * 50, periodTicks * 50, TimeUnit.MILLISECONDS);
            inter.setSchedulerTask(interTask);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(this.plugin,  task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, initialDelayTicks, periodTicks);
            inter.setSchedulerTask(interTask);
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
        return inter;
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            initialDelayTicks = Math.max(1, initialDelayTicks);
            periodTicks = Math.max(1, periodTicks);

            FoliaSchedulerTask inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = Bukkit.getRegionScheduler().runAtFixedRate(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, initialDelayTicks, periodTicks);
            inter.setSchedulerTask(interTask);
            return inter;
        }  else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            initialDelayTicks = Math.max(1, initialDelayTicks);
            periodTicks = Math.max(1, periodTicks);

            FoliaSchedulerTask inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = Bukkit.getRegionScheduler().runAtFixedRate(this.plugin, location, task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, initialDelayTicks, periodTicks);
            inter.setSchedulerTask(interTask);
            return inter;
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            initialDelayTicks = Math.max(1, initialDelayTicks);
            periodTicks = Math.max(1, periodTicks);

            FoliaSchedulerTask inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = entity.getScheduler().runAtFixedRate(this.plugin, task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, retired, initialDelayTicks, periodTicks);
            inter.setSchedulerTask(interTask);
            return inter;
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delayTicks) {
        delayTicks = Math.max(1, delayTicks);

        FoliaSchedulerTask inter;
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            inter = new FoliaSchedulerTask(null, false);
            ScheduledTask interTask = Bukkit.getAsyncScheduler().runDelayed(this.plugin, task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, delayTicks * 50, TimeUnit.MILLISECONDS);
            inter.setSchedulerTask(interTask);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = Bukkit.getGlobalRegionScheduler().runDelayed(this.plugin,  task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, delayTicks);
            inter.setSchedulerTask(interTask);
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
        return inter;
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delayTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runDelayed(schedulerType, callBack, delayTicks);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            delayTicks = Math.max(1, delayTicks);

            FoliaSchedulerTask inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = Bukkit.getRegionScheduler().runDelayed(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, delayTicks);
            inter.setSchedulerTask(interTask);
            return inter;
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delayTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runDelayed(schedulerType, callBack, delayTicks);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            delayTicks = Math.max(1, delayTicks);

            FoliaSchedulerTask inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = Bukkit.getRegionScheduler().runDelayed(this.plugin, location, task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, delayTicks);
            inter.setSchedulerTask(interTask);
            return inter;
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delayTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runDelayed(schedulerType, callBack, delayTicks);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            delayTicks = Math.max(1, delayTicks);

            FoliaSchedulerTask inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = entity.getScheduler().runDelayed(this.plugin, task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, retired, delayTicks);
            inter.setSchedulerTask(interTask);
            return inter;
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        FoliaSchedulerTask inter;
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            inter = new FoliaSchedulerTask(null, false);
            ScheduledTask interTask = Bukkit.getAsyncScheduler().runNow(this.plugin, task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            });
            inter.setSchedulerTask(interTask);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = Bukkit.getGlobalRegionScheduler().run(this.plugin,  task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            });
            inter.setSchedulerTask(interTask);
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
        return inter;
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runTask(schedulerType, callBack);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            FoliaSchedulerTask inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = Bukkit.getRegionScheduler().run(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            });
            inter.setSchedulerTask(interTask);
            return inter;
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runTask(schedulerType, callBack);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            FoliaSchedulerTask inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = Bukkit.getRegionScheduler().run(this.plugin, location, task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            });
            inter.setSchedulerTask(interTask);
            return inter;
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runTask(schedulerType, callBack);
        } else if (schedulerType.equals(SchedulerType.SYNC)) {
            FoliaSchedulerTask inter = new FoliaSchedulerTask(null, true);
            ScheduledTask interTask = entity.getScheduler().run(this.plugin, task -> {
                mapSchedulerTask.put(task.hashCode(), inter);
                callBack.run(inter);
            }, retired);
            inter.setSchedulerTask(interTask);
            return inter;
        } else  {
            throw new UnsupportedOperationException("SchedulerType not supported");
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        try {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return this.runTask(schedulerType, callBack).getTaskId();
            } else if (schedulerType.equals(SchedulerType.SYNC)) {
                Bukkit.getGlobalRegionScheduler().execute(this.plugin, () -> callBack.run(null));
            } else {
                return -1;
            }
            return 0;
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack) {
        try {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return this.runTask(schedulerType, worldChunk, callBack).getTaskId();
            } else if (schedulerType.equals(SchedulerType.SYNC)) {
                Bukkit.getRegionScheduler().execute(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), () -> callBack.run(null));
            } else {
                return -1;
            }
            return 0;
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack) {
        try {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return this.runTask(schedulerType, location, callBack).getTaskId();
            } else if (schedulerType.equals(SchedulerType.SYNC)) {
                Bukkit.getRegionScheduler().execute(this.plugin, location, () -> callBack.run(null));
            } else {
                return -1;
            }
            return 0;
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delay) {
        try {
            return this.runDelayed(schedulerType, callBack, delay).getTaskId();
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delay) {
        try {
            return this.runDelayed(schedulerType, worldChunk, callBack, delay).getTaskId();
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delay) {
        try {
            return this.runDelayed(schedulerType, location, callBack, delay).getTaskId();
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delay) {
        try {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return this.runDelayed(schedulerType, entity, callBack, retired, delay).getTaskId();
            } else if (schedulerType.equals(SchedulerType.SYNC)) {
                entity.getScheduler().execute(this.plugin, () -> callBack.run(null), retired, delay);
            } else {
                return -1;
            }
            return 0;
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delay, long period) {
        try {
            return this.runAtFixedRate(schedulerType, callBack, delay, period).getTaskId();
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delay, long period) {
        try {
            return this.runAtFixedRate(schedulerType, worldChunk, callBack, delay, period).getTaskId();
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delay, long period) {
        try {
            return this.runAtFixedRate(schedulerType, location, callBack, delay, period).getTaskId();
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    public int scheduleSyncRepeating(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delay, long period) {
        try {
            return this.runAtFixedRate(schedulerType, entity, callBack, retired, delay, period).getTaskId();
        } catch (Exception ignored) {
            return -1;
        }
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
