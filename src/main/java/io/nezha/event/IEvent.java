package io.nezha.event;

public interface IEvent {
	<T> Result<T> handleEvent();
	//default IChannel channel() {return null;}
}
