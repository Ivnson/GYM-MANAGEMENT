package Controlador;

import Config.HibernateUtil;
import Vista.VistaActividad;
import Vista.VistaInicio;
import Vista.VistaMonitor;
import Vista.VistaPrincipal;
import Vista.VistaSocio;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.hibernate.SessionFactory;

public class ControladorPrincipal implements ActionListener {

    private SessionFactory sf;

    // Vistas
    private VistaPrincipal vPrincipal;
    private VistaInicio vInicio;
    private VistaMonitor vMonitor;
    private VistaSocio vSocio;
    private VistaActividad vActividad ; 

    // Controladores hijos
    private ControladorMonitor cMonitor;
    private ControladorSocio cSocio;
    private ControladorActividad cActividad;
    // El controlador de utilidades se crea bajo demanda (al pulsar el botón)

    // Gestión del Layout
    private CardLayout cardLayout;
    private JPanel panelContenedor;

    public ControladorPrincipal(SessionFactory sf) {
        this.sf = sf;

        // 1. CREAMOS LAS VISTAS
        vPrincipal = new VistaPrincipal();
        vInicio = new VistaInicio();
        vMonitor = new VistaMonitor();
        vSocio = new VistaSocio();
        vActividad = new VistaActividad(); // ;)
        
        vPrincipal.setSize(1600, 800);

        // 2. INICIALIZAMOS LOS CONTROLADORES DE GESTIÓN
        // (Conectan las vistas con la Base de Datos)
        cMonitor = new ControladorMonitor(vMonitor, sf);
        cSocio = new ControladorSocio(vSocio, sf);
        cActividad = new ControladorActividad(vActividad, sf);

        // 3. CONFIGURAR EL LAYOUT (SOLUCIÓN BUGS VISUALES)
        // Obtenemos el panel principal de la ventana
        panelContenedor = (JPanel) vPrincipal.getContentPane();

        // ¡IMPORTANTE! Borramos lo que NetBeans pone por defecto para evitar conflictos
        panelContenedor.removeAll();

        // Configuramos CardLayout para intercambiar paneles
        panelContenedor.setLayout(new CardLayout());
        cardLayout = (CardLayout) panelContenedor.getLayout();

        // 4. AÑADIR LOS PANELES AL CONTENEDOR (con un nombre clave)
        panelContenedor.add(vInicio, "INICIO");
        panelContenedor.add(vMonitor, "MONITORES");
        panelContenedor.add(vSocio, "SOCIOS");
        panelContenedor.add(vActividad, "ACTIVIDADES");

        // 5. REGISTRAR LOS OYENTES DEL MENÚ
        // Navegación principal
        vPrincipal.menuItemInicio.addActionListener(this);
        vPrincipal.menuItemGestionMonitores.addActionListener(this);
        vPrincipal.menuItemGestionSocios.addActionListener(this);
        vPrincipal.menuItemGestionActividades.addActionListener(this);
        vPrincipal.menuItemSalir.addActionListener(this);

        // NUEVO: Menú de Utilidades / Inscripciones
        // (Asegúrate de haber creado este menuItem en VistaPrincipal como te indiqué)
        if (vPrincipal.jMenuItemInscripciones!= null) {
            vPrincipal.jMenuItemInscripciones.addActionListener(this);
        }

        // 6. MOSTRAR LA VENTANA
        vPrincipal.setLocationRelativeTo(null);
        vPrincipal.setResizable(false);
        vPrincipal.setTitle("Gestión del Gimnasio 'Body Perfect'");

        // Mostrar panel de inicio por defecto
        mostrarPanel("INICIO");

        vPrincipal.setVisible(true);
    }

    // Método auxiliar para cambiar de panel y forzar el redibujado
    private void mostrarPanel(String nombrePanel) {
        cardLayout.show(panelContenedor, nombrePanel);
        panelContenedor.revalidate(); // Recalcula el diseño
        panelContenedor.repaint();    // Pinta de nuevo los píxeles
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();

        // --- NAVEGACIÓN PRINCIPAL ---
        if (fuente == vPrincipal.menuItemInicio) {
            mostrarPanel("INICIO");

        } else if (fuente == vPrincipal.menuItemGestionMonitores) {
            mostrarPanel("MONITORES");
            cMonitor.rellenarTabla(); // Recargamos datos al entrar

        } else if (fuente == vPrincipal.menuItemGestionSocios) {
            mostrarPanel("SOCIOS");
            cSocio.rellenarTabla();

        } else if (fuente == vPrincipal.menuItemGestionActividades) {
            mostrarPanel("ACTIVIDADES");
            cActividad.rellenarTabla();

        } // --- NUEVA UTILIDAD (INSCRIPCIONES) ---
        else if (fuente == vPrincipal.jMenuItemInscripciones) {
            // Al ser una ventana modal independiente (JDialog), 
            // creamos su controlador aquí y él se encarga de mostrarse.
            new ControladorUtilidades(sf);
        } // --- SALIR ---
        else if (fuente == vPrincipal.menuItemSalir) {
            salir();
        }
    }

    private void salir() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vPrincipal,
                "¿Seguro que quieres salir de la aplicación?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            HibernateUtil.close(); // Cerramos conexión a BD
            vPrincipal.dispose();  // Cerramos ventana
            System.exit(0);        // Matamos proceso
        }
    }
}
