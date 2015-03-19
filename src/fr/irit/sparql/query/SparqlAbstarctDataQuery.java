package fr.irit.sparql.query;

import java.util.Map;
import java.util.Set;

public abstract class SparqlAbstarctDataQuery extends SparqlQuery
{
	private String data;
	protected String keyword;
	
	public SparqlAbstarctDataQuery(Set<Map.Entry<String, String>> prefix, String data)
	{
		super(prefix, "", "");
		this.data = data;
	}
	
	public String toString()
	{
		return this.formatPrefixes() + this.keyword+ "{"+data+"}";
	}
}
