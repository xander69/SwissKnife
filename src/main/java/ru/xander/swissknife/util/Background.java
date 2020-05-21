package ru.xander.swissknife.util;

import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Alexander Shakhov
 */
@SuppressWarnings("UnusedReturnValue")
@Slf4j
public class Background {
    public static <T> Future<T> runTask(Runnable before, Callable<T> job, Runnable after) {
        if (before != null) {
            before.run();
        }

        Task<T> task = new Task<T>() {
            @Override
            protected T call() {
                try {
                    return job.call();
                } catch (Exception e) {
                    Dialog.error("Background task failed", Util.getStackTrace(e));
                    log.error("Background task failed: " + e.getMessage(), e);
                    return null;
                }
            }
        };

        if (after != null) {
            task.setOnSucceeded(event -> after.run());
            task.setOnFailed(event -> after.run());
            task.setOnCancelled(event -> after.run());
        }

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        return task;
    }
}
