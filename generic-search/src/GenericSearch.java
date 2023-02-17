import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class GenericSearch {
    public static <T extends Comparable<T>> boolean linearContains(List<T> list, T key) {
        for (T item : list) {
            if (item.compareTo(key) == 0) {
                return true; // поиск совпадений
            }
        }
        return false;
    }

    // предполагается, что *список* уже отсортирован
    public static <T extends Comparable<T>> boolean binaryContains(List<T> list, T key) {
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) { // пока еще есть место для поиска
            int middle = (low + high) / 2;
            int comparison = list.get(middle).compareTo(key);
            if (comparison < 0) { // средний кодон меньше искомого
                low = middle + 1;
            } else if (comparison > 0) { // средний кодон больше искомого
                high = middle - 1;
            } else { // средний кодон равен искомому
                return true;
            }
        }
        return false;
    }

    //Класс Node нужен для отслеживания перехода от одного состояния к другому (местоположение в лабиринте)
    //Будем считать Node состоянием, унаследованным от parent
    //Если объект Node не имеет свойства parent, в качестве указателя будем использовать null
    public static class Node<T> implements Comparable<Node<T>> {
        final T state;
        Node<T> parent;
        double cost;
        double heuristic;

        // для dfs(поиск в глубину) и bfs(поиск в ширину) мы не будем использовать свойства cost и heuristic (стоимость и эвристика)
        Node(T state, Node<T> parent) {
            this.state = state;
            this.parent = parent;
        }

        // для алгоритма А* мы будем использовать свойства cost и heuristic
        Node(T state, Node<T> parent, double cost, double heuristic) {
            this.state = state;
            this.parent = parent;
            this.cost = cost;
            this.heuristic = heuristic;
        }

        @Override
        public int compareTo(Node<T> other) {
            Double mine = cost + heuristic;
            Double theirs = other.cost + other.heuristic;
            return mine.compareTo(theirs);
        }
    }

    public static <T> Node<T> dfs(T initial, Predicate<T> goalTest, Function<T, List<T>> successors) {
        //frontier — то, что нам нужно проверить
        //инициализируем стек проверяемых состояний
        Stack<Node<T>> frontier = new Stack<>();
        //кладем в стек обертку (ноду) внутри которой координаты исследованных местоположений в лабиринте и начальная родительская (в самом начале родитель = null)
        frontier.push(new Node<>(initial, null));
        //explored — то, где мы уже
        //инициализируем сет исследованных местоположений в лабиринте
        Set<T> explored = new HashSet<>();
        //Отфильтровываем дубликаты исследованных местоположений в лабиринте
        //т.к. во время просмотра доступных координат лабиринта они могут быть просмотрены несколько раз и сложены в список доступных
        explored.add(initial);
        //продолжаем, пока есть что просматривать
        while (!frontier.isEmpty()) {
            //Вытягиваем из стека по одной ноде с координатами и кладем ее в текущую ноду
            Node<T> currentNode = frontier.pop();
            //Приравниваем текущей координате координату из текущей ноды
            T currentState = currentNode.state;
            //Проверяем текущую координату,
            //если мы нашли искомое, заканчиваем
            if (goalTest.test(currentState)) {
                return currentNode;
            }
            //проверяем, куда можно двинуться дальше и что мы еще не исследовали
            for (T child : successors.apply(currentState)) {
                if (explored.contains(child)) {
                    continue; // пропустить состояния, которые уже исследовали
                }
                explored.add(child);
                frontier.push(new Node<>(child, currentNode));
            }
        }
        return null; // все проверили, пути к целевой точке не нашли
    }

    public static <T> List<T> nodeToPath(Node<T> node) {    //Метод для построения найденного пути
        List<T> path = new ArrayList<>();
        path.add(node.state);
        // двигаемся назад, от конца к началу
        while (node.parent != null) {
            node = node.parent;
            path.add(0, node.state); // добавить в начало
        }
        return path;
    }


    public static void main(String[] args) {
        System.out.println(linearContains(List.of(1, 5, 15, 15, 15, 15, 20),
                5)); // true
        System.out.println(binaryContains(List.of("a", "d", "e", "f", "z"),
                "f")); // true
        System.out.println(binaryContains(List.of("john", "mark", "ronald",
                "sarah"), "sheila")); // false
    }
}