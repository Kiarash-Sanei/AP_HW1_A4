package view;

import model.*;

import java.util.ArrayList;
import java.util.Objects;

public enum FileManagerMassage {
    directoryOrFileExistence,
    invalidName,
    directoryCreate,
    invalidFileFormat,
    fileCreate,
    invalidPassword,
    directoryEntering,
    directoryExistence,
    hiddenDirectory,
    depth,
    rootParent,
    sameName,
    rename,
    nameRepetition,
    fileExistence,
    fileOpening,
    invalidNumber,
    wrongNumber,
    select,
    directorySelection,
    fileSelection,
    zipExistence,
    zipEmpty,
    zip,
    doesNotOwn,
    unzip,
    directoryEmpty,
    goToUserMenu;

    public void printer() {
        switch (this) {
            case directoryOrFileExistence:
                System.out.println("already exists");
                break;
            case invalidName:
                System.out.println("incorrect name format");
                break;
            case directoryCreate:
                System.out.println("directory created successfully!");
                break;
            case invalidFileFormat:
                System.out.println("incorrect format");
                break;
            case fileCreate:
                System.out.println("file created successfully!");
                break;
            case invalidPassword:
                System.out.println("invalid password");
                break;
            case directoryEntering:
                System.out.println("unable to open directory");
                break;
            case directoryExistence:
                System.out.println("no such directory");
                break;
            case hiddenDirectory:
                System.out.println("hidden directory");
                break;
            case depth:
                System.out.println("too deep");
                break;
            case rootParent:
                System.out.println("root doesn't have parent node");
                break;
            case sameName:
                System.out.println("same name!");
                break;
            case rename:
                System.out.println("renamed successfully");
                break;
            case nameRepetition:
                System.out.println("name is already taken");
                break;
            case fileExistence:
                System.out.println("no such file");
                break;
            case fileOpening:
                System.out.println("unable to open file");
                break;
            case invalidNumber:
                System.out.println("invalid number");
                break;
            case wrongNumber:
                System.out.println("invalid number of arguments");
                break;
            case select:
                System.out.println("selected successfully");
                break;
            case directorySelection:
                System.out.println("no directory selected");
                break;
            case fileSelection:
                System.out.println("no file selected");
                break;
            case zipExistence:
                System.out.println("file already exists");
                break;
            case zipEmpty:
                System.out.println("nothing to zip");
                break;
            case zip:
                System.out.println("zipped successfully");
                break;
            case doesNotOwn:
                System.out.println("no such file or directory");
                break;
            case unzip:
                System.out.println("unzipped successfully");
                break;
            case directoryEmpty:
                System.out.println("directory empty");
                break;
            case goToUserMenu:
                System.out.println("welcome to user menu");
                break;
        }
    }

    public static void showHidden() {
        if (Tree.noHidden())
            System.out.println("no hidden item");
        else {
            ArrayList<Data> all = Tree.getAll();
            System.out.println("dir:");
            int counter = 1;
            for (Data data : all)
                if (data.dataType() == DataType.directory &&
                        data.getVisibility() == Visibility.hidden) {
                    System.out.println(counter + ". " + data.getAddress().substring(data.getAddress().lastIndexOf('/')));
                    counter++;
                }
            System.out.println("file:");
            counter = 1;
            for (Data data : all)
                if (data.dataType() == DataType.file &&
                        data.getVisibility() == Visibility.hidden) {
                    System.out.println(counter + ". " + data.getAddress().substring(data.getAddress().lastIndexOf('/')));
                    counter++;
                }
            System.out.println("zip:");
            counter = 1;
            for (Data data : all)
                if (data.dataType() == DataType.zip &&
                        data.getVisibility() == Visibility.hidden) {
                    System.out.println(counter + ". " + data.getAddress().substring(data.getAddress().lastIndexOf('/')));
                    counter++;
                }
        }
    }

    public static void enterDirectory(Directory directory) {
        System.out.println(directory.getAddress());
        if (Objects.equals(directory.getAddress().substring(directory.getAddress().lastIndexOf('/') + 1), "root"))
            System.out.println("entered directory root");
        else
            System.out.println("entered directory " + directory.getAddress());
    }

    public static void openFile(File file) {
        System.out.println("entered file " + file.getAddress().substring(file.getAddress().lastIndexOf('/') + 1));
    }

    public static void directoryExistence(Directory directory) {
        System.out.println("directory " + directory.getAddress().substring(directory.getAddress().lastIndexOf('/') + 1) + " doesn't exist");
    }

    public static void fileExistence(File file) {
        System.out.println("file " + file.getAddress().substring(file.getAddress().lastIndexOf('/') + 1) + " doesn't exist");
    }

    public static void show() {
        String address = Tree.getWhere();
        if (Directory.findDirectoriesAddress(address).isEmpty() &&
                File.findFilesAddress(address).isEmpty())
            FileManagerMassage.directoryEmpty.printer();
        else {
            System.out.println("dir:");
            int counter = 1;
            for (Directory directory : Directory.findDirectoriesAddress(address)) {
                System.out.println(counter + ". " + directory.getAddress().substring(directory.getAddress().lastIndexOf('/') + 1));
                counter++;
            }
            System.out.println("file:");
            counter = 1;
            for (File file : File.findFilesAddress(address)) {
                System.out.println(counter + ". " + file.getAddress().substring(file.getAddress().lastIndexOf('/') + 1));
                counter++;
            }
            System.out.println("zip:");
            counter = 1;
            for (Zip zip : Zip.findZipsAddress(address)) {
                System.out.println(counter + ". " + zip.getAddress().substring(zip.getAddress().lastIndexOf('/') + 1));
                counter++;
            }
        }
    }
}
