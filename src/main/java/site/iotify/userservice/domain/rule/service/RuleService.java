package site.iotify.userservice.domain.rule.service;

import site.iotify.userservice.domain.rule.dto.request.RuleRequestDto;
import site.iotify.userservice.domain.rule.dto.response.RuleResponseDto;

import java.util.List;

public interface RuleService {
    void saveRule(RuleRequestDto ruleRequestDto);

    List<RuleResponseDto> getRule(String tenantId);
}
