package com.company.dao.inter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;

public class AbstractDAO {

    public static Connection connect() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/resume";
        String username = "root";
        String password = "12345";
        Connection c = DriverManager.getConnection(url, username, password);

        return c;
    }

    private static EntityManagerFactory emf = null;

    public static EntityManager em() {
        if (emf == null) {

            emf = Persistence.createEntityManagerFactory("resumeappPU");
        }
        EntityManager entityManager = emf.createEntityManager();

        return entityManager;
    }
    
    public static void closeEmf(){
        emf.close();
    }

}
