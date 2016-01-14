package fr.irit.sparql.query;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * This class allow easy management, preparation and execution of sparql
 * queries.
 */
public abstract class SparqlQuery
{
	private Set<Map.Entry<String, String>> prefix;
	protected String from;
	protected String where;

	public SparqlQuery(Set<Map.Entry<String, String>> prefix, String from, String where)
	{
		this.prefix = new HashSet<Map.Entry<String,String>>();
		this.prefix.add(new AbstractMap.SimpleEntry<String, String>("rdf", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"));
		this.prefix.add(new AbstractMap.SimpleEntry<String, String>("rdfs", "<http://www.w3.org/2000/01/rdf-schema#>"));
		this.prefix.add(new AbstractMap.SimpleEntry<String, String>("owl", "<http://www.w3.org/2002/07/owl#>"));
		this.prefix.add(new AbstractMap.SimpleEntry<String, String>("xsd", "<http://www.w3.org/2001/XMLSchema#>"));
		if(prefix != null && !prefix.isEmpty())
		{
			this.prefix.addAll(prefix);
		}
		this.from = from;
		this.where = where;
	}
	
	public Set<Map.Entry<String, String>> getPrefix()
	{
		return prefix;
	}

	public void setPrefix(Set<Map.Entry<String, String>> prefix)
	{
		this.prefix = prefix;
	}
	
	public void addPrefix(String prefix, String fullName)
	{
		this.prefix.add(new AbstractMap.SimpleEntry<String, String>(prefix, fullName));
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}
	
	public String getWhere()
	{
		return where;
	}

	public void setWhere(String where)
	{
		this.where = where;
	}
	
	public String formatPrefixes()
	{
		String prefixes = "";
		for(Entry<String, String> p : this.getPrefix())
		{
			prefixes += "PREFIX "+p.getKey()+": "+p.getValue()+"\n";
		}
		return prefixes;
	}
}
