import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OrdenamientoVisual extends JFrame {
    private String[][] libros = generarLibros(2000); // 2,000 libros
    private String[][] usuarios = generarUsuarios(50000, libros); // 50,000 usuarios
    private String[][] datosTabla = combinarDatos(usuarios, libros); // Combinación completa

    private JTable tabla;
    private JComboBox<String> criterioCombo;
    private JComboBox<String> metodoCombo;
    private JTextArea analisisArea;
    private volatile boolean isCancelled = false;
    private JButton detenerBtn;
    private DefaultTableModel tableModel;

    public OrdenamientoVisual() {
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
        tableModel = new DefaultTableModel(Arrays.copyOfRange(datosTabla, 0, 2000), columnas); // Cambiado a 2000
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
        detenerBtn.addActionListener(e -> isCancelled = true);
    }

    private String[][] generarLibros(int cantidad) {
        String[] generos = {"Drama", "Thriller", "Comedia", "Romántica", "Ficción", "Documental", "Fantasía", "Terror"};
        String[] titulosBase = {"El", "La", "Un", "Mi", "Nuestro", "Sombras de", "Luz en", "Caminos de"};
        String[] titulosFin = {"Secreto", "Destino", "Amor", "Oscuridad", "Tiempo", "Vida", "Misterio", "Alma"};
        Random rand = new Random();
        String[][] libros = new String[cantidad][2];

        for (int i = 0; i < cantidad; i++) {
            String titulo = titulosBase[rand.nextInt(titulosBase.length)] + " " + 
                           titulosFin[rand.nextInt(titulosFin.length)];
            String genero = generos[rand.nextInt(generos.length)];
            libros[i] = new String[]{titulo, genero};
        }
        return libros;
    }

    private String[][] generarUsuarios(int cantidad, String[][] libros) {
        String[] nombres = {"Ana", "Luis", "Pedro", "María", "Carlos", "Sofía", "Daniel", "Elena", "Juan", "Lucía",
                            "Alberto", "Fernanda", "Oscar", "Raúl", "Patricia", "Javier", "Mónica", "Rubén", "Gloria", "Héctor"};
        String[] apellidos = {"Gómez", "Fernández", "Martínez", "López", "Pérez", "García", "Hernández", "Rodríguez", "Torres", "Vargas"};
        Random rand = new Random();
        String[][] usuarios = new String[cantidad][5];

        for (int i = 0; i < cantidad; i++) {
            String matricula = String.format("U%05d", i + 1);
            String nombre = nombres[rand.nextInt(nombres.length)];
            String apellido = apellidos[rand.nextInt(apellidos.length)];
            String[] libro = libros[rand.nextInt(libros.length)];
            String estado = rand.nextBoolean() ? "Apartado" : "Devuelto";
            usuarios[i] = new String[]{matricula, nombre, apellido, libro[0], estado};
        }
        return usuarios;
    }

    private String[][] combinarDatos(String[][] usuarios, String[][] libros) {
        String[][] datos = new String[usuarios.length][6];
        for (int i = 0; i < usuarios.length; i++) {
            String libroUsuario = usuarios[i][3];
            String genero = "";
            for (String[] libro : libros) {
                if (libro[0].equals(libroUsuario)) {
                    genero = libro[1];
                    break;
                }
            }
            datos[i] = new String[]{usuarios[i][0], usuarios[i][1], usuarios[i][2], usuarios[i][3], genero, usuarios[i][4]};
        }
        return datos;
    }

    private String[][] copiarDatos(String[][] original) {
        String[][] copia = new String[original.length][];
        for (int i = 0; i < original.length; i++) {
            copia[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copia;
    }

    private void ordenar() {
        isCancelled = false;
        detenerBtn.setEnabled(true);
        analisisArea.setText("Procesando ordenamiento...");
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                int criterioIndex = criterioCombo.getSelectedIndex();
                int metodoIndex = metodoCombo.getSelectedIndex();
                String[][] copia = copiarDatos(datosTabla);

                long startTime = System.nanoTime();
                switch (metodoIndex) {
                    case 0: burbuja(copia, criterioIndex); break;
                    case 1: seleccion(copia, criterioIndex); break;
                    case 2: insercion(copia, criterioIndex); break;
                    case 3: quicksort(copia, 0, copia.length - 1, criterioIndex); break;
                    case 4: shellsort(copia, criterioIndex); break;
                }
                long endTime = System.nanoTime();
                if (isCancelled) return "Ordenamiento detenido por el usuario.";
                long tiempoMs = (endTime - startTime) / 1_000_000;
                double tiempoS = tiempoMs / 1000.0;

                // Actualizar tabla con muestra de 2,000 registros
                tableModel.setDataVector(Arrays.copyOfRange(copia, 0, 2000), 
                    new String[]{"Matrícula", "Nombre", "Apellido", "Libro", "Género", "Estado"});
                tableModel.fireTableDataChanged();

                return "Método seleccionado: " + metodoCombo.getSelectedItem() + "\n" +
                       "Tiempo: " + tiempoMs + " ms (" + String.format("%.3f", tiempoS) + " s)";
            }

            @Override
            protected void done() {
                try {
                    analisisArea.setText(get());
                    detenerBtn.setEnabled(false);
                } catch (Exception e) {
                    analisisArea.setText("Error al ordenar: " + e.getMessage());
                    detenerBtn.setEnabled(false);
                }
            }
        };
        worker.execute();
    }

    private void analizarTodos() {
        isCancelled = false;
        detenerBtn.setEnabled(true);
        analisisArea.setText("Procesando análisis completo...\nEsto puede tomar varios segundos debido al tamaño de los datos (50,000 usuarios).");
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                int criterioIndex = criterioCombo.getSelectedIndex();
                String[] metodos = {"Burbuja", "Selección", "Inserción", "Quicksort", "Shell"};
                long[] tiempos = new long[metodos.length];
                StringBuilder analisis = new StringBuilder("=== Análisis de Métodos ===\n\n");

                for (int i = 0; i < metodos.length; i++) {
                    if (isCancelled) return "Análisis detenido por el usuario.";
                    String[][] copia = copiarDatos(datosTabla);
                    long startTime = System.nanoTime();
                    switch (i) {
                        case 0: burbuja(copia, criterioIndex); break;
                        case 1: seleccion(copia, criterioIndex); break;
                        case 2: insercion(copia, criterioIndex); break;
                        case 3: quicksort(copia, 0, copia.length - 1, criterioIndex); break;
                        case 4: shellsort(copia, criterioIndex); break;
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
                }
            }
        };
        worker.execute();
    }

    private void burbuja(String[][] arr, int criterio) {
        int n = arr.length;
        for (int i = 0; i < n - 1 && !isCancelled; i++) {
            for (int j = 0; j < n - i - 1 && !isCancelled; j++) {
                if (compare(arr[j], arr[j + 1], criterio) > 0) {
                    String[] temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    private void seleccion(String[][] arr, int criterio) {
        int n = arr.length;
        for (int i = 0; i < n - 1 && !isCancelled; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n && !isCancelled; j++) {
                if (compare(arr[j], arr[minIdx], criterio) < 0) {
                    minIdx = j;
                }
            }
            String[] temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    private void insercion(String[][] arr, int criterio) {
        int n = arr.length;
        for (int i = 1; i < n && !isCancelled; i++) {
            String[] key = arr[i];
            int j = i - 1;
            while (j >= 0 && compare(arr[j], key, criterio) > 0 && !isCancelled) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private void quicksort(String[][] arr, int low, int high, int criterio) {
        if (low < high && !isCancelled) {
            int pi = partition(arr, low, high, criterio);
            quicksort(arr, low, pi - 1, criterio);
            quicksort(arr, pi + 1, high, criterio);
        }
    }

    private int partition(String[][] arr, int low, int high, int criterio) {
        String[] pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high && !isCancelled; j++) {
            if (compare(arr[j], pivot, criterio) < 0) {
                i++;
                String[] temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        String[] temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    private void shellsort(String[][] arr, int criterio) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0 && !isCancelled; gap /= 2) {
            for (int i = gap; i < n && !isCancelled; i++) {
                String[] temp = arr[i];
                int j;
                for (j = i; j >= gap && compare(arr[j - gap], temp, criterio) > 0 && !isCancelled; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = temp;
            }
        }
    }

    private int compare(String[] a, String[] b, int criterio) {
        switch (criterio) {
            case 0: return a[3].compareTo(b[3]); // Título A-Z
            case 1: return b[3].compareTo(a[3]); // Título Z-A
            case 2: return a[1].compareTo(b[1]); // Nombre A-Z
            case 3: return b[1].compareTo(a[1]); // Nombre Z-A
            case 4: return a[2].compareTo(b[2]); // Apellido A-Z
            case 5: return b[2].compareTo(a[2]); // Apellido Z-A
            case 6: return "Devuelto".equals(a[5]) ? -1 : 1; // Libros Disponibles
            case 7: return "Apartado".equals(a[5]) ? -1 : 1; // Libros No Disponibles
            case 8: return a[0].compareTo(b[0]); // Matrícula Menor a Mayor
            case 9: return a[4].compareTo(b[4]); // Género A-Z
            default: return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrdenamientoVisual app = new OrdenamientoVisual();
            app.setVisible(true);
        });
    }
}