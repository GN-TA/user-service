package site.iotify.userservice.domain.rule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RuleResponseDto {
    private String host;
    private int port;
}
