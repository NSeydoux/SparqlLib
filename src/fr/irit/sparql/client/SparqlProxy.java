/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.irit.sparql.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author murloc
 */
public class SparqlProxy implements Serializable
{

	// static part

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static HashMap<String, SparqlProxy> insts;

	private static ArrayList<String> labelsUri;
	private static ArrayList<String> subUri;
	private static ArrayList<String> excludeUri;

	private static boolean initiated = false;

	public static SparqlProxy getSparqlProxy(String url)
	{
		if (!initiated)
		{
			insts = new HashMap<>();

			labelsUri = new ArrayList<>();
			labelsUri.add("http://www.w3.org/2004/02/skos/core#prefLabel");
			labelsUri.add("http://www.w3.org/2004/02/skos/core#altLabel");

			subUri = new ArrayList<>();
			subUri.add("http://www.w3.org/2004/02/skos/core#broader");

			excludeUri = new ArrayList<>();
			excludeUri.add("http://www.w3.org/2004/02/skos/core#prefLabel");
			excludeUri.add("http://www.w3.org/2004/02/skos/core#altLabel");
			excludeUri.add("http://www.w3.org/2004/02/skos/core#broader");
			excludeUri.add("http://www.w3.org/2008/05/skos-xl#prefLabel");
			excludeUri.add("http://www.w3.org/2008/05/skos-xl#altLabel");
			excludeUri.add("http://www.w3.org/2004/02/skos/core#narrower");
			excludeUri.add("http://www.w3.org/2004/02/skos/core#broader");

			initiated = true;
		}

		SparqlProxy inst = insts.get(url);

		if (inst == null)
		{
			inst = new SparqlProxy(url);
			insts.put(url, inst);
		}

		return inst;
	}

	public static String makeQuery(String q)
	{
		String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX owl:    <http://www.w3.org/2002/07/owl#>"
				+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
				+ "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>"
				+ "PREFIX agrovoc: <http://aims.fao.org/aos/agrontology#>" + q;
		return query;
	}

	public static StringBuilder makeQuery(StringBuilder q)
	{
		StringBuilder query = new StringBuilder(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ "PREFIX owl:    <http://www.w3.org/2002/07/owl#>"
						+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
						+ "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>"
						+ "PREFIX agrovoc: <http://aims.fao.org/aos/agrontology#>");
		query.append(q);

		return query;
	}

	public static String cleanString(String s)
	{
		return s.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\"", "");
	}

	public static boolean isSubRel(String relUri)
	{
		return subUri.contains(relUri);
	}

	public static boolean isLabelRel(String relUri)
	{
		return labelsUri.contains(relUri);
	}

	public static boolean isExcludeRel(String relUri)
	{
		return excludeUri.contains(relUri);
	}

	// non static part
	private String urlServer;

	private SparqlProxy(String urlServer)
	{
		this.urlServer = urlServer;
	}

	public ArrayList<JsonNode> getResponse(String query)
	{
		HttpURLConnection connection = null;
		ArrayList<JsonNode> arr = new ArrayList<>();
		String jsonRet = "";
		query = SparqlProxy.makeQuery(query);
		try
		{
			URL url = new URL(this.urlServer + "query?output=json&query="
					+ URLEncoder.encode(query, "UTF-8"));
			// Create connection
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = rd.readLine()) != null)
			{
				response.append(line);
				response.append('\r');
			}
			rd.close();
			jsonRet = response.toString();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(jsonRet);
			Iterator<JsonNode> i = root.get("results").get("bindings")
					.iterator();
			while (i.hasNext())
			{
				arr.add(i.next());
			}
		}
		catch (IOException e)
		{
			System.err.println("ERROR during the response parsing... : " + e);
			System.err.println(query);
			System.exit(0);
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
		}
		return arr;
	}

	public boolean storeData(StringBuilder query)
	{
		boolean ret = true;
		//query = SparqlProxy.makeQuery(query);
		HttpURLConnection connection = null;
		try
		{
			String urlParameters = "update="
					+ URLEncoder.encode(query.toString(), "UTF-8");
			URL url = new URL(this.urlServer + "update");
			// Create connection
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			connection.setDoOutput(true);

			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());

			writer.write(urlParameters);
			writer.flush();

			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String rep = "";
			while ((line = reader.readLine()) != null)
			{
				rep += line;
			}
			writer.close();
			reader.close();
		}
		catch (Exception e)
		{
			System.err.println("ERROR UPDATE : " + e);
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
		}
		return ret;
	}

	public File writeKBFile(String dirPath, String fileName)
	{
		File ret = null;
		HttpURLConnection connection = null;
		try
		{
			URL url = new URL(this.urlServer + "data?default");
			// Create connection
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			FileWriter f = new FileWriter(dirPath + fileName + ".owl");

			while ((line = reader.readLine()) != null)
			{
				f.write(line + "\n");
			}

			ret = new File("out/" + fileName + ".owl");
			reader.close();
			f.close();
		}
		catch (IOException e)
		{
			System.err.println("ERROR writing file : " + e);
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
		}

		return ret;
	}

	public boolean sendAskQuery(String query)
	{
		boolean ret = false;

		HttpURLConnection connection = null;
		JsonNode arr = null;
		query = SparqlProxy.makeQuery(query);
		try
		{
			URL url = new URL(this.urlServer + "query?output=json&query="
					+ URLEncoder.encode(query, "UTF-8"));
			// Create connection
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = rd.readLine()) != null)
			{
				response.append(line);
				response.append('\r');
			}
			rd.close();
			String jsonRet = response.toString();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(jsonRet);
			// JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonRet);
			ret = root.get("boolean").asBoolean();
		}
		catch (IOException e)
		{
			System.err.println("ERROR during the response parsing...");
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
		}
		return ret;
	}

	public void clearSp()
	{
		this.storeData(new StringBuilder("DELETE WHERE{?a ?b ?c}"));
	}

	public String getUrlServer()
	{
		return this.urlServer;
	}

}
