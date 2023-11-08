import java.util.ArrayList;
import java.util.List;

/**
 * The ProgramInternalForm class stores tokens, identifiers, and constants along with their positions in the symbol table
 * and their corresponding categories. It provides methods to add tokens and their positions, as well as generating a
 * formatted string representation of the internal form.
 */
public class ProgramInternalForm {

    private List<Pair<String, Pair<Integer, Integer>>> tokenPositionPair;
    private List<Integer> types;

    /**
     * Initializes the internal form by creating lists to store tokens, positions, and categories.
     */
    public ProgramInternalForm() {
        this.tokenPositionPair = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    /**
     * Adds a token, identifier, or constant to the internal form along with its position in the symbol table and its category.
     *
     * @param pair - The token/constant/identifier along with its position in the symbol table.
     * @param type - The category of the token: reserved words (2), operators (3), separators (4), constants (0), or identifiers (1).
     */
    public void add(Pair<String, Pair<Integer, Integer>> pair, Integer type) {
        this.tokenPositionPair.add(pair);
        this.types.add(type);
    }

    /**
     * Generates a formatted string representation of the internal form, including tokens, positions, and categories.
     *
     * @return - A string containing tokens, positions, and categories.
     */
    @Override
    public String toString() {
        StringBuilder computedString = new StringBuilder();
        for (int i = 0; i < this.tokenPositionPair.size(); i++) {
            computedString.append(this.tokenPositionPair.get(i).getFirst()).append(" - ")
                    .append(this.tokenPositionPair.get(i).getSecond()).append(" -> ")
                    .append(types.get(i)).append("\n");
        }

        return computedString.toString();
    }
}
