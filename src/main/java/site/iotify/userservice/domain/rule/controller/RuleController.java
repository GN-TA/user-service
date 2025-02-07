package site.iotify.userservice.domain.rule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.iotify.userservice.domain.rule.dto.request.RuleRequestDto;
import site.iotify.userservice.domain.rule.dto.response.RuleResponseDto;
import site.iotify.userservice.domain.rule.service.RuleService;

import java.util.List;

@RequestMapping("/rule")
@RestController
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @PostMapping
    public void registerRule(@RequestBody RuleRequestDto ruleRequestDto) {
        ruleService.saveRule(ruleRequestDto);
    }

    @GetMapping
    public List<RuleResponseDto> getRules(@RequestParam String tenantId) {
        return ruleService.getRule(tenantId);
    }
}
