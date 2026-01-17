package Utilidad;

import Vista.VistaMonitor;
import Modelo.Monitor;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Clase auxiliar para gestionar la tabla de Monitores
 */
public class GestionTablasMonitor {

    // Definimos el modelo de la tabla (el objeto que guarda los datos)
    // Sobreescribimos isCellEditable para que el usuario no pueda editar las celdas directamente
    public static DefaultTableModel modeloTablaMonitores = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    // 1. Inicializar la tabla: Asigna el modelo a la tabla de la vista
    public static void inicializarTablaMonitores(VistaMonitor vMonitor) {
        vMonitor.jTableMonitores.setModel(modeloTablaMonitores);
    }

    // 2. Dibujar la tabla: Define las columnas y sus anchos
    public static void dibujarTablaMonitores(VistaMonitor vMonitor) {
        String[] columnas = {"Código", "Nombre", "DNI", "Teléfono", "Correo", "Fecha Incorp.", "Nick"};

        modeloTablaMonitores.setColumnIdentifiers(columnas);

        // Configuración estética: Ajuste automático y prohibir mover columnas
        vMonitor.jTableMonitores.getTableHeader().setResizingAllowed(false);
        vMonitor.jTableMonitores.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Opcional: Definir anchos específicos para que se vea mejor
        vMonitor.jTableMonitores.getColumnModel().getColumn(0).setPreferredWidth(50);  // Código
        vMonitor.jTableMonitores.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        vMonitor.jTableMonitores.getColumnModel().getColumn(2).setPreferredWidth(80);  // DNI
        // El resto se ajustarán automáticamente
    }

    // 3. Rellenar la tabla: Recibe la lista de monitores y crea las filas
    public static void rellenarTablaMonitores(List<Monitor> monitores) {
        // Primero vaciamos la tabla para no duplicar datos si recargamos
        vaciarTablaMonitores();

        Object[] fila = new Object[7]; // 7 columnas

        for (Monitor monitor : monitores) {
            fila[0] = monitor.getCodMonitor();
            fila[1] = monitor.getNombre();
            fila[2] = monitor.getDni();
            fila[3] = monitor.getTelefono();
            fila[4] = monitor.getCorreo();
            fila[5] = monitor.getFechaEntrada();
            fila[6] = monitor.getNick();

            modeloTablaMonitores.addRow(fila);
        }
    }

    // 4. Vaciar la tabla: Elimina todas las filas
    public static void vaciarTablaMonitores() {
        modeloTablaMonitores.setRowCount(0);
    }
}