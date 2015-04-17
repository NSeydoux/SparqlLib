package fr.irit.sparql.query;

import java.util.Map.Entry;
import java.util.Set;

public class SparqlAsk extends SparqlQuery
{
	public SparqlAsk(Set<Entry<String, String>> prefix, String data)
	{
		super(prefix, "", data);
	}
	
	public String toString()
	{
		return this.formatPrefixes()+" ASK{"+this.getWhere()+"}";
	}

}
