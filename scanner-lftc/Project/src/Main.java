import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    private static void printToFile(String filePath, Object object) {
        try (PrintStream printStream = new PrintStream(filePath)) {
            printStream.println(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void run(String filePath) {
        Scanner scanner = new Scanner(filePath);
        scanner.scan();
        printToFile(filePath.replace(".txt", "ST.txt"), scanner.getSymbolTable());
        printToFile(filePath.replace(".txt", "PIF.txt"), scanner.getPif());
    }

    public static void main(String[] args) {
        run("in_out/p1.txt");
        run("in_out/p2.txt");
        run("in_out/p3.txt");
        run("in_out/p1err.txt");
    }
}
