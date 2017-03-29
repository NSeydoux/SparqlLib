package fr.irit.sparql.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import fr.irit.sparql.exceptions.NotAFolderException;

/**
 * This class helps managing a folder of files containing SPARQL queries
 */
public class FolderManager {
	File folder;
	Map<String, String> queries;
	
	public FolderManager(String path) throws NotAFolderException{
		this.folder = new File(path);
		if(!this.folder.isDirectory()){
			throw new NotAFolderException(path+" is not a folder");
		}
		this.queries = new HashMap<String, String>();
	}
	
	public void loadQueries(){
		for (File fileEntry : folder.listFiles()) {
	        if (!fileEntry.isDirectory() && fileEntry.getName().endsWith(".sparql")) {
	        	try {
		        	String query = new String(Files.readAllBytes(Paths.get(fileEntry.getPath())));
		        	// The file name without the extension is used as the key
		        	queries.put(fileEntry.getName().split("\\.")[0], query);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    }
	}
	
	public Map<String, String> getQueries(){
		return this.queries;
	}
}
