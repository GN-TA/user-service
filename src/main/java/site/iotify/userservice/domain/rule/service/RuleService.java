package site.iotify.userservice.domain.rule.service;

import site.iotify.userservice.domain.rule.dto.request.RuleRequestDto;
import site.iotify.userservice.domain.rule.dto.response.RuleResponseDto;

import java.util.List;

public interface RuleService {
//    RuleResponseDto createRule(RuleRequestDto ruleRequestDto);

    RuleResponseDto getRule(String tenantId);

    void removeRule(String tenantId);
}
