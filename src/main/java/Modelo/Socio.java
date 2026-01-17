/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
 * CLASE ENTIDAD QUE REPRESENTA UN SOCIO EN EL SISTEMA PROPORCIONA
 * FUNCIONALIDADES PARA LA GESTION COMPLETA DEL SISTEMA DE GIMNASIO
 *
 * @author SISTEMA DE GESTION DE GIMNASIO
 * @version 1.0
 */
@Entity
@Table(name = "SOCIO")
@NamedQueries({
    @NamedQuery(name = "Socio.findAll", query = "SELECT s FROM Socio s"),
    @NamedQuery(name = "Socio.findByNumeroSocio", query = "SELECT s FROM Socio s WHERE s.numeroSocio = :numeroSocio"),
    @NamedQuery(name = "Socio.findByNombre", query = "SELECT s FROM Socio s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "Socio.findByDni", query = "SELECT s FROM Socio s WHERE s.dni = :dni"),
    @NamedQuery(name = "Socio.findByFechaNacimiento", query = "SELECT s FROM Socio s WHERE s.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Socio.findByTelefono", query = "SELECT s FROM Socio s WHERE s.telefono = :telefono"),
    @NamedQuery(name = "Socio.findByCorreo", query = "SELECT s FROM Socio s WHERE s.correo = :correo"),
    @NamedQuery(name = "Socio.findByFechaEntrada", query = "SELECT s FROM Socio s WHERE s.fechaEntrada = :fechaEntrada"),
    @NamedQuery(name = "Socio.findByCategoria", query = "SELECT s FROM Socio s WHERE s.categoria = :categoria")})
public class Socio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numeroSocio")
    /**
     * IDENTIFICADOR UNICO DEL NUMEROSOCIO
     */
    private String numeroSocio;
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
    @Column(name = "fechaNacimiento")
    /**
     * ATRIBUTO FECHANACIMIENTO
     */
    private String fechaNacimiento;
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
    @Basic(optional = false)
    @Column(name = "categoria")
    /**
     * ATRIBUTO CATEGORIA
     */
    private Character categoria;
    @ManyToMany(mappedBy = "socioSet")
    /**
     * COLECCION DE ACTIVIDADSET ASOCIADOS
     */
    private Set<Actividad> actividadSet;

    public Socio() {
    }

    public Socio(String numeroSocio) {
        this.numeroSocio = numeroSocio;
    }

    public Socio(String numeroSocio, String nombre, String dni, String fechaNacimiento, String telefono, String correo, String fechaEntrada, Character categoria) {
        this.numeroSocio = numeroSocio;
        this.nombre = nombre;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.fechaEntrada = fechaEntrada;
        this.categoria = categoria;
    }

    /**
     * OBTIENE EL VALOR DEL CAMPO NUMEROSOCIO
     *
     * @return RETORNA STRING
     */
    public String getNumeroSocio() {
        return numeroSocio;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO NUMEROSOCIO
     *
     * @param numeroSocio PARAMETRO NUMEROSOCIO
     */
    public void setNumeroSocio(String numeroSocio) {
        this.numeroSocio = numeroSocio;
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
     * OBTIENE EL VALOR DEL CAMPO FECHANACIMIENTO
     *
     * @return RETORNA STRING
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO FECHANACIMIENTO
     *
     * @param fechaNacimiento PARAMETRO FECHANACIMIENTO
     */
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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
     * OBTIENE EL VALOR DEL CAMPO CATEGORIA
     *
     * @return RETORNA CHARACTER
     */
    public Character getCategoria() {
        return categoria;
    }

    /**
     * ESTABLECE EL VALOR DEL CAMPO CATEGORIA
     *
     * @param categoria PARAMETRO CATEGORIA
     */
    public void setCategoria(Character categoria) {
        this.categoria = categoria;
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
        hash += (numeroSocio != null ? numeroSocio.hashCode() : 0);
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
        if (!(object instanceof Socio)) {
            return false;
        }
        Socio other = (Socio) object;
        if ((this.numeroSocio == null && other.numeroSocio != null) || (this.numeroSocio != null && !this.numeroSocio.equals(other.numeroSocio))) {
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
        return getNumeroSocio() + " - " + getNombre();
    }

}
