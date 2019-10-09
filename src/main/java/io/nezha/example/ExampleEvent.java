package io.nezha.example;

import io.nezha.event.AbstractEvent;
import io.nezha.event.Result;
import io.nezha.schedule.DefaultScheduler;
import io.nezha.schedule.IScheduler;

public class ExampleEvent extends AbstractEvent {

	@Override
	public Result<Void> handleEvent() {
		return null;
	}
    public static void main(String[] args) {
    		IScheduler  scheduler = new DefaultScheduler(4,8);
    		for(int index = 0; index < 100; index++ ) {
    			scheduler.submitEvent(String.valueOf(index), new ExampleEvent());
    		}
    		while(true) {
    			try {
	    			Thread.sleep(10000L);
	    		}catch(Throwable e){
	    			
	    		}
    		}
    }
}
