package org.example;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * 定时任务类
 *
 * @author huiziqin
 */
@Slf4j
/**
 * 不允许并发执行
 */
@DisallowConcurrentExecution
public class TaskJob implements Job {


  /**
   * 定时任务需要执行的逻辑
   *
   * @param jobExecutionContext
   * @throws JobExecutionException
   */
  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    log.info("执行任务:{}", jobExecutionContext);
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    log.info("clone");
    return super.clone();
  }


  @Override
  protected void finalize() throws Throwable {
    log.info("finalize");
    super.finalize();
  }
}