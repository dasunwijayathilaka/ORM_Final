package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(name = "registration_date")
    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @Column(name = "paid_amount")
    private double paidAmount;

    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    public Registration(int regId, Student student, Program program, Date registrationDate, double paidAmount, int paymentId) {
        this.regId = regId;
        this.student = student;
        this.program = program;
        this.registrationDate = registrationDate;
        this.paidAmount = paidAmount;
    }

    public Registration(int regId, Student student, Program program, Date registrationDate, double paidAmount) {
        this.regId = regId;
        this.student = student;
        this.program = program;
        this.registrationDate = registrationDate;
        this.paidAmount = paidAmount;
    }

    public Registration(int regId) {
        this.regId = regId;
    }

    public Registration(int regId, double paidAmount) {
        this.regId = regId;
        this.paidAmount = paidAmount;
    }
}

