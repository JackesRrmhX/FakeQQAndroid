package com.cobra.fakeqq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button login;

    private CheckBox rememberPass;

    private int loginResult = -1;
    OkHttpClient client = new OkHttpClient();
    String account;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember_pass);
        login =  findViewById(R.id.login);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            // 将账号和密码都设置到文本框中
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
//        final int result = sendRequestWithOkHttp();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String account = accountEdit.getText().toString();
//                String password = passwordEdit.getText().toString();
                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();
                // 如果账号是admin且密码是123456，就认为登录成功
//                sendRequestWithOkHttp();
//                if (account.equals("admin") && password.equals("123456")) {
//                int result = sendRequestWithOkHttp();
//                if (result==1){
//                    editor = pref.edit();
//                    if (rememberPass.isChecked()) { // 检查复选框是否被选中
//                        editor.putBoolean("remember_password", true);
//                        editor.putString("account", account);
//                        editor.putString("password", password);
//                    } else {
//                        editor.clear();
//                    }
//                    editor.apply();
//                    Toast.makeText(LoginActivity.this, "登录成功",
//                            Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
////                    finish();
//                } else if (result == 2){
//                    Toast.makeText(LoginActivity.this, "密码错误!请重新输入!",
//                            Toast.LENGTH_SHORT).show();
//                } else if (result == 0){
//                    Toast.makeText(LoginActivity.this, "不存在该账号!请确认账号是否输入正确!",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LoginActivity.this, "登录失败!",
//                            Toast.LENGTH_SHORT).show();
//                }
                sendRequestWithOkHttp(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseData = response.body().string();
                        int result = parseJSONUserWithJSONObject(responseData);
                        if (result==1){
                            editor = pref.edit();
                            if (rememberPass.isChecked()) { // 检查复选框是否被选中
                                editor.putBoolean("remember_password", true);
                                editor.putString("account", account);
                                editor.putString("password", password);
                            } else {
                                editor.clear();
                            }
                            editor.apply();
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, "登录成功",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Looper.loop();
        //                    finish();
                        } else if (result == 2){
                            Toast.makeText(LoginActivity.this, "密码错误!请重新输入!",
                                    Toast.LENGTH_SHORT).show();
                        } else if (result == 0){
                            Toast.makeText(LoginActivity.this, "不存在该账号!请确认账号是否输入正确!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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
////                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("account",account)
//                            .add("password",password)
//                            .build();
//                    Request request = new Request.Builder()
//                            .url("http://192.168.0.107:8080/fakeqq/login")
//                            .post(requestBody)
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    loginResult = parseJSONUserWithJSONObject(responseData);
////                    parseJSONWithGSON(responseData);
////                    showResponse(responseData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        Log.e(TAG, "sendRequestWithOkHttp: "+loginResult );
//        return loginResult;
//    }

    private void sendRequestWithOkHttp(final okhttp3.Callback callback) {
        final String account = accountEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("account",account)
                            .add("password",password)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://192.168.0.107:8080/fakeqq/login")
                            .post(requestBody)
                            .build();
                    client.newCall(request).enqueue(callback);
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    loginResult = parseJSONUserWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Log.e(TAG, "sendRequestWithOkHttp: "+loginResult );
//        return loginResult;
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
