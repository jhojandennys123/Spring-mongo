package tws.org.in.Spring.MongoDB.Config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import tws.org.in.Spring.MongoDB.Bean.MongoProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MongoConfiguration {
  private final SSLContext context;
  private final MongoProperties mongoProperties;
  @Bean
  public MongoClientSettings mongoClientSettings() {
    return MongoClientSettings.builder()
        .applyToSslSettings(
            builder -> builder
                .enabled(true)
                .context(context))
        .applyToServerSettings(
            builder -> builder
                .heartbeatFrequency(10000, TimeUnit.MILLISECONDS)
                .minHeartbeatFrequency(500, TimeUnit.MILLISECONDS)
        )
        .applyConnectionString(
            new ConnectionString(mongoProperties.getUri())
        )
        .build();
  }
}