package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PaymentDTO {
    private int paymentId;
    private RegistrationDTO registration;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;
}

