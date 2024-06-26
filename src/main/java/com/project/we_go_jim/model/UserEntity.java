package com.project.we_go_jim.model;

import com.project.we_go_jim.util.TemporalBaseUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"})
)
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends TemporalBaseUtil {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @ColumnDefault("random_uuid()")
    private UUID id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;
    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "users_bookings",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "booking_id", referencedColumnName = "id")
    )
    private Set<BookingEntity> bookings = new HashSet<>();
}
