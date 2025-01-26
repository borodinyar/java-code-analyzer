# Анализатор кода

---

Домашнее задание к курсу Верификация ПО

---

## Описание

- Проверяет наличие пустых блоков в коде.
- Смотрит на отсутствие final в переменных.
- Смотрит на отсутствие фигурных скобок в блоках `if`, `for`, `while`, `do-while`

## Пример

Файл `CommonFailTest.java`:
```Java
public class CommonFail {
    static int staticVal = 1; // Игнорируется, так как статическое
    int regularVal = 5;       // Можно сделать final

    public void myMethod() {
        if(2 == 3){}

        for(int i = 0; i < 100; i++)
            break;

        int a = 10;
        int b = 20; // Можно сделать final
        a += 5;     // Значение переменной 'a' изменяется
        System.out.println(b);

        int[] array = {1,2,3,4,5}; // Можно сделать final
        array[0] = 9;
    }

    public void mySecondMethod() {
        List<Integer> L = List.of(); // Можно сделать final
        L.add(1);

        int val = 0;
        while (val < 10) {
            val++;
        }
    }
}
```

Результат:
```
ErrorMessage[message=Error in file CommonFailTest.java: Warning: Empty block found at line 10]
ErrorMessage[message=Error in file CommonFailTest.java: Warning: 'for' statement without braces at line 12]
ErrorMessage[message=Error in file CommonFailTest.java: Field 'regularVal' in class could be final.]
ErrorMessage[message=Error in file CommonFailTest.java: Local variable 'b' in method 'myMethod' could be final.]
ErrorMessage[message=Error in file CommonFailTest.java: Local variable 'array' in method 'myMethod' could be final.]
ErrorMessage[message=Error in file CommonFailTest.java: Local variable 'L' in method 'mySecondMethod' could be final.]
```

Больше примеров можно найти в [тестовых файлах](src/test/resources)
