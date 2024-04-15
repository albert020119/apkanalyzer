package com.apk.analyzer.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
    public static class SendPostRequest extends AsyncTask<File, Void, String> {

        @Override
        protected String doInBackground(File... files) {
            String apiUrl = "http://192.168.56.1:8000/apk"; // Replace with your API endpoint
            String boundary = "*****"; // Define boundary for multipart/form-data
            String lineEnd = "\r\n";
            String twoHyphens = "--";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());

                // Add file to request
                File file = files[0];
                FileInputStream fileInputStream = new FileInputStream(file);
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Get response
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Close streams
                fileInputStream.close();
                outputStream.flush();
                outputStream.close();
                inputStream.close();

                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("SendPostRequest", "Response: " + result);
        }
    }
}
