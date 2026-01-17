package Utilidad;

import Modelo.Actividad;
import Vista.VistaActividad;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * CLASE UTILITARIA PARA LA GESTIONTABLASACTIVIDAD
 * PROPORCIONA FUNCIONALIDADES PARA LA GESTION COMPLETA DEL SISTEMA DE GIMNASIO
 * 
 * @author SISTEMA DE GESTION DE GIMNASIO
 * @version 1.0
 */
public class GestionTablasActividad {

    /** ATRIBUTO MODELOTABLAACTIVIDADES */
    public static DefaultTableModel modeloTablaActividades = new DefaultTableModel() {
        /**
         * VERIFICA SI CELLEDITABLE
         *
         * @param row PARAMETRO ROW
         * @param column PARAMETRO COLUMN
         * @return RETORNA BOOLEAN
         */
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    /**
     * INICIALIZA LOS COMPONENTES Y CONFIGURACIONES
     *
     * @param vActividad PARAMETRO VACTIVIDAD
     * @return RETORNA STATIC VOID
     */
    public static void inicializarTablaActividades(VistaActividad vActividad) {
        vActividad.jTableActividades.setModel(modeloTablaActividades);
    }

    /**
     * METODO DIBUJARTABLAACTIVIDADES
     *
     * @param vActividad PARAMETRO VACTIVIDAD
     * @return RETORNA STATIC VOID
     */
    public static void dibujarTablaActividades(VistaActividad vActividad) {
        // AÑADIDA COLUMNA "Monitor" al final
        String[] columnas = {"ID", "Nombre", "Día", "Hora", "Descripción", "Precio", "Monitor"};
        modeloTablaActividades.setColumnIdentifiers(columnas);

        vActividad.jTableActividades.getTableHeader().setResizingAllowed(false);
        vActividad.jTableActividades.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Ajuste opcional de anchos
        vActividad.jTableActividades.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        vActividad.jTableActividades.getColumnModel().getColumn(4).setPreferredWidth(200); // Descripción
    }

    /**
     * METODO RELLENARTABLAACTIVIDADES
     *
     * @param actividades PARAMETRO ACTIVIDADES
     */
    public static void rellenarTablaActividades(List<Actividad> actividades) {
        vaciarTablaActividades();
        Object[] fila = new Object[7]; // Aumentado a 7 columnas

        for (Actividad a : actividades) {
            fila[0] = a.getIdActividad();
            fila[1] = a.getNombre();
            fila[2] = a.getDia();
            fila[3] = a.getHora();
            fila[4] = a.getDescripcion();
            fila[5] = a.getPrecioBaseMes();

            // Verificamos si tiene monitor asignado para evitar error NullPointerException
            if (a.getMonitorResponsable() != null) {
                fila[6] = a.getMonitorResponsable().getNombre();
            } else {
                fila[6] = "Sin Asignar";
            }

            modeloTablaActividades.addRow(fila);
        }
    }

    /**
     * METODO VACIARTABLAACTIVIDADES
     *
     */
    public static void vaciarTablaActividades() {
        modeloTablaActividades.setRowCount(0);
    }
}
