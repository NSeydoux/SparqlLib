package fr.irit.sparql.query;

import java.util.ArrayList;

import fr.irit.utils.Pair;


/**
 * This class allow easy management, preparation and execution of sparql
 * queries.
 */
public abstract class SparqlQuery
{
	private ArrayList<Pair<String, String>> prefix;
	protected String from;
	protected String where;

	public SparqlQuery(ArrayList<Pair<String, String>> prefix, String from, String where)
	{
		this.prefix = new ArrayList<>();
		this.prefix.add(new Pair<String, String>("rdf", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"));
		this.prefix.add(new Pair<String, String>("rdfs", "<http://www.w3.org/2000/01/rdf-schema#>"));
		this.prefix.add(new Pair<String, String>("owl", "<http://www.w3.org/2002/07/owl#>"));
		this.prefix.add(new Pair<String, String>("xsd", "<http://www.w3.org/2001/XMLSchema#>"));
		this.prefix.addAll(prefix);
		this.from = from;
		this.where = where;
	}
	
	public ArrayList<Pair<String, String>> getPrefix()
	{
		return prefix;
	}

	public void setPrefix(ArrayList<Pair<String, String>> prefix)
	{
		this.prefix = prefix;
	}
	
	public void addPrefix(String prefix, String fullName)
	{
		this.prefix.add(new Pair<String, String>(prefix, fullName));
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
		for(Pair<String, String> p : this.getPrefix())
		{
			prefixes += "PREFIX "+p.getFirst()+": "+p.getSecond()+"\n";
		}
		return prefixes;
	}
}
