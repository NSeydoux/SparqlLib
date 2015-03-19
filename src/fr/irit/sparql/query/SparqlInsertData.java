package fr.irit.sparql.query;

import java.util.Map;
import java.util.Set;

public class SparqlInsertData extends SparqlAbstarctDataQuery
{
	public SparqlInsertData(Set<Map.Entry<String, String>> prefix, String data)
	{
		super(prefix, data);
		this.keyword = "INSERT DATA";
	}
}
