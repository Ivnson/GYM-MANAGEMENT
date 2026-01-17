/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author ivan
 */
/**
 * CLASE ENTIDAD QUE REPRESENTA UN ACTIVIDAD EN EL SISTEMA PROPORCIONA
 * FUNCIONALIDADES PARA LA GESTION COMPLETA DEL SISTEMA DE GIMNASIO
 *
 * @author SISTEMA DE GESTION DE GIMNASIO
 * @version 1.0
 */
@Entity
@Table(name = "ACTIVIDAD")
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"),
    @NamedQuery(name = "Actividad.findByIdActividad", query = "SELECT a FROM Actividad a WHERE a.idActividad = :idActividad"),
    @NamedQuery(name = "Actividad.findByNombre", query = "SELECT a FROM Actividad a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Actividad.findByDia", query = "SELECT a FROM Actividad a WHERE a.dia = :dia"),
    @NamedQuery(name = "Actividad.findByHora", query = "SELECT a FROM Actividad a WHERE a.hora = :hora"),
    @NamedQuery(name = "Actividad.findByDescripcion", query = "SELECT a FROM Actividad a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Actividad.findByPrecioBaseMes", query = "SELECT a FROM Actividad a WHERE a.precioBaseMes = :precioBaseMes")})
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idActividad")
    /**
     * IDENTIFICADOR UNICO DEL IDACTIVIDAD
     */
    private String idActividad;
    @Basic(optional = false)
    @Column(name = "nombre")
    /**
     * ATRIBUTO NOMBRE
     */
    private String nombre;
    @Basic(optional = false)
    @Column(name = "dia")
    /**
     * ATRIBUTO DIA
     */
    private String dia;
    @Basic(optional = false)
    @Column(name = "hora")
    /**
     * ATRIBUTO HORA
     */
    private int hora;
    @Column(name = "descripcion")
    /**
     * ATRIBUTO DESCRIPCION
     */
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "precioBaseMes")
    /**
     * ATRIBUTO PRECIOBASEMES
     */
    private int precioBaseMes;
    @JoinTable(name = "REALIZA", joinColumns = {
        @JoinColumn(name = "idActividad", referencedColumnName = "idActividad")}, inverseJoinColumns = {
        @JoinColumn(name = "numeroSocio", referencedColumnName = "numeroSocio")})
    @ManyToMany
    /**
     * COLECCION DE SOCIOSET ASOCIADOS
     */
    private Set<Socio> socioSet;
    @JoinColumn(name = "monitorResponsable", referencedColumnName = "codMonitor")
    @ManyToOne
    /**
     * ATRIBUTO MONITORRESPONSABLE
     */
    private Monitor monitorResponsable;

    public Actividad() {
    }

    public Actividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public Actividad(String idActividad, String nombre, String dia, int hora, int precioBaseMes) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.dia = dia;
        this.hora = hora;
        this.precioBaseMes = precioBaseMes;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO IDACTIVIDAD
     *
     * @return RETORNA STRING
     */
    public String getIdActividad() {
        return idActividad;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO IDACTIVIDAD
     *
     * @param idActividad PARAMETRO IDACTIVIDAD
     */
    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
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
     * OBTIENE EL VALOR DEL CAMPO DIA
     *
     * @return RETORNA STRING
     */
    public String getDia() {
        return dia;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO DIA
     *
     * @param dia PARAMETRO DIA
     */
    public void setDia(String dia) {
        this.dia = dia;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO HORA
     *
     * @return RETORNA INT
     */
    public int getHora() {
        return hora;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO HORA
     *
     * @param hora PARAMETRO HORA
     */
    public void setHora(int hora) {
        this.hora = hora;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO DESCRIPCION
     *
     * @return RETORNA STRING
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO DESCRIPCION
     *
     * @param descripcion PARAMETRO DESCRIPCION
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO PRECIOBASEMES
     *
     * @return RETORNA INT
     */
    public int getPrecioBaseMes() {
        return precioBaseMes;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO PRECIOBASEMES
     *
     * @param precioBaseMes PARAMETRO PRECIOBASEMES
     */
    public void setPrecioBaseMes(int precioBaseMes) {
        this.precioBaseMes = precioBaseMes;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO SOCIOSET
     *
     * @return RETORNA SET<SOCIO>
     */
    public Set<Socio> getSocioSet() {
        return socioSet;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO SOCIOSET
     *
     * @param socioSet PARAMETRO SOCIOSET
     */
    public void setSocioSet(Set<Socio> socioSet) {
        this.socioSet = socioSet;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO MONITORRESPONSABLE
     *
     * @return RETORNA MONITOR
     */
    public Monitor getMonitorResponsable() {
        return monitorResponsable;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO MONITORRESPONSABLE
     *
     * @param monitorResponsable PARAMETRO MONITORRESPONSABLE
     */
    public void setMonitorResponsable(Monitor monitorResponsable) {
        this.monitorResponsable = monitorResponsable;
    }

    /**
     *
     *
     * @Override /** METODO HASHCODE
     *
     * @return RETORNA INT
     */
    public int hashCode() {
        int hash = 0;
        hash += (idActividad != null ? idActividad.hashCode() : 0);
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
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.idActividad == null && other.idActividad != null) || (this.idActividad != null && !this.idActividad.equals(other.idActividad))) {
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
        return getIdActividad() + " - " + getNombre();
    }
}
