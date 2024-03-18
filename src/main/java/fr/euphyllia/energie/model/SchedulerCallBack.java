package fr.euphyllia.energie.model;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface SchedulerCallBack {

    void run(@Nullable SchedulerTaskInter schedulerTask);
}
