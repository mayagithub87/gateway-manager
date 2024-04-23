package cu.musala.soft.gatewaysmanager.persistence.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter(AccessLevel.PUBLIC)
@Setter
@Table(name = "gateways")
public class Gateway {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String serialNumber;

    private String name;

    private String ip4vAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "gateway")
    private List<Peripheral> peripherals = new ArrayList<>();

    public Gateway() {
    }

    public Gateway(String serial, String name, String ip) {
        this.serialNumber = serial;
        this.name = name;
        this.ip4vAddress = ip;
    }

}
