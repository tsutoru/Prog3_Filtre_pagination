package tsutsu.exoprog3.model;

import java.time.Instant;

public class Product {
    private int id;
    private String name;
    private Instant creationDateTime;
    private Category category;

    public Product(int id, String name, double price, Instant creationDateTime, Category category) {
        this.id = id;
        this.name = name;
        this.creationDateTime = creationDateTime;
        this.category = category;
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Instant getCreationDateTime() {
        return creationDateTime;
    }


    public Category getCategory() {
        return category;
    }


    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', creationDateTime=" + creationDateTime + ", category=" + category + "}";
    }
}
