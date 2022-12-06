package org.example;

import javax.annotation.Resource;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务配置类
 */
@Configuration
public class ScheduleConfig {

  @Resource
  private SchedulerFactoryBean schedulerFactoryBean;

  @Bean
  public Scheduler scheduler() {
    return schedulerFactoryBean.getScheduler();
  }

}