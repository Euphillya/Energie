package fr.euphyllia.energie.folia;

import fr.euphyllia.energie.model.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();

        TaskRunnable taskRunnable = (task) -> {
            mapSchedulerTask.put(task.getTaskId(), task);
            callBack.run(task);
        };

        if (schedulerType.equals(SchedulerType.ASYNC)) {
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getAsyncScheduler().runAtFixedRate(this.plugin, task -> taskRunnable.run(foliaSchedulerTaskRef.get()), initialDelayTicks * 50, periodTicks * 50, TimeUnit.MILLISECONDS),
                    false
            ));
        } else {
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getGlobalRegionScheduler().runAtFixedRate(this.plugin, task -> taskRunnable.run(foliaSchedulerTaskRef.get()), initialDelayTicks, periodTicks),
                    true
            ));
        }

        return foliaSchedulerTaskRef.get();
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        } else {
            initialDelayTicks = Math.max(1, initialDelayTicks);
            periodTicks = Math.max(1, periodTicks);

            TaskRunnable taskRunnable = (task) -> {
                mapSchedulerTask.put(task.getTaskId(), task);
                callBack.run(task);
            };

            final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getRegionScheduler().runAtFixedRate(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task -> taskRunnable.run(foliaSchedulerTaskRef.get()), initialDelayTicks, periodTicks),
                    true
            ));

            return foliaSchedulerTaskRef.get();
        }
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long initialDelayTicks, long periodTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        } else {
            initialDelayTicks = Math.max(1, initialDelayTicks);
            periodTicks = Math.max(1, periodTicks);

            TaskRunnable taskRunnable = (task) -> {
                mapSchedulerTask.put(task.getTaskId(), task);
                callBack.run(task);
            };

            final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getRegionScheduler().runAtFixedRate(this.plugin, location, task -> taskRunnable.run(foliaSchedulerTaskRef.get()), initialDelayTicks, periodTicks),
                    true
            ));

            return foliaSchedulerTaskRef.get();
        }
    }

    @Override
    public SchedulerTaskInter runAtFixedRate(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        } else {
            initialDelayTicks = Math.max(1, initialDelayTicks);
            periodTicks = Math.max(1, periodTicks);

            TaskRunnable taskRunnable = (task) -> {
                mapSchedulerTask.put(task.getTaskId(), task);
                callBack.run(task);
            };

            final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    entity.getScheduler().runAtFixedRate(this.plugin, task -> taskRunnable.run(foliaSchedulerTaskRef.get()), retired, initialDelayTicks, periodTicks),
                    true
            ));

            return foliaSchedulerTaskRef.get();
        }
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack, long delayTicks) {
        delayTicks = Math.max(1, delayTicks);
        final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();

        TaskRunnable taskRunnable = (task) -> {
            mapSchedulerTask.put(task.getTaskId(), task);
            callBack.run(task);
        };

        if (schedulerType.equals(SchedulerType.ASYNC)) {
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getAsyncScheduler().runDelayed(this.plugin, task -> taskRunnable.run(foliaSchedulerTaskRef.get()), delayTicks * 50, TimeUnit.MILLISECONDS),
                    false
            ));
        } else {
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getGlobalRegionScheduler().runDelayed(this.plugin, task -> taskRunnable.run(foliaSchedulerTaskRef.get()), delayTicks),
                    true
            ));
        }

        return foliaSchedulerTaskRef.get();
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack, long delayTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runDelayed(schedulerType, callBack, delayTicks);
        } else {
            delayTicks = Math.max(1, delayTicks);

            final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();

            TaskRunnable taskRunnable = (task) -> {
                mapSchedulerTask.put(task.getTaskId(), task);
                callBack.run(task);
            };
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getRegionScheduler().runDelayed(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task -> taskRunnable.run(foliaSchedulerTaskRef.get()), delayTicks),
                    true
            ));

            return foliaSchedulerTaskRef.get();
        }
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack, long delayTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runDelayed(schedulerType, callBack, delayTicks);
        } else {
            delayTicks = Math.max(1, delayTicks);

            final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();

            TaskRunnable taskRunnable = (task) -> {
                mapSchedulerTask.put(task.getTaskId(), task);
                callBack.run(task);
            };
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getRegionScheduler().runDelayed(this.plugin, location, task -> taskRunnable.run(foliaSchedulerTaskRef.get()), delayTicks),
                    true
            ));

            return foliaSchedulerTaskRef.get();
        }
    }

    @Override
    public SchedulerTaskInter runDelayed(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired, long delayTicks) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runDelayed(schedulerType, callBack, delayTicks);
        } else {
            delayTicks = Math.max(1, delayTicks);

            final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();

            TaskRunnable taskRunnable = (task) -> {
                mapSchedulerTask.put(task.getTaskId(), task);
                callBack.run(task);
            };
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    entity.getScheduler().runDelayed(this.plugin, task -> taskRunnable.run(foliaSchedulerTaskRef.get()), retired, delayTicks),
                    true
            ));

            return foliaSchedulerTaskRef.get();
        }
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();

        TaskRunnable taskRunnable = (task) -> {
            mapSchedulerTask.put(task.getTaskId(), task);
            callBack.run(task);
        };

        if (schedulerType.equals(SchedulerType.ASYNC)) {
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getAsyncScheduler().runNow(this.plugin, task -> taskRunnable.run(foliaSchedulerTaskRef.get())),
                    false
            ));
        } else {
            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getGlobalRegionScheduler().run(this.plugin, task -> taskRunnable.run(foliaSchedulerTaskRef.get())),
                    true
            ));
        }

        return foliaSchedulerTaskRef.get();
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, MultipleRecords.WorldChunk worldChunk, SchedulerCallBack callBack) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runTask(schedulerType, callBack);
        } else {
            final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();

            TaskRunnable taskRunnable = (task) -> {
                mapSchedulerTask.put(task.getTaskId(), task);
                callBack.run(task);
            };

            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getRegionScheduler().run(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), task -> taskRunnable.run(foliaSchedulerTaskRef.get())),
                    true
            ));

            return foliaSchedulerTaskRef.get();
        }
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, Location location, SchedulerCallBack callBack) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runTask(schedulerType, callBack);
        } else {
            final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();

            TaskRunnable taskRunnable = (task) -> {
                mapSchedulerTask.put(task.getTaskId(), task);
                callBack.run(task);
            };

            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    Bukkit.getRegionScheduler().run(this.plugin, location, task -> taskRunnable.run(foliaSchedulerTaskRef.get())),
                    true
            ));

            return foliaSchedulerTaskRef.get();
        }
    }

    @Override
    public SchedulerTaskInter runTask(@NotNull SchedulerType schedulerType, Entity entity, SchedulerCallBack callBack, @Nullable Runnable retired) {
        if (schedulerType.equals(SchedulerType.ASYNC)) {
            return this.runTask(schedulerType, callBack);
        } else {
            final AtomicReference<FoliaSchedulerTask> foliaSchedulerTaskRef = new AtomicReference<>();

            TaskRunnable taskRunnable = (task) -> {
                mapSchedulerTask.put(task.getTaskId(), task);
                callBack.run(task);
            };

            foliaSchedulerTaskRef.set(new FoliaSchedulerTask(
                    entity.getScheduler().run(this.plugin, task -> taskRunnable.run(foliaSchedulerTaskRef.get()), retired),
                    true
            ));

            return foliaSchedulerTaskRef.get();
        }
    }

    @Override
    public int scheduleSyncDelayed(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        try {
            if (schedulerType.equals(SchedulerType.ASYNC)) {
                return this.runTask(schedulerType, callBack).getTaskId();
            } else {
                Bukkit.getGlobalRegionScheduler().execute(this.plugin, () -> callBack.run(null));
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
            } else {
                Bukkit.getRegionScheduler().execute(this.plugin, worldChunk.world(), worldChunk.chunkX(), worldChunk.chunkZ(), () -> callBack.run(null));
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
            } else {
                Bukkit.getRegionScheduler().execute(this.plugin, location, () -> callBack.run(null));
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
            } else {
                entity.getScheduler().execute(this.plugin, () -> callBack.run(null), retired, delay);
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
    @Deprecated
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, long initialDelayTicks, long periodTicks, SchedulerCallBack callBack) {
        this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
    }

    @Override
    @Deprecated
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLoc, long initialDelayTicks, long periodTicks, SchedulerCallBack callBack) {
        if (chunkOrLoc instanceof Location) {
            this.runAtFixedRate(schedulerType, (Location) chunkOrLoc, callBack, initialDelayTicks, periodTicks);
        } else if (chunkOrLoc instanceof MultipleRecords.WorldChunk) {
            this.runAtFixedRate(schedulerType, (MultipleRecords.WorldChunk) chunkOrLoc, callBack, initialDelayTicks, periodTicks);
        } else if (chunkOrLoc instanceof Chunk) {
            Chunk chunk = (Chunk) chunkOrLoc;
            MultipleRecords.WorldChunk worldChunk = new MultipleRecords.WorldChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
            this.runAtFixedRate(schedulerType, worldChunk, callBack, initialDelayTicks, periodTicks);
        } else {
            this.runAtFixedRate(schedulerType, callBack, initialDelayTicks, periodTicks);
        }
    }

    @Override
    @Deprecated
    public void runAtFixedRate(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLocOrEntity, @Nullable Runnable retired, long initialDelayTicks, long periodTicks, SchedulerCallBack callBack) {
        if (chunkOrLocOrEntity instanceof Entity) {
            Entity entity = (Entity) chunkOrLocOrEntity;
            this.runAtFixedRate(schedulerType, entity, callBack, retired, initialDelayTicks, periodTicks);
        } else {
            this.runAtFixedRate(schedulerType, chunkOrLocOrEntity, initialDelayTicks, periodTicks, callBack);
        }
    }

    @Override
    @Deprecated
    public void runDelayed(@NotNull SchedulerType schedulerType, long delayTicks, SchedulerCallBack callBack) {
        this.runDelayed(schedulerType, callBack, delayTicks);
    }

    @Override
    @Deprecated
    public void runDelayed(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLoc, long delayTicks, SchedulerCallBack callBack) {
        if (chunkOrLoc instanceof Location) {
            this.runDelayed(schedulerType, (Location) chunkOrLoc, callBack, delayTicks);
        } else if (chunkOrLoc instanceof MultipleRecords.WorldChunk) {
            this.runDelayed(schedulerType, (MultipleRecords.WorldChunk) chunkOrLoc, callBack, delayTicks);
        } else if (chunkOrLoc instanceof Chunk) {
            Chunk chunk = (Chunk) chunkOrLoc;
            MultipleRecords.WorldChunk worldChunk = new MultipleRecords.WorldChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
            this.runDelayed(schedulerType, worldChunk, callBack, delayTicks);
        } else {
            this.runDelayed(schedulerType, callBack, delayTicks);
        }
    }

    @Override
    @Deprecated
    public void runDelayed(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLocOrEntity, @Nullable Runnable retired, long delayTicks, SchedulerCallBack callBack) {
        if (chunkOrLocOrEntity instanceof Entity) {
            this.runDelayed(schedulerType, (Entity) chunkOrLocOrEntity, callBack, retired, delayTicks);
        } else {
            this.runDelayed(schedulerType, chunkOrLocOrEntity, delayTicks, callBack);
        }
    }

    @Override
    @Deprecated
    public void execute(@NotNull SchedulerType schedulerType, SchedulerCallBack callBack) {
        this.scheduleSyncDelayed(schedulerType, callBack);
    }

    @Override
    @Deprecated
    public void execute(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLoc, SchedulerCallBack callBack) {
        if (chunkOrLoc instanceof Location) {
            this.scheduleSyncDelayed(schedulerType, (Location) chunkOrLoc, callBack);
        } else if (chunkOrLoc instanceof MultipleRecords.WorldChunk) {
            this.scheduleSyncDelayed(schedulerType, (MultipleRecords.WorldChunk) chunkOrLoc, callBack);
        } else if (chunkOrLoc instanceof Chunk) {
            Chunk chunk = (Chunk) chunkOrLoc;
            MultipleRecords.WorldChunk worldChunk = new MultipleRecords.WorldChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
            this.scheduleSyncDelayed(schedulerType, worldChunk, callBack);
        } else {
            this.scheduleSyncDelayed(schedulerType, callBack);
        }
    }

    @Override
    @Deprecated
    public void execute(@NotNull SchedulerType schedulerType, @Nullable Object chunkOrLocOrEntity, @Nullable Runnable retired, SchedulerCallBack callBack) {
        if (chunkOrLocOrEntity instanceof Entity) {
            this.execute(schedulerType, (Entity) chunkOrLocOrEntity, retired, callBack);
        } else {
            this.execute(schedulerType, chunkOrLocOrEntity, callBack);
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
