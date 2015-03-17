package fr.irit.utils;


public class Pair<F, S>
{
	private F first;
	private S second;
	
	public Pair(F first, S second)
	{
		this.first = first;
		this.second = second;
	}
	
	public F getFirst()
	{
		return this.first;
	}
	
	public S getSecond()
	{
		return this.second;
	}

}
