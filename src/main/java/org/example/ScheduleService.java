package org.example;


import java.util.List;

public interface ScheduleService {

  /**
   * 添加定时任务Job
   */
  void addSchedule(ScheduleInfoDto dto);

  void executeOnce(String jobName, String jobGroup);

  /**
   * 暂停定时任务
   *
   * @param jobName
   * @param jobGroup
   */
  void pauseSchedule(String jobName, String jobGroup);

  /**
   * 恢复定时任务
   *
   * @param jobName
   * @param jobGroup
   */
  void resumeSchedule(String jobName, String jobGroup);

  /**
   * 更新定时任务
   */
  void updateSchedule(ScheduleInfoDto dto);

  /**
   * 删除定时任务
   *
   * @param jobName
   * @param jobGroup
   */
  void deleteSchedule(String jobName, String jobGroup);

  /**
   * 获取所有的定时任务
   *
   * @return 定时任务列表
   */
  List<ScheduleInfoDto> getAllSchedule();

  /**
   * 根据Cron表达式获取任务最近 几次的执行时间
   *
   * @param cron  cron表达式
   * @param count 次数
   * @return
   */
  List<String> getCronSchdule(String cron, int count);
}
