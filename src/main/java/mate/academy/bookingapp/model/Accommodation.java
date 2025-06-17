package mate.academy.bookingapp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "accommodations")
@Getter
@Setter
@ToString
@SQLDelete(sql = "UPDATE accommodations SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccommodationType type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address location;

    @Column(nullable = false)
    private String size;

    @ElementCollection
    private List<String> amenities = new ArrayList<>();

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal dailyRate;

    @Column(nullable = false)
    private Integer availability;

    private boolean isDeleted;
}

