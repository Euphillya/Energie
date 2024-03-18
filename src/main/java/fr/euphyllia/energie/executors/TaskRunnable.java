package fr.euphyllia.energie.executors;

@FunctionalInterface
public interface TaskRunnable {
    void run(ExecutorsSchedulerTask executorsSchedulerTask);
}
