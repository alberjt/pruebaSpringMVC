package com.votaciones.services;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Service;

import com.votaciones.model.Reclamacion;

@Service
public class ReclamacionesServiceImpl implements ReclamacionesService {
	
	private static SessionFactory factory;
	
	public ReclamacionesServiceImpl() {
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml")
				.build();
		Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
		factory = metaData.getSessionFactoryBuilder().build();
	}

	@Override
	public Reclamacion add(Reclamacion reclamacion) {
		Session session = factory.openSession();
		session.beginTransaction();
		
		session.save(reclamacion);
		session.getTransaction().commit();
		
		session.close(); 
		return reclamacion;
	}

	@Override
	public List<Reclamacion> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
