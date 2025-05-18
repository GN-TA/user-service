package site.iotify.userservice.domain.rule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.iotify.userservice.domain.rule.dto.request.RuleRequestDto;
import site.iotify.userservice.domain.rule.dto.response.RuleResponseDto;
import site.iotify.userservice.domain.rule.service.RuleService;


@RequestMapping("/v1/rule")
@RestController
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @PostMapping
    public void createRule(@RequestBody RuleRequestDto.RuleCreate ruleRequestDto) {
        ruleService.createRule(ruleRequestDto);
    }

    @GetMapping
    public RuleResponseDto getRules(@RequestParam String tenantId) {
        return ruleService.getRule(tenantId);
    }

    @DeleteMapping("/{tenantId}")
    public void removeRule(@PathVariable String tenantId) {
        ruleService.removeRule(tenantId);
    }
}
