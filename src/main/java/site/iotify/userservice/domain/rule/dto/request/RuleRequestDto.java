package site.iotify.userservice.domain.rule.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RuleRequestDto {

    @Getter
    @AllArgsConstructor
    public static class RuleCreate {
        String devEui;
        String tenantId;
    }

}
