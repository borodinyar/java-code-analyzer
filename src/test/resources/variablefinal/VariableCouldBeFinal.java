import java.util.List;

public class VariableCouldBeFinal {
    int field = 10; // Можно сделать final
    int anotherVal = 10;      // Переменная изменяется, её нельзя сделать final

    void method(String Lol) {
        int a = 5; // Можно сделать final
        int b = 10; // Можно сделать final
        int c = 15;
        c += 5;  // Изменяется, нельзя сделать final

        List<Integer> L = List.of(); // Можно сделать final
        L.add(1);

        anotherVal = 50;
    }
}
