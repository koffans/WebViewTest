package com.example.kof.webviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView Response_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Response_Text = (TextView) findViewById(R.id.Response_Text);
        Button Send_Request = (Button) findViewById(R.id.Send_Request);
        Send_Request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Send_Request:
                Response_Text.setText("");
                SendRequestWithHttpURLConnection();
                break;
            default:
                break;
        }
    }

    private void SendRequestWithHttpURLConnection()
    {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                            URL url = new URL("https://www.baidu.com");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);

                            InputStream in = connection.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null)
                                response.append(line);

                            ShowResponseText(response.toString());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        finally {
                            if (reader != null)
                            {
                                try
                                {
                                    reader.close();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            if (connection != null)
                                connection.disconnect();
                        }
                    }
                }
        ).start();
    }

    private void ShowResponseText(final String response)
    {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Response_Text.setText(response);
                    }
                }

        );
    }
}
