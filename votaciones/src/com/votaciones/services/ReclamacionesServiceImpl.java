package com.votaciones.services;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

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
	public Reclamacion get(Integer id) {
		Session session = factory.openSession();
		session.beginTransaction();
		
		Reclamacion reclamacion =  (Reclamacion) session.get(Reclamacion.class, id);
		
		session.getTransaction().commit();
		
		session.close(); 
		return reclamacion;
	}

	@Override
	public List<Reclamacion> getAll() {
		
		Session session = factory.openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Reclamacion> criteria = builder.createQuery(Reclamacion.class);
		criteria.from(Reclamacion.class);
				
		List<Reclamacion> reclamaciones = session.createQuery(criteria).getResultList();
		
		session.close(); 
		return reclamaciones;
	}

}
