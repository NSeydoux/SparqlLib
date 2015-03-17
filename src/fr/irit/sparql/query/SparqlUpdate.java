package fr.irit.sparql.query;

import java.util.ArrayList;

import fr.irit.utils.Pair;

public class SparqlUpdate extends SparqlQuery
{
	private String delete;
	private String insert;
	
	public SparqlUpdate(ArrayList<Pair<String, String>> prefix, String delete, String insert, String where)
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
