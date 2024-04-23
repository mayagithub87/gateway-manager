package cu.musala.soft.gatewaysmanager.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cu.musala.soft.gatewaysmanager.domain.PeripheralStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter(AccessLevel.PUBLIC)
@Table(name = "peripherals")
@EntityListeners(AuditingEntityListener.class)
public class Peripheral {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    private int uid;

    private String vendor;

    @Enumerated(EnumType.STRING)
    private PeripheralStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "gateway_id", nullable = false)
    private Gateway gateway;

    @CreatedDate
    private Date createdDate;

    public Peripheral() {
        gateway = null;
    }

    public Peripheral(int uid, String vendor, PeripheralStatus status) {
        this.uid = uid;
        this.vendor = vendor;
        this.status = status;
    }
//
//    @PrePersist
//    public void onCreate() {
//        this.createdDate = new Date();
//    }
}
