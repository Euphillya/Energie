package fr.euphyllia.energie.utils;

import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class EntityUtils {

    public static CompletableFuture<Boolean> teleportAsync(@NotNull Entity entity, @NotNull Location location) {
        return TeleportUtils.teleportAsync(entity, location);
    }

    public static CompletableFuture<Boolean> teleportAsync(@NotNull Entity entity, @NotNull Location location, PlayerTeleportEvent.TeleportCause cause) {
        return cause != null ? TeleportUtils.teleportAsync(entity, location, cause) : TeleportUtils.teleportAsync(entity, location);
    }

    public static CompletableFuture<Boolean> teleportAsync(@NotNull Entity entity, @NotNull Location location, PlayerTeleportEvent.TeleportCause cause, TeleportFlag flag) {
        if (cause != null && flag != null) {
            return TeleportUtils.teleportAsync(entity, location, cause, flag);
        } else {
            return cause != null ? TeleportUtils.teleportAsync(entity, location, cause) : TeleportUtils.teleportAsync(entity, location);
        }
    }
}
