package lk.ijse.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class RegistrationTM {
    private int registrationId;
    private int studentId;
    private String studentName;
    private String proId;
    private String proName;
    private double paidAmount;
    private double fee;
}
