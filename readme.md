## What is Energie ?
Energie is a library allowing you to maintain compatibility between Spigot and Folia. The library is still under development, but can be used.

## Lists of plugins that officially use Energie

| Name    | Github                                         | Download                                                               |
|---------|------------------------------------------------|------------------------------------------------------------------------|
| Skyllia | [Github](https://github.com/Euphillya/Skyllia) | [Modrinth](https://modrinth.com/plugin/skyllia/versions#all-versions)  |

## Setup
```kotlin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val jitpack = "https://jitpack.io"

repositories {
    mavenCentral()
    maven(jitpack)
}

dependencies  {
    implementation("com.github.Euphillya:Energie:TAG")
}

tasks.withType<ShadowJar> {
    relocate("fr.euphyllia.energie", "[your plugin].dependency.energie")
}

```
## API
The API remains quite simple to use:

### Bukkit Implementation
```java
// before bukkit
private void runTaskBukkitExample() {
    // Sync
    Bukkit.getScheduler().runTask(JavaPlugin, Callback);
    // Async
    Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin, Callback);
}

private void runTaskLaterExample() {
    Bukkit.getScheduler().runTaskLater(JavaPlugin, Callback, Long);
}

private void runTaskTimerExample() {
    Bukkit.getScheduler().runTaskTimer(JavaPlugin, Callback, Long, Long);
}

private void runnableExample() {
    new BukkitRunnable() {
        @Override
        public void run() {

        }
    }.runTask(JavaPlugin);
}
```

### Energie Implementation
```java

private @NotNull Energie energie;
private @NotNull Scheduler scheduler;

@Override
public void onEnable() {
    this.energie = new Energie(JavaPlugin);
    this.scheduler = scheduler.getMinecraftScheduler();
}

public Scheduler getScheduler() {
    return scheduler;
}


private void runTaskBukkitExample() {
    // Sync
    getScheduler().runTask(SchedulerType.SYNC, Callback); // GlobalScheduler
    getScheduler().runTask(SchedulerType.SYNC, Location/MultipleRecords.WorldChunk, Callback); // RegionScheduler
    getScheduler().runTask(SchedulerType.SYNC, Entity, Callback, Retired); // EntityScheduler

    // Async
    getScheduler().runTask(SchedulerType.ASYNC, Callback);
}

private void runTaskLaterExample() {
    getScheduler().runDelayed(SchedulerType.SYNC, Callback, Long); // global
    ...
}

private void runTaskTimerExample() {
    getScheduler().runAtFixedRate(SchedulerType.SYNC, Callback, Long, Long);
    ...
}

private void runnableExample() {
    new SchedulerTaskRunnable() {
        @Override
        public void run() {

        }
    }.runTask(JavaPlugin);
}
```
