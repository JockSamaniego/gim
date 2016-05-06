package org.loxageek.common.ws;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 
 * @author WilmanChamba 
 * wilman at loxageek dot com
 * 
 */

public class MapAdapter extends XmlAdapter<MapType, Map<String, Object>>{

	@Override
	public MapType marshal(Map<String, Object> arg0) throws Exception {
		MapType myMapType = new MapType();
	    for(Entry<String, Object> entry : arg0.entrySet()) {
	    	MapEntryType myMapEntryType = new MapEntryType();
	        myMapEntryType.setKey(entry.getKey());
	        myMapEntryType.setValue(entry.getValue());
	        myMapType.getEntry().add(myMapEntryType);
	      }
	      return myMapType;
		
	}

	@Override
	public Map<String, Object> unmarshal(MapType arg0) throws Exception {
		Map<String, Object> hashMap = new LinkedHashMap<String, Object>();
	    for(MapEntryType myEntryType : arg0.getEntry()) {
	         hashMap.put(myEntryType.getKey(), myEntryType.getValue());
	    }
	    return hashMap;
	}

}
