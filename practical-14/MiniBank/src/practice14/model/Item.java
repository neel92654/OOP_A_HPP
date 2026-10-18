package practice14.model;

import java.io.Serializable;

public record Item(String name) implements Serializable {
    private static final long serialVersionUID = 1L;
    public Item {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Item name cannot be blank");
    }
}
