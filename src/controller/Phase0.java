package controller;

import java.util.regex.Pattern;

public abstract class Phase0 extends Menu {
    protected static final Pattern logout = Pattern.compile
            ("\\s*logout\\s*");
}
