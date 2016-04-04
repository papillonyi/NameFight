package com.example.zhouyou.namefight;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouyou on 16/3/30.
 */

public class FightTask extends AsyncTask<Integer, Integer, String> {

    String IPADDR="192.168.1.9";
    private Button rock, paper, scissor;
    private TextView result;
    private RoleValue myRoleValue;
    private String PATH = "http://"+IPADDR+":8080/test/servlet/FightServlet";
    private URL url;


    public FightTask(Button rock, Button paper, Button scissor, TextView result, RoleValue myRoleValue) {
        this.rock = rock;
        this.paper = paper;
        this.scissor = scissor;
        this.result = result;
        this.myRoleValue = myRoleValue;
    }

    public String sendPostMessage(Map<String, String> params, String encode) {
        //拼接URL请求
        StringBuffer buffer = new StringBuffer(PATH);
        buffer.append("?");
        try {
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
                }
            }
            buffer.deleteCharAt(buffer.length() - 1);

            System.out.println(buffer.toString());

            //发送请求
            url = new URL(buffer.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            byte[] mydata = buffer.toString().getBytes();
            urlConnection.setRequestProperty("Content-Type", "application/x-www/form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(mydata.length));

            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(mydata);

            //获得服务器响应的结果
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                return changeInputStream(urlConnection.getInputStream(), encode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    //将一个输入流转换为字符串，encode为指定编码
    private String changeInputStream(InputStream inputStream, String encode) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[128];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                result = new String(outputStream.toByteArray(), encode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //execute后先执行pre，该方法在UI线程中
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        rock.setClickable(false);
        paper.setClickable(false);
        scissor.setClickable(false);
        result.setText("please wait your opponent...");
    }

    //然后background后台处理
    @Override
    protected String doInBackground(Integer... params) {
        //System.out.println(params[0]);

        Map<String, String> sendParams = new HashMap<String, String>();
        sendParams.put("TYPE", String.valueOf(params[0]));
        String response = sendPostMessage(sendParams, "utf-8");
        //System.out.println(response);

      //  String response = "lose";
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return response;
    }

    //background结束，该方法在UI线程中
    @Override
    protected void onPostExecute(String response) {
        rock.setClickable(true);
        paper.setClickable(true);
        scissor.setClickable(true);
        System.out.println("返回结果:" + response);
        System.out.println("比较结果:" + response.equals("win"));


        if (response.equals("win")) {
            result.setText("you win! you have " + myRoleValue.gethitPoint());
        } else if (response.equals("draw")) {
            result.setText("draw!");
        } else if (response.equals("lose")) {
            myRoleValue.sethitPoint(myRoleValue.gethitPoint() - myRoleValue.getattack());
            result.setText("you lose!you have " + myRoleValue.gethitPoint());
        } else {
            System.out.println("error");
        }
    }
}
