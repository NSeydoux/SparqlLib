package fr.irit.sparql.query;

import java.util.ArrayList;

import fr.irit.utils.Pair;

public class SparqlInsertData extends SparqlAbstarctDataQuery
{
	public SparqlInsertData(ArrayList<Pair<String, String>> prefix, String data)
	{
		super(prefix, data);
		this.keyword = "INSERT DATA";
	}
}
