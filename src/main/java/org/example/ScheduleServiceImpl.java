package org.example;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

  @Resource
  private Scheduler scheduler;

  /**
   * 添加定时任务Job
   */
  @SneakyThrows
  @Override
  public void addSchedule(ScheduleInfoDto dto) {
    JobKey jobKey = new JobKey(dto.getJobName(), dto.getJobGroup());
    //检查任务key是否存在
    if (scheduler.checkExists(jobKey)) {
      log.warn("该任务名称及任务组已存在！");
      return;
    }
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put("serveName", dto.getServeName());
    if (ObjectUtil.isNotEmpty(dto.getExtras())) {
      jobDataMap.put("extras", dto.getExtras());
    }
    //如果不传Job的话直接使用默认的Job
    if (StrUtil.isEmpty(dto.getJobClassName())) {
      dto.setJobClassName("org.example.TaskJob");
    }
    if (StrUtil.isEmpty(dto.getTriggerName())) {
      dto.setTriggerName("jobTrigger" + IdUtil.fastSimpleUUID());
    }
    if (StrUtil.isEmpty(dto.getTriggerGroup())) {
      dto.setTriggerGroup("jobTriggerGroup" + IdUtil.fastSimpleUUID());
    }
    try {
      JobDetail jobDetail = JobBuilder.newJob(
              (Class<? extends Job>) Class.forName(dto.getJobClassName()))
          .withIdentity(dto.getJobName(), dto.getJobGroup())
          .usingJobData(jobDataMap)
          .withDescription(dto.getDescName())
          .storeDurably()
          .build();
      CronTrigger cronTrigger = TriggerBuilder.newTrigger()
          .withIdentity(dto.getTriggerName(), dto.getTriggerGroup())
          .startNow()
          .withSchedule(CronScheduleBuilder.cronSchedule(dto.getCronExpression()))
          .build();
      scheduler.scheduleJob(jobDetail, cronTrigger);
      log.info("任务添加成功,任务信息：{}", dto);
    } catch (SchedulerException | ClassNotFoundException e) {
      log.error("任务添加异常异常，{}", e);
    }
  }

  /**
   * 执行一次
   */
  @Override
  public void executeOnce(String jobName, String jobGroup) {
    if (StrUtil.isEmpty(jobName) || StrUtil.isEmpty(jobGroup)) {
      log.warn("该任务名称:{} 任务组:{} 不存在！", jobName, jobGroup);
      return;
    }
    try {
      //执行任务
      scheduler.triggerJob(JobKey.jobKey(jobName, jobGroup));
      log.info("任务名：{}，任务组：{} ，执行成功", jobName, jobGroup);
    } catch (SchedulerException e) {
      log.error("任务名：{}，任务组：{},执行异常,{}", jobName, jobGroup, e);
    }
  }

  /**
   * 暂停定时任务
   *
   * @param jobName
   * @param jobGroup
   */
  @Override
  public void pauseSchedule(String jobName, String jobGroup) {
    try {
      scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
      log.info("任务名：{}，任务组：{} ，暂停成功", jobName, jobGroup);
    } catch (SchedulerException e) {
      log.error("任务名：{}，任务组：{}，暂停异常,{}", jobName, jobGroup, e);
    }
  }

  /**
   * 恢复定时任务
   *
   * @param jobName
   * @param jobGroup
   */
  @Override
  public void resumeSchedule(String jobName, String jobGroup) {
    try {
      scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
      log.info("任务名：{}，任务组：{} ，恢复成功", jobName, jobGroup);
    } catch (SchedulerException e) {
      log.error("任务名：{}，任务组：{}，恢复异常,{}", jobName, jobGroup, e);
    }
  }

  /**
   * 更新定时任务
   */
  @SneakyThrows
  @Override
  public void updateSchedule(ScheduleInfoDto dto) {
    JobKey jobKey = new JobKey(dto.getJobName(), dto.getJobGroup());
    //检查任务key是否存在
    if (scheduler.checkExists(jobKey)) {
      deleteSchedule(dto.getJobName(), dto.getJobGroup());
    }
    //按新的trigger重新设置job执行，重启触发器
    addSchedule(dto);
    log.info("任务更新成功：{}", dto);
  }

  /**
   * 删除定时任务
   *
   * @param jobName
   * @param jobGroup
   */
  @Override
  public void deleteSchedule(String jobName, String jobGroup) {
    try {
      scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
      log.info("任务名：{}，任务组：{} ，删除成功", jobName, jobGroup);
    } catch (SchedulerException e) {
      log.error("任务名：{}，任务组：{}，删除异常,{}", jobName, jobGroup, e);
    }
  }

  /**
   * 获取所有定时任务*
   *
   * @return
   */
  @Override
  public List<ScheduleInfoDto> getAllSchedule() {
    List<ScheduleInfoDto> list = new ArrayList<ScheduleInfoDto>();
    GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
    Set<JobKey> jobKeys = null;
    try {
      jobKeys = scheduler.getJobKeys(matcher);
      jobKeys.forEach(jobKey -> {
        try {
          List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
          triggers.forEach(trigger -> {
            ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
            //获取任务名
            scheduleInfoDto.setJobName(jobKey.getName());
            //获取任务组
            scheduleInfoDto.setJobGroup(jobKey.getGroup());
            try {
              //获取任务状态
              Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
              scheduleInfoDto.setTriggerState(triggerState.name());
              //获取任务表达式
              if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                scheduleInfoDto.setCronExpression(cronExpression);
              }
            } catch (SchedulerException e) {
              log.error("获取所有定时任务TriggerState出错 {}", e);
            }
            list.add(scheduleInfoDto);
          });
        } catch (SchedulerException e) {
          log.error("获取所有定时任务triggers出错 {}", e);
        }

      });
    } catch (SchedulerException e) {
      log.error("获取所有定时任务jobKeys发生异常 ,{}", e);
    }
    return list;
  }

  /**
   * 根据Cron表达式获取任务最近 几次的执行时间
   *
   * @param cron  cron表达式
   * @param count 次数
   * @return
   */
  @Override
  public List<String> getCronSchdule(String cron, int count) {
    List<String> retList = new ArrayList<String>();
    if (!CronExpression.isValidExpression(cron)) {
      //Cron表达式不正确
      return retList;
    }
    try {
      CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("Caclulate Date")
          .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date startTime = trigger.getStartTime();
      for (int i = 0; i < count; i++) {
        Date time = trigger.getFireTimeAfter(startTime);
        retList.add(format.format(time));
        startTime = time;
      }
    } catch (Exception e) {
      log.error("计算异常:{}", e);
    }
    return retList;
  }


}
