package app;
import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;

import util.HibernateUtil;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Initializing Hibernate...");
        HibernateUtil.getSessionFactory(); 
        System.out.println("Hibernate initialized");
        
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8085);


        String projectRoot = new File(".").getCanonicalPath();
        String docBase = new File(projectRoot, "src/main/webapp").getAbsolutePath();
        
        Context context = tomcat.addContext("", docBase);
        context.addWelcomeFile("/displayBooks.html");

        Tomcat.addServlet(context, "default", new DefaultServlet());
        context.addServletMappingDecoded("/*", "default");

        context.setResources(new org.apache.catalina.webresources.StandardRoot(context));

        tomcat.addServlet("", "BookServlet", "controller.BookServlet");
        context.addServletMappingDecoded("/books/*", "BookServlet");

       
        
        tomcat.getConnector(); 
        
        System.out.println("Starting Tomcat...");
        tomcat.start();
        System.out.println("Tomcat started on http://localhost:8085");
        tomcat.getServer().await();
    }
}