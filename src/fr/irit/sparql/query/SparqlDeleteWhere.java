package fr.irit.sparql.query;

import java.util.ArrayList;

import fr.irit.utils.Pair;

public class SparqlDeleteWhere extends SparqlAbstarctDataQuery
{
	public SparqlDeleteWhere(ArrayList<Pair<String, String>> prefix, String data)
	{
		super(prefix, data);
		this.keyword = "DELETE WHERE";
	}
}
