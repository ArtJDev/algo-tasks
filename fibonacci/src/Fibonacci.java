import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Fibonacci {
    private static int fibNumber1(int n) {           //классическая рекурсия
        if (n < 2) {
            return n;
        }
        return fibNumber1(n - 1) + fibNumber1(n - 2);
    }

    static Map<Integer, Integer> memo = new HashMap<>(Map.of(0, 0, 1, 1));

    private static int fibNumber2(int n) {          //рекурсия с мемоизацией
        if (!memo.containsKey(n)) {
            // шаг мемоизации
            memo.put(n, fibNumber2(n - 1) + fibNumber2(n - 2));
        }
        return memo.get(n);
    }

    private static int fibNumber3(int n) {          //метод с итерацией
        int last = 0, next = 1; // fib(0), fib(1)
        for (int i = 0; i < n; i++) {
            int oldLast = last;
            last = next;
            next = oldLast + next;
        }
        return last;
    }

    private int last = 0, next = 1; // fib(0), fib(1)
    public IntStream stream() {                     //метод генерации последовательности Фибоначчи
        return IntStream.generate(() -> {
            int oldLast = last;
            last = next;
            next = oldLast + next;
            return oldLast;
        });
    }

    public static void main(String[] args) {
        System.out.println(fibNumber1(7));
        System.out.println(fibNumber2(15));
        System.out.println(fibNumber3(7));

        Fibonacci fibNumbers = new Fibonacci();
        fibNumbers.stream().limit(20).forEachOrdered(System.out::println);

    }
}
