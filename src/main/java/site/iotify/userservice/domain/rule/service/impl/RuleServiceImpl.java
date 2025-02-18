package site.iotify.userservice.domain.rule.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.iotify.userservice.domain.rule.dto.request.RuleRequestDto;
import site.iotify.userservice.domain.rule.dto.response.RuleResponseDto;
import site.iotify.userservice.domain.rule.repository.RuleRepository;
import site.iotify.userservice.domain.rule.service.RuleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;

    @Override
    public void saveRule(RuleRequestDto ruleRequestDto) {
        ruleRepository.save(RuleRequestDto.toEntity(ruleRequestDto));
    }

    @Override
    public List<RuleResponseDto> getRule(String tenantId) {
        return ruleRepository.findAllByTenantId(tenantId).stream()
                .map(RuleResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
