package com.votaciones.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.votaciones.model.Reclamacion;
import com.votaciones.services.ReclamacionesService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReclamacionesController {
	
	@Resource
	private ReclamacionesService reclamacionesService;
	
	@PostMapping
	@RequestMapping("/saveReclamacion")
	public ResponseEntity<String> saveVotacion(@RequestBody Reclamacion reclamacion){
		return null;
	}

	@PostMapping("/pruebaFichero")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, 
			RedirectAttributes redirectAttributes) {
		 
		try {
			Files.copy(file.getInputStream(), Paths.get("C:\\Iberdrola").resolve(file.getOriginalFilename()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return null;
	}

}