package controller;

import view.MenuMassage;

import java.util.regex.Pattern;

public abstract class Menu {
    protected static final Pattern exit = Pattern.compile
            ("\\s*exit\\s*");
    protected static void invalidCommand() {
        MenuMassage.invalidCommand.printer();
    }
}
