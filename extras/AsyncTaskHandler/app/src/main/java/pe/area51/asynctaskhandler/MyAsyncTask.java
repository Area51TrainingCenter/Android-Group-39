package pe.area51.asynctaskhandler;


import android.os.Handler;

public abstract class MyAsyncTask<Result, Progress> {

    private final Handler threadHandler;

    public MyAsyncTask() {
        threadHandler = new Handler();
    }

    protected abstract void onPreExecute();

    protected abstract Result doInBackground();

    protected abstract void onPostExecute(Result result);

    protected void onProgressUpdate(final Progress progress) {
    }

    public void postProgress(final Progress progress) {
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                onProgressUpdate(progress);
            }
        });
    }

    public void execute() {
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                onPreExecute();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Result result = doInBackground();
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                onPostExecute(result);
                            }
                        });
                    }
                }).start();
            }
        });
    }

}
