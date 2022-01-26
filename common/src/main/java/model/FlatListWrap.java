package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@Data
@XmlRootElement(name = "flatList")
@XmlAccessorType(XmlAccessType.FIELD)
@XStreamAlias("flatList")
public class FlatListWrap implements Serializable {

    @XStreamImplicit
    @XmlElement(name = "flat")
    private List<Flat> flats;

    @XmlElement
    private Integer totalFlats;

    public FlatListWrap() {
    }

    public FlatListWrap(List<Flat> flats) {
        this.flats = flats;
        this.totalFlats = flats.size();
    }

    public FlatListWrap(List<Flat> flats, int totalFlats) {
        this.flats = flats;
        this.totalFlats = totalFlats;
    }
}
