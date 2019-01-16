package com.votaciones.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.votaciones.model.Reclamacion;
import com.votaciones.services.ElasticsearchService;
import com.votaciones.services.ReclamacionesService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReclamacionesController {
	
	private static String PATH = "C:\\Iberdrola\\ficheros";
	
	@Resource
	private ReclamacionesService reclamacionesService;
	@Resource
	private ElasticsearchService elasticsearchService;

	
	@PostMapping("/saveReclamacion")
	public ResponseEntity<Reclamacion> saveReclamacion(@RequestParam(value = "file", required = false) MultipartFile file, 
			@RequestParam("reclamacion") String reclamacionParam) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Reclamacion reclamacion = null;
		String nombreFichero = null;

		reclamacion = objectMapper.readValue(reclamacionParam, Reclamacion.class);
		if (file != null) {
			if (!Files.exists(Paths.get(PATH))) {
				Files.createDirectory(Paths.get(PATH));
			}
			
			nombreFichero = new String (
					FilenameUtils.getName(
							new String(file.getOriginalFilename()
									.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)))
					+ "_" + RandomStringUtils.random(4, true, false)
					+ "." + FilenameUtils.getExtension(file.getOriginalFilename());
			Files.copy(file.getInputStream(), Paths.get(PATH).resolve(nombreFichero));
			reclamacion.setFichero(nombreFichero);
			
			//Guardamos el adjunto en elasticsearch
			//elasticsearchService.saveReclamacionAttachment(reclamacion.getId(), file);
			
		}
		
		return new ResponseEntity<Reclamacion>(reclamacionesService.add(reclamacion), HttpStatus.OK);
	}
	
	@GetMapping
	@RequestMapping("/searchInReclamacionesAttachment")
	public ResponseEntity<List<Reclamacion>>  searchInAttachment(@RequestParam String textoBusqueda){
		
		return new ResponseEntity<List<Reclamacion>>(elasticsearchService.searchInReclamacionesAttachment(textoBusqueda), HttpStatus.OK);
	}
	
	@GetMapping
	@RequestMapping("/obtenerReclamaciones")
	public ResponseEntity<List<Reclamacion>> getReclamaciones() {
		return new ResponseEntity<List<Reclamacion>>(reclamacionesService.getAll(), HttpStatus.OK);
	}
	 
	@GetMapping
	@RequestMapping("/reclamacion/{id}/fichero")
	public ResponseEntity<InputStreamResource> getFichero(@PathVariable("id") String id) throws FileNotFoundException{
		Reclamacion reclamacion = reclamacionesService.get(Integer.valueOf(id));
		File file = new File(Paths.get(PATH).resolve(reclamacion.getFichero()).toUri());
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		String nombreBbDd = FilenameUtils.getName(reclamacion.getFichero());
		String nombre = nombreBbDd.substring(0, nombreBbDd.length() - 5) + "." 
				+ FilenameUtils.getExtension(reclamacion.getFichero());
		 
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" + nombre)
	            .contentLength(file.length())
	            .body(resource);
	}

}