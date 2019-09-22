package roy.anubhav.core.threading.threadpools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import roy.anubhav.core.threading.BackgroundThreadFactory;
import roy.anubhav.core.threading.callables.ActivityDataLoader;
import roy.anubhav.core.threading.callables.ActivityLogSender;

public class NetworkThreadpool {

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;;

    private final ExecutorService mExecutorService;

    private final BlockingQueue<Runnable> mTaskQueue;

    private static NetworkThreadpool mInstance ;

    NetworkThreadpool(){
        // initialize a queue for the thread pool. New tasks will be added to this queue
        mTaskQueue = new LinkedBlockingQueue<Runnable>();

        // initialize a queue for the thread pool. New tasks will be added to this queue
        mExecutorService =  new ThreadPoolExecutor(NUMBER_OF_CORES,
                NUMBER_OF_CORES*2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mTaskQueue,
                new BackgroundThreadFactory());
    }

    public static NetworkThreadpool getInstance(){
        if(mInstance==null)
            mInstance = new NetworkThreadpool();

        return mInstance;
    }

    public void addActivityLog(ActivityLogSender sender){
        mExecutorService.submit(sender);
    }

    public void fetchDataFromServer(ActivityDataLoader dataLoader){
        mExecutorService.submit(dataLoader);
    }
}
