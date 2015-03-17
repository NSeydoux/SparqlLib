package fr.irit.sparql.query;

public class SparqlEndpointUnreachableException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6591977685812151888L;
	public String toString()
	{
		return "The endpoint you specified is unreachable";
	}
}
