# SparqlLib
This library intends to make the SPARQL language easier to use.

### Exemple 
Sparql query : 
```
SELECT ?a ?b ?c
WHERE
{
    ?a ?b ?c.
}
LIMIT 10
```

SparqlLib : 
```
public static void main(String[] args) 
    {
        SparqlProxy spIn = SparqlProxy.getSparqlProxy("http://sparql.endpoint.url/");
        SparqlSelect query = new SparqlSelect("?a ?b ?c", "?a ?b ?c.");
        query.setLimit(10);
        try 
        {       
            for(JsonNode jnode : spIn.getResponse(query))
            {
                System.out.println("new triple : ");
                System.out.println("\t subject : "+jnode.get("a").get("value").asText());
                System.out.println("\t predicate : "+jnode.get("b").get("value").asText());
                System.out.println("\t object : "+jnode.get("c").get("value").asText());
            }
        } 
        catch (SparqlQueryMalFormedException ex) 
        {
            System.err.println("Query mal formed ...");
        } 
        catch (SparqlEndpointUnreachableException ex) 
        {
            System.err.println("Sparql endpoint unreachable ...");
        }
    }
```
