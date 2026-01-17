/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidad;

import Modelo.Socio;
import Vista.VistaSocio;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ivan
 */
/**
 * CLASE UTILITARIA PARA LA GESTIONTABLASSOCIOS
 * PROPORCIONA FUNCIONALIDADES PARA LA GESTION COMPLETA DEL SISTEMA DE GIMNASIO
 * 
 * @author SISTEMA DE GESTION DE GIMNASIO
 * @version 1.0
 */
public class GestionTablasSocios {

    /** ATRIBUTO MODELOTABLASOCIOS */
    public static DefaultTableModel modeloTablaSocios = new DefaultTableModel() {
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
     * @param vSocio PARAMETRO VSOCIO
     */
    public static void inicializarTablaSocios(VistaSocio vSocio) {
        vSocio.jTableSocios.setModel(modeloTablaSocios);
    }

    /**
     * METODO DIBUJARTABLASOCIOS
     *
     * @param vSocio PARAMETRO VSOCIO
     */
    public static void dibujarTablaSocios(VistaSocio vSocio) {
        String[] columnas = {"Num. Socio", "Nombre", "DNI", "Fecha Nac.", "Teléfono", "Correo", "Fecha Entrada", "Cat."};
        modeloTablaSocios.setColumnIdentifiers(columnas);

        vSocio.jTableSocios.getTableHeader().setResizingAllowed(false);
        vSocio.jTableSocios.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Ajuste opcional de anchos
        vSocio.jTableSocios.getColumnModel().getColumn(0).setPreferredWidth(60); // Num Socio
        vSocio.jTableSocios.getColumnModel().getColumn(7).setPreferredWidth(40); // Categoría
    }

    /**
     * METODO RELLENARTABLASOCIOS
     *
     * @param socios PARAMETRO SOCIOS
     */
    public static void rellenarTablaSocios(List<Socio> socios) {
        vaciarTablaSocios();
        Object[] fila = new Object[8];

        for (Socio s : socios) {
            fila[0] = s.getNumeroSocio();
            fila[1] = s.getNombre();
            fila[2] = s.getDni();
            fila[3] = s.getFechaNacimiento();
            fila[4] = s.getTelefono();
            fila[5] = s.getCorreo();
            fila[6] = s.getFechaEntrada();
            fila[7] = s.getCategoria();
            modeloTablaSocios.addRow(fila);
        }
    }

    /**
     * METODO VACIARTABLASOCIOS
     *
     */
    public static void vaciarTablaSocios() {
        modeloTablaSocios.setRowCount(0);
    }

}
