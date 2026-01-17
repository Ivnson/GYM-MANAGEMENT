package Aplicacion;

import org.hibernate.SessionFactory;
import Config.HibernateUtil;
import Modelo.Actividad;
import Modelo.Monitor;
import Modelo.Socio;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ivan
 */
public class PROYECTO_DDSI_1 {

    //private static Object HibernateUtil;
    
    public static void main(String[] args) {

        try {
            // Se obtiene la SessionFactory definida en HibernateUtil
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            System.out.println("Conexión Correcta con Hibernate");

            Scanner sc = new Scanner(System.in);
            boolean salir = false;
            int opc = 0;
            do {
                System.out.println("----------MENU----------\n");
                System.out.println("1. Información de los socios (HQL)");
                System.out.println("2. Información de los socios (SQL Nativo)");
                System.out.println("3. Información de los socios (Consulta nombrada)");
                System.out.println("4. Nombre y teléfono de los socios");
                System.out.println("5. Nombre y categoría de los socios por categoría");
                System.out.println("6. Nombre de monitor por nick");
                System.out.println("7. Información de socio por nombre");
                System.out.println("8. Información de actividades por día y cuota");
                System.out.println("9. Información de socios por categoría (HQL nombrada)");
                System.out.println("10.Información de socios por categoría (SQL nativo nombrada)");
                System.out.println("11.Insercion de socio");
                System.out.println("12.Borrar socio");
                System.out.println("13.Información de la actividad de la que es responsable un monitor");
                System.out.println("14.Información de las actividades en las que está inscrito un socio por DNI");
                System.out.println("15.Información de los socios inscritos en una actividad por nombre de la actividad");
                System.out.println("16.Inscripción de un socio en una actividad");
                System.out.println("17.Baja de un socio de una actividad");
                System.out.println("18.Mostrar el horario de un monitor por el DNI");
                System.out.println("19.Mostrar la cuota que paga un socio");
                System.out.println("20.Mostrar los socios que sean mayores a una edad");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");

                opc = sc.nextInt();

                try {
                    List<Socio> Lista_socios1 = null;
                    Session sesion1 = sessionFactory.openSession();

                    switch (opc) {
                        case 1:
                            Transaction transaccion1 = sesion1.beginTransaction();

                            try {
                                Query consulta1 = sesion1.createQuery("FROM Socio", Socio.class);
                                Lista_socios1 = consulta1.getResultList();

                                for (int i = 0; i < Lista_socios1.size(); i++) {
                                    System.out.println(Lista_socios1.get(i).toString());
                                }
                                transaccion1.commit();
                            } catch (Exception e) {
                                transaccion1.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 1 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 2:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion2 = sesion1.beginTransaction();

                            try {
                                Query consulta2 = sesion1.createNativeQuery("SELECT * FROM SOCIO", Socio.class);

                                Lista_socios1 = consulta2.getResultList();

                                for (int i = 0; i < Lista_socios1.size(); i++) {
                                    System.out.println(Lista_socios1.get(i).toString());
                                }
                            } catch (Exception e) {
                                transaccion2.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 2 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 3:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion3 = sesion1.beginTransaction();
                            try {
                                Query consulta3 = sesion1.getNamedQuery("Socio.findAll");

                                Lista_socios1 = consulta3.getResultList();

                                for (int i = 0; i < Lista_socios1.size(); i++) {
                                    System.out.println(Lista_socios1.get(i).toString());
                                }
                            } catch (Exception e) {
                                transaccion3.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 3 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        //PEDIR EL CASO 4 YA QUE ME DA ERROR 
                        case 4:
                            List<Object[]> lista1 = null;
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion4 = sesion1.beginTransaction();
                            try {
                                Query consulta4 = sesion1.createNativeQuery("SELECT S.NOMBRE, S.TELEFONO FROM SOCIO S", Socio.class);
                                lista1 = consulta4.getResultList();

                                for (Object[] elemento : lista1) {
                                    System.out.println("Nombre = " + elemento[0] + ", telefono = " + elemento[1]);
                                }
                            } catch (Exception e) {
                                transaccion4.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 4 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 5:
                            List<Object[]> lista2 = null;
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion5 = sesion1.beginTransaction();
                            try {
                                System.out.println("INTRODUCE LA CATEGORIA : ");
                                Scanner scanner = new Scanner(System.in);
                                String categoria_leida = scanner.nextLine();
                                Query consulta5 = sesion1.createNativeQuery("SELECT S.NOMBRE, S.CATEGORIA FROM SOCIO S WHERE CATEGORIA = ?", Socio.class);
                                consulta5.setParameter(1, categoria_leida);

                                lista2 = consulta5.getResultList();

                                for (int i = 0; i < lista2.size(); i++) {
                                    Object[] elemento = lista2.get(i);
                                    System.out.println("NOMBRE --> " + (String) elemento[0]
                                            + " || CATEGORIA --> " + elemento[1]);
                                }
                                //scanner.close();
                            } catch (Exception e) {
                                transaccion5.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 5 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 6:
                            List<Object[]> lista3 = null;
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion6 = sesion1.beginTransaction();
                            try {
                                System.out.println("INTRODUCE EL NICK DEL MONITOR : ");
                                Scanner scanner = new Scanner(System.in);
                                String nick_leido = scanner.nextLine();

                                Query consulta6 = sesion1.createNativeQuery("SELECT M.NOMBRE, M.NICK FROM MONITOR M WHERE NICK = ?", Monitor.class);
                                consulta6.setParameter(1, nick_leido);

                                lista3 = consulta6.getResultList();

                                for (int i = 0; i < lista3.size(); i++) {
                                    Object[] elemento = lista3.get(i);
                                    System.out.println("NOMBRE --> " + (String) elemento[0]
                                            + " ====== NICK --> " + elemento[1]);
                                }
                                //scanner.close();
                            } catch (Exception e) {
                                transaccion6.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 6 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 7:
                            //List<Object[]> lista4 = null;
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion7 = sesion1.beginTransaction();
                            try {
                                System.out.println("INTRODUCE EL NOMBRE DEL SOCIO : ");
                                Scanner scanner = new Scanner(System.in);
                                String nombre_leido = scanner.nextLine();

                                Query consulta7 = sesion1.createNativeQuery("SELECT * FROM SOCIO WHERE NOMBRE = ?", Socio.class);
                                consulta7.setParameter(1, nombre_leido);

                                Lista_socios1 = consulta7.getResultList();

                                for (int i = 0; i < Lista_socios1.size(); i++) {
                                    //Object[] elemento = lista3.get(i);
                                    /*System.out.println("NOMBRE --> " + (String) elemento[0]
                                            + " ====== NICK --> " + elemento[1]);
                                     */
                                    System.out.println(Lista_socios1.get(i).toString());
                                }
                                //scanner.close();
                            } catch (Exception e) {
                                transaccion7.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 7 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 8:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion8 = sesion1.beginTransaction();
                            List<Actividad> lista_actividades = null;
                            try {
                                System.out.println("INTRODUCE UN DIA DETERMINADO : ");
                                Scanner scanner = new Scanner(System.in);
                                String dia_leido = scanner.nextLine();

                                System.out.println("INTRODUCE UNA CUOTA : ");
                                int cuota_leida = scanner.nextInt();

                                Query consulta8 = sesion1.createNativeQuery("SELECT * FROM ACTIVIDAD WHERE DIA = ? AND PRECIOBASEMES > ?", Actividad.class);
                                consulta8.setParameter(1, dia_leido);
                                consulta8.setParameter(2, cuota_leida);

                                lista_actividades = consulta8.getResultList();

                                for (int i = 0; i < lista_actividades.size(); i++) {
                                    System.out.println(lista_actividades.get(i).toString());
                                }
                            } catch (Exception e) {
                                transaccion8.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 8 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 9:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion9 = sesion1.beginTransaction();
                            try {
                                System.out.println("INTRODUCE LA CATEGORIA : ");
                                Scanner scanner = new Scanner(System.in);
                                String categoria_leida = scanner.nextLine();
                                Query consulta9 = sesion1.createQuery("SELECT s FROM Socio s WHERE s.categoria = :categoria", Socio.class);
                                consulta9.setParameter(1, categoria_leida);

                                Lista_socios1 = consulta9.getResultList();

                                for (int i = 0; i < Lista_socios1.size(); i++) {
                                    System.out.println(Lista_socios1.get(i).toString());
                                }
                                //scanner.close();
                            } catch (Exception e) {
                                transaccion9.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 9 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 10:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion10 = sesion1.beginTransaction();
                            try {
                                System.out.println("INTRODUCE LA CATEGORIA : ");
                                Scanner scanner = new Scanner(System.in);
                                String categoria_leida = scanner.nextLine();
                                Query consulta10 = sesion1.createQuery("SELECT * FROM SOCIO WHERE CATEGORIA = ?", Socio.class);
                                consulta10.setParameter(1, categoria_leida);

                                Lista_socios1 = consulta10.getResultList();

                                for (int i = 0; i < Lista_socios1.size(); i++) {
                                    System.out.println(Lista_socios1.get(i).toString());
                                }
                                //scanner.close();
                            } catch (Exception e) {
                                transaccion10.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 10 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 11:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion11 = sesion1.beginTransaction();
                            try {
                                Scanner scanner = new Scanner(System.in);

                                System.out.println("Introduce el numero del Socio (SXXX) : ");
                                String numeroSocio = scanner.nextLine();

                                System.out.println("Introduce el nombre completo del socio : ");
                                String nombre_completo = scanner.nextLine();

                                System.out.println("Introduce el DNI del socio : ");
                                String DNI = scanner.nextLine();

                                System.out.println("Introduce la fecha de nacimiento del socio (dd/MM/yyyy) : ");
                                String fecha_nacimiento = scanner.nextLine();

                                System.out.println("Introduce el telefono del socio : ");
                                String telefono = scanner.nextLine();

                                System.out.println("Introduce el email del socio : ");
                                String email = scanner.nextLine();

                                System.out.println("Introduce la fecha de entrada del socio : ");
                                String fecha_entrada = scanner.nextLine();

                                System.out.println("Introduce la categoria del socio : ");
                                Character categoria = scanner.next().charAt(0);

                                Query consulta11 = sesion1.createNativeQuery("SELECT * FROM SOCIO WHERE DNI = ?", Socio.class);
                                consulta11.setParameter(1, DNI);

                                Socio Socio_existente = null;

                                try {
                                    Socio_existente = (Socio) consulta11.getSingleResult();
                                } catch (Exception e) {
                                    Socio_existente = null;
                                }

                                if (Socio_existente == null) {
                                    Socio newSocio = new Socio(numeroSocio, nombre_completo, DNI, fecha_nacimiento, telefono, email, fecha_entrada, categoria);
                                    sesion1.persist(newSocio);
                                    System.out.println("SOCIO INSERTADO");
                                }

                                transaccion11.commit();
                            } catch (Exception e) {
                                transaccion11.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 11 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;
                        case 12:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion12 = sesion1.beginTransaction();

                            try {
                                Scanner scanner = new Scanner(System.in);

                                System.out.println("Introduce el DNI del socio : ");
                                String DNI = scanner.nextLine();

                                Query consulta12 = sesion1.createNativeQuery("SELECT * FROM SOCIO WHERE DNI = ?", Socio.class);
                                consulta12.setParameter(1, DNI);

                                //Socio borrarSocio = sesion1.find(Socio.class,id )
                                Socio Socio_existente = null;
                                try {
                                    Socio_existente = (Socio) consulta12.getSingleResult();
                                } catch (Exception e) {
                                    Socio_existente = null;
                                    System.out.println("SOCIO NO ENCONTRADO");
                                }

                                if (Socio_existente != null) {
                                    sesion1.remove(Socio_existente);
                                }

                                transaccion12.commit();

                            } catch (Exception e) {
                                transaccion12.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 12 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;
                        case 13:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion13 = sesion1.beginTransaction();
                            lista_actividades = null;
                            try {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("Introduce el DNI del monitor : ");
                                String DNI = scanner.nextLine();

                                Query consulta13 = sesion1.createNativeQuery("SELECT * FROM MONITOR WHERE DNI = ? ", Monitor.class);
                                consulta13.setParameter(1, DNI);

                                Monitor monitor_existente = null;
                                try {
                                    monitor_existente = (Monitor) consulta13.getSingleResult();

                                    Query consulta13_1 = sesion1.createNativeQuery("SELECT * FROM ACTIVIDAD WHERE MONITORRESPONSABLE = ?", Actividad.class);
                                    consulta13_1.setParameter(1, monitor_existente.getCodMonitor());

                                    lista_actividades = consulta13_1.getResultList();

                                    for (int i = 0; i < lista_actividades.size(); i++) {
                                        System.out.println(lista_actividades.get(i).toString());

                                    }

                                    transaccion13.commit();
                                } catch (Exception e) {
                                    monitor_existente = null;
                                    System.out.println("NO SE PUEDE ENCONTRAR AL MONITOR POR EL DNI");
                                }
                            } catch (Exception e) {
                                transaccion13.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 13 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 14:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion14 = sesion1.beginTransaction();

                            try {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("Introduce el DNI del socio : ");
                                String DNI = scanner.nextLine();

                                //obtener el socio por el dni 
                                Query consulta14 = sesion1.createNativeQuery("SELECT * FROM SOCIO WHERE DNI = ?", Socio.class);
                                consulta14.setParameter(1, DNI);

                                Socio socio_encontrado = (Socio) consulta14.getSingleResult();

                                Set<Actividad> actividades = socio_encontrado.getActividadSet();

                                if (actividades.isEmpty()) {
                                    System.out.println("EL SOCIO NO ESTA INSCRITO EN NINGUNA ACTIVIDAD");
                                } else {
                                    System.out.println("-----ACTIVIDADES DEL SOCIO " + socio_encontrado.getNombre()
                                            + " DNI : " + socio_encontrado.getDni() + "-----");

                                    for (Actividad actividad_socio : actividades) {
                                        System.out.println(" ---> " + actividad_socio.getNombre()
                                                + " | Día: " + actividad_socio.getDia()
                                                + " | Hora: " + actividad_socio.getHora()
                                                + " | Precio: " + actividad_socio.getPrecioBaseMes() + "€/mes\n");
                                    }
                                }

                                transaccion14.commit();
                            } catch (Exception e) {
                                transaccion14.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 14 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 15:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion15 = sesion1.beginTransaction();

                            try {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("Introduce el nombre de una actividad: ");
                                String nombre_actividad = scanner.nextLine();

                                //obtener el socio por el dni 
                                Query consulta15 = sesion1.createNativeQuery("SELECT * FROM ACTIVIDAD WHERE NOMBRE = ?", Actividad.class);
                                consulta15.setParameter(1, nombre_actividad);

                                Actividad actividad_encontrada = (Actividad) consulta15.getSingleResult();

                                Set<Socio> socio = actividad_encontrada.getSocioSet();

                                if (socio.isEmpty()) {
                                    System.out.println("NO SE HA ENCONTRADO NINGUNA ACTIVIDAD CON ESE NOMBRE");
                                } else {
                                    System.out.println("-----INFORMACION DEL SOCIO INSCRITO EN LA ACTIVIDAD " + actividad_encontrada.getNombre() + "-----");

                                    for (Socio socioSet : socio) {
                                        System.out.println(" Nombre : " + socioSet.getNombre()
                                                + " | Telefono: " + socioSet.getTelefono()
                                                + " | DNI: " + socioSet.getDni()
                                                + " | Email: " + socioSet.getCorreo());
                                    }
                                }

                                transaccion15.commit();
                            } catch (Exception e) {
                                transaccion15.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 15 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 16:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion16 = sesion1.beginTransaction();

                            try {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("Introduce el numero del socio (SXXX): ");
                                String numero_socio = scanner.nextLine();

                                System.out.println("Introduce el numero de la actividad para apuntar al socio (ACXX): ");
                                String num_actividad_a_apuntarse = scanner.nextLine();

                                Query consulta16 = sesion1.createNativeQuery("SELECT * FROM SOCIO WHERE NUMEROSOCIO = ?", Socio.class);
                                consulta16.setParameter(1, numero_socio);

                                Query consulta16_1 = sesion1.createNativeQuery("SELECT * FROM ACTIVIDAD WHERE IDACTIVIDAD = ?", Actividad.class);
                                consulta16_1.setParameter(1, num_actividad_a_apuntarse);

                                Socio socioEncontrado = (Socio) consulta16.getSingleResult();
                                Actividad actividadEncontrada = (Actividad) consulta16_1.getSingleResult();

                                if (socioEncontrado == null || actividadEncontrada == null) {
                                    System.out.println("VALORES DE BUSQUEDA ERRONEOS");
                                    transaccion16.rollback();
                                } else {
                                    Set<Actividad> actividades = new HashSet<>();
                                    actividades = socioEncontrado.getActividadSet();
                                    Set<Socio> socios = new HashSet<>();
                                    socios = actividadEncontrada.getSocioSet();

                                    if (actividades.contains(actividadEncontrada) == true && socios.contains(socioEncontrado) == true) {
                                        System.out.println("EL SOCIO YA ESTA INSCRITO EN ESA ACTIVIDAD ");
                                    } else {
                                        actividades.add(actividadEncontrada);
                                        socios.add(socioEncontrado);

                                        System.out.println("Socio " + socioEncontrado.getNombre()
                                                + " inscrito correctamente en la actividad "
                                                + actividadEncontrada.getNombre());
                                    }
                                }
                                transaccion16.commit();
                            } catch (Exception e) {
                                transaccion16.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 16 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 17:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion17 = sesion1.beginTransaction();

                            try {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("Introduce el numero del socio (SXXX): ");
                                String numero_socio = scanner.nextLine();

                                System.out.println("Introduce el numero de la actividad para dar de baja al socio (ACXX): ");
                                String num_actividad_a_apuntarse = scanner.nextLine();

                                Query consulta17 = sesion1.createNativeQuery("SELECT * FROM SOCIO WHERE NUMEROSOCIO = ?", Socio.class);
                                consulta17.setParameter(1, numero_socio);

                                Query consulta17_1 = sesion1.createNativeQuery("SELECT * FROM ACTIVIDAD WHERE IDACTIVIDAD = ?", Actividad.class);
                                consulta17_1.setParameter(1, num_actividad_a_apuntarse);

                                Socio socioEncontrado = (Socio) consulta17.getSingleResult();
                                Actividad actividadEncontrada = (Actividad) consulta17_1.getSingleResult();

                                if (socioEncontrado == null || actividadEncontrada == null) {
                                    System.out.println("VALORES DE BUSQUEDA ERRONEOS");
                                    transaccion17.rollback();
                                } else {
                                    Set<Actividad> actividades = new HashSet<>();
                                    actividades = socioEncontrado.getActividadSet();
                                    Set<Socio> socios = new HashSet<>();
                                    socios = actividadEncontrada.getSocioSet();

                                    if (actividades.contains(actividadEncontrada) == true && socios.contains(socioEncontrado) == true) {
                                        actividades.remove(actividadEncontrada);
                                        socios.remove(socioEncontrado);

                                        System.out.println("Socio " + socioEncontrado.getNombre()
                                                + " dado de baja correctamente de la actividad "
                                                + actividadEncontrada.getNombre());

                                    } else {
                                        System.out.println("NO SE ENCUENTRA UN SOCIO INSCRITO EN ESA ACTIVIDAD");
                                        transaccion17.rollback();
                                    }
                                }
                                transaccion17.commit();
                            } catch (Exception e) {
                                transaccion17.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 17 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 18:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion18 = sesion1.beginTransaction();

                            try {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("Introduce el DNI del monitor: ");
                                String dniMonitor = scanner.nextLine();

                                Query consulta18 = sesion1.createNativeQuery("SELECT * FROM MONITOR WHERE DNI = ?", Monitor.class);
                                consulta18.setParameter(1, dniMonitor);

                                Monitor monitorEncontrado = (Monitor) consulta18.getSingleResult();

                                if (monitorEncontrado != null) {

                                    Query consulta18_1 = sesion1.createNativeQuery("SELECT * FROM ACTIVIDAD WHERE MONITORRESPONSABLE = ?", Actividad.class);
                                    consulta18_1.setParameter(1, monitorEncontrado.getCodMonitor());

                                    lista_actividades = consulta18_1.getResultList();

                                    System.out.println("NOMBRE DEL MONITOR: " + monitorEncontrado.getNombre());
                                    System.out.println("-----------------------");
                                    for (int i = 0; i < lista_actividades.size(); i++) {
                                        System.out.println("ACTIVIDAD: " + lista_actividades.get(i).getNombre()
                                                + " DIA: " + lista_actividades.get(i).getDia()
                                                + " HORA: " + lista_actividades.get(i).getHora());
                                    }
                                } else {
                                    transaccion18.rollback();
                                }
                                transaccion18.commit();
                            } catch (Exception e) {
                                transaccion18.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 18 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 19:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion19 = sesion1.beginTransaction();

                            try {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("Introduce el numero del socio (SXXX): ");
                                String numero_socio = scanner.nextLine();

                                Query consulta19 = sesion1.createNativeQuery("SELECT * FROM SOCIO WHERE NUMEROSOCIO = ?", Socio.class);
                                consulta19.setParameter(1, numero_socio);

                                Socio socioEncontrado = (Socio) consulta19.getSingleResult();

                                if (socioEncontrado != null) {
                                    Set<Actividad> actividadSet = new HashSet<>();
                                    actividadSet = socioEncontrado.getActividadSet();

                                    int precioTotal = 0;

                                    for (Actividad actividad_socio : actividadSet) {
                                        System.out.println(" NOMBRE ACTIVIDAD: " + actividad_socio.getNombre()
                                                + " PRECIO MES: " + actividad_socio.getPrecioBaseMes());

                                        precioTotal = precioTotal + actividad_socio.getPrecioBaseMes();
                                    }

                                    System.out.println("-----------------------");
                                    System.out.println("PRECIO TOTAL : " + precioTotal + " $ ");

                                }
                                transaccion19.commit();
                            } catch (Exception e) {
                                transaccion19.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 19 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 20:
                            sesion1 = sessionFactory.openSession();
                            Transaction transaccion20 = sesion1.beginTransaction();

                            try {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("Introduce una edad: ");
                                int edad = scanner.nextInt();
                                int Birth_day;
                                String fechaNac;
                                LocalDate fechaActual = LocalDate.now();
                                int anioActual = fechaActual.getYear(), edadCalculada;

                                Query consulta20 = sesion1.createNativeQuery("SELECT * FROM SOCIO", Socio.class);
                                Lista_socios1 = consulta20.getResultList();

                                for (int i = 0; i < Lista_socios1.size(); i++) {
                                    if (Lista_socios1.get(i).getFechaNacimiento() != null) {
                                        fechaNac = Lista_socios1.get(i).getFechaNacimiento();
                                        Birth_day = Integer.parseInt(fechaNac.substring(6, 10));

                                        edadCalculada = anioActual - Birth_day;

                                        if (edadCalculada > edad) {
                                            System.out.println(" Nombre : " + Lista_socios1.get(i).getNombre()
                                                    + " | Telefono: " + Lista_socios1.get(i).getTelefono()
                                                    + " | DNI: " + Lista_socios1.get(i).getDni()
                                                    + " | Email: " + Lista_socios1.get(i).getCorreo());
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                transaccion20.rollback();
                                System.out.println("ERROR EN LA TRANSACCION 20 --> " + e.getMessage());
                            } finally {
                                if (sesion1 != null && sesion1.isOpen()) {
                                    sesion1.close();
                                }
                            }
                            break;

                        case 0:
                            salir = true;
                            break;
                        default:
                            throw new IllegalArgumentException("VALOR ERRONEO");
                    }
                } catch (Exception e) {
                }
            } while (salir == false);
        } catch (ExceptionInInitializerError e) {
            Throwable cause = e.getCause();
            System.out.println("Error en la conexión. Revise el fichero .cfg.xml: " + cause.getMessage());
        }
    }
}
