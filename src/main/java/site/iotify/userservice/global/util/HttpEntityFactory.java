package site.iotify.userservice.global.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpEntityFactory {
    private HttpEntityFactory() {
        throw new UnsupportedOperationException("Utility Class cannot be instanced!");
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private HttpHeaders headers = new HttpHeaders();
        private T body;

        public Builder<T> contentType(MediaType mediaType) {
            headers.setContentType(mediaType);
            return this;
        }

        public Builder<T> setBearerAuth(String auth) {
            headers.setBearerAuth(auth);
            return this;
        }

        public Builder<T> addHeader(String key, String value) {
            headers.set(key, value);
            return this;
        }

        public Builder<T> body(T body) {
            this.body = body;
            return this;
        }

        public HttpEntity<T> build() {
            return new HttpEntity<>(body, headers);
        }
    }

}
