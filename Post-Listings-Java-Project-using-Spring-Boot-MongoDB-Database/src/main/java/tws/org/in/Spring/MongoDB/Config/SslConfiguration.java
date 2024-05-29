package tws.org.in.Spring.MongoDB.Config;

import java.io.InputStream;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
public class SslConfiguration {
  @Value("classpath:ca.jks")
  public String fileLocation = "classpath:ca.jks";

  @Value("changeit")
  public String keyStorePass = "changeit";

  private static final String CLASSPATH_LOC = "classpath:";

  @Bean
  @Scope("singleton")
  public KeyStore getKs() {
    InputStream is = null;
    KeyStore ks;

    try {
      if (fileLocation.startsWith(CLASSPATH_LOC)) {
        is = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(fileLocation.substring(CLASSPATH_LOC.length()));
      }

      if (is != null) {
        ks = KeyStore.getInstance("JKS");
        ks.load(is, keyStorePass.toCharArray());
        is.close();

        return ks;
      }

      return null;
    } catch (Exception e) {
      log.error("Exception: {}", ExceptionUtils.getStackTrace(e));
      throw new IllegalArgumentException(e);
    }
  }

  @Bean
  public SSLContext createSslContext(TrustManager trustManager) {
    try {
      SSLContext c = SSLContext.getInstance("TLSv1.2");
      c.init(null, new TrustManager[]{trustManager}, null);

      return c;
    } catch (Exception e) {
      log.error("Exception: {}", ExceptionUtils.getStackTrace(e));
      throw new IllegalArgumentException(e);
    }
  }

  @Bean
  @Scope("singleton")
  public TrustManagerFactory getTrustManager(@Autowired KeyStore ks) {
    TrustManagerFactory tmf;

    try {
      tmf = TrustManagerFactory.getInstance("X509");
      tmf.init(ks);
    } catch (Exception e) {
      log.error("Exception: {}", ExceptionUtils.getStackTrace(e));
      throw new IllegalArgumentException(e);
    }

    return tmf;
  }

  @Bean
  public TrustManager createTrustManager(TrustManagerFactory tmf) {
    return tmf.getTrustManagers()[0];
  }
}