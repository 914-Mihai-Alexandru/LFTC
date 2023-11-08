import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The Scanner class tokenizes the input program, classifying tokens, identifiers, constants, and separators.
 * It also constructs the symbol table and the Program Internal Form (PIF) based on the input program.
 */
public class Scanner {

    private final ArrayList<String> operators = new ArrayList<>(List.of("+", "-", "*", "/", "%", "<=", ">=", "==", "!=", "<", ">", "="));
    private final ArrayList<String> separators = new ArrayList<>(List.of("{", "}", "(", ")", "[", "]", ":", ";", " ", ",", "\t", "\n", "'", "\""));
    private final ArrayList<String> reservedWords = new ArrayList<>(List.of("read", "write", "if", "else", "for", "while", "int", "string", "char", "return", "start", "array"));
    private final String filePath;
    private SymbolTable symbolTable;
    private ProgramInternalForm pif;

    /**
     * Constructor for the Scanner class. Initializes the symbolTable, pif, and filePath.
     *
     * @param filePath - The file path from which the program will be read.
     */
    public Scanner(String filePath) {
        this.filePath = filePath;
        this.symbolTable = new SymbolTable(100);
        this.pif = new ProgramInternalForm();
    }

    /**
     * Reads the content of the file, replacing tabs with empty strings.
     *
     * @return The content of the read file.
     * @throws FileNotFoundException if the file doesn't exist.
     */
    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        java.util.Scanner scanner = new java.util.Scanner(new File(this.filePath));
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine()).append("\n");
        }
        return fileContent.toString().replace("\t", "");
    }

    /**
     * Prepares the input program elements for tokenization, splitting tokens, identifiers, constants, and separators.
     *
     * @return The list of pairs composed of tokens/identifiers/constants and their line and column positions.
     */
    private List<Pair<String, Pair<Integer, Integer>>> createListOfProgramElems() {
        try {
            String content = this.readFile();
            String separatorsString = this.separators.stream().reduce("", (a, b) -> (a + b));
            StringTokenizer tokenizer = new StringTokenizer(content, separatorsString, true);
            List<String> tokens = Collections.list(tokenizer).stream().map(t -> (String) t).collect(Collectors.toList());
            return tokenize(tokens);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tokenizes the input program elements, classifying tokens, identifiers, and constants.
     *
     * @param tokensToBe - The list of program elements (strings) including separators.
     * @return The list of pairs composed of tokens/identifiers/constants and their line and column positions.
     */
    private List<Pair<String, Pair<Integer, Integer>>> tokenize(List<String> tokensToBe) {
        List<Pair<String, Pair<Integer, Integer>>> resultedTokens = new ArrayList<>();
        boolean isStringConstant = false;
        boolean isCharConstant = false;
        StringBuilder createdString = new StringBuilder();
        int numberLine = 1;
        int numberColumn = 1;

        for (String t : tokensToBe) {
            switch (t) {
                case "\"":
                    if (isStringConstant) {
                        createdString.append(t);
                        resultedTokens.add(new Pair<>(createdString.toString(), new Pair<>(numberLine, numberColumn)));
                        createdString = new StringBuilder();
                    } else {
                        createdString.append(t);
                    }
                    isStringConstant = !isStringConstant;
                    break;
                case "'":
                    if (isCharConstant) {
                        createdString.append(t);
                        resultedTokens.add(new Pair<>(createdString.toString(), new Pair<>(numberLine, numberColumn)));
                        createdString = new StringBuilder();
                    } else {
                        createdString.append(t);
                    }
                    isCharConstant = !isCharConstant;
                    break;
                case "\n":
                    numberLine++;
                    numberColumn = 1;
                    break;
                default:
                    if (isStringConstant) {
                        createdString.append(t);
                    } else if (isCharConstant) {
                        createdString.append(t);
                    } else if (!t.equals(" ")) {
                        resultedTokens.add(new Pair<>(t, new Pair<>(numberLine, numberColumn)));
                        numberColumn++;
                    }
                    break;
            }
        }
        return resultedTokens;
    }

    /**
     * Scans the list of created tokens and classifies them as reserved words, operators, separators, constants, or identifiers.
     * Adds them to the Symbol Table and constructs the Program Internal Form (PIF) accordingly.
     */
    public void scan() {
        List<Pair<String, Pair<Integer, Integer>>> tokens = createListOfProgramElems();
        AtomicBoolean lexicalErrorExists = new AtomicBoolean(false);

        if (tokens == null) {
            return;
        }

        tokens.forEach(t -> {
            String token = t.getFirst();
            if (this.reservedWords.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 2);
            } else if (this.operators.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 3);
            } else if (this.separators.contains(token)) {
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 4);
            } else if (Pattern.compile("^0|[-|+][1-9]([0-9])*|'[1-9]'|'[a-zA-Z]'|\"[0-9]*[a-zA-Z ]*\"$").matcher(token).matches()) {
                this.symbolTable.add(token);
                this.pif.add(new Pair<>(token, symbolTable.findPositionOfTerm(token)), 0);
            } else if (Pattern.compile("^([a-zA-Z]|_)|[a-zA-Z_0-9]*").matcher(token).matches()) {
                this.symbolTable.add(token);
                this.pif.add(new Pair<>(token, symbolTable.findPositionOfTerm(token)), 1);
            } else {
                Pair<Integer, Integer> pairLineColumn = t.getSecond();
                System.out.println("Error at line: " + pairLineColumn.getFirst() + " and column: " + pairLineColumn.getSecond() + ", invalid token: " + t.getFirst());
                lexicalErrorExists.set(true);
            }
        });

        if (!lexicalErrorExists.get()) {
            System.out.println("Program is lexically correct!");
        }
    }

    /**
     * Gets the Program Internal Form (PIF).
     *
     * @return The Program Internal Form.
     */
    public ProgramInternalForm getPif() {
        return this.pif;
    }

    /**
     * Gets the Symbol Table.
     *
     * @return The Symbol Table.
     */
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
}
