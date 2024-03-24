package com.apk.analyzer.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Backend {
    public static class SendGetRequest extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... params) {
            String result = "";
            try {
                System.out.println("constructing url");
                URL url = new URL(params[0].toString()); // Replace this with your actual API URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Read response
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                // Close resources
                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Response: " + result);
        }
    }
}
