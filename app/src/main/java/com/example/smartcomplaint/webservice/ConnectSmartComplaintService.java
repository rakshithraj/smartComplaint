/*
package com.example.smartcomplaint.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;





public class ConnectSmartComplaintService {
	
	 public String executeRequest(HttpUriRequest request, int _timeout,
	            String url,Context mContext) {
     	// Log.d("tag","executeRequest");
		
	        String response;
	        DefaultHttpClient client = getNewHttpClient();
	        request.setHeader("User-Agent", "USER_AGENT");	    
	        HttpResponse httpResponse;
	        HttpEntity entity;
	        try {
	       	
	            httpResponse = client.execute(request);	
	       	
	       		 
	            Log.d("tag","client.execute(request) ");
	            String message = httpResponse.getStatusLine().getReasonPhrase();
	            Log.d("tag","responseCode "+message);
	          //System.out.println("responseCode+ "+message);
	            entity = httpResponse.getEntity();

	            if (entity != null) {
	            	
	               // response = EntityUtils.toString(entity);
	            	 Log.d("tag","entity != null ");
	            	response = getResponseBody(entity);
	                System.out.println("EntityUtils+ "+response);
	               
	                
	                return response;
	            }
	        } catch (Exception e) {
	            client.getConnectionManager().shutdown();
	            Log.d("tag", e.toString());
	            e.printStackTrace();
	        }finally{
	            client.getConnectionManager().shutdown();
	            client = null;
	            request = null;
	            httpResponse = null;
	            entity = null;
	        }
	        Log.d("tag","return null ");
	        return null;
	    }

	    private String execute(String url, String methodName, int _timeout,
	            RequestMethod methodType,String  json,Context mContext)
	    throws Exception {
	        switch (methodType) {
	        
	        
	        case PUT: {
	               
	        	try{
	                HttpPut request = new HttpPut(url);
	                
	               
	                Log.d("tag","my "+json);
	                           
	                StringEntity se = new StringEntity(json);
	                request.setEntity(se);    
	     
	                request.setHeader("Accept", "application/json");
	                request.setHeader("Content-type", "application/json");
	                
	               return executeRequest(request, _timeout, url,mContext);
	                
	            	}catch(Exception e){
	            		Log.d("tag","my error  "+e.toString());
	            	}
            }
	        
	            case GET: {
	               
	            	  Log.d("tag","GET");
	                HttpGet request = new HttpGet(url);    
	               // System.out.println("**** "+request.getURI());
	                return executeRequest(request, _timeout, url,mContext);
	            }
	            case POST: {
	            	 Log.d("tag","POST");
	            	try{
	                HttpPost request = new HttpPost(url);
	                
	               
	                Log.d("tag","my "+json);
	                           
	                StringEntity se = new StringEntity(json);
	                request.setEntity(se);    
	     
	                request.setHeader("Accept", "application/json");
	                request.setHeader("Content-type", "application/json");
	                
	               return executeRequest(request, _timeout, url,mContext);
	                
	            	}catch(Exception e){
	            		Log.d("tag","my error  "+e.toString());
	            	}
	               
	            }
	            default:
	                return null;
	        }
	    }
	    
	    

	   public String execute(String url,String json,Context mContext) throws Exception {
	        return execute(url, "", DEFAULT_TIMEOUT, RequestMethod.POST,json,mContext);
	    }
	   public String executeget(String url,String json,Context mContext) throws Exception {
		   Log.d("tag","execute");
	        return execute(url, "", DEFAULT_TIMEOUT, RequestMethod.GET,json,mContext);
	    }
	    
	   public String executePut(String url,String json,Context mContext) throws Exception {
	        return execute(url, "", DEFAULT_TIMEOUT, RequestMethod.PUT,json,mContext);
	    }
	    public enum RequestMethod {
	        GET, POST,PUT
	    }
	    public static int DEFAULT_TIMEOUT = 10000;
	    
	   // private ArrayList<NameValuePair> headers;

	    public DefaultHttpClient getNewHttpClient() {
	        try {
	            System.setProperty("http.keepAlive", "false");
	            KeyStore trustStore = KeyStore.getInstance(KeyStore
	                    .getDefaultType());
	            trustStore.load(null, null);
	            org.apache.http.conn.ssl.SSLSocketFactory sf = new MySSLSocketFactory1111(trustStore);
	            sf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	            HttpParams params = new BasicHttpParams();
	            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	            SchemeRegistry registry = new SchemeRegistry();
	            registry.register(new Scheme("http", PlainSocketFactory
	                    .getSocketFactory(), 80));
	            registry.register(new Scheme("https", sf, 443));

	            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
	                    params, registry);

	            return new DefaultHttpClient(ccm, params);
	        } catch (Exception e) {
	            return new DefaultHttpClient();
	        }
	    }
	    
	    
	    
	    public String getResponseBody(final HttpEntity entity) throws IOException,ParseException {


	        if (entity == null) {
	            throw new IllegalArgumentException("HTTP entity may not be null");
	        }

	        InputStream instream = entity.getContent();
	       

	        if (instream == null) {
	            return "";
	        }

	        if (entity.getContentLength() > Integer.MAX_VALUE) {
	            throw new IllegalArgumentException(

	            "HTTP entity too large to be buffered in memory");
	        }


	        StringBuilder buffer = new StringBuilder();

	        BufferedReader reader = new BufferedReader(new InputStreamReader(instream, HTTP.UTF_8));

	        String line = null;
	        try {
	           while ((line = reader.readLine()) != null) {
	                buffer.append(line);
	         
	           }

	        } finally {
	            instream.close();
	            reader.close();
	        }
			return buffer.toString();
	    }
}
*/
