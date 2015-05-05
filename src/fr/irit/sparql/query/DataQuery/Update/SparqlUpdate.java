package fr.irit.sparql.query.DataQuery.Update;

import fr.irit.sparql.query.SparqlQuery;
import java.util.Map;
import java.util.Set;

public class SparqlUpdate extends SparqlQuery
{
	private String delete;
	private String insert;
	
	public SparqlUpdate(Set<Map.Entry<String, String>> prefix, String delete, String insert, String where)
	{
		super(prefix, "", where);
		this.delete = delete;
		this.insert = insert;
	}
	
	public String toString()
	{
		return this.formatPrefixes()
				+"DELETE {"+this.delete+"}\n"
				+"INSERT {"+this.insert+"}\n"
				+"WHERE {\n"+this.where+"\n}";
	}
}
