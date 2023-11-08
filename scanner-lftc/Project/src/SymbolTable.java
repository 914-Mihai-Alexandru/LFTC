public class SymbolTable {

    private HashTable hashTable;


    public SymbolTable(Integer size) {
        hashTable = new HashTable(size);
    }

    public Pair findPositionOfTerm(String term) {
        return hashTable.findPositionOfTerm(term);
    }

    public boolean add(String term) {
        return hashTable.add(term);
    }

    @Override
    public String toString() {
        return this.hashTable.toString();
    }

}
