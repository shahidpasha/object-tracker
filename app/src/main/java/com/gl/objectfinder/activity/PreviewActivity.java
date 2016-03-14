package com.gl.objectfinder.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.gl.objectfinder.R;
import com.gl.objectfinder.asynctask.DetectAsyncTask;
import com.gl.objectfinder.entity.Annotation;
import com.gl.objectfinder.utilities.Constants;
import com.gl.objectfinder.utilities.Utils;
import com.isseiaoki.simplecropview.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;


public class PreviewActivity extends AppCompatActivity  {

    //preview image view
    private CropImageView mPreviewImage;
    public static String ARGUMENT_PREVIEW_IMAGE = "preview_image";
    private Bitmap mSnappedBitmap;
    private String TAG = "PreviewActivity";
    private File mPictureFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        //get the data through intent from previous activity
        getData();
        //initialise the activity
        initialise();
    }

    /**
     * initialise the activities
     */
    private void initialise(){
        mPreviewImage = (CropImageView)findViewById(R.id.iv_crop);
        mSnappedBitmap = Utils.readBitmapFromFilePath(mPictureFile);
        mPreviewImage.setImageBitmap(mSnappedBitmap);
        Log.v(TAG, "on pic recieved " + mSnappedBitmap.getWidth() + "x" + mSnappedBitmap.getHeight());

    }




    /**
     * get the data from the previous activity
     */
    private void getData(){
        Intent intent = getIntent();
        if (intent != null) {
            mPictureFile = (File)intent.getSerializableExtra(ARGUMENT_PREVIEW_IMAGE);
        }
    }


    public void cropContinue(View view){
        Bitmap croppedBitmap = mPreviewImage.getCroppedBitmap();
        Log.v(TAG, "cropped bitmap size " + croppedBitmap.getWidth() + "x" + croppedBitmap.getHeight());
//        mPreviewImage.setImageBitmap(croppedBitmap);
        moveToDetectScreen(croppedBitmap);

    }

    public void proceedNext(View view){
        moveToDetectScreen(mSnappedBitmap);
    }

    private void moveToDetectScreen(Bitmap imageBitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Intent listIntent = new Intent(PreviewActivity.this,TaskListActivity.class);
        listIntent.putExtra(TaskListActivity.ARGUMENT_PREVIEW_IMAGE, byteArray);
        startActivity(listIntent);
    }



}
