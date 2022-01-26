package util;

import com.thoughtworks.xstream.XStream;
import model.Coordinates;
import model.Flat;
import model.FlatListWrap;
import model.House;

public class XStreamUtil {
    public static XStream createXStream() {
        XStream result = new XStream();
        result.processAnnotations(Flat.class);
        result.processAnnotations(House.class);
        result.processAnnotations(FlatListWrap.class);
        Class<?>[] classes = new Class[]{Flat.class, House.class, Coordinates.class, FlatListWrap.class};
        result.allowTypes(classes);
        result.setMode(XStream.NO_REFERENCES);

        result.registerConverter(new CustomDateConverter());
        return result;
    }
}
