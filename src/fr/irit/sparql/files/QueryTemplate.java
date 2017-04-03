package fr.irit.sparql.files;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.irit.sparql.exceptions.IncompleteSubstitutionException;

/**
 * This class is made to ease the reuse of predefined queries. Their syntax is inspired by the 
 * Jinja template syntax (http://jinja.pocoo.org/), where variables can be specified in html
 * code between double curly brackets : {{ myVariableName }}. These variables are then substituted
 * with their value before rendering the HTML. The principle is the same here, with SPARQL queries.
 */
public class QueryTemplate {
	private String query;
	private Set<String> toSubstitute;
	
	public QueryTemplate(String query){
		this.query = query;
		this.toSubstitute = new HashSet<String>();
		Pattern p = Pattern.compile("\\{\\{ (.+) \\}\\}");
		Matcher m = p.matcher(query);
		while(m.find()){
			this.toSubstitute.add(m.group(1));
		}
	}
	
	/**
	 * @return the list of elements to be substituted (their names in the query templates)
	 */
	public Set<String> getSubstitute(){
		return this.toSubstitute;
	}
	
	/**
	 * 
	 * @param substitution
	 * @return a query, where the unbound parameters are instantiated according to the substitution map
	 * @throws IncompleteSubstitutionException
	 */
	public String substitute(Map<String, String> substitution) throws IncompleteSubstitutionException{
		String query = this.query;
		if(substitution.keySet().containsAll(this.toSubstitute)){
			for(String key : this.toSubstitute){
				query = query.replaceAll("\\{\\{ "+key+" \\}\\}", substitution.get(key));
			}
		} else {
			throw new IncompleteSubstitutionException("Some elements of the substitution "+this.toSubstitute+"are not resolved by "+substitution);
		}
		return query;
	}
}
