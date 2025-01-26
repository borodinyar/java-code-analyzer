package variablefinal;

public class FieldModification {
    static int value = 0;
    final String aba = "aba";

    void updateValue() {
        int fixedValue = 100;
        fixedValue += 10;
    }
}
