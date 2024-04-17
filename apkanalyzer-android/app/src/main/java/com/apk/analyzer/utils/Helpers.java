package com.apk.analyzer.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helpers {
    public static String calculateMD5(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(new FileInputStream(file), md);
            byte[] buffer = new byte[8192]; // 8 KB buffer size (adjust as needed)
            while (dis.read(buffer) != -1) {
                // Read file contents to update the digest
            }
            dis.close();
            byte[] digest = md.digest();

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable getIcon(File file, PackageManager pm){
        PackageInfo pi = pm.getPackageArchiveInfo(file.getPath(), 0);
        pi.applicationInfo.sourceDir       = file.getPath();
        pi.applicationInfo.publicSourceDir = file.getPath();
        Drawable APKicon = pi.applicationInfo.loadIcon(pm);
        return APKicon;
    }

    public static Drawable getIcon(String path, PackageManager pm){
        PackageInfo pi = pm.getPackageArchiveInfo(path, 0);
        pi.applicationInfo.sourceDir       = path;
        pi.applicationInfo.publicSourceDir = path;
        Drawable APKicon = pi.applicationInfo.loadIcon(pm);
        return APKicon;
    }
}
