package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "programs")
public class Program {

    @Id
    @Column(name = "program_id", length = 10)
    private String programId;

    @Column(name = "program_name", nullable = false, length = 100)
    private String programName;

    @Column(name = "duration", length = 20)
    private String duration;

    @Column(name = "fee")
    private double fee;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Registration> registrations = new HashSet<>();

    public Program(String programId, String programName, String duration, double fee) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
    }

    public Program(String programId) {
        this.programId = programId;
    }
}
