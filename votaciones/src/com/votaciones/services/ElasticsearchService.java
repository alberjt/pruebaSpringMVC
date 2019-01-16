package com.votaciones.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.votaciones.model.Reclamacion;

@Service
public class ElasticsearchService { 
    private final static String ATTACHMENT = "reclamaciones_attachment"; 
    private static final Logger logger = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

    static RestHighLevelClient  restClient;
    
    public ElasticsearchService(){
    	conectToElasticsearch();
    }
    
	public static void conectToElasticsearch(){
		restClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")));
		
	}
    
    /**
     * PARA QUE FUNCIONE HAY QUE CREAR EL INDICE ANTES DESDE LA CONSOLA DE ELASTICSEARCH
     * 	PUT reclamaciones_attachment
		{
		    "settings" : {
		        "index" : {
		            "number_of_shards" : 3, 
		            "number_of_replicas" : 2 
		        }
		    }
		}
		
		PUT /_ingest/pipeline/reclamaciones_attachment?pretty
		{
		
		 "description" : "Extract attachment information encoded in Base64 with UTF-8 charset",
		
		 "processors" : [
		
		   {
		
		     "attachment" : {
		
		       "field" : "data"
		
		     }
		
		   }
		
		 ]
		
		}
     * @param reclamacionId
     * @param file
     * @param fileName
     */
	public void saveReclamacionAttachment(Integer reclamacionId, MultipartFile file, String fileName){
		
		
	    Map<String, Object> jsonMap ;
	
	    String encodedfile = null;

        try {
            byte[] bytes = file.getBytes();
            encodedfile = new String(Base64.getEncoder().encodeToString(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
	    jsonMap = new HashMap<String, Object>();
	    jsonMap.put("id", reclamacionId);
	    jsonMap.put("data", encodedfile); // inserting null here when file is not available and it is not able to encoded.
	    jsonMap.put("name", fileName);
	    
	    IndexRequest request = new IndexRequest(ATTACHMENT, "test_type", Long.toString(reclamacionId))
	            .source(jsonMap)
	            .setPipeline(ATTACHMENT);

        try {
			IndexResponse response = restClient.index(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

/*
        try {
			restClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    logger.info("Indexing done.....");
	
	}
	
	public List<Reclamacion> searchInReclamacionesAttachment(String texto){
		
		
		List<Reclamacion> reclamaciones = new ArrayList<Reclamacion>();
		
		SearchRequest contentSearchRequest = new SearchRequest(ATTACHMENT); 
	    SearchSourceBuilder contentSearchSourceBuilder = new SearchSourceBuilder();
	    QueryStringQueryBuilder attachmentQB = new QueryStringQueryBuilder(texto); 
	    attachmentQB.defaultField("attachment.content");
	    contentSearchSourceBuilder.query(attachmentQB);
	    contentSearchSourceBuilder.size(50);
	    contentSearchRequest.source(contentSearchSourceBuilder);
	    SearchResponse contentSearchResponse = null;

	    try {
	        contentSearchResponse = restClient.search(contentSearchRequest); // returning null response
	    } catch (IOException e) {
	        e.getLocalizedMessage();
	    }

	    JSONArray hitsArray;
	    JSONObject json = new JSONObject(contentSearchResponse);
		JSONObject hits = json.getJSONObject("hits");
		hitsArray = hits.getJSONArray("hits");
		JSONObject h;
		JSONObject sourceJObj;
		Reclamacion reclamacion;
		String id;
		//String data;
		String nombre;
		for (int i=0; i<hitsArray.length(); i++) {
			h = hitsArray.getJSONObject(i);
			
			sourceJObj = h.getJSONObject("sourceAsMap");
			nombre = (String)sourceJObj.get("name");
			id = (String) h.get("id");
			
			reclamacion = new Reclamacion();
			reclamacion.setId(Integer.valueOf(id));
			reclamacion.setFichero(nombre);
			
			reclamaciones.add(reclamacion);
		}
		
		return reclamaciones;
	}
	
}
