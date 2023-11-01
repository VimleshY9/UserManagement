package dev.vimlesh.userservicetestfinal.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Setter
@Getter
public class SignUpResponseDto {
    private String userName;
    @Value("${custom.signUpMessage}")
    private String signupMessage;
}
