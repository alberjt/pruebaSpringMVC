package com.votaciones.controller;
 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.votaciones.model.Votacion;
import com.votaciones.services.VotacionesService; 

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class VotacionesController {
 
	@Resource
    private VotacionesService votacionesService;
	
	 @PostMapping
	 @RequestMapping("/saveVotacion")
	 public ResponseEntity<?> saveVotacion(@RequestBody Votacion votacion){
		 return new ResponseEntity<String>(votacionesService.add(votacion));
	 }
	
	 @RequestMapping("/welcome")
	public ModelAndView helloWorld() throws SQLException {
 
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hola y tal, a Spring MVC **********</div><br><br>";
		
		
		DataSource ds = null;  
		Connection con = null;  
		Statement stmt = null;  
		InitialContext ic;  
	              
        try {
      		ic = new InitialContext();
      		ds = (DataSource) ic.lookup("java:jboss/postgresDS");
      		con = ds.getConnection();
      		stmt = con.createStatement();
      		ResultSet rs = stmt.executeQuery("select * from votaciones");

      		while (rs.next()) {
      			message += "<br> " + rs.getString("enlace") + " | " + rs.getString("votacion");
      		}
      		rs.close();
      		stmt.close();
      	} catch (Exception e) {
      		e.printStackTrace();
      	} finally {
      		if (con != null) {
      			con.close();
      		}
      	}
        
        return new ModelAndView("welcome", "message", message);
	}
}