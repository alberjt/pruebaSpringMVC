package com.votaciones.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.votaciones.model.Reclamacion;
import com.votaciones.services.ReclamacionesService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReclamacionesController {
	
	private static String PATH = "C:\\Iberdrola\\ficheros";
	
	@Resource
	private ReclamacionesService reclamacionesService;

	
	@PostMapping("/saveReclamacion")
	public ResponseEntity<String> saveReclamacion(@RequestParam(value = "file", required = false) MultipartFile file, 
			@RequestParam("reclamacion") String reclamacionParam) throws IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		Reclamacion reclamacion = null;
		String nombreFichero = null;

		reclamacion = objectMapper.readValue(reclamacionParam, Reclamacion.class);
		if (file != null) {
			if (!Files.exists(Paths.get(PATH))) {
				Files.createDirectory(Paths.get(PATH));
			}
			nombreFichero = new String (
					file.getOriginalFilename().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
					+ "_" + RandomStringUtils.random(4, true, false)
					+ "." + FilenameUtils.getExtension(file.getOriginalFilename());
			Files.copy(file.getInputStream(), Paths.get(PATH).resolve(nombreFichero));
			reclamacion.setFichero(nombreFichero);
		}
		
		return new ResponseEntity<String>(reclamacionesService.add(reclamacion));
	}

}
