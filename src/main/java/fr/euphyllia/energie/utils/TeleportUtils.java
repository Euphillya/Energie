package fr.euphyllia.energie.utils;

import fr.euphyllia.energie.Energie;
import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.CompletableFuture;

public class TeleportUtils {
    public static CompletableFuture<Boolean> teleportAsync(Entity entity, Location location) {
        return Energie.isPaper() && hasTeleportAsync() ? entity.teleportAsync(location) : CompletableFuture.completedFuture(entity.teleport(location));
    }
    public static CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        return Energie.isPaper() && hasTeleportAsync() ? entity.teleportAsync(location, cause) : CompletableFuture.completedFuture(entity.teleport(location, cause));
    }
    public static CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause, TeleportFlag flag) {
        return Energie.isPaper() && hasTeleportAsync() ? entity.teleportAsync(location, cause) : CompletableFuture.completedFuture(entity.teleport(location, cause, flag));
    }

    private static boolean hasTeleportAsync() {
        try {
            Entity.class.getMethod("teleportAsync", Location.class, PlayerTeleportEvent.TeleportCause.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
