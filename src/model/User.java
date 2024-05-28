package model;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private final static ArrayList<User> users = new ArrayList<>();
    private static User loggedIn;
    private final String username;
    private final String password;
    private final boolean isOneTime;
    private final ArrayList<Data> own;
    private final ArrayList<Data> access;
    private final ArrayList<User> blocked;
    private final ArrayList<User> blocker;

    public User(String username, String password, boolean isOneTime) {
        this.username = username;
        this.password = password;
        this.isOneTime = isOneTime;
        this.own = new ArrayList<>();
        this.access = new ArrayList<>();
        this.blocked = new ArrayList<>();
        this.blocker = new ArrayList<>();
        User.users.add(this);
    }

    public static boolean userExistence(String username) {
        for (User user : User.users)
            if (Objects.equals(user.username, username))
                return true;
        return false;
    }

    public static boolean passwordValidation(String username, String password) {
        return Objects.equals(Objects.requireNonNull(getUser(username)).password, password);
    }

    public static User getUser(String username) {
        for (User user : User.users)
            if (Objects.equals(user.username, username))
                return user;
        return null;
    }

    public static User getLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(String username) {
        User.loggedIn = getUser(username);
    }

    public void addToAccess(Data data) {
        this.access.add(data);
    }

    public boolean isInAccessDirectories(String address) {
        for (Directory directory : this.getAccessDirectories())
            if (Objects.equals(directory.address, address))
                return true;
        return false;
    }

    public boolean isInAccessFiles(String address) {
        for (File file : this.getAccessFiles())
            if (Objects.equals(file.address, address))
                return true;
        return false;
    }

    public void addToOwn(Data data) {
        this.own.add(data);
    }

    public void removeFromOwn(Data data) {
        this.own.remove(data);
    }

    public boolean isInOwnDirectories(String address) {
        for (Directory directory : this.getOwnDirectories())
            if (Objects.equals(directory.address, address))
                return true;
        return false;
    }

    public boolean isInOwnFiles(String address) {
        for (File file : this.getOwnFiles())
            if (Objects.equals(file.address, address))
                return true;
        return false;
    }

    public Directory findInOwnDirectories(String address) {
        for (Directory directory : this.getOwnDirectories())
            if (Objects.equals(directory.address, address))
                return directory;
        return null;
    }

    public File findInOwnFiles(String address) {
        for (File file : this.getOwnFiles())
            if (Objects.equals(file.address, address))
                return file;
        return null;
    }

    public boolean isBlockedByMe(User user) {
        return this.blocked.contains(user);
    }

    public boolean isBlockerOfMe(User user) {
        return this.blocker.contains(user);
    }


    public ArrayList<Directory> getOwnDirectories() {
        ArrayList<Directory> result = new ArrayList<>();
        for (Data data : this.own)
            if (Objects.equals(data.dataType(), DataType.directory))
                result.add((Directory) data);
        return result;
    }

    public ArrayList<Directory> getAccessDirectories() {
        ArrayList<Directory> result = new ArrayList<>();
        for (Data data : this.access)
            if (Objects.equals(data.dataType(), DataType.directory))
                result.add((Directory) data);
        return result;
    }

    public ArrayList<File> getOwnFiles() {
        ArrayList<File> result = new ArrayList<>();
        for (Data data : this.own)
            if (Objects.equals(data.dataType(), DataType.file))
                result.add((File) data);
        return result;
    }

    public ArrayList<File> getAccessFiles() {
        ArrayList<File> result = new ArrayList<>();
        for (Data data : this.access)
            if (Objects.equals(data.dataType(), DataType.file))
                result.add((File) data);
        return result;
    }

    public String getUsername() {
        return this.username;
    }

    public static ArrayList<User> getUsers() {
        return User.users;
    }

    public void block(User user) {
        this.blocked.add(user);
        user.blocker.add(this);
    }

    public void unBlock(User user) {
        this.blocked.remove(user);
        user.blocker.remove(this);
    }

    public ArrayList<User> getBlockedList() {
        return this.blocked;
    }

    public ArrayList<User> getBlockerList() {
        return this.blocker;
    }

    public boolean getIsOneTime() {
        return this.isOneTime;
    }

    public static void deleteUser(User user) {
        User.users.remove(user);
    }

    public ArrayList<Zip> getOwnZips() {
        ArrayList<Zip> result = new ArrayList<>();
        for (Data data : this.own)
            if (Objects.equals(data.dataType(), DataType.zip))
                result.add((Zip) data);
        return result;
    }

    public ArrayList<Zip> getAccessZips() {
        ArrayList<Zip> result = new ArrayList<>();
        for (Data data : this.access)
            if (Objects.equals(data.dataType(), DataType.zip))
                result.add((Zip) data);
        return result;
    }

    public boolean hasNotShared(User user) {
        for (Directory directory : this.getOwnDirectories())
            for (Directory directory1 : user.getAccessDirectories())
                if (directory == directory1)
                    return false;
        for (File file : this.getOwnFiles())
            for (File file1 : user.getAccessFiles())
                if (file == file1)
                    return false;
        return true;
    }

    public boolean hasNotBlocked() {
        return this.blocked.isEmpty() && this.blocker.isEmpty();
    }
}
