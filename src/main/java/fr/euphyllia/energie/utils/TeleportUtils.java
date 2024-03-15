package fr.euphyllia.energie.utils;

import fr.euphyllia.energie.Energie;
import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.CompletableFuture;

public class TeleportUtils {
    public static CompletableFuture<Boolean> teleportAsync(Entity entity, Location location) {
        return Energie.isFolia() ? entity.teleportAsync(location) : CompletableFuture.completedFuture(entity.teleport(location));
    }
    public static CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        return Energie.isFolia() ? entity.teleportAsync(location, cause) : CompletableFuture.completedFuture(entity.teleport(location, cause));
    }
    public static CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause, TeleportFlag flag) {
        return Energie.isFolia() ? entity.teleportAsync(location, cause) : CompletableFuture.completedFuture(entity.teleport(location, cause, flag));
    }
}
