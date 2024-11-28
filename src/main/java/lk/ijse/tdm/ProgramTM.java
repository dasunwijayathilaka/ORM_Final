package lk.ijse.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class ProgramTM {
    private String id;
    private String name;
    private String duration;
    private double fee;
}
