package site.iotify.userservice.domain.rule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.iotify.userservice.domain.rule.entity.Rule;

@Data
@AllArgsConstructor
public class RuleRequestDto {
    String tenantId;
    String script;

    public static Rule toEntity(RuleRequestDto ruleRequestDto) {
        return new Rule(0, ruleRequestDto.getTenantId(), ruleRequestDto.getScript());
    }

    public static RuleRequestDto toDto(Rule rule) {
        return new RuleRequestDto(rule.getTenantId(), rule.getScript());
    }
}
