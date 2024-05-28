package controller;

import model.User;
import view.UserMenuMassage;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UserMenu extends Phase0 {
    private static final Pattern share = Pattern.compile
            ("\\s*share\\s+-(?<option>dir|file)\\s+(?<address>\\S+)\\s+-target\\s+(?<username>\\S+)\\s*");
    private static final Pattern viewShared = Pattern.compile
            ("\\s*view\\s+-shared\\s+-username\\s+(?<username>\\S+)\\s*");
    private static final Pattern viewSharedOptional = Pattern.compile
            ("\\s*view\\s+-shared\\s+-username\\s+(?<username>\\S+)\\s+-(?<who>me|user)\\s*");
    private static final Pattern shareAll = Pattern.compile
            ("\\s*share_all\\s+-(?<option>dir|file)\\s+(?<address>\\S+)\\s*");
    private static final Pattern block = Pattern.compile
            ("\\s*block\\s+-user\\s+(?<username>\\S+)\\s*");
    private static final Pattern unblock = Pattern.compile
            ("\\s*unblock\\s+-user\\s+(?<username>\\S+)\\s*");
    private static final Pattern showBlocklist = Pattern.compile
            ("\\s*show\\s+blocklist\\s*");
    private static final Pattern goToFileManager = Pattern.compile
            ("\\s*manage\\s+files\\s*");

    public static void run(Scanner scanner) {
        String line = scanner.nextLine();
        Matcher matcher = exit.matcher(line);
        while (!matcher.matches()) {
            matcher = share.matcher(line);
            if (matcher.matches()) {
                if (Objects.equals(matcher.group("option"), "dir"))
                    shareDirectory(matcher.group("address"),
                            matcher.group("username"));
                else
                    shareFile(matcher.group("address"),
                            matcher.group("username"));
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = viewSharedOptional.matcher(line);
            if (Objects.requireNonNull(matcher).matches()) {
                UserMenu.viewSharedOptional(matcher.group("username"),
                        Objects.equals(matcher.group("who"), "me"));
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = viewShared.matcher(line);
            if (Objects.requireNonNull(matcher).matches()) {
                UserMenu.viewShared(matcher.group("username"));
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = shareAll.matcher(line);
            if (Objects.requireNonNull(matcher).matches()) {
                if (Objects.equals(matcher.group("option"), "dir"))
                    UserMenu.shareAllDirectory(matcher.group("address"));
                else
                    UserMenu.shareAllFile(matcher.group("address"));
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = block.matcher(line);
            if (Objects.requireNonNull(matcher).matches()) {
                UserMenu.block(matcher.group("username"));
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = unblock.matcher(line);
            if (Objects.requireNonNull(matcher).matches()) {
                UserMenu.unBlock(matcher.group("username"));
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = showBlocklist.matcher(line);
            if (Objects.requireNonNull(matcher).matches()) {
                UserMenu.showBlocklist();
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = goToFileManager.matcher(line);
            if (Objects.requireNonNull(matcher).matches()) {
                UserMenu.goToFileManager(scanner);
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = logout.matcher(line);
            if (Objects.requireNonNull(matcher).matches()) {
                UserMenu.logout();
                break;
            }
            matcher = exit.matcher(line);
            if (Objects.requireNonNull(matcher).matches())
                System.exit(0);
            else {
                Menu.invalidCommand();
                line = scanner.nextLine();
                matcher = exit.matcher(line);
            }
        }
        matcher = exit.matcher(line);
        if (matcher.matches())
            System.exit(0);
    }

    private static void shareDirectory(String address, String username) {
        User currentUser = User.getLoggedIn();
        User otherUser = User.getUser(username);
        if (Objects.equals(address, "root"))
            UserMenuMassage.isRoot.printer();
        else if (!currentUser.isInOwnDirectories(address))
            UserMenuMassage.doesNotOwn.printer();
        else if (!User.userExistence(username))
            UserMenuMassage.userExistence.printer();
        else if (Objects.requireNonNull(otherUser).isInAccessDirectories(address))
            UserMenuMassage.sharedBefore.printer();
        else if (otherUser.isBlockedByMe(currentUser))
            UserMenuMassage.blocked.printer();
        else if (Objects.equals(currentUser.getUsername(), username))
            UserMenuMassage.selfInteraction.printer();
        else {
            otherUser.addToAccess(currentUser.findInOwnDirectories(address));
            UserMenuMassage.share.printer();
        }
    }

    private static void shareFile(String address, String username) {
        User currentUser = User.getLoggedIn();
        User otherUser = User.getUser(username);
        if (!User.userExistence(username))
            UserMenuMassage.userExistence.printer();
        else if (!currentUser.isInOwnFiles(address))
            UserMenuMassage.doesNotOwn.printer();
        else if (Objects.requireNonNull(otherUser).isInAccessFiles(address))
            UserMenuMassage.sharedBefore.printer();
        else if (otherUser.isBlockedByMe(currentUser))
            UserMenuMassage.blocked.printer();
        else if (Objects.equals(currentUser.getUsername(), username))
            UserMenuMassage.selfInteraction.printer();
        else {
            otherUser.addToAccess(currentUser.findInOwnFiles(address));
            UserMenuMassage.share.printer();
        }
    }

    private static void viewShared(String username) {
        User currentUser = User.getLoggedIn();
        User otherUser = User.getUser(username);
        if (otherUser == null)
            UserMenuMassage.userExistence.printer();
        else if (Objects.equals(currentUser.getUsername(), username))
            UserMenuMassage.selfInteraction.printer();
        else
            UserMenuMassage.shareList(currentUser, otherUser);
    }

    private static void viewSharedOptional(String username, boolean option) {
        User currentUser = User.getLoggedIn();
        User otherUser = User.getUser(username);
        if (otherUser == null)
            UserMenuMassage.userExistence.printer();
        else if (Objects.equals(currentUser.getUsername(), username))
            UserMenuMassage.selfInteraction.printer();
        else
            UserMenuMassage.shareList(currentUser, otherUser, option);
    }

    private static void shareAllDirectory(String address) {
        User currentUser = User.getLoggedIn();
        if (Objects.equals(address, "root"))
            UserMenuMassage.isRoot.printer();
        else if (!currentUser.isInOwnDirectories(address))
            UserMenuMassage.doesNotOwn.printer();
        else {
            boolean isDone = false;
            for (User user : User.getUsers()) {
                if (user != currentUser &&
                        !user.isBlockedByMe(currentUser) &&
                        !currentUser.isBlockedByMe(user) &&
                        !user.isInAccessDirectories(address)) {
                    user.addToAccess(currentUser.findInOwnDirectories(address));
                    isDone = true;
                }
            }
            if (!isDone)
                UserMenuMassage.noAvailableUser.printer();
            else
                UserMenuMassage.share.printer();
        }
    }

    private static void shareAllFile(String address) {
        User currentUser = User.getLoggedIn();
        if (!currentUser.isInOwnFiles(address))
            UserMenuMassage.doesNotOwn.printer();
        else {
            boolean isDone = false;
            for (User user : User.getUsers()) {
                if (user != currentUser &&
                        !user.isBlockedByMe(currentUser) &&
                        !currentUser.isBlockedByMe(user) &&
                        !user.isInAccessFiles(address)) {
                    user.addToAccess(currentUser.findInOwnFiles(address));
                    isDone = true;
                }
            }
            if (!isDone)
                UserMenuMassage.noAvailableUser.printer();
            else
                UserMenuMassage.share.printer();
        }
    }

    private static void block(String username) {
        User currentUser = User.getLoggedIn();
        User otherUser = User.getUser(username);
        if (otherUser == null)
            UserMenuMassage.userExistence.printer();
        else if (currentUser.isBlockedByMe(otherUser))
            UserMenuMassage.alreadyBlocked.printer();
        else if (Objects.equals(currentUser.getUsername(), username))
            UserMenuMassage.selfInteraction.printer();
        else {
            currentUser.block(otherUser);
            UserMenuMassage.block(otherUser);
        }
    }

    private static void unBlock(String username) {
        User currentUser = User.getLoggedIn();
        User otherUser = User.getUser(username);
        if (otherUser == null)
            UserMenuMassage.userExistence.printer();
        else if (Objects.equals(currentUser.getUsername(), username))
            UserMenuMassage.selfInteraction.printer();
        else if (!currentUser.isBlockedByMe(otherUser))
            UserMenuMassage.notBlocked.printer();
        else {
            currentUser.unBlock(otherUser);
            UserMenuMassage.unblock(otherUser);
        }
    }

    private static void showBlocklist() {
        UserMenuMassage.showBlocklist();
    }

    private static void goToFileManager(Scanner scanner) {
        UserMenuMassage.goToFileManager.printer();
        FileManager.run(scanner);
    }

    private static void logout() {
        UserMenuMassage.logout.printer();
        User user = User.getLoggedIn();
        if (user.getIsOneTime()) {
            User.deleteUser(user);
            UserMenuMassage.accountRemoved.printer();
        }
        User.setLoggedIn(null);
    }
}
