package pl.arekbednarz.travelcompany.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(name = "segments")
class Segments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String city;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Plans plan;

    public Segments() {
    }

    public Segments(String type, String city) {
        this.type = type;
        this.city = city;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

