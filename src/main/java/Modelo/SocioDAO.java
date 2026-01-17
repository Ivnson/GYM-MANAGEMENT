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
public class SocioDAO {

    private SessionFactory sf;

    public SocioDAO(SessionFactory sessionfactory) {
        this.sf = sessionfactory;
    }

    public void insertaSocio(Session sesion, Socio socio) throws Exception {
        sesion.persist(socio);
    }

    public boolean ExisteSocio(Session sesion, String dni, String numeroSocio) throws Exception {

        List<Socio> lista_socios = new ArrayList<>();

        Query consulta = sesion.createNativeQuery(
                "SELECT * FROM SOCIO WHERE NUMEROSOCIO = ? OR DNI = ?", Socio.class);

        consulta.setParameter(1, numeroSocio);
        consulta.setParameter(2, dni);

        lista_socios = consulta.getResultList();

        return !lista_socios.isEmpty();
    }

    // 1. Listar todos los socios (Usamos HQL o NamedQuery)
    public List<Socio> listaSocios(Session sesion) {
        Query consulta = sesion.createNamedQuery("Socio.findAll", Socio.class);
        return consulta.getResultList();
    }

    // 2. Insertar nuevo socio
    public void insertarSocio(Session sesion, Socio socio) throws Exception {
        sesion.persist(socio);
    }

    // 3. Actualizar socio existente
    public void actualizarSocio(Session sesion, Socio socio) throws Exception {
        sesion.merge(socio);
    }

    // 4. Borrar socio
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
