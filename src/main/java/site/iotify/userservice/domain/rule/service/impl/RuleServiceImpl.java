package site.iotify.userservice.domain.rule.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.iotify.userservice.domain.rule.dto.request.RuleRequestDto;
import site.iotify.userservice.domain.rule.dto.response.RuleResponseDto;
import site.iotify.userservice.domain.rule.exception.NodeRedException;
import site.iotify.userservice.domain.rule.service.RuleService;
import site.iotify.userservice.domain.tenant.repository.TenantRepository;
import site.iotify.userservice.global.adaptor.ChirpstackAdaptor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {
    private final ChirpstackAdaptor chirpstackAdaptor;

    private RuleResponseDto createRule(RuleRequestDto ruleRequestDto) {
        String tenantId = ruleRequestDto.getTenantId();
        int tenantCount = chirpstackAdaptor.getTenantTotalCount();
        int port = 18000 + tenantCount;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "docker", "run", "-d",
                    "--name", tenantId, "-p", port + ":1880",
                    "nodered/node-red"
            );
            processBuilder.start();

        } catch (Exception e) {
            throw new NodeRedException("Node-Red 생성 실패", e);
        }

        return new RuleResponseDto(tenantId, port);
    }

    @Override
    public RuleResponseDto getRule(String tenantId) {
        try {
            Process checkProcess = new ProcessBuilder(
                    "docker", "port", tenantId
            ).start();
            checkProcess.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()));
            String portMapping = reader.readLine();
            reader.close();

            int port = 0;
            if (portMapping == null) {
                port = createRule(new RuleRequestDto(tenantId)).getPort();
            } else {
                port = Integer.parseInt(portMapping.split(":")[1]);
            }

            return new RuleResponseDto(tenantId, port);

        } catch (Exception e) {
            throw new NodeRedException("Node Red err", e);
        }
    }

    @Override
    public void removeRule(String tenantId) {
        try {
            new ProcessBuilder("docker", "stop", tenantId).start().waitFor();
            new ProcessBuilder("docker", "rm", tenantId).start().waitFor();
        } catch (Exception e) {
            throw new NodeRedException("Node-Red 삭제 실패", e);
        }
    }
}
