package com.gl.objectfinder.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by mohamedpasha on 3/3/16.
 */
public class Utils {

    public static String TAG = "Utils";

    /**
     * read bitmap from a file path
     * @param filePath the path of the image file
     * @return bitmap read from the file path
     */
    public static Bitmap readBitmapFromFilePath(File filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath.toString(), options);
        return bitmap;
    }


    /**
     * converts a bitmap to base64 string
     * @param pictureBitmap the bitmap that needs to be converted
     * @return base64 string
     */

    public static String convertImageBitmaptoBase64(Bitmap pictureBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    /**
     * Converts dp value in pixels
     * @param context
     * @param valueInDP
     * @return
     */
    public static int  dpToPixels(Context context,int valueInDP){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDP, r.getDisplayMetrics());
        return (int)px;
    }
}
