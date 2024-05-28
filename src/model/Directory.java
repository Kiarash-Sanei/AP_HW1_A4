package model;

import java.util.ArrayList;
import java.util.Objects;

public class Directory extends Data {
    protected final static ArrayList<Directory> directories = new ArrayList<>();

    public Directory(String name, User owner) {
        super(name, owner);
        Directory.directories.add(this);
    }

    public DataType dataType() {
        return DataType.directory;
    }

    public static boolean directoryExistenceAddress(String address) {
        if (Objects.equals(address, "root"))
            return true;
        for (Directory directory : Directory.directories)
            if (Objects.equals(directory.address, address))
                return true;
        return false;
    }

    public boolean directoryAddressValidation(String address) {
        return Objects.equals(this.address, address);
    }

    public static ArrayList<Directory> findDirectoriesAddress(String address) {
        ArrayList<Directory> result = new ArrayList<>();
        for (Directory directory : Directory.directories)
            if (directory.address.startsWith(address))
                result.add(directory);
        return result;
    }

    public static Directory findDirectoryAddress(String address) {
        if (Objects.equals(address, "root")) {
            return Tree.root;
        }
        for (Directory directory : directories)
            if (Objects.equals(directory.address, address))
                return directory;
        return null;
    }

    public static void removeFromDirectories(Directory directory) {
        directories.remove(directory);
    }
}
