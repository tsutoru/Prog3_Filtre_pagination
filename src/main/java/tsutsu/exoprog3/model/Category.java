package tsutsu.exoprog3.model;

public class Category {
    private int id;
    private String name;

    // Constructeur
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "'}";
    }
}
