package ch.viascom.groundwork.serviceresult.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NameValuePair {
    private String name;
    private String value;
 
    
    /**
     * Constructor
     */
    public NameValuePair() {
    	
    }
    
    /**
     * Constructor.
     */
    public NameValuePair(String name, String value){
        if (name == null)
        	name = "";
        
        if (value == null)
        	value = "";
        
        this.name = name;
        this.value = value;
    }
 
    public String getName() {
        return name;
    }
 
    public String getValue() {
        return value;
    }
 
    /**
     * Get the name and value query parameter, URL encoded as UTF-8.
     * 
     * Example: myName=myValue
     */
    @Override
    public String toString(){
        try {
            return toString("UTF-8");
        } catch (UnsupportedEncodingException uex){
            throw new RuntimeException(uex);
        }
    }
 
    /**
     * Get the name and value query parameter, URL encoded to the given encoding.
     *
     * @param encoding	Encoding to use
     * @throws UnsupportedEncodingException if the given encoding set is not supported
     */
    public String toString(String encoding) throws UnsupportedEncodingException {
        return URLEncoder.encode(name, encoding) + "=" + URLEncoder.encode(value, encoding);
    }
}