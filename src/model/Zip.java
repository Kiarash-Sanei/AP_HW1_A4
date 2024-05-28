package model;

import java.util.ArrayList;
import java.util.Objects;

public class Zip extends File {
    protected final static ArrayList<Zip> zips = new ArrayList<>();
    protected final ArrayList<Directory> zippedDirectories;
    protected final ArrayList<File> zippedFiles;

    public Zip(String name, User owner) {
        super(name, owner, FileType.zip);
        this.zippedDirectories = new ArrayList<>();
        this.zippedFiles = new ArrayList<>();
        Zip.zips.add(this);
    }

    public DataType dataType() {
        return DataType.zip;
    }

    public void addToZip(Data data) {
        if (data.dataType() == DataType.directory)
            this.zippedDirectories.add((Directory) data);
        else
            this.zippedFiles.add((File) data);
    }

    public ArrayList<Directory> getZippedDirectories() {
        return this.zippedDirectories;
    }

    public ArrayList<File> getZippedFiles() {
        return zippedFiles;
    }

    public static ArrayList<Zip> findZipsAddress(String address) {
        ArrayList<Zip> result = new ArrayList<>();
        for (Zip zip : Zip.zips)
            if (zip.address.startsWith(address) &&
                    Objects.equals(zip.fileType, FileType.zip))
                result.add(zip);
        return result;
    }

    public static boolean zipExistenceAddress(String address) {
        for (Zip zip : Zip.zips)
            if (Objects.equals(zip.address, address))
                return true;
        return false;
    }

    public static Zip findZipAddress(String address) {
        for (Zip zip : Zip.zips)
            if (Objects.equals(zip.address, address))
                return zip;
        return null;
    }

}
