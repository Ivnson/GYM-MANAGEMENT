/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Config.HibernateUtil;
import Vista.VistaConexion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.hibernate.SessionFactory;

/**
 *
 * @author ivan
 */
public class ControladorConexion implements ActionListener {

    private SessionFactory sf;
    private Vista.VistaConexion VistaConexion;

    public ControladorConexion() {

        //CREAMOS LA VENTANA DE CONEXION A LA APLICACION 
        VistaConexion = new VistaConexion();

        // REGISTRAMOS ESTE CONTROLADOR PARA QUE PUEDA REACCIONAR
        //ANTE LOS BOTONES DE LA VENTANA VISTACONEXION
        this.VistaConexion.jButton2.addActionListener((ActionListener) this);// BOTON ENTRAR
        this.VistaConexion.jButton1.addActionListener((ActionListener) this);//BOTON CANCELAR

        //CONFIGURAR Y MOSTRAR LA VENTANA 
        //CENTRAR LA VENTANA 
        VistaConexion.setLocationRelativeTo(null);
        //EVITAR EL REDIMENSIONAMIENTO
        VistaConexion.setResizable(false);
        //HACER VISIBLE LA VENTANA 
        VistaConexion.setVisible(true);

    }

    /*public static SessionFactory conectarBD() {// poner try 
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        return sessionFactory;
    }*/
    @Override
    public void actionPerformed(ActionEvent e) {

        String accion = e.getActionCommand();

        if (accion.equals(VistaConexion.jButton2.getActionCommand())) {
            Entrar();
        } else if (accion.equals(VistaConexion.jButton1.getActionCommand())) {
            Salir();
        }
    }

    private void Entrar() {
        try {

            //LEE TEXTO DE USUARIO Y CONTRASEÑA 
            String usuario = VistaConexion.jTextField1.getText().trim();
            String password = new String(VistaConexion.jPasswordField1.getPassword());
            
            System.out.println("user " + usuario + " contraseña " + password);

            //CONSTRUIR EL SESSIONFACTORY
            this.sf = HibernateUtil.buildSessionFactory(usuario, password);
            
           

            if (sf == null || sf.isClosed() == true) {

                //VENTANA EMERGENTE PARA MOSTRAR ERROR 
                JOptionPane.showMessageDialog(VistaConexion, "USUARIO O CONTRASEÑA INCORRECTOS");

                //NO SE CIERRA LA VENTANA PARA QUE EL USUARIO LO VUELVA
                //A INTENTAR 
                HibernateUtil.close();

            } else {
                JOptionPane.showMessageDialog(VistaConexion, "CONEXION ESTABLECIDA");

                //CERRAMOS LA VENTANA DE LOGIN 
                VistaConexion.dispose();

                //LANZAMOS EL CONTROLADOR PRINCIPAL 
               
                new ControladorPrincipal(sf);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(VistaConexion, "ERROR ENTRAR : "+ e.getMessage());
        }
    }
    
    private void Salir()
    {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(VistaConexion, "¿QUIERES SALIR?", "SALIDA", JOptionPane.YES_NO_OPTION);
            
            //SI LA RESPUESTA ES 0 ENTONCES ES QUE QUIERE SALIR 
            if (confirmacion == JOptionPane.YES_NO_OPTION)
            {
                VistaConexion.dispose();
                System.exit(0);
            }
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(VistaConexion, "ERROR SALIR");
            
        }
    }
}
