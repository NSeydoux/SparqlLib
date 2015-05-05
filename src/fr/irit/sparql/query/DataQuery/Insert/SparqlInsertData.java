package fr.irit.sparql.query.DataQuery.Insert;

import fr.irit.sparql.query.DataQuery.SparqlAbstractDataQuery;
import java.util.Map;
import java.util.Set;

public class SparqlInsertData extends SparqlAbstractDataQuery
{
	public SparqlInsertData(Set<Map.Entry<String, String>> prefix, String data)
	{
		super(prefix, data);
		this.keyword = "INSERT DATA";
	}
	
	public void setKeyWords(String keyword)
	{
		this.keyword = keyword;
	}
}
