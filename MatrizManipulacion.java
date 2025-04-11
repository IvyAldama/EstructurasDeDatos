/*************************************************
 *  Programa de operaciones con matrices         *
 * Ivette Yanin Aldama Aguiñaga                  *
 * UTEL Diplomado en ciencia de datos semana 2   *
 *************************************************/

public class MatrizManipulacion {

    /*Este es un metodo para declarar e inicializar una matriz con valores de 1 en adelante*/
    public static int[][] inicializarMatriz(int filas, int columnas) {
        int[][] matriz = new int[filas][columnas];
        int valor = 1;

        // Recorremos cada posición para asignar un valor secuencial con estos ciclos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = valor++;
            }
        }
        return matriz; // matriz con valores secuenciales
    }

    /* Metodo para imprimir los valores de una matriz en consola.*/
    public static void imprimirMatriz(int[][] matriz) {
        for (int[] fila : matriz) {
            for (int valor : fila) {
                System.out.print(valor + "\t"); // Imprime valor com una tabulacion \t
            }
            System.out.println(); // Este es un salto de linea
        }
    }

    /* Metodo que suma todos los elementos de la matriz. */
    public static int sumaMatriz(int[][] matriz) {
        int suma = 0;

        // Sumar elemento x elemento
        for (int[] fila : matriz) {
            for (int valor : fila) {
                suma += valor;
            }
        }

        return suma; //devuelve la suma
    }

    /* Este metodo promedia los elementos de la matriz.*/
    public static double promedioMatriz(int[][] matriz) {
        int suma = sumaMatriz(matriz); // Usamos la función suma del metodo anterior
        int totalElementos = matriz.length * matriz[0].length; // Se usa length para obtener todos los elementos (filas x columnas)
        return (double) suma / totalElementos; // devuelve el promedio valos de la suma entre todos los elementos
    }

    /*Este metodo rota la matriz 90 grados en sentido a la derecha.*/
    public static int[][] rotarMatrizDerecha(int[][] matriz) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        int[][] rotada = new int[columnas][filas]; // Intercambiamos filas y columnas

     // Estos bucñes siven para reubicar cada elemento en la nueva matriz rotada
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                rotada[j][filas - 1 - i] = matriz[i][j];
            }
        }
        return rotada; // devuelve la nueva matriz rotada
    }

    /*Metodo para aplicar la reflexion de la matriz
     *es decir "reflejar horizontalmente la matriz (efecto espejo)"".*/
    public static int[][] reflejarHorizontal(int[][] matriz) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        int[][] reflejada = new int[filas][columnas]; //nueva matriz

        // en estos ciclos se invierte el orden de las columnas por fila
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                reflejada[i][j] = matriz[i][columnas - 1 - j];
            }
        }

        return reflejada;// refleja la matriz reflejada
    }

    /*Este es el metodo principal para probar el programa*/
    public static void main(String[] args) {
        int[][] matriz = inicializarMatriz(3, 3); //Crea la matriz bidimensional de 3 x 3
        System.out.println("Matriz original con valores secuenciales:");
        imprimirMatriz(matriz);

        System.out.println("\nSuma de elementos: " + sumaMatriz(matriz)); //Muestra la suma de los elementos del metodo suma

        System.out.println("El promedio de los elementos es: " + promedioMatriz(matriz)); //promedio de los elementos del metodo promedio

    
        System.out.println("\nMatriz rotada 90 grados a la derecha:");// Aqui se muestra el resultado de rotar la matriz
        int[][] rotada = rotarMatrizDerecha(matriz);
        imprimirMatriz(rotada);

        System.out.println("\nMatriz con reflexion horizontalmente:");//Resultado de aplicar reflexion 
        int[][] reflejada = reflejarHorizontal(matriz);
        imprimirMatriz(reflejada);
    }
}