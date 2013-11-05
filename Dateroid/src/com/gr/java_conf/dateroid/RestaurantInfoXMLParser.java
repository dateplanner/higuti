package com.gr.java_conf.dateroid;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class RestaurantInfoXMLParser {

	public ArrayList<HashMap<String, String>> xmlParse(InputStream xml, String[] attributeNames){
		ArrayList<HashMap<String, String>> parentList = new ArrayList<HashMap<String,String>>();
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(xml, "UTF-8");
			
			int index = 0;
			int eventType = parser.getEventType();
			String elementName;
			HashMap<String, String> xmlData = new HashMap<String, String>();
			
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					elementName = parser.getName();
					if (elementName.equals(attributeNames[index])) {
						xmlData.put(elementName, String.valueOf(parser.nextText()));
						index++;
					}
				default:
					break;
				}
				eventType = parser.next();
				if(attributeNames.length == index){
					index = 0;
					parentList.add(xmlData);
					xmlData = new HashMap<String, String>();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return parentList;
	}
}
