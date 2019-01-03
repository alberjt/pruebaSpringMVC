package com.votaciones.services;

import java.util.List;

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
	
	private static SessionFactory factory;
	
	public VotacionesServiceImpl() {
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml")
				.build();
		Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
		factory = metaData.getSessionFactoryBuilder().build();

	}
	
	public HttpStatus add(Votacion votacion) {
		Session session = factory.openSession();
		session.beginTransaction();
		
		session.save(votacion);
		session.getTransaction().commit();
		
		session.close(); 
		return HttpStatus.OK;
	}
	
	@SuppressWarnings("unchecked")
	public List<Votacion> getAll() {
		
		Session session = factory.openSession();
		List<Votacion> votaciones = (List<Votacion>) session.createQuery("FROM Votacion").list();
		
		session.close(); 
		
		return votaciones;
	}
	
	
	public HttpStatus update(Votacion votacion) {
		
		Session session = factory.openSession();
		session.beginTransaction();
		
		session.update(votacion);
		session.getTransaction().commit();
		
		session.close(); 
		return HttpStatus.OK;
	}
}