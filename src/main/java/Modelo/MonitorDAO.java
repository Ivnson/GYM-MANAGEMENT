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
/**
 * CLASE DAO PARA LA GESTION DE OPERACIONES DE ACCESO A DATOS DE MONITOR
 * PROPORCIONA FUNCIONALIDADES PARA LA GESTION COMPLETA DEL SISTEMA DE GIMNASIO
 * 
 * @author SISTEMA DE GESTION DE GIMNASIO
 * @version 1.0
 */
public class MonitorDAO {

    /** ATRIBUTO SF */
    private SessionFactory sf;

    public MonitorDAO(SessionFactory sessionfactory) {
        this.sf = sessionfactory;
    }

   
    /**
     * RECUPERA UNA LISTA DE ACTIVIDADES ASIGNADAS A UN MONITOR ESPECIFICO UTILIZANDO SU DNI.
     * EL METODO PRIMERO BUSCA AL MONITOR EN LA BASE DE DATOS POR SU DNI Y, SI LO ENCUENTRA,
     * CONSULTA TODAS LAS ACTIVIDADES EN LAS QUE DICHO MONITOR FIGURA COMO RESPONSABLE.
     *
     * @param sesion LA SESION ACTIVA UTILIZADA PARA REALIZAR LAS CONSULTAS A LA BASE DE DATOS.
     * @param DNI EL DNI DEL MONITOR DEL CUAL SE QUIEREN OBTENER LAS ACTIVIDADES.
     * @return UNA LISTA CON LAS ACTIVIDADES DEL MONITOR, O NULL SI NO SE ENCONTRARON ACTIVIDADES.
     * @throws Exception SI OCURRE ALGUN ERROR DURANTE LA EJECUCION DE LAS CONSULTAS SQL.
     */
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

    /**
     * VERIFICA SI EXISTE UN MONITOR EN LA BASE DE DATOS DADO SU DNI.
     * REALIZA UNA CONSULTA NATIVA PARA CONTAR SI HAY REGISTROS QUE COINCIDAN CON EL DNI PROPORCIONADO.
     *
     * @param sesion LA SESION DE HIBERNATE ACTIVA PARA REALIZAR LA CONSULTA.
     * @param DNI EL DNI DEL MONITOR QUE SE DESEA VERIFICAR.
     * @return TRUE SI EL MONITOR EXISTE, FALSE EN CASO CONTRARIO.
     * @throws Exception SI OCURRE ALGUN ERROR DURANTE LA CONSULTA.
     */
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

    /**
     * METODO LISTAMONITORES
     *
     * @param sesion PARAMETRO SESION
     * @return RETORNA LIST<MONITOR>
     */
    public List<Monitor> listaMonitores(Session sesion) {
        Query consulta = sesion.createNamedQuery("Monitor.findAll", Monitor.class);

        return consulta.getResultList();
    }

    /**
     * ELIMINA UN MONITOR DE LA BASE DE DATOS A PARTIR DE SU CODIGO IDENTIFICADOR.
     * BUSCA EL MONITOR POR SU CODIGO Y, SI LO ENCUENTRA, PROCEDE A ELIMINARLO.
     * SI NO SE ENCUENTRA, LANZA UNA EXCEPCION INDICANDO QUE NO EXISTE.
     *
     * @param sesion LA SESION ACTIVA UTILIZADA PARA LA BUSQUEDA Y EL BORRADO.
     * @param codMonitor EL CODIGO UNICO DEL MONITOR QUE SE VA A ELIMINAR.
     * @throws Exception SI EL MONITOR NO EXISTE O SI OCURRE UN ERROR DURANTE EL BORRADO.
     */
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

    /**
     * INSERTA UN NUEVO REGISTRO DE MONITOR EN LA BASE DE DATOS.
     * UTILIZA EL METODO PERSIST DE LA SESION PARA GUARDAR EL OBJETO NUEVO.
     *
     * @param sesion LA SESION ACTIVA DE HIBERNATE.
     * @param monitor EL OBJETO MONITOR QUE CONTIENE LOS DATOS A INSERTAR.
     * @throws Exception SI OCURRE UN ERROR DURANTE EL PROCESO DE GUARDADO.
     */
    public void insertarMonitor(Session sesion, Monitor monitor) throws Exception {
        sesion.persist(monitor); // 'persist' es para guardar objetos nuevos
    }

    /**
     * ACTUALIZA LA INFORMACION DE UN MONITOR EXISTENTE EN LA BASE DE DATOS.
     * UTILIZA EL METODO MERGE PARA APLICAR LOS CAMBIOS AL REGISTRO CORRESPONDIENTE.
     *
     * @param sesion LA SESION ACTIVA QUE GESTIONA LA TRANSACCION.
     * @param monitor EL OBJETO MONITOR CON LOS DATOS ACTUALIZADOS.
     * @throws Exception SI OCURRE UN ERROR DURANTE LA ACTUALIZACION.
     */
    public void actualizarMonitor(Session sesion, Monitor monitor) throws Exception {
        sesion.merge(monitor); // 'merge' actualiza si existe, o guarda si no
    }

    /**
     * CALCULA Y GENERA EL SIGUIENTE CODIGO AUTOMATICO PARA UN NUEVO MONITOR.
     * OBTIENE EL CODIGO MAS ALTO ACTUAL, EXTRAE LA PARTE NUMERICA, LA INCREMENTA
     * Y DEVUELVE EL NUEVO CODIGO FORMATEADO (EJEMPLO M001 -> M002).
     *
     * @param sesion LA SESION ACTIVA PARA CONSULTAR EL ULTIMO CODIGO REGISTRADO.
     * @return UN STRING CON EL NUEVO CODIGO GENERADO EN FORMATO MXXX.
     */
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
