package roy.anubhav.core.threading;

import android.os.Process;

import java.util.concurrent.ThreadFactory;

/**
 *
 * Thread factory class that helps creating new disposable threads for each transaction.
 *
 */
public class BackgroundThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);

        // A exception handler is created to log the exception from threads
        thread.setUncaughtExceptionHandler((thread1, ex) -> {
        });
        return thread;
    }
}