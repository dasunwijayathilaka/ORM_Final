package lk.ijse.config;

import lk.ijse.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class SessionFactoryConfiguration {
    private static SessionFactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;
    private SessionFactoryConfiguration() throws IOException {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.properties"));
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Student.class);
        configuration.addAnnotatedClass(Program.class);
        configuration.addAnnotatedClass(Registration.class);
        configuration.addAnnotatedClass(Payment.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static SessionFactoryConfiguration getInstance() throws IOException {
        return (factoryConfiguration==null) ? factoryConfiguration=new SessionFactoryConfiguration():factoryConfiguration;
    }

    public Session getSession(){
        return sessionFactory.openSession();
    }
}