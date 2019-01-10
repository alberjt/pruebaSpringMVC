package com.votaciones.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.votaciones.model.Votacion;

public interface VotacionesService {

	Votacion add(Votacion votacion);
	
	UUID delete(Votacion votacion);

	List<Votacion> getAll();
	
	HttpStatus update(Votacion votacion);

}
