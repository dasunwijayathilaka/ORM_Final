package lk.ijse.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class StudentTm {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private Date registerDate;
    private int userId;
}