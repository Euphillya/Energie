package fr.euphyllia.energie.folia;

@FunctionalInterface
public interface TaskRunnable {
    void run(FoliaSchedulerTask foliaSchedulerTask);
}
