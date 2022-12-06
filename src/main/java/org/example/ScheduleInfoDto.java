package org.example;


import java.io.Serializable;
import lombok.Data;


@Data
public class ScheduleInfoDto implements Serializable {

  //@ApiModelProperty(value = "服务名称")
  private String serveName;

  //@ApiModelProperty(value = "任务描述")
  private String descName;

  //@ApiModelProperty(value = "任务名称")
  private String jobName;

  //@ApiModelProperty(value = "任务组")
  private String jobGroup;

  //@ApiModelProperty(value = "任务路径")
  private String jobClassName;

  //@ApiModelProperty(value = "调度器名称")
  private String triggerName;

  //@ApiModelProperty(value = "调度器组")
  private String triggerGroup;

  //@ApiModelProperty(value = "表达式")
  private String cronExpression;

  //@ApiModelProperty(value = "附加参数，会传给定时任务接口")
  private String extras;

  /**
   * 状态 ： NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED;
   */
  private String triggerState;

}
