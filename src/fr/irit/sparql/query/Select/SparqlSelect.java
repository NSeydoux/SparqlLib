package fr.irit.sparql.query.Select;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import fr.irit.sparql.query.SparqlQuery;


/**
 * A select query. No keyword has to be explicitly written, except for aggregation attributes and filters.
 */
public class SparqlSelect extends SparqlQuery
{
	private String select;
	private String aggregate;
        private int limit = -1;

	public SparqlSelect(Set<Map.Entry<String, String>> prefix, String from, String select,
			String where, String aggregate)
	{
		super(prefix, from, where);
		this.setSelect(select);
		this.setAggregate(aggregate);
	}
	
	public SparqlSelect(Set<Map.Entry<String, String>> prefix, String select,	String where)
	{
		super(prefix, "", where);
		this.setSelect(select);
		this.setAggregate("");
	}
	
	public SparqlSelect(String select,	String where)
	{
		super(new HashSet<Map.Entry<String, String>>(), "", where);
		this.setSelect(select);
		this.setAggregate("");
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
        
        public void setLimit(int i)
        {
            this.limit = i;
        }

	public String toString()
	{
		
		return    this.formatPrefixes()
				+ "SELECT "+this.select+"\n"
				+ (this.getFrom().equals("")?"":" FROM "+this.getFrom()+"\n")
				+ " WHERE {\n"+this.getWhere()+"}\n"
				+ (this.aggregate.equals("")?"":this.aggregate)
                                + ((this.limit==-1)?"":" LIMIT "+this.limit);
	}
	
	public static void displayResult(ArrayList<JsonNode> results)
	{
		for (JsonNode jn : results)
		{
			System.out.println(jn);
		}
	}
}
