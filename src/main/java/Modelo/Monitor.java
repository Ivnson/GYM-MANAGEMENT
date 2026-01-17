/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author ivan
 */
/**
 * CLASE ENTIDAD QUE REPRESENTA UN MONITOR EN EL SISTEMA PROPORCIONA
 * FUNCIONALIDADES PARA LA GESTION COMPLETA DEL SISTEMA DE GIMNASIO
 *
 * @author SISTEMA DE GESTION DE GIMNASIO
 * @version 1.0
 */
@Entity
@Table(name = "MONITOR")
@NamedQueries({
    @NamedQuery(name = "Monitor.findAll", query = "SELECT m FROM Monitor m"),
    @NamedQuery(name = "Monitor.findByCodMonitor", query = "SELECT m FROM Monitor m WHERE m.codMonitor = :codMonitor"),
    @NamedQuery(name = "Monitor.findByNombre", query = "SELECT m FROM Monitor m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Monitor.findByDni", query = "SELECT m FROM Monitor m WHERE m.dni = :dni"),
    @NamedQuery(name = "Monitor.findByTelefono", query = "SELECT m FROM Monitor m WHERE m.telefono = :telefono"),
    @NamedQuery(name = "Monitor.findByCorreo", query = "SELECT m FROM Monitor m WHERE m.correo = :correo"),
    @NamedQuery(name = "Monitor.findByFechaEntrada", query = "SELECT m FROM Monitor m WHERE m.fechaEntrada = :fechaEntrada"),
    @NamedQuery(name = "Monitor.findByNick", query = "SELECT m FROM Monitor m WHERE m.nick = :nick")})
public class Monitor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codMonitor")
    /**
     * ATRIBUTO CODMONITOR
     */
    private String codMonitor;
    @Basic(optional = false)
    @Column(name = "nombre")
    /**
     * ATRIBUTO NOMBRE
     */
    private String nombre;
    @Basic(optional = false)
    @Column(name = "dni")
    /**
     * ATRIBUTO DNI
     */
    private String dni;
    @Column(name = "telefono")
    /**
     * ATRIBUTO TELEFONO
     */
    private String telefono;
    @Column(name = "correo")
    /**
     * ATRIBUTO CORREO
     */
    private String correo;
    @Basic(optional = false)
    @Column(name = "fechaEntrada")
    /**
     * ATRIBUTO FECHAENTRADA
     */
    private String fechaEntrada;
    @Column(name = "nick")
    /**
     * ATRIBUTO NICK
     */
    private String nick;
    @OneToMany(mappedBy = "monitorResponsable")
    /**
     * COLECCION DE ACTIVIDADSET ASOCIADOS
     */
    private Set<Actividad> actividadSet;

    public Monitor() {
    }

    public Monitor(String codMonitor) {
        this.codMonitor = codMonitor;
    }

    public Monitor(String codMonitor, String nombre, String dni, String fechaEntrada) {
        this.codMonitor = codMonitor;
        this.nombre = nombre;
        this.dni = dni;
        this.fechaEntrada = fechaEntrada;
    }

    public Monitor(String codMonitor, String nombre, String dni, String telefono, String correo, String fechaEntrada, String nick, Set<Actividad> actividadSet) {
        this.codMonitor = codMonitor;
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.correo = correo;
        this.fechaEntrada = fechaEntrada;
        this.nick = nick;
        this.actividadSet = actividadSet;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO CODMONITOR
     *
     * @return RETORNA STRING
     */
    public String getCodMonitor() {
        return codMonitor;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO CODMONITOR
     *
     * @param codMonitor PARAMETRO CODMONITOR
     */
    public void setCodMonitor(String codMonitor) {
        this.codMonitor = codMonitor;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO NOMBRE
     *
     * @return RETORNA STRING
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO NOMBRE
     *
     * @param nombre PARAMETRO NOMBRE
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO DNI
     *
     * @return RETORNA STRING
     */
    public String getDni() {
        return dni;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO DNI
     *
     * @param dni PARAMETRO DNI
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO TELEFONO
     *
     * @return RETORNA STRING
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO TELEFONO
     *
     * @param telefono PARAMETRO TELEFONO
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO CORREO
     *
     * @return RETORNA STRING
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO CORREO
     *
     * @param correo PARAMETRO CORREO
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO FECHAENTRADA
     *
     * @return RETORNA STRING
     */
    public String getFechaEntrada() {
        return fechaEntrada;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO FECHAENTRADA
     *
     * @param fechaEntrada PARAMETRO FECHAENTRADA
     */
    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO NICK
     *
     * @return RETORNA STRING
     */
    public String getNick() {
        return nick;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO NICK
     *
     * @param nick PARAMETRO NICK
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO ACTIVIDADSET
     *
     * @return RETORNA SET<ACTIVIDAD>
     */
    public Set<Actividad> getActividadSet() {
        return actividadSet;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO ACTIVIDADSET
     *
     * @param actividadSet PARAMETRO ACTIVIDADSET
     */
    public void setActividadSet(Set<Actividad> actividadSet) {
        this.actividadSet = actividadSet;
    }

    @Override
    /**
     * METODO HASHCODE
     *
     * @return RETORNA INT
     */
    public int hashCode() {
        int hash = 0;
        hash += (codMonitor != null ? codMonitor.hashCode() : 0);
        return hash;
    }

    @Override
    /**
     * METODO EQUALS
     *
     * @param object PARAMETRO OBJECT
     * @return RETORNA BOOLEAN
     */
    public boolean equals(Object object) {
        if (!(object instanceof Monitor)) {
            return false;
        }
        Monitor other = (Monitor) object;
        if ((this.codMonitor == null && other.codMonitor != null) || (this.codMonitor != null && !this.codMonitor.equals(other.codMonitor))) {
            return false;
        }
        return true;
    }

    @Override
    /**
     * METODO TOSTRING
     *
     * @return RETORNA STRING
     */
    public String toString() {
        return getNombre();
    }

}
