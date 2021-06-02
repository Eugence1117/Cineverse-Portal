package com.ms.config;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class contextListener implements ServletContextListener{

	public static Logger log = LogManager.getLogger(contextListener.class);
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
	    // Loop through all drivers
	    Enumeration<Driver> drivers = DriverManager.getDrivers();
	    while (drivers.hasMoreElements()) {
	        Driver driver = drivers.nextElement();
	        if (driver.getClass().getClassLoader() == cl) {
	            // This driver was registered by the webapp's ClassLoader, so deregister it:
	            try {
	                log.info("Deregistering JDBC driver {}", driver);
	                DriverManager.deregisterDriver(driver);
	            } catch (SQLException ex) {
	                log.error("Error deregistering JDBC driver {}", driver, ex);
	            }
	        } else {
	            // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
	            log.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader", driver);
	        }
	    }
	}

}
