package com.example.tinder2.Account;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public final class MemoryData {
    public static void saveData(String data, Context context) {
        try (FileOutputStream fileOutputStream = context.openFileOutput("data.txt", Context.MODE_PRIVATE)) {
            fileOutputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveConversationLast(String data,String convoID, Context context) {
        try (FileOutputStream fileOutputStream = context.openFileOutput(convoID + ".txt", Context.MODE_PRIVATE)) {
            fileOutputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveName(String data, Context context) {
        try (FileOutputStream fileOutputStream = context.openFileOutput("name.txt", Context.MODE_PRIVATE)) {
            fileOutputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getData(Context context) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = context.openFileInput("data.txt");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getLastConvo(Context context, String chatID) {
        StringBuilder sb = new StringBuilder();
        String data = "";
        try (FileInputStream fis = context.openFileInput(chatID+".txt");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static String getName(Context context) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = context.openFileInput("name.txt");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
