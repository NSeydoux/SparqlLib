package fr.irit.sparql.query;

public enum SparqlRole
{
	SUBJECT("subject"), PROPERTY("property"), OBJECT("object");
	
	private String label;
	private SparqlRole(String label)
	{
		this.label = label;
	}
	
	public String toString()
	{
		return this.label;
	}
	
	
}
