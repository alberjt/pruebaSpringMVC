package com.votaciones.services;

import java.util.List;

import com.votaciones.model.Reclamacion;

public interface ReclamacionesService {

	Reclamacion add(Reclamacion reclamacion);

	List<Reclamacion> getAll();

}
