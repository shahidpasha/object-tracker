/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gl.objectfinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.gl.objectfinder.R;
import com.gl.objectfinder.fragment.Camera2BasicFragment;
import com.gl.objectfinder.utilities.Constants;

import java.io.File;

public class CameraActivity extends Activity implements Camera2BasicFragment.OnPictureTakenCallBack {

    private Uri outputUri;
    private String TAG = "CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void OnPictureTaken(File pictureFile) {
//        Intent previewIntent = new Intent(CameraActivity.this,PreviewActivity.class);
//        previewIntent.putExtra(PreviewActivity.ARGUMENT_PREVIEW_IMAGE,pictureFile);
//        startActivity(previewIntent);
        gotoPreview(pictureFile);


    }

    /**
     * go to the preview screen
     */

    private void gotoPreview(File pictureFile){
        Intent previewIntent = new Intent(CameraActivity.this,PreviewActivity.class);
        previewIntent.putExtra(PreviewActivity.ARGUMENT_PREVIEW_IMAGE,pictureFile);
        startActivity(previewIntent);
    }


}
