package ch.viascom.groundwork.serviceresult.util;

import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Name and value pair implementation to store input properties.
 */
@Data
public class NameValuePair implements Serializable {
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