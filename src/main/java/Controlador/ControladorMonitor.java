package Controlador;

import Modelo.Monitor;
import Modelo.MonitorDAO;
import Utilidad.GestionTablasMonitor;
import Vista.VistaDialogoMonitor;
import Vista.VistaMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ControladorMonitor implements ActionListener {

    private VistaMonitor vMonitor;
    private SessionFactory sf;
    private MonitorDAO monitorDAO;
    private VistaDialogoMonitor vDialogo;

    // Formato de fecha que usas en tu base de datos (dd/MM/yyyy)
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    public ControladorMonitor(VistaMonitor vMonitor, SessionFactory sf) {
        this.vMonitor = vMonitor;
        this.sf = sf;
        this.monitorDAO = new MonitorDAO(sf);

        // Inicializar tabla
        GestionTablasMonitor.inicializarTablaMonitores(vMonitor);
        GestionTablasMonitor.dibujarTablaMonitores(vMonitor);

        // Listeners de los botones principales
        this.vMonitor.jButtonNuevoMonitor.addActionListener(this);
        this.vMonitor.jButtonBajadeMonitor.addActionListener(this);
        this.vMonitor.jButtonActualizar.addActionListener(this);
    }

    public void rellenarTabla() {
        Session sesion = null;
        try {
            sesion = sf.openSession();
            List<Monitor> lista = monitorDAO.listaMonitores(sesion);
            GestionTablasMonitor.rellenarTablaMonitores(lista);
        } catch (Exception e) {
            System.out.println("Error al rellenar tabla: " + e.getMessage());
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
            case "NuevoMonitor":
                mostrarDialogo(false); // false = Modo Inserción
                break;
            case "BajadeMonitor":
                bajaMonitor();
                break;
            case "Actualizar":
                mostrarDialogo(true);  // true = Modo Edición
                break;
            case "Aceptar":
                guardarMonitor();      // Guardar desde el diálogo
                break;
            case "Cancelar":
                vDialogo.dispose();    // Cerrar el diálogo
                break;
        }
    }

    private void mostrarDialogo(boolean esEdicion) {
        // 1. Crear el diálogo modal
        // Usamos null como padre, y true para que sea MODAL (bloquea la ventana de atrás)
        vDialogo = new VistaDialogoMonitor(null, true);

        // Añadimos los listeners a los botones DEL DIÁLOGO
        vDialogo.jButtonAceptar.addActionListener(this);
        vDialogo.jButtonAceptar.setActionCommand("Aceptar"); // Aseguramos el comando
        vDialogo.jButtonCancelar.addActionListener(this);
        vDialogo.jButtonCancelar.setActionCommand("Cancelar");

        Session sesion = null;
        try {
            sesion = sf.openSession();

            if (esEdicion) {
                // --- MODO EDICIÓN ---
                int fila = vMonitor.jTableMonitores.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(vMonitor, "Selecciona un monitor para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                vDialogo.setTitle("Actualización de Monitor");
                vDialogo.jTextFieldCodigo.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 0));
                vDialogo.jTextFieldNombre.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 1));
                vDialogo.jTextFieldDNI.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 2));
                vDialogo.jTextFieldTelefono.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 3));
                vDialogo.jTextFieldCorreo.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 4));
                vDialogo.jTextFieldNick.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 6));

                // --- GESTIÓN DE FECHA CON JCALENDAR ---
                // Leemos el String de la tabla (ej: "20/08/2023")
                String fechaString = (String) vMonitor.jTableMonitores.getValueAt(fila, 5);
                if (fechaString != null && !fechaString.isEmpty()) {
                    try {
                        // Lo convertimos a Date para el JDateChooser
                        Date fechaDate = formatoFecha.parse(fechaString);
                        vDialogo.jDateChooserFechaEntrada.setDate(fechaDate);
                    } catch (Exception ex) {
                        System.out.println("Error al parsear fecha: " + ex.getMessage());
                    }
                }

                vDialogo.jTextFieldCodigo.setEditable(false);

            } else {
                // --- MODO NUEVO ---
                vDialogo.setTitle("Nuevo Monitor");

                // Calculamos el siguiente código automáticamente
                String siguienteCodigo = monitorDAO.getSiguienteCodigo(sesion);
                vDialogo.jTextFieldCodigo.setText(siguienteCodigo);

                // El código es automático, no se debe editar
                vDialogo.jTextFieldCodigo.setEditable(false);

                // Limpiamos el resto de campos por si acaso
                vDialogo.jTextFieldNombre.setText("");
                vDialogo.jTextFieldDNI.setText("");
                // ... (el resto nacen vacíos)

                // Inicializamos la fecha a la de hoy
                vDialogo.jDateChooserFechaEntrada.setDate(new Date());
            }

            // Centramos y mostramos
            vDialogo.setLocationRelativeTo(null);
            vDialogo.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vMonitor, "Error al preparar ventana: " + ex.getMessage());
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }

    private void guardarMonitor() {
        Session sesion = null;
        Transaction tr = null;
        try {
            // Recoger datos
            String cod = vDialogo.jTextFieldCodigo.getText();
            String nom = vDialogo.jTextFieldNombre.getText();
            String dni = vDialogo.jTextFieldDNI.getText();
            String tlf = vDialogo.jTextFieldTelefono.getText();
            String mail = vDialogo.jTextFieldCorreo.getText();

            // --- RECOGER FECHA DEL JCALENDAR ---
            Date fechaSeleccionada = vDialogo.jDateChooserFechaEntrada.getDate();
            String fechaStr = "";

            if (fechaSeleccionada != null) {
                // La convertimos a String para guardarla en el Modelo (BD)
                fechaStr = formatoFecha.format(fechaSeleccionada);
            } else {
                JOptionPane.showMessageDialog(vDialogo, "Debes seleccionar una fecha.");
                return;
            }

            String nick = vDialogo.jTextFieldNick.getText();

            // Validación básica (puedes añadir más)
            if (nom.isEmpty() || dni.isEmpty()) {
                JOptionPane.showMessageDialog(vDialogo, "Nombre y DNI son obligatorios.");
                return;
            }

            Monitor monitor = new Monitor(cod, nom, dni, tlf, mail, fechaStr, nick, null);

            sesion = sf.openSession();
            tr = sesion.beginTransaction();

            // Si el código ya existe en la tabla, Hibernate hará update. Si no, insert.
            // Para ser más explícitos, podríamos comprobarlo, pero saveOrUpdate/merge funciona bien.
            monitorDAO.actualizarMonitor(sesion, monitor); // Usamos actualizar (merge) para ambos casos

            tr.commit();

            vDialogo.dispose(); // Cerrar ventana
            rellenarTabla();    // Refrescar tabla automáticamente
            JOptionPane.showMessageDialog(vMonitor, "Operación realizada con éxito.");

        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            JOptionPane.showMessageDialog(vDialogo, "Error al guardar: " + e.getMessage());
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }

    private void bajaMonitor() {
        int fila = vMonitor.jTableMonitores.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vMonitor, "Debes seleccionar un monitor primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = (String) vMonitor.jTableMonitores.getValueAt(fila, 0);
        String nombre = (String) vMonitor.jTableMonitores.getValueAt(fila, 1);

        // Diálogo de confirmación personalizado (como en el PDF)
        int opcion = JOptionPane.showConfirmDialog(vMonitor,
                "¿Seguro que quieres dar de baja a " + nombre + "?",
                "Atención",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE); // Icono amarillo de alerta

        if (opcion == JOptionPane.YES_OPTION) {
            Session sesion = null;
            Transaction tr = null;
            try {
                sesion = sf.openSession();
                tr = sesion.beginTransaction();

                monitorDAO.borrarMonitor(sesion, codigo);

                tr.commit();
                rellenarTabla(); // Refrescar tabla

            } catch (Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                JOptionPane.showMessageDialog(vMonitor, "Error al borrar (¿Tiene actividades asignadas?): " + e.getMessage());
            } finally {
                if (sesion != null) {
                    sesion.close();
                }
            }
        }
    }
}
