package com.example.networktest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String Tag = "MainActivity";

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
            /*sendRequestWithHttpURLConnection();*/
            sendRequestWithOkHttp();
            /*sendRequestWithOkHttp();*/
        }
    }

    private void sendRequestWithHttpURLConnection() {
        Log.d(Tag, "sendRequestWithHttpURLConnection");
        // 再Activity主线程中必须开另外的一个线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*HttpURLConnection这个类专门用于请求HTTP请求*/
                HttpURLConnection connection = null;
                /*请求的数据做一个缓存处理*/
                BufferedReader reader = null;
                try {
                    /*URl是一个网络请求的地址*/
                    URL url = new URL("http://www.baidu.com");
                    Log.d(Tag, url.toString());
                    /*建立一个Http connection*/
                    connection = (HttpURLConnection) url.openConnection();
                    /*connection设置为Get获取 取数据*/
                    connection.setRequestMethod("GET");
                    /*connection设置连接超时时间*/
                    connection.setConnectTimeout(8000);
                    /*connection设置读取超时时间*/
                    connection.setReadTimeout(8000);
                    /*从服务器读取数据放入输入流*/
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    /*输入流的数据放入Buffer中*/
                    reader = new BufferedReader(new InputStreamReader(in));
                     /*Buffer数据放入String里面*/
                    StringBuilder response = new StringBuilder();
                    String line;
                    /*Buffer数据放一行行放入String里面*/
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        Log.d("MainActivity", "sendRequestWithHttpURLConnection");
                    }
                     /*String数据显示到Activity主界面*/
                    showResponse(response.toString());
                } catch (Exception e) {
                     /*异常打印*/
                    e.printStackTrace();
                } finally {
                    /*关闭缓存buffer*/
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    /*关闭网络连接*/
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void sendRequestWithOkHttp() {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    *//*new create a client;*//*
                    OkHttpClient client = new OkHttpClient();
                    *//*create a connecnt request*//*
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            .url("http://www.baidu.com")
                            .build();
                    *//*post a connecnt request and recieve response*//*
                    Response response = client.newCall(request).execute();
                     *//*recieve response Data*//*
                    String responseData = response.body().string();
                     *//*update view in Activity *//*
                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            .url("http://10.0.2.2/get_data.json")
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        HttpUtil.sendOkHttpRequest("http://10.0.2.2/get_data.json", new okhttp3.Callback() {
            /**
             * Called when the request could not be executed due to cancellation, a connectivity problem or
             * timeout. Because networks can fail during an exchange, it is possible that the remote server
             * accepted the request before the failure.
             *
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                responseData=parseJSONWithJSONObject(responseData);
                showResponse(responseData);
            }
        });

        /*
        HttpUtil.sendOkHttpRequest("http://10.0.2.2/get_data.json", new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseJSONWithJSONObject(responseData);
                showResponse(responseData);
            }
        } );*/
    }

    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    // 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG: {
                        if ("app".equals(nodeName)) {
                            Log.d("MainActivity", "id is " + id);
                            Log.d("MainActivity", "name is " + name);
                            Log.d("MainActivity", "version is " + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseXMLWithSAX(String xmlData) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            // 将ContentHandler的实例设置到XMLReader中
            xmlReader.setContentHandler(handler);
            // 开始执行解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String parseJSONWithJSONObject(String jsonData) {
        try {
            String responseData="";
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                responseData+="id="+id+","+"name="+name+","+"version="+version+"\n";
            }
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>() {
        }.getType());
        for (App app : appList) {
            Log.d("MainActivity", "id is " + app.getId());
            Log.d("MainActivity", "name is " + app.getName());
            Log.d("MainActivity", "version is " + app.getVersion());
        }
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                responseText.setText(response);
            }
        });
    }

}
