package c6231.Server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordFactory {

    public static final DateFormat recordDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            map.put("ld", recordDateFormat.format(mr.getLastDate()));
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
            Date ld = new Date();
            try {
                ld = recordDateFormat.parse(map.get("ld"));
            } catch (ParseException ex) {
            }
            return new MissingRecord(idnum, fn, ln, ad, ld, ll, st);
        }
        return null;
    }
}
