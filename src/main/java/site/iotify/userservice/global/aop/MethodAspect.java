package site.iotify.userservice.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import site.iotify.userservice.domain.tenant.dto.TenantResponseDto;
import site.iotify.userservice.global.adaptor.ChirpstackAdaptor;
import site.iotify.userservice.global.exception.UnAuthorizedException;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class MethodAspect {
    private final ChirpstackAdaptor chirpstackAdaptor;

    @Before("@annotation(site.iotify.userservice.global.annotations.TenantAdminOnly)")
    public void isTenantAdmin() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();

        String userId = request.getHeader("X-USER-ID");
        String tenantId = Arrays.stream(request.getServletPath().split("/"))
                .dropWhile(e -> !"tenant".equals(e))
                .skip(1)
                .findFirst()
                .orElse(null);
        TenantResponseDto.TenantUserGet tenantUser = chirpstackAdaptor.getTenantUser(tenantId, userId);

        if (!tenantUser.getTenantUser().isAdmin()) {
            throw new UnAuthorizedException();
        }
    }
}
