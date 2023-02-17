import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Maze {             //Класс лабиринт
    public enum Cell {          //Ячейки для лабиринта
        EMPTY(" "),
        BLOCKED("X"),
        START("S"),
        GOAL("G"),
        PATH("*");

        private final String code;

        Cell(String c) {
            code = c;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    public static class MazeLocation {      //Местоположение в лабиринте
        public final int row;
        public final int column;

        public MazeLocation(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MazeLocation that)) return false;

            if (row != that.row) return false;
            return column == that.column;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + column;
            return result;
        }
    }

    private final int rows, columns;
    private final MazeLocation start, goal;
    private Cell[][] grid;

    public Maze(int rows, int columns, MazeLocation start, MazeLocation goal, double sparseness) {
        // инициализация базовых переменных экземпляра
        this.rows = rows;
        this.columns = columns;
        this.start = start;
        this.goal = goal;
        // заполнение сетки пустыми ячейками
        grid = new Cell[rows][columns];
        for (Cell[] row : grid) {
            Arrays.fill(row, Cell.EMPTY);
        }
        // заполнение сетки заблокированными ячейками
        randomlyFill(sparseness);
        // заполнение начальной и конечной позиций в лабиринте
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    public Maze() {     //Инициализация лабиринта размером 10х10, задание начальной точки 0,0 и конечной точки 9,9, разряженность 15%
        this(10, 10, new MazeLocation(0, 0), new MazeLocation(9, 9), 0.15);
    }

    private void randomlyFill(double sparseness) {      //Рандомное заполнение лабиринта заблокированными ячейками
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (Math.random() < sparseness) {       //Math.random() выдает число от 0.0 до 1.0 и сравнивает с разряженностью 0.15
                    grid[row][column] = Cell.BLOCKED;   //Если условие совпало, ставит в ячейку блок
                }
            }
        }
    }

    // вывести красиво отформатированную версию лабиринта для печати
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                sb.append(cell.toString());
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public boolean goalTest(MazeLocation ml) {      //Метод проверяет, достигли ли мы целевой точки
        return goal.equals(ml);
    }

    public List<MazeLocation> successors(MazeLocation ml) {     //Функция передвижения по лабиринту от начальной точки к целевой
        //Функция проверят на пустоту верхнюю, нижнюю, левую и правую ячейку от текущей
        List<MazeLocation> locations = new ArrayList<>();
//если координата строки вверх меньше длины всей строки и ячейка с координатой этой строки и текущей колонки не заблокирована
        if (ml.row + 1 < rows && grid[ml.row + 1][ml.column] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row + 1, ml.column)); //то добавляем эту позицию в список
        }
//если координата строки вниз больше-равно 0 и ячейка с координатой этой строки и текущей колонки не заблокирована
        if (ml.row - 1 >= 0 && grid[ml.row - 1][ml.column] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row - 1, ml.column)); //то добавляем эту позицию в список
        }
//если координата колонки вверх меньше длины всей колонки и ячейка с координатой текущей строки и этой колонки не заблокирована
        if (ml.column + 1 < columns && grid[ml.row][ml.column + 1] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row, ml.column + 1)); //то добавляем эту позицию в список
//если координата колонки вниз больше-равно 0 и ячейка с координатой текущей строки и этой колонки не заблокирована
        }
        if (ml.column - 1 >= 0 && grid[ml.row][ml.column - 1] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row, ml.column - 1)); //то добавляем эту позицию в список
        }
        return locations;       //вернули список доступных местоположений
    }

    public void mark(List<MazeLocation> path) {     //Метод для разметки найденного пути
        for (MazeLocation ml : path) {
            grid[ml.row][ml.column] = Cell.PATH;
        }
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    public void clear(List<MazeLocation> path) {    //Метод для очистки размеченного пути
        for (MazeLocation ml : path) {
            grid[ml.row][ml.column] = Cell.EMPTY;
        }
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    public static void main(String[] args) {
        Maze m = new Maze();
        System.out.println(m);

        GenericSearch.Node<MazeLocation> solution1 = GenericSearch.dfs(m.start, m::goalTest, m::successors);
        if (solution1 == null) {
            System.out.println("Решение не найдено используя поиск в глубину!");
        } else {
            List<MazeLocation> path1 = GenericSearch.nodeToPath(solution1);
            m.mark(path1);
            System.out.println(m);
            m.clear(path1);
        }
    }
}