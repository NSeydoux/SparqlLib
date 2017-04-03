package fr.irit.sparql.files;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.irit.sparql.exceptions.IncompleteSubstitutionException;

public class QueryTemplate {
	private String query;
	private Set<String> toSubstitute;
	
	public QueryTemplate(String query){
		this.query = query;
		this.toSubstitute = new HashSet<String>();
		Pattern p = Pattern.compile("{{ (.+) }}");
		Matcher m = p.matcher(query);
		while(m.find()){
			this.toSubstitute.add(m.group(1));
		}
	}
	
	public Set<String> getSubstitute(){
		return this.toSubstitute;
	}
	
	public String substitute(Map<String, String> substitution) throws IncompleteSubstitutionException{
		String query = this.query;
		if(substitution.keySet().containsAll(this.toSubstitute)){
			for(String key : this.toSubstitute){
				query.replaceAll("{{ "+key+" }}", substitution.get(key));
			}
		} else {
			throw new IncompleteSubstitutionException("Some elements of the substitution "+this.toSubstitute+"are not resolved by "+substitution);
		}
		return query;
	}
}
