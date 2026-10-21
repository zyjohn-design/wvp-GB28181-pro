package com.genersoft.iot.vmp.vmanager.server;

import com.genersoft.iot.vmp.conf.GlobalResponseAdvice;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ReadinessLifecycleTest {
    @Configuration(proxyBeanMethods = false)
    @EnableWebMvc
    @Import({ReadinessController.class, GlobalResponseAdvice.class, ApplicationAvailabilityAutoConfiguration.class})
    static class TestApplication {
        @Bean TomcatServletWebServerFactory webServerFactory() { return new TomcatServletWebServerFactory(0); }
        @Bean DispatcherServletRegistrationBean dispatcherServletRegistration(WebApplicationContext context) {
            return new DispatcherServletRegistrationBean(new DispatcherServlet(context), "/");
        }
    }

    @Test
    void actualHttpReadinessWaitsForAllReadyEventHandlers() throws Exception {
        runStartup(false);
    }

    @Test
    void failedReadyEventCannotBeReportedAsSuccessfulStartup() throws Exception {
        runStartup(true);
    }

    private void runStartup(boolean failStartup) throws Exception {
        CountDownLatch readyHandlerEntered = new CountDownLatch(1);
        CountDownLatch releaseHandler = new CountDownLatch(1);
        AtomicInteger port = new AtomicInteger();
        ApplicationListener<ApplicationEvent> listener = event -> {
            if (event instanceof ServletWebServerInitializedEvent web) port.set(web.getWebServer().getPort());
            if (event instanceof ApplicationReadyEvent) {
                readyHandlerEntered.countDown();
                try {
                    if (!releaseHandler.await(20, TimeUnit.SECONDS)) throw new IllegalStateException("test timeout");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(e);
                }
                if (failStartup) throw new IllegalStateException("simulated subscription recovery failure");
            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ConfigurableApplicationContext> startup = executor.submit(() -> new SpringApplicationBuilder(TestApplication.class)
                .listeners(listener).properties("spring.main.banner-mode=off").run());
        ConfigurableApplicationContext context = null;
        try (HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build()) {
            assertTrue(readyHandlerEntered.await(20, TimeUnit.SECONDS));
            URI uri = URI.create("http://127.0.0.1:" + port.get() + "/api/server/health/ready");
            HttpRequest request = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(3)).build();
            HttpResponse<String> waiting = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(503, waiting.statusCode(), "open Tomcat port must not imply startup completed");
            assertEquals("{\"status\":\"NOT_READY\"}", waiting.body());
            releaseHandler.countDown();
            if (failStartup) {
                ExecutionException failure = assertThrows(ExecutionException.class, () -> startup.get(20, TimeUnit.SECONDS));
                assertTrue(failure.getCause().getMessage().contains("simulated subscription recovery failure"));
            } else {
                context = startup.get(20, TimeUnit.SECONDS);
                HttpResponse<String> ready = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, ready.statusCode());
                assertEquals("{\"status\":\"UP\"}", ready.body());
            }
        } finally {
            releaseHandler.countDown();
            if (context != null) context.close();
            else if (!startup.isDone()) {
                try { startup.get(20, TimeUnit.SECONDS).close(); } catch (Exception ignored) { startup.cancel(true); }
            }
            executor.shutdownNow();
        }
    }
}
