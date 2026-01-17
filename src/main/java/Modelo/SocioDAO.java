/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author ivan
 */
/**
 * CLASE DAO PARA LA GESTION DE OPERACIONES DE ACCESO A DATOS DE SOCIO
 * PROPORCIONA FUNCIONALIDADES PARA LA GESTION COMPLETA DEL SISTEMA DE GIMNASIO
 * 
 * @author SISTEMA DE GESTION DE GIMNASIO
 * @version 1.0
 */
public class SocioDAO {

    /** ATRIBUTO SF */
    private SessionFactory sf;

    public SocioDAO(SessionFactory sessionfactory) {
        this.sf = sessionfactory;
    }

    /**
     * METODO PARA INSERTAR UN NUEVO SOCIO EN LA BASE DE DATOS.
     * UTILIZA EL METODO PERSIST DE LA SESION PARA GUARDAR EL OBJETO.
     *
     * @param sesion LA SESION ACTIVA DE HIBERNATE.
     * @param socio EL OBJETO SOCIO QUE SE DESEA ALMACENAR.
     * @throws Exception SI OCURRE UN ERROR DURANTE LA PERSISTENCIA DE DATOS.
     */
    public void insertaSocio(Session sesion, Socio socio) throws Exception {
        sesion.persist(socio);
    }

    /**
     * COMPRUEBA SI YA EXISTE UN SOCIO REGISTRADO CON EL MISMO NUMERO DE SOCIO O DNI.
     * REALIZA UNA CONSULTA NATIVA QUE BUSCA COINCIDENCIAS EN AMBOS CAMPOS.
     *
     * @param sesion LA SESION ACTIVA PARA REALIZAR LA CONSULTA.
     * @param dni EL DNI DEL SOCIO A VERIFICAR.
     * @param numeroSocio EL NUMERO IDENTIFICATIVO DEL SOCIO A VERIFICAR.
     * @return TRUE SI SE ENCUENTRA ALGUN REGISTRO COINCIDENTE, FALSE EN CASO CONTRARIO.
     * @throws Exception SI OCURRE UN ERROR DURANTE LA EJECUCION DE LA CONSULTA.
     */
    public boolean ExisteSocio(Session sesion, String dni, String numeroSocio) throws Exception {

        List<Socio> lista_socios = new ArrayList<>();

        Query consulta = sesion.createNativeQuery(
                "SELECT * FROM SOCIO WHERE NUMEROSOCIO = ? OR DNI = ?", Socio.class);

        consulta.setParameter(1, numeroSocio);
        consulta.setParameter(2, dni);

        lista_socios = consulta.getResultList();

        return !lista_socios.isEmpty();
    }

    /**
     * OBTIENE UNA LISTA COMPLETA DE TODOS LOS SOCIOS REGISTRADOS EN EL SISTEMA.
     * UTILIZA UNA NAMEDQUERY DEFINIDA EN LA CLASE SOCIO PARA RECUPERAR LOS DATOS.
     *
     * @param sesion LA SESION ACTIVA DE HIBERNATE.
     * @return UNA LISTA DE OBJETOS SOCIO CON TODOS LOS REGISTROS DE LA BASE DE DATOS.
     */
    public List<Socio> listaSocios(Session sesion) {
        Query consulta = sesion.createNamedQuery("Socio.findAll", Socio.class);
        return consulta.getResultList();
    }

    /**
     * INSERTA UN NUEVO SOCIO EN LA BASE DE DATOS.
     * FUNCIONA DE MANERA IDENTICA AL METODO INSERTASOCIO ANTERIOR.
     *
     * @param sesion LA SESION ACTIVA DE HIBERNATE.
     * @param socio EL OBJETO SOCIO A GUARDAR.
     * @throws Exception SI OCURRE UN ERROR AL GUARDAR EL REGISTRO.
     */
    public void insertarSocio(Session sesion, Socio socio) throws Exception {
        sesion.persist(socio);
    }

    /**
     * ACTUALIZA LA INFORMACION DE UN SOCIO YA EXISTENTE EN LA BASE DE DATOS.
     * UTILIZA EL METODO MERGE PARA SINCRONIZAR LOS CAMBIOS DEL OBJETO.
     *
     * @param sesion LA SESION ACTIVA QUE GESTIONA LA TRANSACCION.
     * @param socio EL OBJETO SOCIO CON LOS DATOS MODIFICADOS.
     * @throws Exception SI OCURRE UN ERROR DURANTE EL PROCESO DE ACTUALIZACION.
     */
    public void actualizarSocio(Session sesion, Socio socio) throws Exception {
        sesion.merge(socio);
    }

    /**
     * ELIMINA UN SOCIO DE LA BASE DE DATOS BUSCANDOLO POR SU NUMERO DE SOCIO.
     * PRIMERO RECUPERA EL SOCIO MEDIANTE UNA CONSULTA Y LUEGO LO ELIMINA SI EXISTE.
     *
     * @param sesion LA SESION ACTIVA UTILIZADA PARA LA BUSQUEDA Y EL BORRADO.
     * @param numeroSocio EL NUMERO IDENTIFICADOR DEL SOCIO A BORRAR.
     * @throws Exception SI NO SE ENCUENTRA EL SOCIO CON ESE NUMERO O SI FALLA LA BASE DE DATOS.
     */
    public void borrarSocio(Session sesion, String numeroSocio) throws Exception {
        Query consulta = sesion.createNativeQuery("SELECT * FROM SOCIO WHERE NUMEROSOCIO = ? ", Socio.class);
        consulta.setParameter(1, numeroSocio) ; 
        Socio socio = (Socio) consulta.getSingleResult() ; 
        
        if (socio != null) {
            sesion.remove(socio);
        } else {
            throw new Exception("No se encontró el socio con número: " + numeroSocio);
        }
    }
    
    // NUEVO MÉTODO: Calcular el siguiente código automático (M001 -> M002 -> ...)
    /**
     * OBTIENE EL VALOR DEL CAMPO SIGUIENTECODIGO
     *
     * @param sesion PARAMETRO SESION
     * @return RETORNA STRING
     */
    public String getSiguienteCodigo(Session sesion) {
        String codigoMax = (String) sesion.createNativeQuery("SELECT MAX(numeroSocio) FROM SOCIO").getSingleResult();

        if (codigoMax == null) {
            return "S001";
        }

        // Extraemos el número (quitamos la 'S')
        String numeroStr = codigoMax.substring(1);
        int numero = Integer.parseInt(numeroStr);

        // Sumamos 1
        numero++;

        // Devolvemos formateado con 3 dígitos (ej: S011)
        return String.format("S%03d", numero);
    }
}
