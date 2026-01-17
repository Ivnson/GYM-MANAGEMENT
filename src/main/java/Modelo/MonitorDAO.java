/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author ivan
 */
public class MonitorDAO {

    private SessionFactory sf;

    public MonitorDAO(SessionFactory sessionfactory) {
        this.sf = sessionfactory;
    }

    public List<Actividad> ActividadesMonitor(Session sesion, String DNI) throws Exception {
        List<Actividad> actividades = new ArrayList<>();

        Monitor monitor = null;
        Query consulta = sesion.createNativeQuery("SELECT * FROM MONITOR WHERE DNI = ?", Monitor.class);

        consulta.setParameter(1, DNI);

        monitor = (Monitor) consulta.getSingleResult();

        if (monitor != null) {
            Query consulta2 = sesion.createNativeQuery("SELECT * FROM ACTIVIDAD WHERE MONITORRESPONSABLE = ?", Actividad.class);

            consulta2.setParameter(1, monitor.getCodMonitor());
            actividades = consulta2.getResultList();

            if (actividades.isEmpty() == true) {
                actividades = null;
            }
        }
        return actividades;
    }

    public boolean ExisteMonitor(Session sesion, String DNI) throws Exception {

        List<Monitor> lista_monitores = new ArrayList<>();

        Query consulta = sesion.createNativeQuery(
                "SELECT * FROM MONITOR WHERE DNI = ?", Monitor.class);

        consulta.setParameter(1, DNI);

        lista_monitores = consulta.getResultList();

        if (lista_monitores.isEmpty()) {
            //new Vista.VistaMonitor().MonitorNoEncontrado();
            return false;
        }

        return true;
    }

    public List<Monitor> listaMonitores(Session sesion) {
        Query consulta = sesion.createNamedQuery("Monitor.findAll", Monitor.class);

        return consulta.getResultList();
    }

    public void borrarMonitor(Session sesion, String codMonitor) throws Exception {
        // Buscamos el monitor por su clave primaria
        //Monitor monitor = sesion.get(Monitor.class, codMonitor);

        Query consulta = sesion.createNativeQuery(
                "SELECT * FROM MONITOR WHERE CODMONITOR = ?", Monitor.class);
        consulta.setParameter(1, codMonitor);

        Monitor monitor = (Monitor) consulta.getSingleResult();

        if (monitor != null) {
            // Si existe, lo borramos
            sesion.remove(monitor);
        } else {
            throw new Exception("El monitor no existe");
        }
    }

    // Método para insertar un nuevo monitor
    public void insertarMonitor(Session sesion, Monitor monitor) throws Exception {
        sesion.persist(monitor); // 'persist' es para guardar objetos nuevos
    }

    // Método para actualizar un monitor existente
    public void actualizarMonitor(Session sesion, Monitor monitor) throws Exception {
        sesion.merge(monitor); // 'merge' actualiza si existe, o guarda si no
    }

    // NUEVO MÉTODO: Calcular el siguiente código automático (M001 -> M002 -> ...)
    public String getSiguienteCodigo(Session sesion) {
        String codigoMax = (String) sesion.createNativeQuery("SELECT MAX(codMonitor) FROM MONITOR").getSingleResult();

        if (codigoMax == null) {
            return "M001";
        }

        // Extraemos el número (quitamos la 'M')
        String numeroStr = codigoMax.substring(1);
        int numero = Integer.parseInt(numeroStr);

        // Sumamos 1
        numero++;

        // Devolvemos formateado con 3 dígitos (ej: M011)
        return String.format("M%03d", numero);
    }

}
