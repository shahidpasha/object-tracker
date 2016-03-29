package com.gl.objectfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gl.objectfinder.R;
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
    private ArrayList<Bitmap> mBitmapArrayList = new ArrayList<>();
    private LinearLayout mPreviewLinearLayout;
    private Context mContext = this;


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
        mPreviewLinearLayout = (LinearLayout)findViewById(R.id.ll_preview);
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
        mBitmapArrayList.add(croppedBitmap);
        addBitmapToPreview(croppedBitmap);


        Log.v(TAG, "cropped bitmap size " + croppedBitmap.getWidth() + "x" + croppedBitmap.getHeight());
//        mPreviewImage.setImageBitmap(croppedBitmap);
        //moveToDetectScreen(croppedBitmap);

    }


    private void addBitmapToPreview(Bitmap bitmap){
        ImageView previewImageView = new ImageView(mContext);
        previewImageView.setImageBitmap(bitmap);
        previewImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //setting image position
        previewImageView.setLayoutParams(new LinearLayout.LayoutParams(
                Utils.dpToPixels(mContext,100),
                LinearLayout.LayoutParams.MATCH_PARENT));
        previewImageView.setPadding(10,0,0,0);
        mPreviewLinearLayout.addView(previewImageView);
    }

    public void proceedNext(View view){
        moveToDetectScreen(mSnappedBitmap);
    }

    private void moveToDetectScreen(Bitmap imageBitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Intent listIntent = new Intent(PreviewActivity.this,ObjectsListActivity.class);
        listIntent.putExtra(ObjectsListActivity.ARGUMENT_PREVIEW_IMAGE, mBitmapArrayList);
        startActivity(listIntent);
    }





}
