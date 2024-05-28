package model;

public abstract class Data {
    protected final User owner;
    protected Visibility visibility;
    protected String address;
    public Data(String name, User owner) {
        this.owner = owner;
        if (name.startsWith("."))
            this.visibility = Visibility.hidden;
        else
            this.visibility = Visibility.regular;
        this.address = Tree.getWhere() + "/" + name;
        Tree.addToAll(this);
    }

    public abstract DataType dataType();

    public String getAddress() {
        return this.address;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public void rename(String address) {
        this.address = address;
        if (address.substring(address.lastIndexOf('/') + 1).startsWith("."))
            this.visibility = Visibility.hidden;
        else
            this.visibility = Visibility.regular;
    }
}
