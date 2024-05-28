package model;

import java.util.ArrayList;
import java.util.Objects;

public class File extends Data {
    protected final static ArrayList<File> files = new ArrayList<>();
    protected final FileType fileType;

    public File(String name, User owner, FileType fileType) {
        super(name + "." + fileType, owner);
        this.fileType = fileType;
        if (fileType != FileType.zip)
            File.files.add(this);
    }

    public DataType dataType() {
        return DataType.file;
    }

    public static boolean fileExistenceAddress(String address) {
        for (File file : File.files)
            if (Objects.equals(file.address, address))
                return true;
        return false;
    }

    public static ArrayList<File> findFilesAddress(String address) {
        ArrayList<File> result = new ArrayList<>();
        for (File file : File.files)
            if (file.address.startsWith(address))
                result.add(file);
        return result;
    }

    public static File findFileAddress(String address) {
        for (File file : File.files)
            if (Objects.equals(file.address, address))
                return file;
        return null;
    }

    public boolean fileAddressValidation(String address) {
        return Objects.equals(this.address, address);
    }

    public FileType getFileType() {
        return this.fileType;
    }


    public static FileType formatConverter(String format) {
        switch (format) {
            case "txt":
                return FileType.txt;
            case "mkv":
                return FileType.mkv;
            case "mp3":
                return FileType.mp3;
            case "mp4":
                return FileType.mp4;
            case "csv":
                return FileType.cvs;
            case "dat":
                return FileType.dat;
            case "exe":
                return FileType.exe;
            case "c":
                return FileType.c;
            case "java":
                return FileType.java;
        }
        return null;
    }

    public static void removeFromFiles(File file) {
        if (file.fileType == FileType.zip)
            Zip.zips.remove((Zip) file);
        else
            files.remove(file);
    }
}
