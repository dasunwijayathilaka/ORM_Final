package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "reg_date")
    @Temporal(TemporalType.DATE)
    private Date regDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Registration> registrations = new HashSet<>();

    public Student(int studentId, String name, String address, String phone, Date regDate, User user) {
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.regDate = regDate;
        this.user = user;
    }

    public Student(int studentId, String name, String address, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Student(int studentId) {
        this.studentId = studentId;
    }
}
