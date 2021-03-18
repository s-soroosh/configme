package com.javaworm.configme;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaworm.configme.controllers.ConfigResourceController;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class OperatorRunner {
  public static void main(String[] args) {
    Quarkus.run(Runner.class, args);
  }

  static class Runner implements QuarkusApplication {
    @Override
    public int run(String... args) {
      final var k8sClient = new DefaultKubernetesClient();
      final var operator = new Operator(k8sClient);
      final var objectMapper =
          new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      final var meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
      final var fetchedDataHandler = new FetchedDataHandler(k8sClient, meterRegistry);
      final var configSourceFactory = new ConfigSourceFactory();
      final var resourceScheduler =
          new ResourceSchedulerManager(configSourceFactory, k8sClient, fetchedDataHandler);
      final var controller = new ConfigResourceController(resourceScheduler);
      operator.registerControllerForAllNamespaces(controller);
      Quarkus.waitForExit();
      return 0;
    }
  }
}
