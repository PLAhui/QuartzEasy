package org.example;

import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: CJ
 * @Date: 2021-11-2 11:41
 */
@Slf4j
@Controller
@RequestMapping(path = "/quartz")
public class QuartzController {


  @Resource
  private ScheduleService scheduleService;

  @PostMapping(path = "/addjob")
  @ResponseBody
  public void addjob(String jName, String jGroup, String tName, String tGroup, String cron) {
    ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
    scheduleInfoDto.setJobGroup(jGroup);
    scheduleInfoDto.setJobName(jName);
    scheduleInfoDto.setJobClassName("org.example.TaskJob");
    scheduleInfoDto.setCronExpression(cron);
    scheduleService.addSchedule(scheduleInfoDto);
  }

  @PostMapping(path = "/executeOnce")
  @ResponseBody
  public void executeOnce(String jName, String jGroup, String tName, String tGroup, String cron) {
    scheduleService.executeOnce(jName, jGroup);
  }


  @PostMapping(path = "/updateSchedule")
  @ResponseBody
  public void updateSchedule(String jName, String jGroup, String tName, String tGroup,
      String cron) {
    ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
    scheduleInfoDto.setJobGroup(jGroup);
    scheduleInfoDto.setJobName(jName);
    scheduleInfoDto.setJobClassName("org.example.TaskJob");
    scheduleInfoDto.setCronExpression(cron);
    scheduleService.updateSchedule(scheduleInfoDto);
  }

  @PostMapping(path = "/allSchedule")
  @ResponseBody
  public List<ScheduleInfoDto> allSchedule(String jName, String jGroup, String tName,
      String tGroup,
      String cron) {
    List<ScheduleInfoDto> allSchedule = scheduleService.getAllSchedule();
    System.out.println(allSchedule);
    return allSchedule;
  }

  @PostMapping(path = "/pauseSchedule")
  @ResponseBody
  public void pauseSchedule(String jName, String jGroup, String tName,
      String tGroup,
      String cron) {

    scheduleService.pauseSchedule(jName, jGroup);

  }

  @PostMapping(path = "/resumeSchedule")
  @ResponseBody
  public void resumeSchedule(String jName, String jGroup, String tName,
      String tGroup,
      String cron) {

    scheduleService.resumeSchedule(jName, jGroup);
  }


  @PostMapping(path = "/deleteSchedule")
  @ResponseBody
  public void deleteSchedule(String jName, String jGroup, String tName,
      String tGroup,
      String cron) {

    scheduleService.deleteSchedule(jName, jGroup);
  }


  @PostMapping(path = "/getCronSchdule")
  @ResponseBody
  public List<String> getCronSchdule(String cron, int count) {
    return scheduleService.getCronSchdule(cron, count);
  }


}
