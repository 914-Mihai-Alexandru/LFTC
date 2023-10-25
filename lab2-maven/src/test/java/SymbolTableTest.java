import org.example.Pair;
import org.example.SymbolTable;
import org.junit.Test;
import static org.junit.Assert.*;

public class SymbolTableTest {

    @Test
    public void testAddAndContains() {
        SymbolTable symbolTable = new SymbolTable(13);
        assertTrue(symbolTable.add("a3a"));
        assertTrue(symbolTable.containsElement("a3a"));

        assertTrue(symbolTable.add("3aa"));
        assertTrue(symbolTable.containsElement("3aa"));

        assertTrue(symbolTable.add("aa3"));
        assertTrue(symbolTable.containsElement("aa3"));

        assertFalse(symbolTable.containsElement("ba"));
    }

    @Test
    public void testAddDuplicate() {
        SymbolTable symbolTable = new SymbolTable(13);
        assertTrue(symbolTable.add("a3a"));
        assertFalse(symbolTable.add("a3a")); // Adding a duplicate element should return false
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindByInvalidPos() {
        SymbolTable symbolTable = new SymbolTable(13);
        Pair invalidPos = new Pair(0, 0);
        symbolTable.findByPos(invalidPos);
    }
}
