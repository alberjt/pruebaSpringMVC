package com.votaciones.services;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.votaciones.model.Reclamacion;

public interface ReclamacionesService {

	HttpStatus add(Reclamacion reclamacion);

	List<Reclamacion> getAll();

}
