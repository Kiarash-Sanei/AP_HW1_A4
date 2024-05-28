package controller;

import model.User;
import view.LoginMenuMassage;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LoginMenu extends Phase0 {
    private static final Pattern createUser = Pattern.compile
            ("\\s*create_user\\s+-username\\s+(?<username>\\S+)\\s+-password\\s+(?<password>\\S+)\\s*");
    private static final Pattern createUserOneTime = Pattern.compile
            ("\\s*create_user\\s+-username\\s+(?<username>\\S+)\\s+-password\\s+(?<password>\\S+)\\s+(?<oneTime>-one_time)\\s*");
    private static final Pattern usernameFormat = Pattern.compile
            ("[a-zA-Z0-9_]*");
    private static final Pattern login = Pattern.compile
            ("\\s*login\\s+-username\\s+(?<username>\\S+)\\s+-password\\s+(?<password>\\S+)\\s*");

    public static void run(Scanner scanner) {
        String line = scanner.nextLine();
        Matcher matcher = exit.matcher(line);
        while (!matcher.matches()) {
            matcher = createUserOneTime.matcher(line);
            if (matcher.matches()) {
                createUser(matcher.group("username"),
                        matcher.group("password"),
                        true);
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = createUser.matcher(line);
            if (matcher.matches()) {
                createUser(matcher.group("username"),
                        matcher.group("password"),
                        false);
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = login.matcher(line);
            if (Objects.requireNonNull(matcher).matches()) {
                login(matcher.group("username"),
                        matcher.group("password"),
                        scanner);
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = logout.matcher(line);
            if (Objects.requireNonNull(matcher).matches())
                logout();
            else
                invalidCommand();
            line = scanner.nextLine();
            matcher = exit.matcher(line);
        }
        System.exit(0);
    }

    private static void createUser(String username, String password, boolean isOneTime) {
        if (usernameChecker(username) && passwordChecker(password)) {
            new User(username, password, isOneTime);
            LoginMenuMassage.createUser.printer();
        }
    }

    private static boolean usernameChecker(String username) {
        if (User.userExistence(username)) {
            LoginMenuMassage.userRepetition.printer();
            return false;
        }
        if (!usernameFormat.matcher(username).matches()) {
            LoginMenuMassage.invalidUsername.printer();
            return false;
        }
        return true;
    }

    private static boolean passwordChecker(String password) {
        char[] list = password.toCharArray();
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean isLong = false;
        for (char c : list) {
            if (c >= 'A' &&
                    c <= 'Z')
                hasUppercase = true;
            else if (c >= 'a' &&
                    c <= 'z')
                hasLowercase = true;
            else if (c >= '0' &&
                    c <= '9')
                hasDigit = true;
        }
        if (list.length >= 8)
            isLong = true;
        if (!(hasUppercase &&
                hasLowercase &&
                hasDigit &&
                isLong)) {
            LoginMenuMassage.invalidPassword.printer();
            return false;
        }
        return true;
    }

    private static void login(String username, String password, Scanner scanner) {
        if (!User.userExistence(username))
            LoginMenuMassage.userExistence.printer();
        else if (!User.passwordValidation(username, password))
            LoginMenuMassage.wrongPassword.printer();
        else {
            LoginMenuMassage.login.printer();
            User.setLoggedIn(username);
            goToUserMenu(scanner);
        }
    }

    private static void goToUserMenu(Scanner scanner) {
        UserMenu.run(scanner);
    }

    private static void logout() {
        LoginMenuMassage.noLogin.printer();
    }
}
