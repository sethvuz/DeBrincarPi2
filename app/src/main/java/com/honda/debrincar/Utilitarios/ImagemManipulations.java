package com.honda.debrincar.Utilitarios;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ImagemManipulations {

    private final static String TAG = "MANIPULAÇÃO_IMAGENS";



    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public static Float setupRotation(byte[] imagemBytes){

        try {
            ExifInterface exif = new ExifInterface(new ByteArrayInputStream(imagemBytes));
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90.0f;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180.0f;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270.0f;
            }
        }
        catch (Exception e) {
            Log.d(TAG,"Erro na Rotação: " + e.getMessage());
        }

        return 0.0f;

    }

    public static byte[] getImageBytes(Activity activity, Uri imageUri){
        byte[] inputData = null;

        try {
            InputStream iStream =activity.getContentResolver().openInputStream(imageUri);
            inputData = getBytes(iStream);
            return inputData;
        }catch (IOException e){
            Log.d(TAG, "Erro de conversão em bytes: " + e.getMessage());
            return inputData;
        }
    }

    private static byte[] getBytes(InputStream iStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
