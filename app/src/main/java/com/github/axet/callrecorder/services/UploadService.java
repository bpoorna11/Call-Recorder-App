package com.github.axet.callrecorder.services;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("Upload service called " +RecordingService.filename);
        final String file_path =RecordingService.filename ;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                File f = new File(file_path);
                String content_type = getMimeType(f.getPath());
                System.out.println("Content type " + content_type);
                String file_path = f.getAbsolutePath();
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);

                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type", content_type)
                        .addFormDataPart("audio_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                        .build();

                Request request = new Request.Builder()
                        .url("http://192.168.2.30:9011/api/upload")
                        .post(request_body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    if (!response.isSuccessful()) {
                        throw new IOException("Error : " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        t.start();
        stopSelf();
        return START_STICKY;

    }
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
