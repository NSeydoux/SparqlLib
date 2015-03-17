package fr.irit.sparql.query;

import java.util.ArrayList;

import fr.irit.utils.Pair;


/**
 * A select query. No keyword has to be explicitly written, except for aggregation attributes and filters.
 */
public class SparqlSelect extends SparqlQuery
{
	private String select;
	private String aggregate;

	public SparqlSelect(ArrayList<Pair<String, String>> prefix, String from, String select,
			String where, String aggregate)
	{
		super(prefix, from, where);
		this.setSelect(select);
		this.setAggregate(aggregate);
	}

	public String getSelect()
	{
		return select;
	}

	public void setSelect(String select)
	{
		this.select = select;
	}

	public String getAggregate()
	{
		return aggregate;
	}

	public void setAggregate(String aggregate)
	{
		this.aggregate = aggregate;
	}

	public String toString()
	{
		
		return    this.formatPrefixes()
				+ "SELECT "+this.select+"\n"
				+ (this.getFrom().equals("")?"":"FROM "+this.getFrom()+"\n")
				+ "WHERE {\n"+this.getWhere()+"}\n"
				+ (this.aggregate.equals("")?"":this.aggregate);
	}
}
