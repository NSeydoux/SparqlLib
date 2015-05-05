package fr.irit.sparql.query.Exceptions;

public class SparqlEndpointUnreachableException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6591977685812151888L;
        
        private String message;
        
        public SparqlEndpointUnreachableException(Exception e)
        {
            this.message = e.getLocalizedMessage();
        }
        
	public String toString()
	{
		return "The endpoint you specified is unreachable : "+this.message;
	}
}
