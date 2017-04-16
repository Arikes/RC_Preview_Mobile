package com.example.testandroid.yjj_demo.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StringUtil {
    public static String inputStream2String(InputStream is)
            throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {  
            while ((line = reader.readLine()) != null) {  
                sb.append(line + "\n");  
            }  
        } catch (IOException e) {
            e.printStackTrace();  
        } finally {  
            try {  
                is.close();  
            } catch (IOException e) {
                e.printStackTrace();  
            }  
        }  
        return sb.toString();  
    } 
}
