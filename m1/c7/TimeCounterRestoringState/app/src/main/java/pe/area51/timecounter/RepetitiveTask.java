package pe.area51.timecounter;

import android.os.Handler;

public class RepetitiveTask {

    private final long delayInMillis;
    private final Runnable runnable;
    private final Handler handler;

    private boolean isRunning;

    public RepetitiveTask(final Runnable task, final long delayInMillis) {
        this.delayInMillis = delayInMillis;
        this.runnable = new Runnable() {
            @Override
            public void run() {
                task.run();
                handler.postDelayed(this, delayInMillis);
            }
        };
        handler = new Handler();
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start(final boolean executeImmediately) {
        if (!isRunning) {
            if (executeImmediately) {
            /*Es una buena práctica preguntar si estamos en el hilo actual o no,
            * ya que de esta forma sería innecesario llamar al método post del handler.*/
                if (handler.getLooper().getThread().getId() == Thread.currentThread().getId()) {
                    runnable.run();
                } else {
                    handler.post(runnable);
                }
            } else {
                handler.postDelayed(runnable, delayInMillis);
            }
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            handler.removeCallbacks(runnable);
            isRunning = false;
        }
    }

}
