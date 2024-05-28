package view;

public enum MenuMassage {
    invalidCommand;

    public void printer() {
        switch (this) {
            case invalidCommand:
                System.out.println("invalid command");
                break;
        }
    }
}
