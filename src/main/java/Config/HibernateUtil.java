package Config;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


/**
 * CLASE UTILITARIA PARA LA CONFIGURACION Y GESTION DE HIBERNATE
 * PROPORCIONA FUNCIONALIDADES PARA LA GESTION COMPLETA DEL SISTEMA DE GIMNASIO
 * 
 * @author SISTEMA DE GESTION DE GIMNASIO
 * @version 1.0
 */
public class HibernateUtil {

    /** ATRIBUTO SESSIONFACTORY */
    private static SessionFactory sessionFactory;
    /** ATRIBUTO SERVICEREGISTRY */
    private static StandardServiceRegistry serviceRegistry;

    
    /**
     * METODO BUILDSESSIONFACTORY
     *
     * @param user PARAMETRO USER
     * @param pass PARAMETRO PASS
     * @return RETORNA STATIC SESSIONFACTORY
     */
    public static SessionFactory buildSessionFactory(String user, String pass) {
        try {
            serviceRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .applySetting("hibernate.connection.username", user)
                    .applySetting("hibernate.connection.password", pass)
                    .applySetting("hibernate.connection.url",
                            "jdbc:mariadb://172.18.1.241:3306/" + user).build();
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
            System.out.println("ME HE CREADO CORRECTAMENTE!!");
            return sessionFactory;
        } catch (Exception e) {
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
                serviceRegistry = null;
            }
            sessionFactory = null;
            return null;
        }
    }

    
    /**
     * OBTIENE EL VALOR DEL CAMPO SESSIONFACTORY
     *
     * @return RETORNA STATIC SESSIONFACTORY
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("La SessionFactory aún no está inicializada. "
                    + "Debe llamar al método buildSessionFactory() primero.");
        }
        return sessionFactory;
    }

    
    /**
     * METODO CLOSE
     *
     * @return RETORNA STATIC VOID
     */
    public static void close() {
        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        } finally {
            sessionFactory = null;
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
                serviceRegistry = null;
            }
        }
    }

}
