package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "soa_house")
@XmlRootElement(name = "house")
@XmlAccessorType(XmlAccessType.FIELD)
@XStreamAlias("house")
public class House implements Serializable {
    @Id
    @GeneratedValue
    @XmlElement
    private Integer id;

    @NotNull
    @XmlElement
    private String name; //Поле не может быть null

    @Min(1)
    @XmlElement
    private Integer year; //Значение поля должно быть больше 0

    @Min(1)
    @Max(75)
    @XmlElement
    private Integer numberOfFloors; //Максимальное значение поля: 75, Значение поля должно быть больше 0

    @Min(1)
    @XmlElement
    private Integer numberOfFlatsOnFloor; //Значение поля должно быть больше 0

    @Min(1)
    @XmlElement
    private Integer numberOfLifts; //Значение поля должно быть больше 0

    public void copy(House house) {
        this.name = house.name;
        this.year = house.year;
        this.numberOfFloors = house.numberOfFloors;
        this.numberOfFlatsOnFloor = house.numberOfFlatsOnFloor;
        this.numberOfLifts = house.numberOfLifts;
    }
}
