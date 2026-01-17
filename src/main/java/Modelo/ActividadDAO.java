package Modelo;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class ActividadDAO {

    private SessionFactory sf;

    public ActividadDAO(SessionFactory sessionFactory) {
        this.sf = sessionFactory;
    }

    // 1. Listar todas
    public List<Actividad> listaActividades(Session sesion) {
        Query<Actividad> consulta = sesion.createNamedQuery("Actividad.findAll", Actividad.class);
        return consulta.getResultList();
    }

    // 2. Insertar
    public void insertarActividad(Session sesion, Actividad actividad) throws Exception {
        sesion.persist(actividad);
    }

    // 3. Actualizar
    public void actualizarActividad(Session sesion, Actividad actividad) throws Exception {
        sesion.merge(actividad);
    }

    // 4. Borrar
    public void borrarActividad(Session sesion, String idActividad) throws Exception {
        Query consulta = sesion.createNativeQuery("SELECT * FROM ACTIVIDAD  WHERE IDACTIVIDAD = ?", Actividad.class);
        consulta.setParameter(1, idActividad);
        Actividad actividad = (Actividad) consulta.getSingleResult();

        if (actividad != null) {
            sesion.remove(actividad);
        } else {
            throw new Exception("No se encontró la actividad con ID: " + idActividad);
        }
    }

    public String getSiguienteCodigo(Session sesion) {
        String codigoMax = sesion.createNativeQuery("SELECT MAX(idActividad) FROM ACTIVIDAD", String.class)
                .getSingleResult();

        if (codigoMax == null || codigoMax.trim().isEmpty()) {
            return "AC01";
        }

        try {
            // Extraemos la parte numérica
            int numero = Integer.parseInt(codigoMax.substring(2));
            numero++;

            // %02d asegura al menos 2 dígitos (AC01, AC09, AC10, AC100...)
            return String.format("AC%02d", numero);
        } catch (NumberFormatException e) {
            // Manejo de excepción si el ID en la BD no tiene formato numérico
            return "AC01";
        }
    }

    // Método para validar conflictos de horario
    public boolean existeConflictoMonitor(Session sesion, String codMonitor, String dia, int hora, String idActividadActual) {
        // Consultamos si hay alguna actividad con ese monitor, ese día y esa hora
        // Y IMPORTANTE: Que NO sea la actividad actual (idActividadActual) para permitir editarse a sí misma.
        String hql = "SELECT count(a) FROM Actividad a WHERE a.monitorResponsable.codMonitor = :codMonitor "
                + "AND a.dia = :dia AND a.hora = :hora "
                + "AND a.idActividad != :idActual";

        Long conteo = (Long) sesion.createQuery(hql, Long.class)
                .setParameter("codMonitor", codMonitor)
                .setParameter("dia", dia)
                .setParameter("hora", hora)
                .setParameter("idActual", idActividadActual) // Pasamos el ID actual (o "" si es nueva)
                .uniqueResult();

        return conteo > 0; // Si devuelve > 0, es que hay conflicto
    }

    // Método dinámico para filtrar por cualquier campo
    public List<Actividad> filtrarActividades(Session sesion, String criterio, String valor) {
        String hql = "SELECT a FROM Actividad a WHERE ";

        // Adaptamos el criterio visual al nombre real del atributo en la clase Java
        switch (criterio) {
            case "ID":
                hql += "a.idActividad = :valor";
                break;
            case "Nombre":
                hql += "a.nombre = :valor";
                break;
            case "Dia":
                hql += "a.dia = :valor";
                break;
            case "Hora":
                // Truco: convertimos el número a string en la BD para usar LIKE
                hql += "str(a.hora) = :valor";
                break;
            case "Monitor":
                // OJO: Monitor es un objeto, accedemos a su nombre
                hql += "a.monitorResponsable.nombre = :valor";
                break;
            default:
                hql += "a.nombre = :valor";
                break;
        }

        org.hibernate.query.Query<Actividad> query = sesion.createQuery(hql, Actividad.class);

        if (criterio.equals("Hora")) {
            try {
                // Si buscamos por hora, convertimos el texto a número
                int horaNumerica = Integer.parseInt(valor);
                query.setParameter("valor", horaNumerica);
            } catch (NumberFormatException e) {
                // Si el usuario escribió texto en vez de un número, devolvemos lista vacía
                // para evitar errores de ejecución.
                System.out.println("Error: Se intentó buscar una hora con formato no numérico.");
                return new java.util.ArrayList<>();
            }
        } else {
            // Para todo lo demás (ID, Nombre, Dia, Monitor), pasamos el String directo
            query.setParameter("valor", valor);
        }

        return query.getResultList();
    }

    public List<Object> ejecutarProcedimientoEstadisticas(Session sesion, String idActividad) {

        // VARIABLE 1: NUMERO DE SOCIOS
        // Creamos la consulta llamando al procedimiento 'calc_num_socios'
        StoredProcedureQuery query1 = sesion.createStoredProcedureQuery("calc_num_socios");
        // Registramos entrada (ID Actividad) y salida (Resultado INT)
        query1.registerStoredProcedureParameter("p_idActividad", String.class, ParameterMode.IN);
        query1.registerStoredProcedureParameter("p_resultado", Integer.class, ParameterMode.OUT);
        // Le pasamos el valor y ejecutamos
        query1.setParameter("p_idActividad", idActividad);
        query1.execute();

        // VARIABLE 2: EDAD MEDIA
        StoredProcedureQuery query2 = sesion.createStoredProcedureQuery("calc_edad_media");
        query2.registerStoredProcedureParameter("p_idActividad", String.class, ParameterMode.IN);
        query2.registerStoredProcedureParameter("p_resultado", Double.class, ParameterMode.OUT);

        query2.setParameter("p_idActividad", idActividad);
        query2.execute();

        // VARIABLE 3: CATEGORÍA DE MODA (La letra que más se repite)
        StoredProcedureQuery query3 = sesion.createStoredProcedureQuery("calc_categoria_moda");
        query3.registerStoredProcedureParameter("p_idActividad", String.class, ParameterMode.IN);
        query3.registerStoredProcedureParameter("p_resultado", Character.class, ParameterMode.OUT);

        query3.setParameter("p_idActividad", idActividad);
        query3.execute();

        // VARIABLE 4: INGRESOS MENSUALES
        StoredProcedureQuery query4 = sesion.createStoredProcedureQuery("calc_ingresos");
        query4.registerStoredProcedureParameter("p_idActividad", String.class, ParameterMode.IN);
        query4.registerStoredProcedureParameter("p_resultado", Double.class, ParameterMode.OUT);

        query4.setParameter("p_idActividad", idActividad);
        query4.execute();

        // Guardamos los datos
        Integer numSocios = (Integer) query1.getOutputParameterValue("p_resultado");
        Double edadMedia = (Double) query2.getOutputParameterValue("p_resultado");
        Character moda = (Character) query3.getOutputParameterValue("p_resultado");
        Double ingresos = (Double) query4.getOutputParameterValue("p_resultado");

        List<Object> lista = new ArrayList<>();
        lista.add(numSocios);
        lista.add(edadMedia);
        lista.add(moda);
        lista.add(ingresos);

        // DEVOLVEMOS TODO EMPAQUETADO 
        return lista;
    }

}
