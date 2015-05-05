package fr.irit.sparql.query.DataQuery;

import fr.irit.sparql.query.Exceptions.SparqlQueryMalFormedException;
import fr.irit.sparql.query.Exceptions.SparqlQueryUnseparableException;
import fr.irit.sparql.query.SparqlQuery;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SparqlAbstractDataQuery extends SparqlQuery
{
	private StringBuilder data;
	protected String keyword;
	
	public SparqlAbstractDataQuery(Set<Map.Entry<String, String>> prefix, String data)
	{
		super(prefix, "", "");
		this.data = new StringBuilder(data);
	}
        
        public SparqlAbstractDataQuery(Set<Map.Entry<String, String>> prefix, StringBuilder data)
	{
		super(prefix, "", "");
		this.data = data;
	}
        
        public void addData(String dataSupp)
        {
            this.data.append(dataSupp);
        }
        
        public void setData(StringBuilder data)
        {
            this.data = data;
        }
		
        @Override
	public String toString()
	{
		return this.formatPrefixes() + this.keyword+ "{"+data+"}";
	}
        
        public StringBuilder toStringBuilder()
        {
            StringBuilder ret =  new StringBuilder(this.formatPrefixes()+this.keyword+"{");
            ret.append(data);
            ret.append("}");
            return ret;
        }
        
        public SparqlAbstractDataQuery serparateQuery(int maxSize) throws SparqlQueryUnseparableException, SparqlQueryMalFormedException
        {
            StringBuilder temp =  new StringBuilder(this.data.subSequence(0, maxSize));
            
            Pattern pattern = Pattern.compile(".*(A|B|C)");
            Matcher m = pattern.matcher(temp);
            int idLastTriple = -1;
            if (m.find()) {
                idLastTriple = m.start(1);
            }
            else
            {
                throw new SparqlQueryMalFormedException("no triple founded");
            }
            SparqlAbstractDataQuery ret = null;
            try 
            {
                ret = (SparqlAbstractDataQuery) this.clone();
                ret.setData(new StringBuilder(this.data.substring(idLastTriple)));
            } 
            catch (CloneNotSupportedException ex) 
            {
                throw new SparqlQueryUnseparableException();
            }
            this.setData(new StringBuilder(this.data.subSequence(0, idLastTriple)));
            
            return ret;
        }
        
}
