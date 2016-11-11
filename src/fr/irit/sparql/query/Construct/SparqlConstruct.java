package fr.irit.sparql.query.Construct;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.irit.sparql.query.SparqlQuery;

public class SparqlConstruct extends SparqlQuery{
	private String construct;
	
	public SparqlConstruct(Set<Map.Entry<String, String>> prefix, String from, String construct, String where) {
		super(prefix, from, where);
		this.construct = construct;
	}
	
	public SparqlConstruct(Set<Map.Entry<String, String>> prefix, String construct, String where) {
		super(prefix, "", where);
		this.construct = construct;
	}
	
	public SparqlConstruct(String construct, String where) {
		super(new HashSet<Map.Entry<String, String>>(), "", where);
		this.construct = construct;
	}
	
	public String toString()
	{
		
		return    this.formatPrefixes()
				+ "CONSTRUCT {\n"+this.construct+"}\n"
				+ (this.getFrom().equals("")?"":" FROM "+this.getFrom()+"\n")
				+ " WHERE {\n"+this.getWhere()+"}\n";
	}
}
