import java.util.Random;
import java.util.Arrays;

public class ComparacionSorts3 {

    public static void main(String[] args) {
        int n = 50000;
        int[] original = new int[n];
        Random rand = new Random();

        // Llenar arreglo original con números aleatorios
        for (int i = 0; i < n; i++) {
            original[i] = rand.nextInt(50000);
        }

        // Copias del arreglo para cada algoritmo
        int[] quickSortArr = Arrays.copyOf(original, n);
        int[] selectionSortArr = Arrays.copyOf(original, n);
        int[] bubbleSortArr = Arrays.copyOf(original, n);
        int[] shellSortArr = Arrays.copyOf(original, n);
        int[] insertionSortArr = Arrays.copyOf(original, n);

        // QuickSort
        long t1 = System.currentTimeMillis();
        quickSort(quickSortArr, 0, n - 1);
        long t2 = System.currentTimeMillis();
        System.out.println("QuickSort: " + (t2 - t1) + " ms");

        // SelectionSort
        t1 = System.currentTimeMillis();
        selectionSort(selectionSortArr);
        t2 = System.currentTimeMillis();
        System.out.println("SelectionSort: " + (t2 - t1) + " ms");

        // BubbleSort
        t1 = System.currentTimeMillis();
        bubbleSort(bubbleSortArr);
        t2 = System.currentTimeMillis();
        System.out.println("BubbleSort: " + (t2 - t1) + " ms");

        // ShellSort
        t1 = System.currentTimeMillis();
        shellSort(shellSortArr);
        t2 = System.currentTimeMillis();
        System.out.println("ShellSort: " + (t2 - t1) + " ms");

        // InsertionSort
        t1 = System.currentTimeMillis();
        insertionSort(insertionSortArr);
        t2 = System.currentTimeMillis();
        System.out.println("InsertionSort: " + (t2 - t1) + " ms");

        // Mostrar primeros 20 elementos ordenados (QuickSort)
        System.out.println("\nPrimeros 20 elementos ordenados (QuickSort):");
        for (int i = 0; i < 20; i++) {
            System.out.print(quickSortArr[i] + " ");
        }
    }

    // QuickSort
    public static void quickSort(int[] arr, int bajo, int alto) {
        if (bajo < alto) {
            int pi = particion(arr, bajo, alto);
            quickSort(arr, bajo, pi - 1);
            quickSort(arr, pi + 1, alto);
        }
    }

    public static int particion(int[] arr, int bajo, int alto) {
        int pivote = arr[alto];
        int i = (bajo - 1);
        for (int j = bajo; j < alto; j++) {
            if (arr[j] <= pivote) {
                i++;
                int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
            }
        }
        int temp = arr[i + 1]; arr[i + 1] = arr[alto]; arr[alto] = temp;
        return i + 1;
    }

    // SelectionSort
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx]; arr[minIdx] = arr[i]; arr[i] = temp;
        }
    }

    // BubbleSort
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean intercambiado;
        for (int i = 0; i < n - 1; i++) {
            intercambiado = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = temp;
                    intercambiado = true;
                }
            }
            if (!intercambiado) break;
        }
    }

    // ShellSort
    public static void shellSort(int[] arr) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j;
                for (j = i; j >= gap && arr[j - gap] > temp; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = temp;
            }
        }
    }

    // InsertionSort
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j]; j--;
            }
            arr[j + 1] = key;
        }
    }
}