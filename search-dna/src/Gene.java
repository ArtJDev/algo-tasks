import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Gene {
    public enum Nucleotide {
        A, C, G, T
    }

    public static class Codon implements Comparable<Codon> {
        public final Nucleotide first, second, third;
        private final Comparator<Codon> comparator = Comparator.comparing((Codon c) -> c.first)
                .thenComparing((Codon c) -> c.second)
                .thenComparing((Codon c) -> c.third);

        public Codon(String codonStr) {
            first = Nucleotide.valueOf(codonStr.substring(0, 1));
            second = Nucleotide.valueOf(codonStr.substring(1, 2));
            third = Nucleotide.valueOf(codonStr.substring(2, 3));
        }

        @Override
        public int compareTo(Codon other) {
            // сначала сравнивается первый объект, затем второй и т. д.
            // другими словами, первый объект имеет приоритет перед
            // вторым, а второй — перед третьим
            return comparator.compare(this, other);
        }
    }

    private ArrayList<Codon> codons = new ArrayList<>();

    public Gene(String geneStr) {
        for (int i = 0; i < geneStr.length() - 3; i += 3) {
            // Из каждых трех символов в строке формируем кодон
            codons.add(new Codon(geneStr.substring(i, i + 3)));
        }
    }

    public boolean linearContains(Codon key) {      //Линейный поиск кодона (последовательность из 3-х нуклеотидов) в гене
        for (Codon codon : codons) {
            if (codon.compareTo(key) == 0) {
                return true; // поиск совпадений
            }
        }
        return false;
    }

    public boolean binaryContains(Codon key) {      //Бинарный поиск кодона в гене
        // бинарный поиск работает только с отсортированными коллекциями
        ArrayList<Codon> sortedCodons = new ArrayList<>(codons);
        Collections.sort(sortedCodons);     //сортируем массив кодонов
        int low = 0;                        //начальная точка поиска
        int high = sortedCodons.size() - 1; //конечная точка поиска
        while (low <= high) {               //пока есть место для поиска
            int middle = (low + high) / 2;  //вычислили середину
            int comparison = codons.get(middle).compareTo(key); //сравнили середину с искомым кодоном
            if (comparison < 0) {           //если средний кодон меньше искомого
                low = middle + 1;           //передвигаем нижнюю точку поиска вправо от середины
            } else if (comparison > 0) {    //если средний кодон больше искомого
                high = middle - 1;          //передвигаем конечную току поиска влево от середины
            } else {                        //иначе средний кодон равен искомому
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

        String geneStr = "ACGTGGCTCTCTAACGTACGTACGTACGGGGTTTATATATACCCTAGGACTCCCTTT";
        Gene myGene = new Gene(geneStr);
        Codon acg = new Codon("ACG");
        Codon gat = new Codon("GAT");
        System.out.println(myGene.linearContains(acg)); // true
        System.out.println(myGene.linearContains(gat)); // false
        System.out.println(myGene.binaryContains(acg)); // true
        System.out.println(myGene.binaryContains(gat)); // false
    }
}
//Для линейного метода поиска аналог метод contains() из интерфейса Collection
//Для бинарного метода поиска аналог метод binarySearch() из интерфейса Collection