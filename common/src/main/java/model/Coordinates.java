package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@XmlRootElement(name = "coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
@XStreamAlias("coordinates")
public class Coordinates implements Serializable {

    @XmlElement
    private Long x;

    @Max(22)
    @XmlElement
    private Double y; //Максимальное значение поля: 22

    public static Coordinates fromString(String s) {
        String[] coords = s.split(",");
        if (coords.length != 2) {
            throw new IllegalArgumentException("invalid coordinates format");
        }
        Coordinates result = new Coordinates();
        result.setX(Long.parseLong(coords[0]));
        result.setY(Double.parseDouble(coords[1]));
        return result;
    }
}