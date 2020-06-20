package com.cobra.fakeqq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText accountEdit;

    private EditText passwordEdit;
    private int registerResult = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                // 如果账号是admin且密码是123456，就认为登录成功
//                sendRequestWithOkHttp();
//                if (account.equals("admin") && password.equals("123456")) {
                if (account.equals("admin")){
                    Toast.makeText(RegisterActivity.this, "账号不能设置为admin",
                            Toast.LENGTH_SHORT).show();
                }
                else if (password.equals("")){
                    Toast.makeText(RegisterActivity.this, "密码不能为空!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    sendRequestWithOkHttp(new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseData = response.body().string();
                            int result = parseJSONUserWithJSONObject(responseData);
                            if (result==1){
                                Looper.prepare();
                                Toast.makeText(RegisterActivity.this, "注册成功",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Looper.loop();
                            }
                            if (result==2){
                                Looper.prepare();
                                Toast.makeText(RegisterActivity.this, "账号已存在，请登录!",
                                        Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    });
                }
//                    int result = sendRequestWithOkHttp();
//                    if (result==1){
//
//                        Toast.makeText(RegisterActivity.this, "注册成功",
//                                Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
////                        finish();
//                    }
//                    if (result==2){
//
//                        Toast.makeText(RegisterActivity.this, "账号已存在，请登录!",
//                                Toast.LENGTH_SHORT).show();
//
//                    }
//                }
            }
        });
    }


//    private int sendRequestWithOkHttp() {
//        final String account = accountEdit.getText().toString();
//        final String password = passwordEdit.getText().toString();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("account",account)
//                            .add("password",password)
//                            .build();
//                    Request request = new Request.Builder()
//                            .url("http://192.168.0.107:8080/fakeqq/register")
//                            .post(requestBody)
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    registerResult = parseJSONUserWithJSONObject(responseData);
////                    parseJSONWithGSON(responseData);
////                    showResponse(responseData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        return registerResult;
//    }
    private void sendRequestWithOkHttp(final okhttp3.Callback callback) {
        final String account = accountEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("account",account)
                            .add("password",password)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://192.168.0.107:8080/fakeqq/register")
                            .post(requestBody)
                            .build();
                    client.newCall(request).enqueue(callback);
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    registerResult = parseJSONUserWithJSONObject(responseData);
//                    parseJSONWithGSON(responseData);
//                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        return registerResult;
    }

    private int parseJSONUserWithJSONObject(String jsonData) {
        try {
            JSONObject object = new JSONObject(jsonData);
            int status = object.getInt("status");

            if (status == 1) {
                JSONObject userObject = object.getJSONObject("user");
//                    User user = new User();
//                    user.setAccount(object.getString("account"));
                Log.d(TAG, "user account is " + userObject.getString("account"));
                Log.d(TAG, "user password is " + userObject.getString("password"));
                return 1;

            }
            if (status == 0){
                return 0;
            }
            if (status == 2) {
                return 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
