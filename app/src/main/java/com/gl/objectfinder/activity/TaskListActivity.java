package com.gl.objectfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.gl.objectfinder.R;
import com.gl.objectfinder.adapter.TasksListAdapter;
import com.gl.objectfinder.asynctask.DetectAsyncTask;
import com.gl.objectfinder.entity.Annotation;
import com.gl.objectfinder.utilities.Utils;

import java.io.File;
import java.util.ArrayList;



public class TaskListActivity extends AppCompatActivity implements DetectAsyncTask.DetectCallBack {

    private ArrayList<Annotation> mObjectsList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Context mContext = this;
    View coordinatorView;//reference for showing snack messages

    private int CREATE_TASK_CODE = 1001;
    private int EDIT_TASK_CODE = 1002;
    public static String ARGUMENT_TODO_ITEM = "todo_item";
    public static String ARGUMENT_ITEM_INDEX = "item_index";
    View mNoItemsView;
    View mProgressView;
    private File mPictureFile;
    public static String ARGUMENT_PREVIEW_IMAGE = "preview_image";
    private Bitmap mSnappedBitmap;
    private String TAG = "TaskListActivity";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(navigationClickListener);

        getData();
        initialise();
        //load the data
        loadData();
    }


    @Override
    public void onBackPressed() {
        gotoHome();
    }

    private View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Log.v(TAG,"navigation click listener");
            gotoHome();

        }
    };

    private void gotoHome(){
        Intent cameraIntent = new Intent(TaskListActivity.this,CameraActivity.class);
        cameraIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(cameraIntent);
        finish();
    }





    /**
     * used to intiialise the acitivty
     */
    private void initialise(){
        mProgressView = findViewById(R.id.task_progress);
        mNoItemsView = findViewById(R.id.tv_no_items);
        coordinatorView = findViewById(R.id.coordinatorview);
        mRecyclerView = (RecyclerView) findViewById(R.id.productslist_recycler_view);
        //mObjectsList = new ArrayList<>();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }


    private void populateData(){


        // specify an adapter (see also next example)
        mAdapter = new TasksListAdapter(mObjectsList, mContext,itemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        if (mObjectsList == null || mObjectsList.size() > 0) {
            mNoItemsView.setVisibility(View.GONE);
        }else {
            mNoItemsView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * get the data from the previous activity
     */
    private void getData(){
        Intent intent = getIntent();
        if (intent != null) {
            mPictureFile = (File)intent.getSerializableExtra(ARGUMENT_PREVIEW_IMAGE);
            mSnappedBitmap = Utils.readBitmapFromFilePath(mPictureFile);
        }
    }

    private void loadData(){
        mProgressView.setVisibility(View.VISIBLE);
        DetectAsyncTask detectAsyncTask = new DetectAsyncTask(mSnappedBitmap,this);
        detectAsyncTask.execute();
    }



    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

        }
    };


    /**
     * Called when the Enroll is successfull
     *
     * @param annotationArrayList
     * @return User entity with the user details
     */
    @Override
    public void onDetectSuccessfull(ArrayList<Annotation> annotationArrayList) {
        mProgressView.setVisibility(View.GONE);
        Log.v(TAG, "on detection success " + annotationArrayList);
        mObjectsList = annotationArrayList;
        populateData();
    }

    /**
     * Called when the Enroll Fails
     *
     * @param errorResponse
     * @return User entity with the error message for failure
     */
    @Override
    public void onDetectFailed(String errorResponse) {
        mProgressView.setVisibility(View.GONE);
        Log.v(TAG, "on detection failure " + errorResponse);
    }




}
