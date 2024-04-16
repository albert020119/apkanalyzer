package com.apk.analyzer.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Backend {

    public static String sendGetRequest(String urlStr) {
        String result = "";
        try {
            System.out.println("constructing url");
            URL url = new URL(urlStr); // Replace this with your actual API URL
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

    public static String sendPostRequest(String apiUrl, File file) {
        String result = "";
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
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }

            // Close streams
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
