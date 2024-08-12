package ru.gb.aspect.logging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(RecoverProperties.class)
@ConditionalOnProperty(value = "application.recover.enabled", havingValue = "true")
public class RecoverAutoConfiguration {

  @Bean
  public RecoverAspect recoverAspect(RecoverProperties properties) {
    log.info("Recover configuration loaded: level={}, noRecoverFor={}",
        properties.getLevel(), properties.getNoRecoverFor());
    return new RecoverAspect(properties);
  }

}