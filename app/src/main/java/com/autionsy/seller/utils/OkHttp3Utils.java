package com.autionsy.seller.utils;

import com.autionsy.seller.constant.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp3Utils {
    private static OkHttpClient client = null;

    private OkHttp3Utils() {}

    public static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (OkHttp3Utils.class) {
                if (client == null)
                    client = new OkHttpClient();
            }
        }
        return client;
    }

    /**
     * Get请求
     *
     * @param url
     * @param callback
     */
    public static void doGet(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * Post请求发送键值对数据
     *
     * @param url
     * @param mapParams
     * @param callback
     */
    public static void doPost(String url, Map<String, String> mapParams, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : mapParams.keySet()) {
            builder.add(key, mapParams.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * Post请求发送JSON数据
     *
     * @param url
     * @param jsonParams
     * @param callback
     */
    public static void doPost(String url, String jsonParams, Callback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param pathName
     * @param fileName
     * @param callback
     */
    public static void uploadFile(String url, String pathName, String fileName, Callback callback) {
        //判断文件类型
        MediaType MEDIA_TYPE = MediaType.parse(judgeType(pathName));
        //创建文件参数
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(MEDIA_TYPE.type(), fileName,
                        RequestBody.create(MEDIA_TYPE, new File(pathName)));
        //发出请求参数
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "9199fdef135c122")
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);

    }

    /**
     * 根据文件路径判断MediaType
     *
     * @param path
     * @return
     */
    private static String judgeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 下载文件
     * @param url
     * @param fileDir
     * @param fileName
     */
    public static void downFile(String url, final String fileDir, final String fileName) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(fileDir, fileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) is.close();
                    if (fos != null) fos.close();
                }
            }
        });
    }

    /**
     * Okhttp3参数和文件同时上传
     * */
//    public void uploadFiles(String url, List<File> files){
//        //初始化OkHttpClient
//        OkHttpClient client = new OkHttpClient();
//        // form 表单形式上传
//        MultipartBody.Builder requestBody = new MultipartBody.Builder();
//        requestBody.setType(MultipartBody.FORM);
//        //pathList是文件路径对应的列表
//        if (null != files && files.size() > 0) {
//            for (File file : files) {
//                if (file != null) {
//                    // MediaType.parse() 里面是上传的文件类型。
//                    RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//                    // 参数分别为， 请求key ，文件名称 ， RequestBody
//                    requestBody.addFormDataPart("images", file.getName(), body);
//                }
//            }
//        }
//        //要上传的文字参数
//        Map<String, String> map = new HashMap<>();
//        map.put("param1", "param1" );
//        map.put("param2","param1");
//        if (map != null) {
//            for (String key : map.keySet()) {
//                requestBody.addFormDataPart(key, map.get(key));
//            }
//        }
//        //创建Request对象
//        Request request = new Request.Builder().url(Constants.HTTP_URL)
//                .addHeader("Content-Type", "application/json;charset=UTF-8")//添加header
//                .addHeader("token", "xxxxx").build();
//        // readTimeout("请求超时时间" , 时间单位);
//        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response){
//                if (response.isSuccessful()) {
//
//                } else {
//
//                }
//            }
//        });
//    }

    public void uploadFiles(String url, List<File> files,Callback callback){
        //初始化OkHttpClient
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        //pathList是文件路径对应的列表
        if (null != files && files.size() > 0) {
            for (File file : files) {
                if (file != null) {
                    // MediaType.parse() 里面是上传的文件类型。
                    RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                    // 参数分别为， 请求key ，文件名称 ， RequestBody
                    requestBody.addFormDataPart("images", file.getName(), body);
                }
            }
        }
        //要上传的文字参数
        Map<String, String> map = new HashMap<>();
        map.put("param1", "param1" );
        map.put("param2","param1");
        if (map != null) {
            for (String key : map.keySet()) {
                requestBody.addFormDataPart(key, map.get(key));
            }
        }
        //创建Request对象
        Request request = new Request.Builder().url(Constants.HTTP_URL)
                .addHeader("Content-Type", "application/json;charset=UTF-8")//添加header
                .addHeader("token", "xxxxx").build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(callback);
    }

}
