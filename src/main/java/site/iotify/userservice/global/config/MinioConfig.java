package site.iotify.userservice.global.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url.internal}")
    private String internalUrl;
    @Value("${minio.url.external}")
    private String externalUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean(name = "internalMinioClient")
    public MinioClient internalMinioClient() {
        return MinioClient.builder()
                .endpoint(internalUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean(name = "externalMinioClient")
    public MinioClient externalMinioClient() {
        return MinioClient.builder()
                .endpoint(externalUrl)
                .credentials(accessKey, secretKey)
                .build();
    }
}
