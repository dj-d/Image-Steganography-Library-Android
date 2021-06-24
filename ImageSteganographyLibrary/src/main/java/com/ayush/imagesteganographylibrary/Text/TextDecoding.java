package com.ayush.imagesteganographylibrary.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Utils.Utility;

import java.util.List;

/**
 * In this class all those method in EncodeDecode class are used to decode secret message in image.
 * All the tasks will run in background.
 */
public class TextDecoding extends AsyncTask<ImageSteganography, Void, ImageSteganography> {

    //Tag for Log
    private final static String TAG = TextDecoding.class.getName();

    private final ImageSteganography result;
    //Callback interface for AsyncTask
    private final TextDecodingCallback textDecodingCallback;

    public TextDecoding(TextDecodingCallback textDecodingCallback) {
        super();
        this.textDecodingCallback = textDecodingCallback;
        //making result object
        this.result = new ImageSteganography();
    }

    //pre execution of method
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ImageSteganography imageSteganography) {
        super.onPostExecute(imageSteganography);

        //sending result to callback
        textDecodingCallback.onCompleteTextEncoding(result);
    }

    @Override
    protected ImageSteganography doInBackground(ImageSteganography... imageSteganographies) {

        //If it is not already decoded
        if (imageSteganographies.length > 0) {

            ImageSteganography imageSteganography = imageSteganographies[0];

            //getting bitmap image from file
            Bitmap bitmap = imageSteganography.getImage();

            //return null if bitmap is null
//            if (bitmap == null)
//                return null;

            //splitting images
            List<Bitmap> srcEncodedList = Utility.splitImage(bitmap);

            //decoding encrypted zipped message
            String decoded_message = EncodeDecode.decodeMessage(srcEncodedList);

            Log.d(TAG, "Decoded_Message : " + decoded_message);

            //text decoded = true
            if (!Utility.isStringEmpty(decoded_message)) {
                result.setDecoded(true);
            }

            //decrypting the encoded message
            String decrypted_message = ImageSteganography.decryptMessage(decoded_message, imageSteganography.getSecret_key());
            Log.d(TAG, "Decrypted message : " + decrypted_message);

            //If decrypted_message is null it means that the secret key is wrong otherwise secret key is right.
            if (!Utility.isStringEmpty(decrypted_message)) {

                //secret key provided is right
                result.setSecretKeyWrong(false);

                // Set Results

                result.setMessage(decrypted_message);


                //free memory
                for (Bitmap bitm : srcEncodedList)
                    bitm.recycle();

                //Java Garbage Collector
                System.gc();
            }
        }

        return result;
    }
}
