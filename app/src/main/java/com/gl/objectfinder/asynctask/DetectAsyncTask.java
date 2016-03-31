package com.gl.objectfinder.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.gl.objectfinder.entity.Annotation;
import com.gl.objectfinder.utilities.Constants;
import com.gl.objectfinder.utilities.Utils;
import com.gl.objectfinder.webservices.RESTClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mohamedpasha on 2/9/16.
 */
public class DetectAsyncTask extends AsyncTask{

    /**
     * Interface for Login REST Callbacks
     * The activity which needs the REST responses need to implement this interface
     */
    public interface DetectCallBack {

        /**
         * Called when the Enroll is successfull
         * @return User entity with the user details
         */
        public void onDetectSuccessfull(ArrayList<Annotation> annotationArrayList);

        /**
         * Called when the Enroll Fails
         * @return User entity with the error message for failure
         */
        public void onDetectFailed(String errorResponse);


    }


    private String imagePath;
    private String TAG = "EnrollAsyncTask";
    private ArrayList<Annotation> annotationArrayList;
    private DetectCallBack detectCallBack;
    private String responseErrorString;

    public DetectAsyncTask(String  imagePath, DetectCallBack callBack){
        this.imagePath = imagePath;
        this.detectCallBack = callBack;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Bitmap bitmap = Utils.readBitmapFromFilePath(new File(imagePath));
        String response = RESTClient.performPostCall(Constants.URL_OBJECT_DETECTION, createRequestJSON(bitmap));
        if (response != null){
            annotationArrayList = parseResponse(response);
        }
        Log.v(TAG,"annotation array list "+annotationArrayList);
        boolean deletedStatus = Utils.deleteFile(imagePath);
        Log.v(TAG,"file "+imagePath+" deleted - "+deletedStatus);
       // RESTServices.multipartPost1(null,bitmap);
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (annotationArrayList != null){
            detectCallBack.onDetectSuccessfull(annotationArrayList);
        }else {
            detectCallBack.onDetectFailed(responseErrorString);
        }
    }

    /**
     * parse the JSONResponse from a String
     * @param response
     * @return
     */
    private ArrayList<Annotation> parseResponse(String response){
        try {
            JSONObject responseJSON = new JSONObject(response);
            if (responseJSON.has("responses")) {
                JSONArray annotationArray = responseJSON.getJSONArray("responses");
                if (annotationArray.length() >= 1){
                    JSONObject annotations = annotationArray.getJSONObject(0);
                    JSONArray annotationsArray = annotations.getJSONArray("labelAnnotations");
                    Type listType = new TypeToken<List<Annotation>>() {}.getType();
                    annotationArrayList = new Gson().fromJson(annotationsArray.toString(), listType);
                }
            }else if (responseJSON.has("error")){
                responseErrorString = responseJSON.toString();
            }
        }catch (JSONException e){
            Log.v(TAG,"json exception "+e.getMessage());
        }
        return annotationArrayList;
    }

    private String createRequestJSON(Bitmap imageBitmap){
        String requestJSONString = null;
        try {
            JSONArray requestsJSONArray = new JSONArray();
            JSONObject requestJSON = new JSONObject();

            JSONObject imageJSON = new JSONObject();
            imageJSON.put("content", Utils.convertImageBitmaptoBase64(imageBitmap));

            JSONArray featuresJSONArray = new JSONArray();

            JSONObject featuresJSON = new JSONObject();
            featuresJSON.put("type","LABEL_DETECTION");
            featuresJSON.put("maxResults","10");

            featuresJSONArray.put(0,featuresJSON);

            requestJSON.put("image",imageJSON);
            requestJSON.put("features",featuresJSONArray);

            requestsJSONArray.put(0,requestJSON);

            JSONObject rootRequest = new JSONObject();
            rootRequest.put("requests",requestsJSONArray);
            requestJSONString = rootRequest.toString();

        }catch (JSONException e){
            Log.v(TAG,"json exception create request json "+e.getMessage());
        }
        Log.v(TAG,"the request JSON is "+requestJSONString);
        return requestJSONString;

    }
}

