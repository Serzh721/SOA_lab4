package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "soa_flat")
@XmlRootElement(name = "flat")
@XmlAccessorType(XmlAccessType.FIELD)
@XStreamAlias("flat")
public class Flat implements Serializable {
    @Id
    @GeneratedValue
    @XmlElement
    private Integer id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @NotEmpty
    @NotNull
    @XmlElement
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    @Embedded
    @XmlElement
    private Coordinates coordinates; //Поле не может быть null

    @XmlElement
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Min(0)
    @XmlElement
    private Float area; //Значение поля должно быть больше 0

    @Min(1)
    @XmlElement
    private Long numberOfRooms; //Значение поля должно быть больше 0

    @Min(0)
    @XmlElement
    private Double price; //Значение поля должно быть больше 0

    @Min(0)
    @XmlElement
    private Float kitchenArea; //Поле может быть null, Значение поля должно быть больше 0

    @XmlElement
    private View view; //Поле может быть null

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @XmlElement
    private House house; //Поле не может быть null

    public void copy(Flat fl) {
        this.name = fl.name;
        this.coordinates = fl.coordinates;
        this.area = fl.area;
        this.numberOfRooms = fl.numberOfRooms;
        this.price = fl.price;
        this.kitchenArea = fl.kitchenArea;
        this.view = fl.view;
        this.house = fl.house;
    }
}
