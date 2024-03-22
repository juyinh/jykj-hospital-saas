package org.jeecg.modules.quartz.job;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.monitor.service.RedisService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/*
 *@Description: 清除redis过期缓存
 *@Param:
 *@Return:
 *@author: xiaopeng.wu
 *@DateTime: 11:01 2023/12/13
**/
@Slf4j
@Component
public class ClearRedisExpirJob implements Job {
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info(" Job Execution key："+jobExecutionContext.getJobDetail().getKey());
		log.info(String.format(" 定时清除redis过期数据 :" + DateUtils.getTimestamp()));
		List<String> keyAll = new ArrayList();
		keyAll.add("patient_*");
		keyAll.add("doctor_*");
		for (String key : keyAll) {
			Set<String> patientKeySet = redisTemplate.keys(key);
			for (String deleteKey : patientKeySet) {
				String[] keyArr = deleteKey.split("_");
				try {
					Date dayDate = DateUtils.parseDate(keyArr[keyArr.length -1], "yyyy-MM-dd");
					if (dayDate.getTime() < DateUtil.offsetDay(new Date(), -1).getTime()){
						redisTemplate.delete(deleteKey);
					}
				} catch (ParseException e) {
					log.error("删除过期redis的key值失败：{}", e);
				}
			}
		}
	}
}
