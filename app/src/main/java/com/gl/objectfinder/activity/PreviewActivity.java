package com.gl.objectfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gl.objectfinder.R;
import com.gl.objectfinder.adapter.PreviewGridListAdapter;
import com.gl.objectfinder.adapter.TasksListAdapter;
import com.gl.objectfinder.utilities.BitmapSaver;
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
    private ArrayList<String> mPictureArrayList = new ArrayList<>();
    private ArrayList<Bitmap> mBitmapArrayList = new ArrayList<>();
    private LinearLayout mPreviewLinearLayout;
    private Context mContext = this;
    private static int cropCount = 0;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


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
        mRecyclerView = (RecyclerView) findViewById(R.id.productslist_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PreviewGridListAdapter(mBitmapArrayList, mContext);
        mRecyclerView.setAdapter(mAdapter);

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
        Bitmap croppedBitmap = recreateBitmap(mPreviewImage.getCroppedBitmap());
        File picFile = new File(mContext.getExternalFilesDir(null), "pic"+System.currentTimeMillis()+".jpg");
        new Thread(new BitmapSaver(croppedBitmap,picFile)).start();
        mPictureArrayList.add(picFile.getAbsolutePath());
        mBitmapArrayList.add(0, croppedBitmap);
        mAdapter.notifyDataSetChanged();
        cropCount++;

        Log.v(TAG, "cropped bitmap size " + croppedBitmap.getWidth() + "x" + croppedBitmap.getHeight());
//        mPreviewImage.setImageBitmap(croppedBitmap);
        //moveToDetectScreen(croppedBitmap);

    }




    /**
     * Hack to avoid an issue with bitmap creation from the cropper
     * @param bitmap
     * @return
     */
    private Bitmap recreateBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Bitmap outBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return outBitmap;
    }

    public void proceedNext(View view){
        moveToDetectScreen();
    }

    private void moveToDetectScreen(){
        if (mPictureArrayList.size() == 0){
            mPictureArrayList.add(mPictureFile.getAbsolutePath());
        }
        Intent listIntent = new Intent(PreviewActivity.this,ObjectsListActivity.class);
        listIntent.putExtra(ObjectsListActivity.ARGUMENT_PREVIEW_IMAGE, mPictureArrayList);
        startActivity(listIntent);
    }





}
