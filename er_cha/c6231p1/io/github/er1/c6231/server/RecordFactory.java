package io.github.er1.c6231.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordFactory {

    public static Map<String, String> createMapFromRecord(Record record) {
        Map<String, String> map = new HashMap<>();
        map.put("id", record.getId());
        map.put("fn", record.getFirstName());
        map.put("ln", record.getLastName());
        map.put("st", record.getStatus());
        map.put("ty", record.getRecordType());

        try {
            CriminalRecord cr = (CriminalRecord) record;
            map.put("de", cr.getDescription());
        } catch (ClassCastException ex) {
        }

        try {
            MissingRecord mr = (MissingRecord) record;
            map.put("ad", mr.getAddress());
            map.put("ll", mr.getLastLocation());
            map.put("ld", Long.toString(mr.getLastDate().getTime()));
        } catch (ClassCastException ex) {
        }

        return map;
    }

    public static Record createRecordFromMap(Map<String, String> map) {

        String id = map.get("id");
        String fn = map.get("fn");
        String ln = map.get("ln");
        String st = map.get("st");
        String ty = map.get("ty");

        long idnum = Long.parseLong(id.replace(ty, ""), 10);

        if ("CR".equals(ty)) {
            String de = map.get("de");
            return new CriminalRecord(idnum, fn, ln, de, st);

        }

        if ("MR".equals(ty)) {
            String ad = map.get("ad");
            String ll = map.get("ll");
            Date ld = new Date(Long.parseLong(map.get("ld")));
            return new MissingRecord(idnum, fn, ln, ad, ld, ll, st);
        }
        return null;
    }
}
