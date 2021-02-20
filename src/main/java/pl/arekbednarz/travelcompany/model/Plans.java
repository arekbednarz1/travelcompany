package pl.arekbednarz.travelcompany.model;

import org.springframework.data.annotation.Id;

import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = "plans")
public class Plans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer time;

    @OneToMany
    private List<Segments> segments;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
