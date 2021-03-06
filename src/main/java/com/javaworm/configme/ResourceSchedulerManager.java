package com.javaworm.configme;

import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.schedulers.http.HttpRequestFactory;
import com.javaworm.configme.schedulers.http.HttpResourceScheduler;
import io.fabric8.kubernetes.client.KubernetesClient;
import java.net.http.HttpClient;
import java.util.Map;

public class ResourceSchedulerManager {
  private final ConfigSourceFactory configSourceFactory;

  private final Map<String, ResourceScheduler> sourceTypeSchedulers;

  public ResourceSchedulerManager(
      ConfigSourceFactory configSourceFactory,
      KubernetesClient k8sClient,
      FetchedDataHandler fetchedDataHandler) {
    this.configSourceFactory = configSourceFactory;
    sourceTypeSchedulers =
        Map.of(
            "http",
            new HttpResourceScheduler(
                fetchedDataHandler,
                HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build(),
                new HttpRequestFactory(k8sClient)));
  }

  public void schedule(RequestContext<ConfigSourceResource> context) {
    cancelExistingScheduleFor(context);
    final var configSource = this.configSourceFactory.create(context);
    final var sourceType = configSource.getSourceType();
    final var scheduler = sourceTypeSchedulers.get(sourceType);
    if (scheduler == null) {
      final var errorMessage = String.format("No scheduler found for source type [%s]", sourceType);
      context.emit(errorMessage);
      throw new RuntimeException(errorMessage);
    }
    scheduler.schedule(configSource);
  }

  public void cleanup(RequestContext<ConfigSourceResource> context) {
    cancelExistingScheduleFor(context);
  }

  private void cancelExistingScheduleFor(RequestContext<ConfigSourceResource> context) {
    sourceTypeSchedulers.values().forEach(scheduler -> scheduler.cancel(context));
  }
}
