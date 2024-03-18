## API

Bukkit/Spigot/Paper :

```java
// Example Scheduler
Bukkit.getScheduler().runTask(this.plugin, () -> {
    // code
});
```

Folia :

```java
// Example Scheduler 
Bukkit.getGlobalRegionScheduler().run(this.plugin, task -> {
    // code
});
Bukkit.getRegionScheduler().run(this.plugin, location, task -> {
    // code
});
Entity.getScheduler().run(this.plugin, task -> {
    // code                
}, retired);
```

Energie :

```java
Energie energie = new Energie(this.plugin);

Scheduler minecraftScheduler = energie.getScheduler(SchedulerSoft.MINECRAFT);
// Example getGlobalRegionScheduler
minecraftScheduler.runTask(SchedulerType.SYNC, task -> {
    // code
});
// Example getRegionScheduler
minecraftScheduler.runTask(SchedulerType.SYNC, (location/MultiRecords.WorldChunk), task -> {
    // code
});
// Example EntityScheduler
minecraftScheduler.runTask(SchedulerType.SYNC, entity, task -> {
    // code
}, retired);
```
