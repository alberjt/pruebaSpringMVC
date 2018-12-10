package com.votaciones.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.votaciones.model.Votacion;

@Service
public class VotacionesServiceImpl implements VotacionesService{
	
	public HttpStatus add(Votacion votacion) {
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml")
				.build();
		Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
		SessionFactory sessionFactory = metaData.getSessionFactoryBuilder().build();

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(votacion);
		session.getTransaction().commit();
		
		session.close(); 
		sessionFactory.close();
		return HttpStatus.OK;
	}
}