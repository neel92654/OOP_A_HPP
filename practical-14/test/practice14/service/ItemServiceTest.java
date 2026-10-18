package practice14.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {
    @TempDir Path temp;
    @Test void addingItemIncreasesCount() {
        ItemService service = new ItemService();
        service.add("Notebook");
        assertEquals(1, service.count());
    }
    @Test void invalidActionThrows() {
        ItemService service = new ItemService();
        assertThrows(IllegalArgumentException.class, () -> service.execute("DELETE", "Notebook"));
    }
    @Test void emptyNameThrows() {
        ItemService service = new ItemService();
        assertThrows(IllegalArgumentException.class, () -> service.add(" "));
    }
    @Test void savesAndReloadsDataUnchanged() throws Exception {
        ItemService original = new ItemService();
        original.add("Notebook"); original.add("Pen");
        Path file = temp.resolve("items.bin"); original.save(file);
        ItemService loaded = new ItemService(); loaded.load(file);
        assertEquals(original.all(), loaded.all());
    }
}
