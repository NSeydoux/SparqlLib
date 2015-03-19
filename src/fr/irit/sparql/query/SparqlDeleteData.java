package fr.irit.sparql.query;

import java.util.Map;
import java.util.Set;

public class SparqlDeleteData extends SparqlAbstarctDataQuery
{
	public SparqlDeleteData(Set<Map.Entry<String, String>> prefix, String data)
	{
		super(prefix, data);
		this.keyword = "DELETE DATA";
	}
}
