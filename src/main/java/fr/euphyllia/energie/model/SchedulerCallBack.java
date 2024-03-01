package fr.euphyllia.energie.model;

import org.jetbrains.annotations.Nullable;

public interface SchedulerCallBack {

    void run(@Nullable SchedulerTaskInter schedulerTask);
}
