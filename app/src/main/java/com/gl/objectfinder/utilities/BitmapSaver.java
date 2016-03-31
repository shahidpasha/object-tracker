package com.gl.objectfinder.utilities;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by mohamedpasha on 3/30/16.
 */
public class BitmapSaver implements Runnable {
    /**
     * The JPEG image
     */
    private final Bitmap mImageBitmap;
    /**
     * The file we save the image into.
     */
    private final File mFile;

    public BitmapSaver(Bitmap bitmap, File file) {
        mImageBitmap = bitmap;
        mFile = file;
    }

    @Override
    public void run() {
        String root = Environment.getExternalStorageDirectory().toString();

        if (mFile.exists ()) mFile.delete ();
        try {
            FileOutputStream out = new FileOutputStream(mFile);
            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
