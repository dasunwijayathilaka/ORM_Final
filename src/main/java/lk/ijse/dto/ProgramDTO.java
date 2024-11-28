package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProgramDTO {
    private String programId;
    private String programName;
    private String duration;
    private double fee;

    public ProgramDTO(String id, String name, String duration, String fee) {
        this.programId = id;
        this.programName = name;
        this.duration = duration;
        this.fee = Double.parseDouble(fee);
    }
}
