package controller;

import model.*;
import view.FileManagerMassage;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FileManager extends Phase1 {
    private static final Pattern createDirectory = Pattern.compile
            ("\\s*create\\s+-dir\\s+(?<name>.+)\\s*");
    private static final Pattern directoryOrFileNameFormat = Pattern.compile
            ("[a-zA-Z0-9.]\\S*");
    private static final Pattern createFile = Pattern.compile
            ("\\s*create\\s+-file\\s+(?<name>.+)\\s+-format\\s+(?<format>\\S+)\\s*");
    private static final Pattern showHidden = Pattern.compile
            ("\\s*show_hidden(\\s+-password\\s+(?<password>\\S+))?\\s*");
    private static final Pattern enterDirectory = Pattern.compile
            ("\\s*cd\\s+-name\\s+(?<address>\\S+)(\\s+(?<option>-jmp))?\\s*");
    private static final Pattern enterParent = Pattern.compile
            ("\\s*cd\\s+\\.{2}\\s*");
    private static final Pattern rename = Pattern.compile
            ("\\s*rename\\s+-(?<option>dir|file)\\s+(?<address>\\S+)\\s+-new\\s+(?<newAddress>\\S+)\\s*");
    private static final Pattern openFile = Pattern.compile
            ("\\s*open\\s+-name\\s+(?<address>\\S+)\\s*");
    private static final Pattern select = Pattern.compile
            ("\\s*select\\s+-(?<option>dir|file)\\s+-N\\s+(?<number>\\S+)\\s+-names:(?<addresses>\\S+)\\s*");
    private static final Pattern delete = Pattern.compile
            ("\\s*delete\\s+-(?<option>dir|files)\\s*");
    private static final Pattern zip = Pattern.compile
            ("\\s*zip\\s+-name\\s+(?<name>\\S+)\\s*");
    private static final Pattern unzip = Pattern.compile
            ("\\s*unzip\\s+-name\\s+(?<address>\\S+)\\s*");
    private static final Pattern show = Pattern.compile
            ("\\s*show\\s+current\\s+content\\s*");
    private static final Pattern goToUserMenu = Pattern.compile
            ("\\s*go\\s+to\\s+user\\s+menu\\s*");

    public static void run(Scanner scanner) {
        Tree.setWhere("root");
        int counterSelectDirectory = 0;
        int counterSelectFile = 0;
        String line = scanner.nextLine();
        Matcher matcher = exit.matcher(line);
        while (!matcher.matches()) {
            if (counterSelectDirectory == 2)
                Tree.removeAllSelectDirectories();
            if (counterSelectFile == 2)
                Tree.removeAllSelectFiles();
            matcher = createDirectory.matcher(line);
            if (matcher.matches()) {
                createDirectory(matcher.group("name"));
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = createFile.matcher(line);
            if (matcher.matches()) {
                createFile(matcher.group("name"),
                        matcher.group("format"));
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = showHidden.matcher(line);
            if (matcher.matches()) {
                showHidden(matcher.group("password"));
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = enterDirectory.matcher(line);
            if (matcher.matches()) {
                if (matcher.group("option") == null)
                    enterDirectoryNormal(matcher.group("address"));
                else
                    enterDirectoryJumper(matcher.group("address"));
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = enterParent.matcher(line);
            if (matcher.matches()) {
                enterParent();
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = rename.matcher(line);
            if (matcher.matches()) {
                if (Objects.equals(matcher.group("option"), "dir"))
                    renameDirectory(matcher.group("address"),
                            matcher.group("newAddress"));
                else
                    renameFile(matcher.group("address"),
                            matcher.group("newAddress"));
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = openFile.matcher(line);
            if (matcher.matches()) {
                openFile(matcher.group("address"));
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = select.matcher(line);
            if (matcher.matches()) {
                if (Objects.equals(matcher.group("option"), "dir")) {
                    selectDirectory(matcher.group("number"),
                            matcher.group("addresses"));
                    counterSelectDirectory = 0;
                    counterSelectFile++;
                } else {
                    selectFile(matcher.group("number"),
                            matcher.group("addresses"));
                    counterSelectDirectory++;
                    counterSelectFile = 0;
                }
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = delete.matcher(line);
            if (matcher.matches()) {
                if (Objects.equals(matcher.group("option"), "dir"))
                    deleteDirectory();
                else
                    deleteFile();
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = zip.matcher(line);
            if (matcher.matches()) {
                zip(matcher.group("name"));
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = unzip.matcher(line);
            if (matcher.matches()) {
                unzip(matcher.group("address"));
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = show.matcher(line);
            if (matcher.matches()) {
                show();
                counterSelectDirectory++;
                counterSelectFile++;
                line = scanner.nextLine();
                matcher = exit.matcher(line);
                continue;
            }
            matcher = goToUserMenu.matcher(line);
            if (matcher.matches()) {
                goToUserMenu();
                return;
            } else {
                invalidCommand();
                line = scanner.nextLine();
                matcher = exit.matcher(line);
            }
        }
    }

    private static void createDirectory(String name) {
        User user = User.getLoggedIn();
        if (Directory.directoryExistenceAddress(Tree.getWhere() + "/" + name))
            FileManagerMassage.directoryOrFileExistence.printer();
        else if (!directoryOrFileNameFormat.matcher(name).matches())
            FileManagerMassage.invalidName.printer();
        else {
            user.addToOwn(new Directory(name, user));
            FileManagerMassage.directoryCreate.printer();
        }
    }

    private static void createFile(String name, String format) {
        User user = User.getLoggedIn();
        FileType fileType = File.formatConverter(format);
        if (File.fileExistenceAddress(Tree.getWhere() + "/" + name + "." + format))
            FileManagerMassage.directoryOrFileExistence.printer();
        else if (fileType == null)
            FileManagerMassage.invalidFileFormat.printer();
        else if (!directoryOrFileNameFormat.matcher(name).matches())
            FileManagerMassage.invalidName.printer();
        else {
            user.addToOwn(new File(name, user, fileType));
            FileManagerMassage.fileCreate.printer();
        }
    }

    private static void showHidden(String password) {
        User user = User.getLoggedIn();
        if (user.getIsOneTime())
            FileManagerMassage.showHidden();
        else {
            if (!User.passwordValidation(user.getUsername(), password))
                FileManagerMassage.invalidPassword.printer();
            else
                FileManagerMassage.showHidden();
        }
    }

    private static void enterDirectoryNormal(String address) {
        String where = Tree.getWhere();
        Directory directory = Directory.findDirectoryAddress(address);
        if (!Directory.directoryExistenceAddress(address)) {
            FileManagerMassage.directoryEntering.printer();
            FileManagerMassage.directoryExistence.printer();
        } else if (Objects.requireNonNull(directory).getVisibility() == Visibility.hidden) {
            FileManagerMassage.directoryEntering.printer();
            FileManagerMassage.hiddenDirectory.printer();
        } else if (!directory.directoryAddressValidation(address)) {
            FileManagerMassage.directoryEntering.printer();
            FileManagerMassage.depth.printer();
        } else {
            Tree.setWhere(address);
            FileManagerMassage.enterDirectory(directory);
        }
    }

    private static void enterDirectoryJumper(String address) {
        Directory directory = Directory.findDirectoryAddress(address);
        if (!Directory.directoryExistenceAddress(address)) {
            FileManagerMassage.directoryEntering.printer();
            FileManagerMassage.directoryExistence.printer();
        } else {
            Tree.setWhere(address);
            FileManagerMassage.enterDirectory(Objects.requireNonNull(directory));
        }
    }

    private static void enterParent() {
        String where = Tree.getWhere();
        if (Objects.equals(where, "root"))
            FileManagerMassage.rootParent.printer();
        else {
            where = where.substring(0, where.lastIndexOf('/'));
            System.out.println(where);
            Tree.setWhere(where);
            FileManagerMassage.enterDirectory(Directory.findDirectoryAddress(where.trim()));
        }
    }

    private static void renameDirectory(String address, String newAddress) {
        if (Objects.equals(address, newAddress))
            FileManagerMassage.sameName.printer();
        else if (!Directory.directoryExistenceAddress(address) ||
                !Objects.equals(address.substring(0, address.lastIndexOf('/')), Tree.getWhere()))
            FileManagerMassage.directoryExistence.printer();
        else if (Directory.directoryExistenceAddress(newAddress))
            FileManagerMassage.nameRepetition.printer();
        else {
            Objects.requireNonNull(Directory.findDirectoryAddress(address)).rename(newAddress);
            FileManagerMassage.rename.printer();
        }
    }

    private static void renameFile(String address, String newAddress) {
        if (Objects.equals(address, newAddress))
            FileManagerMassage.sameName.printer();
        else if (!File.fileExistenceAddress(address) ||
                !Objects.equals(address.substring(0, address.lastIndexOf('/')), Tree.getWhere()))
            FileManagerMassage.fileExistence.printer();
        else if (!File.fileExistenceAddress(newAddress))
            FileManagerMassage.nameRepetition.printer();
        else {
            Objects.requireNonNull(File.findFileAddress(address)).rename(newAddress);
            FileManagerMassage.rename.printer();
        }
    }

    private static void openFile(String address) {
        File file = File.findFileAddress(address);
        if (!File.fileExistenceAddress(address) ||
                !Objects.equals(address.substring(0, address.lastIndexOf('/')), Tree.getWhere()))
            FileManagerMassage.fileExistence.printer();
        else if (Objects.requireNonNull(file).getFileType() == FileType.zip
                || file.getVisibility() == Visibility.hidden)
            FileManagerMassage.fileOpening.printer();
        else {
            Tree.setWhere(address);
            FileManagerMassage.openFile(file);
        }
    }

    private static void selectDirectory(String number, String addresses) {
        int count = Integer.parseInt(number);
        String[] allAddresses = addresses.split(",");
        String where = Tree.getWhere();
        if (count < 1 || count > 9)
            FileManagerMassage.invalidNumber.printer();
        else if (allAddresses.length != count)
            FileManagerMassage.wrongNumber.printer();
        else {
            int counter = 0;
            for (String address : allAddresses) {
                Directory directory = Directory.findDirectoryAddress(address);
                if (!Objects.requireNonNull(directory)
                        .directoryAddressValidation(where))
                    FileManagerMassage.directoryExistence(directory);
                else {
                    Tree.addToSelect(directory);
                    counter++;
                }
            }
            if (counter == allAddresses.length)
                FileManagerMassage.select.printer();
        }
    }

    private static void selectFile(String number, String addresses) {
        int count = Integer.parseInt(number);
        String[] allAddresses = addresses.split(",");
        String where = Tree.getWhere();
        if (count < 1 || count > 9)
            FileManagerMassage.invalidNumber.printer();
        else if (allAddresses.length != count)
            FileManagerMassage.wrongNumber.printer();
        else {
            int counter = 0;
            for (String address : allAddresses) {
                File file = File.findFileAddress(address);
                if (!Objects.requireNonNull(file)
                        .fileAddressValidation(where))
                    FileManagerMassage.fileExistence(file);
                else {
                    Tree.addToSelect(file);
                    counter++;
                }
            }
            if (counter == allAddresses.length)
                FileManagerMassage.select.printer();
        }
    }

    private static void deleteDirectory() {
        if (Tree.getSelectDirectories().isEmpty())
            FileManagerMassage.directorySelection.printer();
        else {
            for (Directory directory : Tree.getSelectDirectories()) {
                for (Directory directory1 : Directory.findDirectoriesAddress(directory.getAddress()))
                    Directory.removeFromDirectories(directory1);
                for (File file : File.findFilesAddress(directory.getAddress()))
                    File.removeFromFiles(file);
                Directory.removeFromDirectories(directory);
            }
            Tree.removeAllSelectDirectories();
        }
    }

    private static void deleteFile() {
        if (Tree.getSelectFiles().isEmpty())
            FileManagerMassage.fileSelection.printer();
        else {
            for (File file : Tree.getSelectFiles())
                File.removeFromFiles(file);
            Tree.removeAllSelectFiles();
        }
    }

    private static void zip(String name) {
        if (Zip.zipExistenceAddress(Tree.getWhere() + "/" + name))
            FileManagerMassage.zipExistence.printer();
        else if (Tree.getSelectFiles().isEmpty() &&
                Tree.getSelectDirectories().isEmpty())
            FileManagerMassage.zipEmpty.printer();
        else if (!directoryOrFileNameFormat.matcher(name).matches())
            FileManagerMassage.invalidName.printer();
        else {
            User user = User.getLoggedIn();
            Zip zip = new Zip(name, user);
            user.addToOwn(zip);
            for (File file : Tree.getSelectFiles())
                zip.addToZip(file);
            for (Directory directory : Tree.getSelectDirectories())
                zip.addToZip(directory);
            FileManagerMassage.zip.printer();
        }
    }

    private static void unzip(String address) {
        if (!Zip.zipExistenceAddress(address))
            FileManagerMassage.doesNotOwn.printer();
        else {
            User user = User.getLoggedIn();
            Zip zip = Zip.findZipAddress(address);
            for (File file1 : Objects.requireNonNull(zip).getZippedFiles()) {
                boolean isHere = false;
                for (File file2 : File.findFilesAddress(Tree.getWhere()))
                    if (file2 == file1) {
                        isHere = true;
                        break;
                    }
                if (!isHere)
                    user.addToOwn(new File(file1.getAddress().substring(file1.getAddress().lastIndexOf('/') + 1),
                            user, file1.getFileType()));
                else {
                    user.removeFromOwn(file1);
                    user.addToOwn(file1);
                }
            }
            for (Directory directory : zip.getZippedDirectories()) {
                boolean isHere = false;
                for (Directory directory1 : Directory.findDirectoriesAddress(Tree.getWhere()))
                    if (directory1 == directory) {
                        isHere = true;
                        break;
                    }
                if (!isHere)
                    user.addToOwn(new Directory(directory.getAddress().substring(directory.getAddress().lastIndexOf('/') + 1)
                            , user));
                else {
                    user.removeFromOwn(directory);
                    user.addToOwn(directory);
                }
            }
            FileManagerMassage.unzip.printer();
        }
    }

    private static void show() {
        FileManagerMassage.show();
    }

    private static void goToUserMenu() {
        FileManagerMassage.goToUserMenu.printer();
    }
}
