import java.util.*;
/**********************************************************************************************
* Gestor de Tareas en Consola                                                                 *     
* Este programa permite gestionar tareas utilizando dos estructuras de datos:  pila y cola.   *
*UTEL 2025                                                                                    *
* Ivette Yanin Aldama Aguiñaga                                                                *
***********************************************************************************************/
// Clase para representar una tarea
class Tarea {
    private int id; // Id  de la tarea
    private String descripcion; // Descripción de la tarea
    private int prioridad; // Prioridad  (1 a 5)

    public Tarea(int id, String descripcion, int prioridad) {
        this.id = id;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
    }

    // Método para mostrar la tarea de forma legible
    @Override
    public String toString() { //devuelve una cadena con la información de la tarea
        return "Tarea[ID: " + id + ", Descripción: " + descripcion + ", Prioridad: " + prioridad + "]";
    }
}

public class GestorTareasConsola {// Clase principal del programa

    // Método para simular la limpieza de consola en la terminal
    public static void limpiarConsola() {
        for (int i = 0; i < 50; ++i) System.out.println(); //imprime 50 líneas en blanco para smular limpieza
    }

    // Método para mostrar las tareas según el modo seleccionado (pila o cola)
    public static void mostrarTareas(Stack<Tarea> pila, Queue<Tarea> cola, int modo) {
        if (modo == 1) { // si el modo es pila...
            System.out.println("Tareas en la PILA (última arriba):"); //muestra las tareas en la pila
            if (pila.isEmpty()) { //verifica si la pila está vacía
                System.out.println("Sin tareas.");
            } else {
                for (int i = pila.size() - 1; i >= 0; i--) {//recorre la pila desde la parte superior hasta la inferior
                    //muestra las tareas en la pila desde la parte superior hasta la inferior
                    System.out.println(pila.get(i));//imprime cada tarea en la pila
                }
            }
        } else { //si el modo es cola...
            System.out.println("Tareas en la COLA (primera adelante):");// muestra las tareas en la cola
            if (cola.isEmpty()) { //verifica si la cola está vacía
                System.out.println("Sin tareas.");//si la cola está vacía muestra "Sin tareas"
            } else {//si la cola no está vacía...
                //muestra las tareas en la cola desde la parte frontal hasta la parte trasera
                for (Tarea t : cola) {//recorre la cola desde la parte frontal hasta la parte trasera
                    System.out.println(t);//imprime cada tarea en la cola
                }
            }
        }
    }

    public static void main(String[] args) {// Método principal del programa
        Scanner scanner = new Scanner(System.in);// Crea un objeto Scanner para leer la entrada del usuario
        Stack<Tarea> pila = new Stack<>(); // Estructura tipo pila (LIFO)
        Queue<Tarea> cola = new LinkedList<>(); // Estructura tipo cola (FIFO)

        boolean salirTotal = false;// Bandera para salir del programa

        // Bucle principal del programa
        while (!salirTotal) { //se  ejecuta mientras no se haya solicitado salir del programa
            System.out.println("Bienvenido al Gestor de Tareas");
            System.out.println("Selecciona el modo de trabajo: ");
            System.out.println("1. Pila (LIFO)");
            System.out.println("2. Cola (FIFO)");
            System.out.println("3. Salir");
            int modo = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            if (modo == 3) { //si el usuario selecciona salir...
                salirTotal = true;//se establece la bandera para salir del programa
                System.out.println("Programa finalizado.");
                break;//salida del bucle
            }

            boolean salir = false;
            // Submenú según el modo de trabajo elegido
            while (!salir) {
                limpiarConsola();//llama a la función para limpiar la consola
                mostrarTareas(pila, cola, modo);//llama a la función para mostrar las tareas según el modo seleccionado

                System.out.println("\nMenú:");
                System.out.println("1. Agregar tarea");
                System.out.println("2. Ver siguiente tarea");
                System.out.println("3. Completar tarea");
                System.out.println("4. Volver al menú principal");
                System.out.print("Opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir salto

                switch (opcion) {
                    case 1:
                        // Captura de datos para crear una nueva tarea
                        System.out.print("ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Descripción: ");
                        String descripcion = scanner.nextLine();
                        System.out.print("Prioridad (1-5): ");
                        int prioridad = scanner.nextInt();
                        scanner.nextLine();
                        Tarea nueva = new Tarea(id, descripcion, prioridad);// crea una nueva tarea con los datos capturados

                        // Agrega la tarea a la estructura correspondiente
                        if (modo == 1) {// si el modo es pila...
                            pila.push(nueva); // Agrega la tarea a la pila
                            System.out.println("Tarea agregada a la PILA.");
                        } else {// si el modo es cola...
                            cola.offer(nueva);// Agrega la tarea a la cola
                            System.out.println("Tarea agregada a la COLA.");
                        }
                        break;

                    case 2:
                        // Visualiza la próxima tarea sin eliminarla
                        if (modo == 1) { // si el modo es pila...
                            if (!pila.isEmpty())//verifica si la pila no está vacía
                                System.out.println("Próxima tarea en la PILA: " + pila.peek());//peek devuelve el elemento en la parte superior de la pila sin eliminarlo
                            else
                                System.out.println("No hay tareas en la PILA.");
                        } else {//si el modo es cola...

                            if (!cola.isEmpty())//verifica si la cola no está vacía
                                System.out.println("Próxima tarea en la COLA: " + cola.peek());//peek devuelve el elemento en la parte frontal de la cola sin eliminarlo
                            else
                                System.out.println("No hay tareas en la COLA.");
                        }
                        break;

                    case 3:
                        // Elimina y muestra la tarea en curso (la más reciente en pila o la más antigua en cola)
                        if (modo == 1) { // si el modo es pila...
                            if (!pila.isEmpty())//verifica si la pila no está vacía
                                System.out.println("Tarea completada de la PILA: " + pila.pop());//pop elimina y devuelve el elemento en la parte superior de la pila
                            else
                                System.out.println("No hay tareas en la PILA.");
                        } else {//si el modo es cola...

                            if (!cola.isEmpty())//verifica si la cola no está vacía
                                System.out.println("Tarea completada de la COLA: " + cola.poll());//poll elimina y devuelve el elemento en la parte frontal de la cola
                            
                            else
                                System.out.println("No hay tareas en la COLA.");
                        }
                        break;

                    case 4:
                        // Regresa al menú principal
                        salir = true;
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
                System.out.println("\nPresiona ENTER para continuar...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}
