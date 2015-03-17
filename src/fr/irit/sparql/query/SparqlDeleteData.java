package fr.irit.sparql.query;

import java.util.ArrayList;

import fr.irit.utils.Pair;

public class SparqlDeleteData extends SparqlAbstarctDataQuery
{
	public SparqlDeleteData(ArrayList<Pair<String, String>> prefix, String data)
	{
		super(prefix, data);
		this.keyword = "DELETE DATA";
	}
}
