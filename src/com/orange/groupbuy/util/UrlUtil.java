package com.orange.groupbuy.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.orange.common.utils.ListUtil;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.dao.Gps;

public class UrlUtil {

	static public String URL_ARRAY_SPLIT = ";";
	static public String URL_GPS_SPLIT = ",";

	static public List<Gps> parseGpsArray(String gpsString) {
		List<String> gpsStringArray = parserUrlArray(gpsString);
		if (gpsStringArray == null || gpsStringArray.size() < 1)
			return null;
		List<Gps> gpsList = new ArrayList<Gps>();
		for (String gps : gpsStringArray) {
			if (gps == null || gps.length() < 1)
				continue;
			Gps gpsUnit = parseGpsUnit(gps);
			if (gpsUnit != null)
				gpsList.add(gpsUnit);
		}
		return gpsList;
	}

	static private Gps parseGpsUnit(String gps) {
		String[] gpsArray = gps.split(URL_GPS_SPLIT);
		if (gpsArray == null || gpsArray.length != 2)
			return null;
		double latitude = StringUtil.doubleFromString(gpsArray[0]);
		double longitude = StringUtil.doubleFromString(gpsArray[1]);
		return new Gps(latitude, longitude);
	}

	static public List<String> parserUrlArray(String string) {
		if (string == null || string.length() < 1)
			return null;
		return ListUtil.stringsToList(string.split(URL_ARRAY_SPLIT));
	}

	static public List<Integer> parserUrlIntArray(String string) {
		if (string == null || string.length() < 1)
			return Collections.emptyList();
		return ListUtil.stringsToIntList(string.split(URL_ARRAY_SPLIT));
	}

}
