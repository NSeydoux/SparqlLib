package fr.irit.sparql.query.DataQuery.Delete;

import fr.irit.sparql.query.DataQuery.SparqlAbstractDataQuery;
import java.util.Map;
import java.util.Set;

public class SparqlDeleteWhere extends SparqlAbstractDataQuery
{
	public SparqlDeleteWhere(Set<Map.Entry<String, String>> prefix, String data)
	{
		super(prefix, data);
		this.keyword = "DELETE WHERE";
	}
}
