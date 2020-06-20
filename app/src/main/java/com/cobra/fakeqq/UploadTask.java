package com.cobra.fakeqq;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


class UploadTask extends AsyncTask<String, Integer, String> {
    private ProgressBar mPgBar;
    private TextView mTvProgress;

    public UploadTask(ProgressBar mPgBar, TextView mTvProgress) {
        this.mPgBar = mPgBar;
        this.mTvProgress = mTvProgress;
    }


    @Override
    protected void onPostExecute(String result) {
        //最终结果的显示
        mTvProgress.setText(result);
    }

    @Override
    protected void onPreExecute() {
        //开始前的准备工作
        mTvProgress.setText("loading...");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //显示进度
        mPgBar.setProgress(values[0]);
        mTvProgress.setText("loading..." + values[0] + "%");
    }

    @Override
    protected String doInBackground(String... params) {
        //这里params[0]和params[1]是execute传入的两个参数
        String filePath = params[0];
        String uploadUrl = params[1];
        //下面即手机端上传文件的代码
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(6 * 1000);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(httpURLConnection
                    .getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos
                    .writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                            + filePath.substring(filePath.lastIndexOf("/") + 1)
                            + "\"" + end);
            dos.writeBytes(end);

            //获取文件总大小
            FileInputStream fis = new FileInputStream(filePath);
            long total = fis.available();
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            int length = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
                //获取进度，调用publishProgress()
                length += count;
                publishProgress((int) ((length / (float) total) * 100));
                //这里是测试时为了演示进度,休眠500毫秒，正常应去掉
                Thread.sleep(500);
            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            @SuppressWarnings("unused")
            String result = br.readLine();
            dos.close();
            is.close();
            return "上传成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
    }
}