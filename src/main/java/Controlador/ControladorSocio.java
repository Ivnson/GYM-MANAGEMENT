package Controlador;

import Modelo.Socio;
import Modelo.SocioDAO;
import Utilidad.GestionTablasSocios;
import Utilidad.GestionTablasSocios;
import Vista.VistaDialogoSocio;
import Vista.VistaSocio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ControladorSocio implements ActionListener {

    private VistaSocio vSocio;
    private VistaDialogoSocio vDialogo;
    private SessionFactory sf;
    private SocioDAO socioDAO;

    // Formato de fecha que usas en tu base de datos (dd/MM/yyyy)
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    public ControladorSocio(VistaSocio vSocio, SessionFactory sf) {
        this.vSocio = vSocio;
        this.sf = sf;
        this.socioDAO = new SocioDAO(sf);

        // Inicializar tabla
        GestionTablasSocios.inicializarTablaSocios(vSocio);
        GestionTablasSocios.dibujarTablaSocios(vSocio);

        // Listeners
        this.vSocio.jButtonNuevoSocio.addActionListener(this);
        this.vSocio.jButtonBajaSocio.addActionListener(this);
        this.vSocio.jButtonActualizar.addActionListener(this);

    }

    public void rellenarTabla() {
        Session sesion = null;
        try {
            sesion = sf.openSession();
            List<Socio> lista = socioDAO.listaSocios(sesion);
            GestionTablasSocios.rellenarTablaSocios(lista);
        } catch (Exception e) {
            System.out.println("Error tabla socios: " + e.getMessage());
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
            case "NuevoSocio":
                mostrarDialogo(false);
                break;
            case "BajaSocio":
                bajaSocio();
                break;
            case "Actualizar":
                mostrarDialogo(true);
                break;
            case "Aceptar":
                guardarSocio();
                break;
            case "Cancelar":
                vDialogo.dispose();
                break;
        }
    }

    private void mostrarDialogo(boolean esEdicion) {

        Session sesion = null;
        try {

            sesion = sf.openSession();
            if (esEdicion) {
                int fila = vSocio.jTableSocios.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(vSocio, "Selecciona un socio primero.");
                    return;
                }
            }

            vDialogo = new VistaDialogoSocio(null, true);
            vDialogo.jButtonAceptar.addActionListener(this);
            vDialogo.jButtonCancelar.addActionListener(this);

            cargarCategoriaEnCombo();

            if (esEdicion) {
                int fila = vSocio.jTableSocios.getSelectedRow();
                vDialogo.jTextFieldNumeroSocio.setText((String) vSocio.jTableSocios.getValueAt(fila, 0));
                vDialogo.jTextFieldNombre.setText((String) vSocio.jTableSocios.getValueAt(fila, 1));
                vDialogo.jTextFieldDNI.setText((String) vSocio.jTableSocios.getValueAt(fila, 2));

                String fechanac = (String) vSocio.jTableSocios.getValueAt(fila, 3);
                if (fechanac != null && fechanac.isEmpty() == false) {
                    try {
                        Date fechanacDate = formatoFecha.parse(fechanac);
                        vDialogo.jDateChooser2.setDate(fechanacDate);
                    } catch (Exception ex) {
                        System.out.println("Error al parsear fecha nac: " + ex.getMessage());
                    }
                }

                vDialogo.jTextFieldTelefono.setText((String) vSocio.jTableSocios.getValueAt(fila, 4));
                vDialogo.jTextFieldCorreo.setText((String) vSocio.jTableSocios.getValueAt(fila, 5));

                // --- GESTIÓN DE FECHA CON JCALENDAR ---
                // Leemos el String de la tabla (ej: "20/08/2023")
                String fechaString = (String) vSocio.jTableSocios.getValueAt(fila, 6);
                if (fechaString != null && !fechaString.isEmpty()) {
                    try {
                        // Lo convertimos a Date para el JDateChooser
                        Date fechaDate = formatoFecha.parse(fechaString);
                        vDialogo.jDateChooser1.setDate(fechaDate);
                    } catch (Exception ex) {
                        System.out.println("Error al parsear fecha: " + ex.getMessage());
                    }
                }

                Object letra = vSocio.jTableSocios.getValueAt(fila, 7);

                if (letra != null) {
                    vDialogo.jComboBox1.setSelectedItem(String.valueOf(letra));
                }

                vDialogo.jTextFieldNumeroSocio.setEditable(false); // ID no se toca
            } else {

                String numSocio = socioDAO.getSiguienteCodigo(sesion);
                vDialogo.jTextFieldNumeroSocio.setText(numSocio);

                vDialogo.jTextFieldNumeroSocio.setEditable(false);

                vDialogo.jDateChooser1.setDate(new Date());
            }

            vDialogo.setLocationRelativeTo(null);
            vDialogo.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vSocio, "Error al preparar ventana: " + ex.getMessage());
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }

    }

    private void guardarSocio() {
        Session sesion = null;
        Transaction tr = null;
        try {
            // Recoger datos
            String numSocio = vDialogo.jTextFieldNumeroSocio.getText();
            String nombre = vDialogo.jTextFieldNombre.getText();
            String dni = vDialogo.jTextFieldDNI.getText();
            Date fechaNac = vDialogo.jDateChooser2.getDate() ; 
            
            String fechanacConvertida = null ; 
            
            if (fechaNac != null)
            {
                fechanacConvertida = formatoFecha.format(fechaNac) ; 
            }
            else
            {
                JOptionPane.showMessageDialog(vDialogo, "Debes seleccionar una fecha.");
                return;
            }
            
            String tlf = vDialogo.jTextFieldTelefono.getText();
            String correo = vDialogo.jTextFieldCorreo.getText();
            Date fechaEntrada = vDialogo.jDateChooser1.getDate();

            String FechaEntradaConvertida = null;

            if (fechaEntrada != null) {
                FechaEntradaConvertida = formatoFecha.format(fechaEntrada);
            } else {
                JOptionPane.showMessageDialog(vDialogo, "Debes seleccionar una fecha.");
                return;
            }

            // Recoger la Categoría del ComboBox
            String letra = (String) vDialogo.jComboBox1.getSelectedItem();
            Character categoria = 'A'; // Valor por defecto

            if (letra != null && !letra.isEmpty()) {
                categoria = letra.charAt(0);
            }

            Socio socio = new Socio(numSocio, nombre, dni, fechanacConvertida, tlf, correo, FechaEntradaConvertida, categoria);

            sesion = sf.openSession();
            tr = sesion.beginTransaction();

            if (vDialogo.jTextFieldNumeroSocio.isEditable()) {
                socioDAO.insertarSocio(sesion, socio);
            } else {
                socioDAO.actualizarSocio(sesion, socio);
            }

            tr.commit();
            vDialogo.dispose();
            rellenarTabla();
            JOptionPane.showMessageDialog(vSocio, "Operación realizada con éxito.");

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

    private void bajaSocio() {
        int fila = vSocio.jTableSocios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vSocio, "Selecciona un socio.");
            return;
        }
        String numSocio = (String) vSocio.jTableSocios.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(vSocio, "¿Eliminar socio " + numSocio + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Session sesion = null;
            Transaction tr = null;
            try {
                sesion = sf.openSession();
                tr = sesion.beginTransaction();
                socioDAO.borrarSocio(sesion, numSocio);
                tr.commit();
                rellenarTabla();
            } catch (Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                JOptionPane.showMessageDialog(vSocio, "Error: " + e.getMessage());
            } finally {
                if (sesion != null) {
                    sesion.close();
                }
            }
        }
    }

    private void cargarCategoriaEnCombo() {
        Session sesion = sf.openSession();
        try {
            vDialogo.jComboBox1.removeAllItems();

            vDialogo.jComboBox1.addItem("A");
            vDialogo.jComboBox1.addItem("B");
            vDialogo.jComboBox1.addItem("C");
            vDialogo.jComboBox1.addItem("D");
            vDialogo.jComboBox1.addItem("E");

            vDialogo.jComboBox1.setSelectedIndex(0);

        } catch (Exception e) {
            System.out.println("Error cargando categorias: " + e.getMessage());
        } finally {
            sesion.close();
        }
    }
}
