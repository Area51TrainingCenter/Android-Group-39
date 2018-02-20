package pe.area51.threadsapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_main_thread).setOnClickListener(this);
        findViewById(R.id.button_worker_thread).setOnClickListener(this);
        findViewById(R.id.button_button_async_task).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_main_thread:
                executeLongTaskOnMainThread();
                break;
            case R.id.button_worker_thread:
                executeLongTaskOnWorkerThread();
                break;
            case R.id.button_button_async_task:
                executeLongTaskWithAsyncTask();
                break;
        }
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.progress_title);
        progressDialog.setMessage(getString(R.string.progress_content));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }

    private void executeLongTask() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void executeLongTaskOnMainThread() {
        final ProgressDialog progressDialog = createProgressDialog();
        progressDialog.show();
        executeLongTask();
        progressDialog.dismiss();
    }

    private void executeLongTaskOnWorkerThread() {
        final ProgressDialog progressDialog = createProgressDialog();
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                executeLongTask();
                /*
                Los View se deben modificar sólo desde el hilo principal
                (Main Thread, también conocido como UI Thread).
                */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }

    private void executeLongTaskWithAsyncTask() {
        new AsyncTask<Void, Void, Boolean>() {

            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = createProgressDialog();
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                executeLongTask();
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                progressDialog.dismiss();
            }
        }.execute();
    }
}
