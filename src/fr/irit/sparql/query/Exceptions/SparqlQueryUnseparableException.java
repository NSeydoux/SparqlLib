/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.irit.sparql.query.Exceptions;

/**
 *
 * @author murloc
 */
public class SparqlQueryUnseparableException extends Exception
{
    
    public String toString()
    {
        return "Sparql Query unseparable (too long for only one query)";
    }
    
}
