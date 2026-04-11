package net.justapie.ookami.advice;

import jakarta.servlet.http.HttpServletRequest;
import net.justapie.ookami.annotations.VerifyWebhookSignature;
import net.justapie.ookami.utils.SignatureUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@ControllerAdvice
public class WebhookSignatureAdvice extends RequestBodyAdviceAdapter {
    @Value("${app.secrets.webhook}")
    private String webhookSecret;

    @Override
    public boolean supports(MethodParameter methodParameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(VerifyWebhookSignature.class) ||
                methodParameter.getContainingClass().isAnnotationPresent(VerifyWebhookSignature.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(@NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) throw new IllegalStateException("Not in a web request");

        HttpServletRequest request = attributes.getRequest();
        String signatureHeader = request.getHeader("x-authgear-body-signature");

        byte[] bodyBytes = inputMessage.getBody().readAllBytes();

        if (bodyBytes.length == 0) {
            throw new SecurityException("Empty Body");
        }

        try {
            String sig = SignatureUtils.hmacSHA256String(bodyBytes, webhookSecret.getBytes());
            if (!SignatureUtils.constantTimeCompare(signatureHeader, sig)) {
                throw new SecurityException("Invalid Signature");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("HMAC Signing Failed", e);
        }

        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(bodyBytes);
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }
}