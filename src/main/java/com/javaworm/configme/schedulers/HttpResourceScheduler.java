package com.javaworm.configme.schedulers;

import com.javaworm.configme.ConfigSource;
import com.javaworm.configme.FetchedDataHandler;
import com.javaworm.configme.RequestContext;
import com.javaworm.configme.ResourceScheduler;
import com.javaworm.configme.resources.ConfigSourceResource;
import com.javaworm.configme.sources.HttpSourceConfig;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResourceScheduler implements ResourceScheduler {
  private static final Logger log = LoggerFactory.getLogger(HttpResourceScheduler.class);
  private final HttpClient client;
  private final HttpRequestFactory httpRequestFactory;

  private final FetchedDataHandler fetchedDataHandler;
  private final Timer timer = new Timer(HttpResourceScheduler.class.getName());
  private final Map<String, TimerTask> resourceTimers = new ConcurrentHashMap<>();

  public HttpResourceScheduler(
      FetchedDataHandler fetchedDataHandler,
      HttpClient httpClient,
      HttpRequestFactory httpRequestFactory) {
    this.fetchedDataHandler = fetchedDataHandler;
    this.client = httpClient;
    this.httpRequestFactory = httpRequestFactory;
  }

  public void schedule(ConfigSource<HttpSourceConfig> configSource) {
    cancelCurrentTask(configSource);
    final var intervalSeconds = configSource.getSourceConfig().getIntervalSeconds();
    final var intervalMilliseconds = (int) (intervalSeconds * 1000.0);
    final TimerTask task =
        new TimerTask() {
          @Override
          public void run() {
            final var request = httpRequestFactory.create(configSource.getSourceConfig());
            log.debug("Fetching data from {}", request.uri());
            try {
              final HttpResponse<String> response;
              response = client.send(request, HttpResponse.BodyHandlers.ofString());
              if (response.statusCode() < 200 || response.statusCode() > 299) {
                configSource.getContext().emit("Error");
                log.warn(
                    "Fetching data from url {} failed with status code {} and body {}",
                    request.uri(),
                    response.statusCode(),
                    response.body());
                return;
              }
              final var body = response.body();

              fetchedDataHandler.handle(configSource, body);
            } catch (IOException | InterruptedException e) {
              configSource.getContext().emit("Error");
              log.error("Error in fetching data from " + request.uri(), e);
            }
          }
        };

    timer.schedule(task, 0, intervalMilliseconds);
    resourceTimers.put(configSource.getUid(), task);
    log.info(
        "Config source {} scheduled to refresh every {}s", configSource.getName(), intervalSeconds);
  }

  private void cancelCurrentTask(ConfigSource<HttpSourceConfig> configSource) {
    final var uid = configSource.getUid();
    final var currentTimerTask = resourceTimers.get(uid);
    if (currentTimerTask == null) {
      log.debug("Resource with UID [{}] has no task yet!", uid);
      return;
    }
    log.debug("Canceling task for Resource UID [{}]", uid);
    final var cancellationResult = currentTimerTask.cancel();
    log.debug("Canceling task result [{}]", cancellationResult);
    if (cancellationResult) {
      resourceTimers.remove(uid);
    }
  }

  @Override
  public void cancel(RequestContext<ConfigSourceResource> context) {
    final var uid = context.getResource().getMetadata().getUid();
    final var timerTask = resourceTimers.get(uid);
    if (timerTask != null) {
      log.debug("Canceling task for Resource UID [{}]", uid);
      final var cancellationResult = timerTask.cancel();
      log.debug("Canceling task result for UID [{}] is [{}]", uid, cancellationResult);
      if (cancellationResult) {
        resourceTimers.remove(uid);
      }
    }
  }
}
