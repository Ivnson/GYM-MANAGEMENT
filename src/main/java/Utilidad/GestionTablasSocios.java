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
public class GestionTablasSocios {

    public static DefaultTableModel modeloTablaSocios = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static void inicializarTablaSocios(VistaSocio vSocio) {
        vSocio.jTableSocios.setModel(modeloTablaSocios);
    }

    public static void dibujarTablaSocios(VistaSocio vSocio) {
        String[] columnas = {"Num. Socio", "Nombre", "DNI", "Fecha Nac.", "Teléfono", "Correo", "Fecha Entrada", "Cat."};
        modeloTablaSocios.setColumnIdentifiers(columnas);

        vSocio.jTableSocios.getTableHeader().setResizingAllowed(false);
        vSocio.jTableSocios.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Ajuste opcional de anchos
        vSocio.jTableSocios.getColumnModel().getColumn(0).setPreferredWidth(60); // Num Socio
        vSocio.jTableSocios.getColumnModel().getColumn(7).setPreferredWidth(40); // Categoría
    }

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

    public static void vaciarTablaSocios() {
        modeloTablaSocios.setRowCount(0);
    }

}
