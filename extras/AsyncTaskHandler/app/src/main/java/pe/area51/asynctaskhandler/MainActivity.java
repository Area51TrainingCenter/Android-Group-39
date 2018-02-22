package pe.area51.asynctaskhandler;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_execute_long_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeLongTaskWithMyAsyncTask();
            }
        });
    }

    private void executeLongTaskWithMyAsyncTask() {
        new MyAsyncTask<Void, Integer>() {
            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = createProgressDialog();
                progressDialog.show();
            }

            @Override
            protected Void doInBackground() {
                executeLongTask();
                postProgress(20);
                executeLongTask();
                postProgress(40);
                executeLongTask();
                postProgress(60);
                executeLongTask();
                postProgress(80);
                executeLongTask();
                postProgress(100);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressDialog.dismiss();
            }

            @Override
            protected void onProgressUpdate(Integer progress) {
                progressDialog.setProgress(progress);
            }
        }.execute();
    }

    private ProgressDialog createProgressDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.progress_title);
        progressDialog.setMessage(getString(R.string.progress_message));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        return progressDialog;
    }

    private void executeLongTask() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
