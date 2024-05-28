package view;

public enum LoginMenuMassage {
    userRepetition,
    invalidUsername,
    invalidPassword,
    createUser,
    userExistence,
    wrongPassword,
    login,
    noLogin;

    public void printer() {
        switch (this) {
            case userRepetition:
                System.out.println("user already exists");
                break;
            case invalidUsername:
                System.out.println("invalid username format");
                break;
            case invalidPassword:
                System.out.println("invalid password");
                break;
            case createUser:
                System.out.println("register successful");
                break;
            case userExistence:
                System.out.println("user doesn't exist");
                break;
            case wrongPassword:
                System.out.println("password doesn't match");
                break;
            case login:
                System.out.println("login successful");
                break;
            case noLogin:
                System.out.println("There's no account to be logged out");
                break;
        }
    }
}

