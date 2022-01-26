package soap.util;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@XmlRootElement(name = "filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Filter {

    // pagination
    @XmlElement
    private String pageNumber;
    @XmlElement
    private String pageSize;

    // sorting
    @XmlElement
    private String id;
    @XmlElement
    private String name;
    @XmlElement
    private String coordinates;
    @XmlElement
    private String creationDate;
    @XmlElement
    private String area;
    @XmlElement
    private String numberOfRooms;
    @XmlElement
    private String price;
    @XmlElement
    private String kitchenArea;
    @XmlElement
    private String view;
    @XmlElement
    private String house;

    // ordering
    @XmlElement(name = "orderBy")
    private List<String> orderBy;

    public Map<String, String[]> toMap() throws IllegalAccessException {
        Map<String, String[]> result = new HashMap<>();

        for (Field f : this.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.getType().equals(String.class) && f.get(this) != null) {
                String[] array = new String[1];
                array[0] = (String) f.get(this);
                result.put(f.getName(), array);
            }
            f.setAccessible(false);
        }

        if (orderBy != null && orderBy.size() > 0) {
            String[] array = new String[orderBy.size()];
            for (int i = 0; i < orderBy.size(); i++) {
                array[i] = orderBy.get(i);
            }
            result.put("orderBy", array);
        }

        return result;
    }
}
