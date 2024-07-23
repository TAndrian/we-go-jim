package com.project.we_go_jim.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.we_go_jim.util.TemporalBaseUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@Table(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
public class BookingEntity extends TemporalBaseUtil {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @ColumnDefault("random_uuid()")
    private UUID id;

    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "max_participant")
    private Integer maxParticipant;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER,
            mappedBy = "bookings", cascade = CascadeType.MERGE)
    private Set<UserEntity> users = new HashSet<>();
}
