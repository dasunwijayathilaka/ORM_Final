package lk.ijse.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO {
    private int userId;
    private String username;
    private String password;
    private String role;

    public UserDTO(int userID) {
        this.userId = userID;
    }
}
