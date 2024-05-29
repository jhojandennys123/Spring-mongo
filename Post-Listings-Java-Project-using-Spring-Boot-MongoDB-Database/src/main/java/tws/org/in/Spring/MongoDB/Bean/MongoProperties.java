package tws.org.in.Spring.MongoDB.Bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.data.mongodb")
@Component
@Getter
@Setter
public class MongoProperties {
  private String uri;
}
