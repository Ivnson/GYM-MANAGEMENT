package Controlador;

import Modelo.Actividad;
import Modelo.ActividadDAO;
import Modelo.Monitor;
import Modelo.MonitorDAO;
import Utilidad.GestionTablasActividad;
import Vista.VistaDialogoActividad;
import Vista.VistaActividad;
import Vista.VistaEstadisticas;
import Vista.VistaFiltrado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ControladorActividad implements ActionListener {

    private VistaActividad vActividad;
    private VistaDialogoActividad vDialogo;
    private SessionFactory sf;
    private ActividadDAO actividadDAO;
    private MonitorDAO monitorDAO;
    private VistaFiltrado vFiltrado;
    private VistaEstadisticas vEstadisticas;

    public ControladorActividad(VistaActividad vActividad, SessionFactory sf) {
        this.vActividad = vActividad;
        this.sf = sf;
        this.actividadDAO = new ActividadDAO(sf);
        this.monitorDAO = new MonitorDAO(sf);

        GestionTablasActividad.inicializarTablaActividades(vActividad);
        GestionTablasActividad.dibujarTablaActividades(vActividad);

        this.vActividad.jButtonNuevaActividad.addActionListener(this);
        this.vActividad.jButtonBajaActividad.addActionListener(this);
        this.vActividad.jButtonActualizar.addActionListener(this);
        this.vActividad.jButtonEstadisticas.addActionListener(this);
        this.vActividad.jButtonFiltrar.addActionListener(this);
    }

    public void rellenarTabla() {
        Session sesion = null;
        try {
            sesion = sf.openSession();
            List<Actividad> lista = actividadDAO.listaActividades(sesion);
            GestionTablasActividad.rellenarTablaActividades(lista);
        } catch (Exception e) {
            System.out.println("Error tabla actividades: " + e.getMessage());
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        switch (comando) {
            case "NuevaActividad":
                mostrarDialogo(false);
                break;
            case "BajaActividad":
                bajaActividad();
                break;
            case "Actualizar":
                mostrarDialogo(true);
                break;
            case "Aceptar":
                guardarActividad();
                break;
            case "Cancelar":
                vDialogo.dispose();
                break;
            case "Filtrar":
                mostrarDialogoBusqueda();
                break;
            case "Buscar":
                realizarBusqueda();
                break;
            case "CancelarFiltrado":
                vFiltrado.dispose();
                break;
            case "Estadisticas":
                mostrarEstadisticas();
                break;
        }
    }

    private void mostrarDialogoBusqueda() {
        vFiltrado = new VistaFiltrado(null, true);

        vFiltrado.jButtonBuscarFiltrado.addActionListener(this);
        vFiltrado.jButtonCancelarFiltrado.addActionListener(this);

        vFiltrado.setLocationRelativeTo(null);
        vFiltrado.setVisible(true);
    }

    private void realizarBusqueda() {
        String critero = (String) vFiltrado.jComboBox1.getSelectedItem();
        String valor = vFiltrado.jTextField1.getText();

        Session sesion = null;

        try {
            sesion = sf.openSession();
            List<Actividad> listaFiltrada = new ArrayList<>();

            if (valor.isEmpty() == true) {
                listaFiltrada = actividadDAO.listaActividades(sesion);
            } else {
                listaFiltrada = actividadDAO.filtrarActividades(sesion, critero, valor);
            }

            GestionTablasActividad.rellenarTablaActividades(listaFiltrada);

            vFiltrado.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vActividad, "Error al realizar la busqueda: " + ex.getMessage());
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }

    private void mostrarDialogo(boolean esEdicion) {

        Session sesion = null;
        try {
            sesion = sf.openSession();
            if (esEdicion) {
                int fila = vActividad.jTableActividades.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(vActividad, "Selecciona una actividad.");
                    return;
                }
            }
            vDialogo = new VistaDialogoActividad(null, true);
            vDialogo.jButtonAceptar.addActionListener(this);
            vDialogo.jButtonCancelar.addActionListener(this);

            cargarDiasEnCombo();
            cargarHorasEnCombo();
            cargarMonitores();

            if (esEdicion) {
                int fila = vActividad.jTableActividades.getSelectedRow();
                vDialogo.jTextFieldCodigo.setText((String) vActividad.jTableActividades.getValueAt(fila, 0));
                vDialogo.jTextFieldNombre.setText((String) vActividad.jTableActividades.getValueAt(fila, 1));

                vDialogo.jComboBox1.setSelectedItem((String) vActividad.jTableActividades.getValueAt(fila, 2));

                vDialogo.jComboBox2.setSelectedItem(vActividad.jTableActividades.getValueAt(fila, 3));

                vDialogo.jTextFieldDescripcion.setText((String) vActividad.jTableActividades.getValueAt(fila, 4));
                vDialogo.jTextFieldPrecio.setText(String.valueOf(vActividad.jTableActividades.getValueAt(fila, 5)));

                vDialogo.jTextFieldCodigo.setEditable(false);

                try {
                    String nombreMonitorTabla = (String) vActividad.jTableActividades.getValueAt(fila, 6);
                    if (nombreMonitorTabla != null) {
                        // Buscamos en el combo el objeto Monitor que tenga ese nombre
                        for (int i = 0; i < vDialogo.jComboBox3.getItemCount(); i++) {
                            Monitor m = (Monitor) vDialogo.jComboBox3.getItemAt(i);
                            if (m.getNombre().equals(nombreMonitorTabla)) {
                                vDialogo.jComboBox3.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("No se pudo cargar el monitor seleccionado (¿Falta la columna en la tabla?)");
                }

            } else {
                String codigo_nuevo = actividadDAO.getSiguienteCodigo(sesion);
                vDialogo.jTextFieldCodigo.setText(codigo_nuevo);
                vDialogo.jTextFieldCodigo.setEditable(false);
            }
            vDialogo.setLocationRelativeTo(null);
            vDialogo.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vActividad, "Error al preparar ventana: " + ex.getMessage());
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }

    }

    private void guardarActividad() {
        Session sesion = null;
        Transaction tr = null;
        try {
            String id = vDialogo.jTextFieldCodigo.getText();
            String nombre = vDialogo.jTextFieldNombre.getText();
            String dia = (String) vDialogo.jComboBox1.getSelectedItem();
            String desc = vDialogo.jTextFieldDescripcion.getText();

            // 1. OBTENER EL MONITOR (OBJETO) DEL COMBO
            Monitor monitorSeleccionado = (Monitor) vDialogo.jComboBox3.getSelectedItem();

            // Convertimos Texto a Entero
            int hora = Integer.parseInt((String) vDialogo.jComboBox2.getSelectedItem());
            int precio = Integer.parseInt(vDialogo.jTextFieldPrecio.getText());

            sesion = sf.openSession();
            tr = sesion.beginTransaction();

            // --- VALIDACIÓN DE CONFLICTO DE HORARIO (NUEVO) ---
            // Le preguntamos al DAO si este monitor ya está ocupado ese día/hora
            // Le pasamos el ID actual para que si estamos EDITANDO, no choque consigo mismo.
            boolean hayConflicto = actividadDAO.existeConflictoMonitor(sesion,
                    monitorSeleccionado.getCodMonitor(),
                    dia,
                    hora,
                    id);

            if (hayConflicto) {
                JOptionPane.showMessageDialog(vDialogo,
                        "El monitor " + monitorSeleccionado.getNombre() + " ya tiene una clase el " + dia + " a las " + hora + "h.\n"
                        + "Por favor, selecciona otro horario u otro monitor.",
                        "Conflicto de Horario",
                        JOptionPane.WARNING_MESSAGE);

                // IMPORTANTE: Hacemos rollback (por si acaso) y salimos del método
                if (tr != null) {
                    tr.rollback();
                }
                return;
            }

            // Creamos la actividad (sin monitor asignado por ahora para simplificar)
            Actividad actividad = new Actividad(id, nombre, dia, hora, precio);
            actividad.setDescripcion(desc);
            actividad.setMonitorResponsable(monitorSeleccionado);

            if (vDialogo.jTextFieldCodigo.isEditable()) {
                actividadDAO.insertarActividad(sesion, actividad);
            } else {
                actividadDAO.actualizarActividad(sesion, actividad);
            }

            tr.commit();
            vDialogo.dispose();
            rellenarTabla();
            JOptionPane.showMessageDialog(vActividad, "Éxito");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(vDialogo, "Hora y Precio deben ser números enteros.");
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            JOptionPane.showMessageDialog(vDialogo, "Error: " + e.getMessage());
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }

    private void bajaActividad() {
        int fila = vActividad.jTableActividades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vActividad, "No has elegido ninguna actividad");
            return;
        }

        String id = (String) vActividad.jTableActividades.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(vActividad, "¿Borrar actividad " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Session sesion = null;
            Transaction tr = null;
            try {
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                actividadDAO.borrarActividad(sesion, id);
                tr.commit();
                rellenarTabla();
            } catch (Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                JOptionPane.showMessageDialog(vActividad, "Error: " + e.getMessage());
            } finally {
                if (sesion != null) {
                    sesion.close();
                }
            }
        }
    }

    private void cargarDiasEnCombo() {
        Session sesion = sf.openSession();
        try {
            vDialogo.jComboBox1.removeAllItems();

            vDialogo.jComboBox1.addItem("Lunes");
            vDialogo.jComboBox1.addItem("Martes");
            vDialogo.jComboBox1.addItem("Miercoles");
            vDialogo.jComboBox1.addItem("Jueves");
            vDialogo.jComboBox1.addItem("Viernes");
            vDialogo.jComboBox1.addItem("Sabado");
            vDialogo.jComboBox1.addItem("Domingo");

            vDialogo.jComboBox1.setSelectedIndex(0);

        } catch (Exception e) {
            System.out.println("Error cargando categorias: " + e.getMessage());
        } finally {
            sesion.close();
        }
    }

    private void cargarHorasEnCombo() {
        Session sesion = sf.openSession();
        try {
            vDialogo.jComboBox2.removeAllItems();

            for (int i = 7; i <= 23; i++) {
                vDialogo.jComboBox2.addItem(i);
            }

            vDialogo.jComboBox1.setSelectedIndex(0);

        } catch (Exception e) {
            System.out.println("Error cargando categorias: " + e.getMessage());
        } finally {
            sesion.close();
        }
    }

    private void cargarMonitores() {
        Session sesion = sf.openSession();

        try {

            vDialogo.jComboBox3.removeAllItems();

            List<Monitor> monitores = new ArrayList<>();
            monitores = monitorDAO.listaMonitores(sesion);

            if (monitores != null && monitores.isEmpty() == false) {
                for (Monitor monitore : monitores) {
                    vDialogo.jComboBox3.addItem(monitore);
                }
            }

            vDialogo.jComboBox3.setSelectedIndex(0);

        } catch (Exception e) {
            System.out.println("Error cargando monitores: " + e.getMessage());
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }

    private void mostrarEstadisticas() {
        // 1. Comprobar que el usuario ha seleccionado una fila en la tabla
        int fila = vActividad.jTableActividades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vActividad, "Por favor, selecciona una actividad de la tabla.");
            return;
        }

        // 2. Obtener el ID de la actividad (suponiendo columna 0)
        String idActividad = (String) vActividad.jTableActividades.getValueAt(fila, 0);

        Session sesion = null;
        try {
            sesion = sf.openSession();

            // 3. LLAMAR AL DAO (Aquí ocurre la magia con los 4 procedimientos)
            List<Object> resultados = actividadDAO.ejecutarProcedimientoEstadisticas(sesion, idActividad);

            // 4. CREAR Y RELLENAR LA VENTANA
            vEstadisticas = new Vista.VistaEstadisticas(null, true);

            // Sacamos los datos del array y los ponemos en las etiquetas
            vEstadisticas.jLabelNumSocios.setText(resultados.get(0).toString());
            vEstadisticas.jLabelEdadMedia.setText(String.format("%.2f años", resultados.get(1))); // %.2f reduce a 2 decimales
            vEstadisticas.jLabelModa.setText(resultados.get(2).toString());
            vEstadisticas.jLabelIngresos.setText(String.format("%.2f €", resultados.get(3)));

            // Acción para el botón cerrar
            vEstadisticas.jButtonCerrar.addActionListener(e -> vEstadisticas.dispose());

            // Mostrar
            vEstadisticas.setLocationRelativeTo(null);
            vEstadisticas.setVisible(true);

        } catch (Exception e) {
            System.out.println("Error calculando estadísticas: " + e.getMessage());
            JOptionPane.showMessageDialog(vActividad, "Error: " + e.getMessage());
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }

}
