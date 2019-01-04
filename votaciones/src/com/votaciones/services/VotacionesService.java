package com.votaciones.services;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.votaciones.model.Votacion;

public interface VotacionesService {

	HttpStatus add(Votacion votacion);
	
	HttpStatus delete(Votacion votacion);

	List<Votacion> getAll();

	HttpStatus update(Votacion votacion);

}
