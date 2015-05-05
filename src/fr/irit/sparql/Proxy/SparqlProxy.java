/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.irit.sparql.Proxy;

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
import fr.irit.sparql.query.DataQuery.SparqlAbstractDataQuery;
import fr.irit.sparql.query.Ask.SparqlAsk;
import fr.irit.sparql.query.Exceptions.SparqlEndpointUnreachableException;
import fr.irit.sparql.query.Exceptions.SparqlQueryMalFormedException;
import fr.irit.sparql.query.Exceptions.SparqlQueryUnseparableException;
import fr.irit.sparql.query.Select.SparqlSelect;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author murloc
 */
public class SparqlProxy
{

	// static part

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
        
        private static final int maxPostSize = 4000000;

	private static final HashMap<String, SparqlProxy> insts = new HashMap<>();
        

	public static SparqlProxy getSparqlProxy(String url)
	{
            
		SparqlProxy inst = insts.get(url);

		if (inst == null)
		{
			inst = new SparqlProxy(url);
			insts.put(url, inst);
		}

		return inst;
	}

	public static String cleanString(String s)
	{
		return s.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\"", "");
	}
        
        
        public static void replaceAll(StringBuilder builder, String from, String to)
        {
            int index = builder.indexOf(from);
            while (index != -1)
            {
                builder.replace(index, index + from.length(), to);
                index += to.length(); // Move to the end of the replacement
                index = builder.indexOf(from, index);
            }
        }
        
        public static void cleanString(StringBuilder s)
	{
            replaceAll(s, "\r", "");
            replaceAll(s, "\n", "");
            replaceAll(s, "\"", "");
	}

	// non static part
	private String urlServer;

	private SparqlProxy(String urlServer)
	{
		this.urlServer = urlServer;
	}

	public ArrayList<JsonNode> getResponse(String query) throws SparqlQueryMalFormedException, SparqlEndpointUnreachableException
	{
		HttpURLConnection connection = null;
		ArrayList<JsonNode> arr = new ArrayList<>();
		String jsonRet = "";
		query = SparqlProxy.cleanString(query);
                //System.out.println("Query : "+query);
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
            catch (MalformedURLException ex) {
                throw new SparqlQueryMalFormedException("Query malformed : "+query);
            } catch (UnsupportedEncodingException ex) {
                throw new SparqlQueryMalFormedException("Encoding unsupported");
            } catch (IOException ex) {
                throw new SparqlEndpointUnreachableException(ex);
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
        
        public ArrayList<JsonNode> getResponse(SparqlSelect query) throws SparqlQueryMalFormedException, SparqlEndpointUnreachableException
        {
            return this.getResponse(query.toString());
        }

	public boolean storeDataString(StringBuilder query) throws SparqlQueryMalFormedException, SparqlEndpointUnreachableException
	{
            boolean ret = true;
            SparqlProxy.cleanString(query);
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
            catch (UnsupportedEncodingException ex) 
            {
                throw new SparqlQueryMalFormedException("Encoding unsupported");
            } 
            catch (MalformedURLException ex) 
            {
                throw new SparqlQueryMalFormedException("Query malformed");
            } 
            catch (IOException ex) 
            {
                throw new SparqlEndpointUnreachableException(ex);
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
        
        public boolean storeData(SparqlAbstractDataQuery query) throws SparqlQueryMalFormedException, SparqlEndpointUnreachableException, SparqlQueryUnseparableException
        {
            
            StringBuilder qString = query.toStringBuilder();
            boolean ret = false;
            if(qString.length() > SparqlProxy.maxPostSize)
            {
                SparqlAbstractDataQuery q2 = query.serparateQuery(SparqlProxy.maxPostSize);
                ret = (this.storeData(query) && this.storeData(q2));
            }
            else
            {
                ret = this.storeDataString(qString);
            }
            
            return ret;
        }

	public File writeKBFile(String dirPath, String fileName) throws SparqlQueryMalFormedException, SparqlEndpointUnreachableException
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
		catch (UnsupportedEncodingException ex) 
                {
                    throw new SparqlQueryMalFormedException("Encoding unsupported");
                } 
                catch (MalformedURLException ex) 
                {
                    throw new SparqlQueryMalFormedException("Query malformed");
                } 
                catch (IOException ex) 
                {
                    throw new SparqlEndpointUnreachableException(ex);
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

	public boolean sendAskQuery(String query) throws SparqlQueryMalFormedException, SparqlEndpointUnreachableException
	{
		boolean ret = false;

		HttpURLConnection connection = null;
		JsonNode arr = null;
		query = SparqlProxy.cleanString(query);
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
		catch (UnsupportedEncodingException ex) 
                {
                    throw new SparqlQueryMalFormedException("Encoding unsupported");
                } 
                catch (MalformedURLException ex) 
                {
                    throw new SparqlQueryMalFormedException("Query malformed");
                } 
                catch (IOException ex) 
                {
                    throw new SparqlEndpointUnreachableException(ex);
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
        
        public boolean sendAskQuery(SparqlAsk query) throws SparqlQueryMalFormedException, SparqlEndpointUnreachableException
        {
            return this.sendAskQuery(query.toString());
        }

	public void clearSp() throws SparqlQueryMalFormedException, SparqlEndpointUnreachableException
	{
		this.storeDataString(new StringBuilder("DELETE WHERE{?a ?b ?c}"));
	}

	public String getUrlServer()
	{
		return this.urlServer;
	}

}
