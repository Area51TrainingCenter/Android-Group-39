package pe.area51.cameraproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView previewImageView;

    private Uri pictureContentUri;

    public static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.previewImageView = (ImageView) findViewById(R.id.activity_main_imageview_preview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_take_photo) {
            takePhoto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                setPicture();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void takePhoto() {
        dispatchTakePhotoIntent();
    }

    private Bitmap setPicture() throws FileNotFoundException {
        final int imageViewWidth = previewImageView.getWidth();
        final int imageViewHeight = previewImageView.getHeight();

        final BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(pictureContentUri),
                null,
                bitmapFactoryOptions
        );

        final int bitmapWidth = bitmapFactoryOptions.outWidth;
        final int bitmapHeight = bitmapFactoryOptions.outHeight;

        final int scaleFactor = Math.min(bitmapWidth / imageViewWidth, bitmapHeight / imageViewHeight);

        bitmapFactoryOptions.inJustDecodeBounds = false;
        bitmapFactoryOptions.inSampleSize = scaleFactor;
        bitmapFactoryOptions.inPurgeable = true;

        final Bitmap decodedBitmap = BitmapFactory.decodeStream(
                getContentResolver().openInputStream(pictureContentUri),
                null,
                bitmapFactoryOptions
        );

        previewImageView.setImageBitmap(decodedBitmap);

        return decodedBitmap;
    }

    private Uri createTempPictureContentUri() throws IOException {
        final String fileName = "picture_" + System.currentTimeMillis();
        final File storageDir = new File(getCacheDir(), "taked_photos");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        final File pictureFile = File.createTempFile(fileName, ".jpg", storageDir);
        return FileProvider.getUriForFile(this, "pe.area51.cameraproject.FileProvider", pictureFile);
    }

    private void dispatchTakePhotoIntent() {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isActivityAvailable(this, takePictureIntent)) {
            try {
                pictureContentUri = createTempPictureContentUri();
                startActivityForResult(
                        takePictureIntent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                pictureContentUri
                        ),
                        REQUEST_TAKE_PHOTO);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            MessageDialogFragment.newInstance(
                    getString(R.string.error),
                    getString(R.string.no_camera_app)
            ).show(getSupportFragmentManager(), "alert_dialog");
        }
    }

    private static boolean isActivityAvailable(final Context context, final Intent intent) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    public static class MessageDialogFragment extends DialogFragment {

        private static final String ARG_TITLE = "title";
        private static final String ARG_MESSAGE = "message";

        public static MessageDialogFragment newInstance(final String title, final String message) {
            final MessageDialogFragment fragment = new MessageDialogFragment();
            final Bundle args = new Bundle();
            args.putString(ARG_TITLE, title);
            args.putString(ARG_MESSAGE, message);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final String title = getArguments().getString(ARG_TITLE, "");
            final String message = getArguments().getString(ARG_MESSAGE, "");
            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }
    }
}
