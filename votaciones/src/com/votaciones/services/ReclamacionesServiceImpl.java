package com.votaciones.services;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.http.HttpStatus;
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
	public HttpStatus add(Reclamacion reclamacion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reclamacion> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
