package io.github.er1.c6231;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Serialization Serializes and parses data from maps into string and
 * vice-versa
 *
 * @author chanman
 */
public class MapSerializer {

    /**
     * parse a string into a Map
     *
     * @param in content to parse
     * @return parsed output as map
     */
    public static Map<String, String> parse(String in) {

        String[] entries = in.trim().split(";");
        HashMap<String, String> map = new HashMap<String, String>();

        for (String entry : entries) {
            int colon = entry.indexOf(":");
            String key = entry.substring(0, colon).trim();
            String value = entry.substring(colon + 1).trim();
            map.put(key, value);
        }

        return map;
    }

    /**
     * create a string which is the serialzation of a map
     *
     * @param in map to serialize
     * @return
     */
    public static String stringify(Map<String, String> in) {
        String str = "";
        boolean started = false;

        for (String entry : in.keySet()) {
            if (started) {
                str += ";";
            }
            str += entry + ":" + in.get(entry);
            started = true;
        }
        return str;
    }
}
