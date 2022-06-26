package me.nostyll.Kingdoms.levels.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Splitter;

public class MetadataKingdom {
	
	Map<String, String> result = new HashMap<String, String>();
	
	public MetadataKingdom(String map){
	    result = Splitter.on(',')
	    	    .trimResults()
	    	    .withKeyValueSeparator(
	    	        Splitter.on('=')
	    	            .limit(2)
	    	            .trimResults())
	    	    .split(map.replaceAll("[\\[\\](){}]",""));
	}
	
	public static MetadataKingdom getMetadata(String meta){
		return new MetadataKingdom(meta);
	}
	
	public static Map<String, String> getMetaList(String meta){
		return new MetadataKingdom(meta).result;
	}
	
    public String getString(String value) {
        return this.result.get(value);
    }
    
    public int getInt(String value) {
        return Integer.parseInt(this.result.get(value));
    }
    
    public float getFloat(String value) {
        return Float.parseFloat(this.result.get(value));
    }
    
    public double getDouble(String value) {
        return Double.parseDouble(this.result.get(value));
    }
    
    public boolean getBoolean(String value) {
        return Boolean.parseBoolean(this.result.get(value));
    }
    
    public long getLong(String value) {
        return Long.parseLong(this.result.get(value));
    }

}
