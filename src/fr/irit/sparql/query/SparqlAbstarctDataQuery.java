package fr.irit.sparql.query;

import java.util.ArrayList;

import fr.irit.utils.Pair;

public abstract class SparqlAbstarctDataQuery extends SparqlQuery
{
	private String data;
	protected String keyword;
	
	public SparqlAbstarctDataQuery(ArrayList<Pair<String, String>> prefix, String data)
	{
		super(prefix, "", "");
		this.data = data;
	}
	
	public String toString()
	{
		return this.formatPrefixes() + this.keyword+ "{"+data+"}";
	}
}
