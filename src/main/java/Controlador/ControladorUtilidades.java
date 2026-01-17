package Controlador;

import Modelo.Actividad;
import Modelo.ActividadDAO;
import Modelo.Socio;
import Modelo.SocioDAO;
import Vista.VistaInscripciones;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * CONTROLADOR PARA LA LOGICA DE NEGOCIO DE UTILIDADES
 * PROPORCIONA FUNCIONALIDADES PARA LA GESTION COMPLETA DEL SISTEMA DE GIMNASIO
 * 
 * @author SISTEMA DE GESTION DE GIMNASIO
 * @version 1.0
 */
public class ControladorUtilidades implements ActionListener {

    /** ATRIBUTO SF */
    private SessionFactory sf;
    /** ATRIBUTO VINSCRIPCIONES */
    private VistaInscripciones vInscripciones;
    /** ATRIBUTO SOCIODAO */
    private SocioDAO socioDAO;
    /** ATRIBUTO ACTIVIDADDAO */
    private ActividadDAO actividadDAO;

    // Modelos para manejar los datos visuales de las listas
    /** COLECCION DE MODELOINSCRITAS ASOCIADOS */
    private DefaultListModel<Object> modeloInscritas;
    /** COLECCION DE MODELONOINSCRITAS ASOCIADOS */
    private DefaultListModel<Object> modeloNoInscritas;


    public ControladorUtilidades(SessionFactory sf) {
        this.sf = sf;
        this.socioDAO = new SocioDAO(sf);
        this.actividadDAO = new ActividadDAO(sf);

        // 1. Crear la vista (Modal = true para bloquear la ventana de atrás)
        this.vInscripciones = new VistaInscripciones(null, true);

        // 2. Inicializar los modelos de las listas
        modeloInscritas = new DefaultListModel<>();
        modeloNoInscritas = new DefaultListModel<>();

        // Asignamos los modelos a los componentes JList de la vista
        vInscripciones.jListInscritas.setModel(modeloInscritas);
        vInscripciones.jListNoInscritas.setModel(modeloNoInscritas);

        // 3. Configurar Listeners (Escuchadores)
        this.vInscripciones.jButtonAlta.addActionListener(this);
        this.vInscripciones.jButtonBaja.addActionListener(this);

        // Listener especial: Cuando cambiamos de socio en el combo, recargamos las listas
        this.vInscripciones.jComboBoxSocios.addActionListener(e -> cargarListasActividades());

        // 4. Cargar datos iniciales
        cargarSociosEnCombo();

        // 5. Mostrar la ventana centrada
        this.vInscripciones.setLocationRelativeTo(null);
        this.vInscripciones.setVisible(true);
    }

    /**
     * Carga todos los socios de la BD en el desplegable (JComboBox)
     */
    private void cargarSociosEnCombo() {
        Session sesion = sf.openSession();
        try {
            vInscripciones.jComboBoxSocios.removeAllItems();
            List<Socio> listaSocios = socioDAO.listaSocios(sesion);

            for (Socio s : listaSocios) {
                vInscripciones.jComboBoxSocios.addItem(s);
            }

            // Si hay socios, seleccionamos el primero para que se vean datos
            if (!listaSocios.isEmpty()) {
                vInscripciones.jComboBoxSocios.setSelectedIndex(0);
            }
        } catch (Exception e) {
            System.out.println("Error cargando socios: " + e.getMessage());
        } finally {
            sesion.close();
        }
    }

    /**
     * Método clave: Separa las actividades en "Inscritas" y "No Inscritas" para
     * el socio que esté seleccionado actualmente.
     */
    private void cargarListasActividades() {
        // Obtenemos el socio seleccionado del combo
        Socio socioSeleccionado = (Socio) vInscripciones.jComboBoxSocios.getSelectedItem();

        // Si no hay socio seleccionado (o el combo está vacío), no hacemos nada
        if (socioSeleccionado == null) {
            return;
        }

        Session sesion = sf.openSession();
        try {
            // RECARGAMOS el socio desde la BD para asegurar que tenemos sus datos actualizados
            // (especialmente las actividades inscritas)
            socioSeleccionado = sesion.get(Socio.class, socioSeleccionado.getNumeroSocio());

            // Limpiamos las listas visuales
            modeloInscritas.clear();
            modeloNoInscritas.clear();

            // 1. Obtenemos lo que tiene el socio (Set de actividades)
            Set<Actividad> actividadesDelSocio = socioSeleccionado.getActividadSet();

            // 2. Obtenemos TODAS las actividades del gimnasio
            List<Actividad> todasLasActividades = actividadDAO.listaActividades(sesion);

            // 3. Clasificamos y rellenamos
            for (Actividad a : todasLasActividades) {
                if (actividadesDelSocio.contains(a)) {
                    // Si el socio la tiene -> Lista Izquierda
                    modeloInscritas.addElement(a);
                } else {
                    // Si no la tiene -> Lista Derecha
                    modeloNoInscritas.addElement(a);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al clasificar actividades: " + e.getMessage());
        } finally {
            sesion.close();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("DardeAlta".equals(comando)) {
            realizarAlta();
        } else if ("DardeBaja".equals(comando)) {
            realizarBaja();
        }
    }

    // Pasa una actividad de la derecha (No inscrita) a la izquierda (Inscrita)
    private void realizarAlta() {
        // 1. Recoger selecciones

        Actividad actividad = (Actividad) vInscripciones.jListNoInscritas.getSelectedValue();
        Socio socioCombo = (Socio) vInscripciones.jComboBoxSocios.getSelectedItem();

        if (actividad == null || socioCombo == null) {
            JOptionPane.showMessageDialog(vInscripciones, "Selecciona una actividad de la lista derecha (No Inscritas).");
            return;
        }

        Session sesion = sf.openSession();
        Transaction tr = sesion.beginTransaction();
        try {
            // 2. Traer entidades persistentes en esta sesión
            Socio s = sesion.get(Socio.class, socioCombo.getNumeroSocio());
            Actividad a = sesion.get(Actividad.class, actividad.getIdActividad());

            // 3. Añadir la relación (en ambos lados para mantener coherencia en memoria)
            s.getActividadSet().add(a);
            a.getSocioSet().add(s);

            // 4. Guardar cambios
            sesion.merge(s);
            tr.commit();

            // 5. Refrescar la pantalla
            cargarListasActividades();

        } catch (Exception ex) {
            if (tr != null) {
                tr.rollback();
            }
            JOptionPane.showMessageDialog(vInscripciones, "Error al dar de alta: " + ex.getMessage());
        } finally {
            sesion.close();
        }
    }

    // Pasa una actividad de la izquierda (Inscrita) a la derecha (No inscrita)
    private void realizarBaja() {
        // 1. Recoger selecciones
        Actividad actividad = (Actividad) vInscripciones.jListInscritas.getSelectedValue();
        Socio socioCombo = (Socio) vInscripciones.jComboBoxSocios.getSelectedItem();

        if (actividad == null || socioCombo == null) {
            JOptionPane.showMessageDialog(vInscripciones, "Selecciona una actividad de la lista izquierda (Inscritas).");
            return;
        }

        Session sesion = sf.openSession();
        Transaction tr = sesion.beginTransaction();
        try {
            Socio s = sesion.get(Socio.class, socioCombo.getNumeroSocio());
            Actividad a = sesion.get(Actividad.class, actividad.getIdActividad());

            // 3. Eliminar la relación
            s.getActividadSet().remove(a);
            a.getSocioSet().remove(s);

            // 4. Guardar
            sesion.merge(s);
            tr.commit();

            // 5. Refrescar
            cargarListasActividades();

        } catch (Exception ex) {
            if (tr != null) {
                tr.rollback();
            }
            JOptionPane.showMessageDialog(vInscripciones, "Error al dar de baja: " + ex.getMessage());
        } finally {
            sesion.close();
        }
    }
}
