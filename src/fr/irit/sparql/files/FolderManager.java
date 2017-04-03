package fr.irit.sparql.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import fr.irit.sparql.exceptions.NotAFolderException;

/**
 * This class helps managing a folder of files containing SPARQL queries. These queries can be completely 
 * determined, or they can be templates that need instantiation before being ran {@link QueryTemplate}.
 */
public class FolderManager {
	File folder;
	// The queries are organized in two different maps, because their management
	// is quite different.
	Map<String, String> queries;
	Map<String, QueryTemplate> queryTemplates;
	
	public FolderManager(String path) throws NotAFolderException{
		this.folder = new File(path);
		if(!this.folder.isDirectory()){
			throw new NotAFolderException(path+" is not a folder");
		}
		this.queries = new HashMap<String, String>();
		this.queryTemplates = new HashMap<String, QueryTemplate>();
	}
	
	public void loadQueries(){
		for (File fileEntry : folder.listFiles()) {
	        if (!fileEntry.isDirectory() && fileEntry.getName().endsWith(".sparql")) {
	        	try {
		        	String query = new String(Files.readAllBytes(Paths.get(fileEntry.getPath())));
		        	// The file name without the extension is used as the key
		        	// If the query contains {{ ... }}, it is a template, otherwise it is a regular query
		        	if(query.replaceAll("\n", " ").matches("^.*\\{\\{ .* \\}\\}.*$")){
		        		queryTemplates.put(fileEntry.getName().split("\\.")[0], new QueryTemplate(query));
		        	} else {
		        		System.out.println(query+" is not a template");
		        		queries.put(fileEntry.getName().split("\\.")[0], query);
		        	}
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    }
	}
	
	public Map<String, String> getQueries(){
		return this.queries;
	}
	
	public Map<String, QueryTemplate> getTemplateQueries(){
		return this.queryTemplates;
	}
}
