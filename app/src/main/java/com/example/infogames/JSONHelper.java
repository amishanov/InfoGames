package com.example.infogames;

import android.content.Context;

import com.example.infogames.model.Test;
import com.example.infogames.model.Theme;
import com.example.infogames.model.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JSONHelper {
    private static final String FILE_USER = "user.json";
    private static final String FILE_TESTS = "tests.json";
    private static final String FILE_THEMES = "themes.json";

    //TODO сделать нормальную проверку существования файла, а не return через exeption
    public static boolean check(Context context) {
        try(FileInputStream fileInputStream = context.openFileInput(FILE_USER)) {
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean exportUserToJSON(Context context, User user) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(user);
        try(FileOutputStream fileOutputStream =
                    context.openFileOutput(FILE_USER, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static User importUserFromJSON(Context context) {
        try(FileInputStream fileInputStream = context.openFileInput(FILE_USER);
            InputStreamReader streamReader = new InputStreamReader(fileInputStream)){
            Gson gson = new Gson();
            return gson.fromJson(streamReader, User.class);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    private static class TestsItems {
        private List<Test> tests;

        List<Test> getTests() {
            return tests;
        }
        void setTests(List<Test> tests) {
            this.tests = tests;
        }
    }

    public static boolean exportTestsToJSON(Context context, List<Test> tests) {
        Gson gson = new Gson();
        TestsItems testsItems = new TestsItems();
        testsItems.setTests(tests);
        String jsonString = gson.toJson(testsItems);
        try(FileOutputStream fileOutputStream =
                    context.openFileOutput(FILE_TESTS, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<Test> importTestsFromJSON(Context context) {
        try(FileInputStream fileInputStream = context.openFileInput(FILE_TESTS);
            InputStreamReader streamReader = new InputStreamReader(fileInputStream)){
            Gson gson = new Gson();
            TestsItems testsItems = gson.fromJson(streamReader, TestsItems.class);
            return testsItems.getTests();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    private static class ThemesItems {
        private List<Theme> themes;

        List<Theme> getThemes() {
            return themes;
        }
        void setThemes(List<Theme> themes) {
            this.themes = themes;
        }
    }
    public static boolean exportThemesToJSON(Context context, List<Theme> themes) {
        Gson gson = new Gson();
        ThemesItems themesItems = new ThemesItems();
        themesItems.setThemes(themes);
        String jsonString = gson.toJson(themesItems);
        try(FileOutputStream fileOutputStream =
                    context.openFileOutput(FILE_THEMES, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<Theme> importThemesFromJSON(Context context) {
        try(FileInputStream fileInputStream = context.openFileInput(FILE_THEMES);
            InputStreamReader streamReader = new InputStreamReader(fileInputStream)){
            Gson gson = new Gson();
            ThemesItems themesItems = gson.fromJson(streamReader, ThemesItems.class);
            return themesItems.getThemes();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
