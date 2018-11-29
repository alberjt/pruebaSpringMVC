package com.votaciones.model;

public class Votacion {
	
	private int id;
	private String enlace;
	private int numVotos;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEnlace() {
		return enlace;
	}
	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}
	public int getNumVotos() {
		return numVotos;
	}
	public void setNumVotos(int numVotos) {
		this.numVotos = numVotos;
	}
}