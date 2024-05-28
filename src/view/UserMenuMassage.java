package view;

import model.*;

public enum UserMenuMassage {
    doesNotOwn,
    userExistence,
    sharedBefore,
    isRoot,
    blocked,
    selfInteraction,
    share,
    noAvailableUser,
    alreadyBlocked,
    notBlocked,
    goToFileManager,
    logout,
    accountRemoved;

    public void printer() {
        switch (this) {
            case doesNotOwn:
                System.out.println("no such file or directory");
                break;
            case userExistence:
                System.out.println("no such user exists");
                break;
            case sharedBefore:
                System.out.println("already shared");
                break;
            case isRoot:
                System.out.println("you can't share root");
                break;
            case blocked:
                System.out.println("can't share, you are blocked");
                break;
            case selfInteraction:
                System.out.println("You can't interact with yourself!");
                break;
            case share:
                System.out.println("shared successfully");
                break;
            case noAvailableUser:
                System.out.println("no available user");
                break;
            case alreadyBlocked:
                System.out.println("user already blocked");
                break;
            case notBlocked:
                System.out.println("user is not blocked");
                break;
            case goToFileManager:
                System.out.println("you are now in directory root");
                break;
            case logout:
                System.out.println("logged out successfully");
                break;
            case accountRemoved:
                System.out.println("account removed!");
                break;
        }
    }

    public static void shareList(User user1, User user2) {
        if (user1.hasNotShared(user2) && user2.hasNotShared(user1))
            System.out.println("share list empty");
        else {
            System.out.println("shared to " + user2.getUsername() + " by me:");
            shareListOneUser(user1, user2);
            System.out.println("shared to me by " + user2.getUsername() + ":");
            shareListOneUser(user2, user1);
        }
    }

    public static void shareList(User user1, User user2, boolean option) {
        if (option) {
            if (user1.hasNotShared(user2))
                System.out.println("share list empty");
            else {
                System.out.println("shared to " + user2.getUsername() + " by me:");
                shareListOneUser(user1, user2);
            }
        } else {
            if (user2.hasNotShared(user1))
                System.out.println("share list empty");
            else {
                System.out.println("shared to me by " + user2.getUsername() + ":");
                shareListOneUser(user2, user1);
            }
        }
    }

    private static void shareListOneUser(User user1, User user2) {
        System.out.println("dir:");
        int counter = 1;
        for (Directory directory : user1.getOwnDirectories())
            for (Directory directory1 : user2.getAccessDirectories())
                if (directory == directory1) {
                    System.out.println(counter + ". " + directory.getAddress());
                    counter++;
                }
        System.out.println("file:");
        counter = 1;
        for (File file : user1.getOwnFiles())
            for (File file1 : user2.getAccessFiles())
                if (file == file1) {
                    System.out.println(counter + ". " + file.getAddress());
                    counter++;
                }
        System.out.println("zip:");
        counter = 1;
        for (Zip zip : user1.getOwnZips())
            for (Zip zip1 : user2.getAccessZips())
                if (zip == zip1) {
                    System.out.println(counter + ". " + zip.getAddress());
                    counter++;
                }
    }

    public static void block(User user) {
        System.out.println("user " + user.getUsername() + " is now blocked");
    }

    public static void unblock(User user) {
        System.out.println("user " + user.getUsername() + " is now unblocked");
    }

    public static void showBlocklist() {
        User user = User.getLoggedIn();
        if (user.hasNotBlocked())
            System.out.println("block list empty");
        else {
            System.out.println("blocked:");
            int counter = 1;
            for (User user1 : user.getBlockedList()) {
                System.out.println(counter + ". " + user1.getUsername());
                counter++;
            }
            System.out.println("blocker:");
            counter = 1;
            for (User user1 : user.getBlockerList()) {
                System.out.println(counter + ". " + user1.getUsername());
                counter++;
            }
        }
    }
}
