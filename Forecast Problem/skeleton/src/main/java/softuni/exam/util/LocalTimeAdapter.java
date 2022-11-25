package softuni.exam.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
//    DateTimeFormatter dtf = DateTimeFormatter.ISO_TIME;

    @Override
    public LocalTime unmarshal(String s) throws Exception {
        return LocalTime.parse(s);
    }

    @Override
    public String marshal(LocalTime time) throws Exception {
        return time.format(dtf);
    }
}
