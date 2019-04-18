package com.ajay.config;


import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ajay.doa.LoanDoaImplementation;
import com.ajay.doa.MySessionFactoryBean;


@Configuration
@ComponentScan(basePackages = {"com.ajay.entities","com.ajay.doa"})
@PropertySource(value = { "classpath:db_props.prop" })
@EnableTransactionManagement
@EnableJpaRepositories
public class AppConfig {
	@Autowired
	private DataSource datasource;
	
	@Autowired
	private SessionFactory sessionFactory;
	// create database connection
	@Bean("datasource")
	public DataSource getDataSource() {
		Properties prop = Environment.getProperties();
		BasicDataSource source = new BasicDataSource();
		source.setUrl("jdbc:sqlserver://172.16.0.33\\SQLPROD");
		source.setUsername("sa");
		source.setPassword("sgrh@2016");
		source.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		return source;
	}
	
	// get hibernate session factroy
	@Bean("factory")
	@DependsOn("datasource")
	public SessionFactory getFactoryBean() {
		System.out.println("Session Factory Called.");
		MySessionFactoryBean bean = new MySessionFactoryBean();
		bean.setPackagesToScan("com.ajay.entities");
		bean.setDataSource(datasource);
		Properties prop = new Properties();
		//prop.setProperty("hibernate.show_sql", "true");
		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
		bean.setHibernateProperties(prop);
		bean.init();
		return bean.getObject();
	}
	
	@Bean("transaction")
	@DependsOn("factory")
	public PlatformTransactionManager getTransactionManager() {
		HibernateTransactionManager manager = new HibernateTransactionManager();
		manager.setSessionFactory(sessionFactory);
		//manager.setDataSource(getDataSource());
		return manager;
	}
	
	@Bean("doa")
	public LoanDoaImplementation getDoa() {
		return new LoanDoaImplementation(this.sessionFactory);
	}
}
