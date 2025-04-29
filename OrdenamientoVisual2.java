import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Clase para representar un libro
class Book {
    private String title;//titulo
    private String genre;//genero

    public Book(String title, String genre) {
        this.title = title;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    @Override //override toString para mostrar el libro en la tabla
    public String toString() {
        return title + " (" + genre + ")";
    }

     // Sobreescribe equals y hashCode para comparar libros correctamente (si es necesario en colecciones)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre);
    }
}

// Clase para representar un usuario
class User {
    private String matricula;
    private String nombre;
    private String apellido;
    private Book libro;  // Referencia a un objeto Libro
    private String estado;

    public User(String matricula, String nombre, String apellido, Book libro, String estado) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.libro = libro;
        this.estado = estado;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Book getLibro() {
        return libro;
    }

    public String getEstado() {
        return estado;
    }

    @Override //
    public String toString() {
        return matricula + " - " + nombre + " " + apellido;
    }
}

public class OrdenamientoVisualOptimizado extends JFrame {

    private List<Book> libros = generarLibros(2000); // Usamos List en lugar de String[][]
    private List<User> usuarios = generarUsuarios(50000, libros); // Usamos List
    private List<User> datosTabla = combinarDatos(usuarios, libros); // Usamos List

    private JTable tabla;
    private JComboBox<String> criterioCombo;
    private JComboBox<String> metodoCombo;
    private JTextArea analisisArea;
    private final AtomicBoolean isCancelled = new AtomicBoolean(false); // Para la interrupción de hilos de forma segura
    private JButton detenerBtn;
    private DefaultTableModel tableModel;

    public OrdenamientoVisualOptimizado() {
        // Inicialización de la interfaz gráfica (similar al original, pero adaptado para List y Book/User)
        setTitle("Sistema de Ordenamiento - Biblioteca");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.setBackground(new Color(173, 216, 230));

        criterioCombo = new JComboBox<>(new String[]{
                "Título del Libro (A-Z)", "Título del Libro (Z-A)",
                "Nombre del Usuario (A-Z)", "Nombre del Usuario (Z-A)",
                "Apellido del Usuario (A-Z)", "Apellido del Usuario (Z-A)",
                "Libros Disponibles", "Libros No Disponibles",
                "Matrícula (Menor a Mayor)", "Género (A-Z)"
        });
        metodoCombo = new JComboBox<>(new String[]{"Burbuja", "Selección", "Inserción", "Quicksort", "Shell"});
        JButton ordenarBtn = new JButton("Ordenar");
        JButton analizarBtn = new JButton("Analizar Todos");
        detenerBtn = new JButton("Detener");
        detenerBtn.setEnabled(false);

        panelSuperior.add(new JLabel("Criterio:"));
        panelSuperior.add(criterioCombo);
        panelSuperior.add(new JLabel("Método:"));
        panelSuperior.add(metodoCombo);
        panelSuperior.add(ordenarBtn);
        panelSuperior.add(analizarBtn);
        panelSuperior.add(detenerBtn);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla central (muestra de 2,000 registros)
        String[] columnas = {"Matrícula", "Nombre", "Apellido", "Libro", "Género", "Estado"};
        tableModel = new DefaultTableModel(convertUsersToTableData(datosTabla, 0, 2000), columnas); // Mostramos un subconjunto
        tabla = new JTable(tableModel);
        tabla.setFillsViewportHeight(true);
        tabla.setBackground(Color.WHITE);
        tabla.setGridColor(new Color(200, 200, 200));
        JScrollPane scrollTabla = new JScrollPane(tabla);
        add(scrollTabla, BorderLayout.CENTER);

        analisisArea = new JTextArea(10, 50);
        analisisArea.setEditable(false);
        analisisArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        analisisArea.setBackground(new Color(245, 245, 220));
        JScrollPane scrollAnalisis = new JScrollPane(analisisArea);
        add(scrollAnalisis, BorderLayout.SOUTH);

        ordenarBtn.addActionListener(e -> ordenar());
        analizarBtn.addActionListener(e -> analizarTodos());
        detenerBtn.addActionListener(e -> isCancelled.set(true)); // Usamos AtomicBoolean
    }

    // Método para generar la lista de libros
    private List<Book> generarLibros(int cantidad) {
        List<Book> libros = new ArrayList<>();
        Set<String> genres = new HashSet<>(Arrays.asList("Drama", "Thriller", "Comedia", "Romántica", "Ficción", "Documental", "Fantasía", "Terror")); // Usamos Set para evitar duplicados
        String[] titulosBase = {"El", "La", "Un", "Mi", "Nuestro", "Sombras de", "Luz en", "Caminos de"};
        String[] titulosFin = {"Secreto", "Destino", "Amor", "Oscuridad", "Tiempo", "Vida", "Misterio", "Alma"};
        Random rand = new Random();

        for (int i = 0; i < cantidad; i++) {
            String titulo = titulosBase[rand.nextInt(titulosBase.length)] + " " +
                    titulosFin[rand.nextInt(titulosFin.length)];
            String genero = genres.toArray(new String[0])[rand.nextInt(genres.size())]; // Obtenemos un género aleatorio del Set
            libros.add(new Book(titulo, genero));
        }
        return libros;
    }

    // Método para generar la lista de usuarios
    private List<User> generarUsuarios(int cantidad, List<Book> libros) {
        List<User> usuarios = new ArrayList<>();
        String[] nombres = {"Ana", "Luis", "Pedro", "María", "Carlos", "Sofía", "Daniel", "Elena", "Juan", "Lucía",
                "Alberto", "Fernanda", "Oscar", "Raúl", "Patricia", "Javier", "Mónica", "Rubén", "Gloria", "Héctor"};
        String[] apellidos = {"Gómez", "Fernández", "Martínez", "López", "Pérez", "García", "Hernández", "Rodríguez", "Torres", "Vargas"};
        Random rand = new Random();
        Set<String> usedUserCombinations = new HashSet<>(); // Rastrear combinaciones de nombres usadas (enfoque simple para un conjunto limitado de nombres)

        for (int i = 0; i < cantidad; i++) {
            String matricula = String.format("U%05d", i + 1);
            String nombre = nombres[rand.nextInt(nombres.length)];
            String apellido = apellidos[rand.nextInt(apellidos.length)];
            Book libro = libros.get(rand.nextInt(libros.size()));
            String estado = rand.nextBoolean() ? "Apartado" : "Devuelto";

            String userCombination = nombre + apellido;
            // Una forma básica de reducir (no eliminar) nombres completos duplicados dado el conjunto limitado de nombres
            if (!usedUserCombinations.contains(userCombination)) {
                usuarios.add(new User(matricula, nombre, apellido, libro, estado));
                usedUserCombinations.add(userCombination);
            } else {
                // Si la combinación de nombres ya se usó, creamos un nombre ligeramente modificado
                usuarios.add(new User(matricula, nombre + i, apellido, libro, estado));
            }        }
        return usuarios;
    }

    // Método para combinar datos (innecesario en esta versión mejorada)
    private List<User> combinarDatos(List<User> usuarios, List<Book> libros) {
        // En esta versión mejorada, el 'género' ya es parte del objeto Libro
        // dentro del objeto Usuario.  Por lo tanto, no es necesario volver a combinar.
        return new ArrayList<>(usuarios); // Devuelve una copia para evitar modificaciones accidentales
    }

    // Método auxiliar para convertir una List<User> en un String[][] para mostrar en la JTable
    private String[][] convertUsersToTableData(List<User> users, int start, int end) {
        int displayLength = Math.min(end, users.size()) - start;
        String[][] data = new String[displayLength][6];
        for (int i = 0; i < displayLength; i++) {
            User user = users.get(start + i);
            data[i] = new String[]{
                    user.getMatricula(),
                    user.getNombre(),
                    user.getApellido(),
                    (user.getLibro() != null) ? user.getLibro().getTitle() : "",
                    (user.getLibro() != null) ? user.getLibro().getGenre() : "",
                    user.getEstado()
            };
        }
        return data;
    }

    // Método para iniciar el ordenamiento
    private void ordenar() {
        isCancelled.set(false); // Reseteamos el indicador de cancelación
        detenerBtn.setEnabled(true);
        analisisArea.setText("Procesando ordenamiento...");
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                int criterioIndex = criterioCombo.getSelectedIndex();
                List<User> copia = new ArrayList<>(datosTabla); // Copiamos la lista para no modificar la original

                long startTime = System.nanoTime();
                switch (metodoCombo.getSelectedIndex()) {
                    case 0:
                        burbuja(copia, criterioIndex);
                        break;
                    case 1:
                        seleccion(copia, criterioIndex);
                        break;
                    case 2:
                        insercion(copia, criterioIndex);
                        break;
                    case 3:
                        quicksort(copia, 0, copia.size() - 1, criterioIndex);
                        break;
                    case 4:
                        shellsort(copia, criterioIndex);
                        break;
                }
                long endTime = System.nanoTime();
                if (isCancelled.get()) return "Ordenamiento detenido por el usuario."; // Verificamos AtomicBoolean

                long tiempoMs = (endTime - startTime) / 1_000_000;
                double tiempoS = tiempoMs / 1000.0;

                // Actualizamos la tabla con un subconjunto de los datos ordenados
                tableModel.setDataVector(convertUsersToTableData(copia, 0, 2000),
                        new String[]{"Matrícula", "Nombre", "Apellido", "Libro", "Género", "Estado"});
                tableModel.fireTableDataChanged();

                return "Método seleccionado: " + metodoCombo.getSelectedItem() + "\n" +
                        "Tiempo: " + tiempoMs + " ms (" + String.format("%.3f", tiempoS) + " s)";
            }

            @Override
            protected void done() {
                try {
                    analisisArea.setText(get());
                    detenerBtn.setEnabled(false); // Deshabilitamos el botón Detener al finalizar
                } catch (Exception e) {
                    analisisArea.setText("Error al ordenar: " + e.getMessage());
                    detenerBtn.setEnabled(false);
                }
            }
        };
        worker.execute();
    }

    // Método para analizar todos los métodos de ordenamiento
    private void analizarTodos() {
        isCancelled.set(false);
        detenerBtn.setEnabled(true);
        analisisArea.setText("Procesando análisis completo...\nEsto puede tardar varios segundos debido al tamaño de los datos (50,000 usuarios).");
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                String[] metodos = {"Burbuja", "Selección", "Inserción", "Quicksort", "Shell"};
                long[] tiempos = new long[metodos.length];
                StringBuilder analisis = new StringBuilder("=== Análisis de Métodos ===\n\n");

                for (int i = 0; i < metodos.length; i++) {
                    if (isCancelled.get()) return "Análisis detenido por el usuario."; // Verificamos la cancelación
                    List<User> copia = new ArrayList<>(datosTabla); // Copiamos la lista para cada método
                    long startTime = System.nanoTime();
                    switch (i) {
                        case 0:
                            burbuja(copia, criterioCombo.getSelectedIndex());
                            break;
                        case 1:
                            seleccion(copia, criterioCombo.getSelectedIndex());
                            break;
                        case 2:
                            insercion(copia, criterioCombo.getSelectedIndex());
                            break;
                        case 3:
                            quicksort(copia, 0, copia.size() - 1, criterioCombo.getSelectedIndex());
                            break;
                        case 4:
                            shellsort(copia, criterioCombo.getSelectedIndex());
                            break;
                    }
                    long endTime = System.nanoTime();
                    tiempos[i] = (endTime - startTime) / 1_000_000;
                    double tiempoS = tiempos[i] / 1000.0;
                    analisis.append(String.format("%-12s: %6d ms  (%.3f s)\n", metodos[i], tiempos[i], tiempoS));
                }
                return analisis.toString();
            }

            @Override
            protected void done() {
                try {
                    analisisArea.setText(get());
                    detenerBtn.setEnabled(false);
                } catch (Exception e) {
                    analisisArea.setText("Error al analizar: " + e.getMessage());
                    detenerBtn.setEnabled(false);
                }            }
        };
        worker.execute();
    }

    // Implementación del algoritmo de ordenamiento de burbuja
    private void burbuja(List<User> arr, int criterio) {
        int n = arr.size();
        for (int i = 0; i < n - 1 && !isCancelled.get(); i++) { // Verificamos la cancelación en cada iteración
            for (int j = 0; j < n - i - 1 && !isCancelled.get(); j++) {
                if (compare(arr.get(j), arr.get(j + 1), criterio) > 0) {
                    User temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);
                }
            }
        }
    }

    // Implementación del algoritmo de ordenamiento de selección
    private void seleccion(List<User> arr, int criterio) {
        int n = arr.size();
        for (int i = 0; i < n - 1 && !isCancelled.get(); i++) { // Verificamos la cancelación en cada iteración
            int minIdx = i;
            for (int j = i + 1; j < n && !isCancelled.get(); j++) {
                if (compare(arr.get(j), arr.get(minIdx), criterio) < 0) {
                    minIdx = j;
                }
            }
            User temp = arr.get(minIdx);
            arr.set(minIdx, arr.get(i));
            arr.set(i, temp);
        }
    }

    // Implementación del algoritmo de ordenamiento por inserción
    private void insercion(List<User> arr, int criterio) {
        int n = arr.size();
        for (int i = 1; i < n && !isCancelled.get(); i++) { // Verificamos la cancelación en cada iteración
            User key = arr.get(i);
            int j = i - 1;
            while (j >= 0 && compare(arr.get(j), key, criterio) > 0 && !isCancelled.get()) {
                arr.set(j + 1, arr.get(j));
                j--;
            }
            arr.set(j + 1, key);
        }
    }

    // Implementación del algoritmo de ordenamiento Quicksort
    private void quicksort(List<User> arr, int low, int high, int criterio) {
        if (low < high && !isCancelled.get()) { // Verificamos la cancelación antes de la recursión
            int pi = partition(arr, low, high, criterio);
            quicksort(arr, low, pi - 1, criterio);
            quicksort(arr, pi + 1, high, criterio);
        }
    }

    private int partition(List<User> arr, int low, int high, int criterio) {
        User pivot = arr.get(high);
        int i = low - 1;
        for (int j = low; j < high && !isCancelled.get(); j++) { // Verificamos la cancelación en el bucle
            if (compare(arr.get(j), pivot, criterio) < 0) {
                i++;
                User temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }
        User temp = arr.get(i + 1);
        arr.set(i + 1, arr.get(high));
        arr.set(high, temp);
        return i + 1;
    }

    // Implementación del algoritmo de ordenamiento Shellsort
    private void shellsort(List<User> arr, int criterio) {
        int n = arr.size();
        for (int gap = n / 2; gap > 0 && !isCancelled.get(); gap /= 2) { // Verificamos la cancelación en el bucle externo
            for (int i = gap; i < n && !isCancelled.get(); i++) { // Y en el interno
                User temp = arr.get(i);
                int j;
                for (j = i; j >= gap && compare(arr.get(j - gap), temp, criterio) > 0 && !isCancelled.get(); j -= gap) {
                    arr.set(j, arr.get(j - gap));
                }
                arr.set(j, temp);
            }
        }
    }

    // Método para comparar dos usuarios según el criterio de ordenamiento
    private int compare(User a, User b, int criterio) {
        switch (criterio) {
            case 0:
                return a.getLibro().getTitle().compareTo(b.getLibro().getTitle()); // Título A-Z
            case 1:
                return b.getLibro().getTitle().compareTo(a.getLibro().getTitle()); // Título Z-A
            case 2:
                return a.getNombre().compareTo(b.getNombre()); // Nombre A-Z
            case 3:
                return b.getNombre().compareTo(a.getNombre()); // Nombre Z-A
            case 4:
                return a.getApellido().compareTo(b.getApellido()); // Apellido A-Z
            case 5:
                return b.getApellido().compareTo(a.getApellido()); // Apellido Z-A
            case 6:
                return "Devuelto".equals(a.getEstado()) ? -1 : 1; // Libros Disponibles
            case 7:
                return "Apartado".equals(a.getEstado()) ? -1 : 1; // Libros No Disponibles
            case 8:
                return a.getMatricula().compareTo(b.getMatricula()); // Matrícula Menor a Mayor
            case 9:
                return a.getLibro().getGenre().compareTo(b.getLibro().getGenre()); // Género A-Z
            default:
                return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrdenamientoVisualOptimizado app = new OrdenamientoVisualOptimizado();
            app.setVisible(true);
        });
    }
}
