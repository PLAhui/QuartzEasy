package org.example;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuartzTest {

  @Resource
  private ScheduleService scheduleService;

  @Test
  public void testAddSchedule() {
    ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
    scheduleInfoDto.setJobGroup("LOL");
    scheduleInfoDto.setJobName("寒冰射手");
    scheduleInfoDto.setJobClassName("org.example.AmazonDataCaptureJob");
    scheduleInfoDto.setCronExpression("0 30 16 * * ?");
    scheduleService.addSchedule(scheduleInfoDto);
    
    scheduleService.executeOnce("寒冰射手", "LOL");
    //TimeUnit.SECONDS.sleep(1000);
  }

  @Test
  public void testExecuteOnce() throws InterruptedException {
    scheduleService.executeOnce("寒冰射手", "LOL");
    TimeUnit.SECONDS.sleep(1000);
  }

  @Test
  public void testUdateSchedule() {
    ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
    scheduleInfoDto.setJobGroup("狗子组");
    scheduleInfoDto.setJobName("哈巴狗");
    scheduleInfoDto.setJobClassName("com.sifan.erp.common.AmazonDataCaptureJob");
    scheduleInfoDto.setCronExpression("0 34 16 * * ?");
    scheduleService.updateSchedule(scheduleInfoDto);
  }

  @Test
  public void testGetAllSchedule() {
    List<ScheduleInfoDto> allSchedule = scheduleService.getAllSchedule();
    System.out.println(allSchedule);
  }

  @Test
  public void testPauseSchedule() {
    scheduleService.pauseSchedule("哈巴狗", "狗子组");
  }

  @Test
  public void testResumeSchedule() {
    scheduleService.resumeSchedule("哈巴狗", "狗子组");
  }
}