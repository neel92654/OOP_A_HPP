package practice14.service;

import practice14.model.Item;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ItemService {
    private final List<Item> items = new ArrayList<>();
    public void add(String name) { items.add(new Item(name)); }
    public int count() { return items.size(); }
    public List<Item> all() { return List.copyOf(items); }
    public void execute(String action, String value) {
        if ("ADD".equalsIgnoreCase(action)) add(value);
        else if ("LIST".equalsIgnoreCase(action)) all().forEach(System.out::println);
        else throw new IllegalArgumentException("Invalid action: " + action);
    }
    public void save(Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(new ArrayList<>(items));
        }
    }
    @SuppressWarnings("unchecked")
    public void load(Path file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            List<Item> loaded = (List<Item>) in.readObject();
            items.clear();
            items.addAll(loaded);
        }
    }
}
