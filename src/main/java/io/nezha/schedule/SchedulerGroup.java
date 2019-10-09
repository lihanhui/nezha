package io.nezha.schedule;

public class SchedulerGroup {
	private static IScheduler scheduler;
	private SchedulerGroup(){
		
	}
	public static void init(){
		scheduler = new DefaultScheduler(32, 128);
	}
	
	public static IScheduler getScheduler(){
		return scheduler;
	}
}
