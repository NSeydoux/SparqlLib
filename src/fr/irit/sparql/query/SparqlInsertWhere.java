package fr.irit.sparql.query;

import java.util.Map.Entry;
import java.util.Set;


public class SparqlInsertWhere extends SparqlInsertData
{
	private String where;
	
	public SparqlInsertWhere(Set<Entry<String, String>> prefix, String data, String where)
	{
		super(prefix, data);
		this.where = where;
		this.setKeyWords("INSERT");
	}

	public String toString()
	{
		return super.toString()+"WHERE{"+where+"}";
	}
	
}
