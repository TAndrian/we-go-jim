package com.project.we_go_jim.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.we_go_jim.util.TemporalBaseUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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
    private Integer maxParticipant = 0;

    @JsonBackReference
    @ManyToMany(mappedBy = "bookings", fetch = FetchType.EAGER)
    private Set<UserEntity> users = new HashSet<>();
}
