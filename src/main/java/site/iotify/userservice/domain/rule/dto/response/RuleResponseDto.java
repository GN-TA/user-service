package site.iotify.userservice.domain.rule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.iotify.userservice.domain.rule.entity.Rule;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleResponseDto {
    public int id;
    public String tenantId;
    public String script;

    public static Rule toEntity(RuleResponseDto ruleResponseDto) {
        return new Rule(0, ruleResponseDto.getTenantId(), ruleResponseDto.getScript());
    }

    public static RuleResponseDto toDto(Rule rule) {
        return new RuleResponseDto(rule.getId(), rule.getTenantId(), rule.getScript());
    }
}
