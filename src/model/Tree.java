package model;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Tree {
    private final static ArrayList<File> selectFiles = new ArrayList<>();
    private final static ArrayList<Directory> selectDirectories = new ArrayList<>();
    private static String where;
    private static final ArrayList<Data> all = new ArrayList<>();
    public static final Directory root = new Directory("root", null);

    public static void addToAll(Data data) {
        all.add(data);
    }

    public static void removeFromAll(Data data) {
        all.remove(data);
    }

    public static void setWhere(String where) {
        Tree.where = where;
    }

    public static String getWhere() {
        return where;
    }

    public static void addToSelect(Data data) {
        if (data.dataType() == DataType.directory)
            selectDirectories.add((Directory) data);
        else
            selectFiles.add((File) data);
    }

    public static void removeAllSelectDirectories() {
        selectDirectories.clear();
    }

    public static void removeAllSelectFiles() {
        selectFiles.clear();
    }

    public static ArrayList<Directory> getSelectDirectories() {
        return selectDirectories;
    }

    public static ArrayList<File> getSelectFiles() {
        return selectFiles;
    }

    public static boolean noHidden() {
        for (Data data : all)
            if (data.visibility == Visibility.hidden)
                return false;
        return true;
    }

    public static ArrayList<Data> getAll() {
        return all;
    }
}
