package io.nezha.event;

public class Result<T> {
	private T value;
	private boolean succeeded = true;
	private Throwable cause = null;
	private String desc = null;
	public Result(T value) {
		this.value = value;
	}
	public Result(T value, Throwable cause) {
		this.value = value;
		this.succeeded = false;
		this.cause = cause;
		this.desc = cause.getMessage();
	}
	public Result(Throwable cause, String desc) {
		this.value = null;
		this.succeeded = false;
		this.cause = cause;
		this.desc = desc;
	}
	public T getValue() {
		return value;
	}
	public boolean isSucceeded() {
		return succeeded;
	}
	public Throwable getCause() {
		return cause;
	}
	public String getDesc() {
		return desc;
	}
	@Override
	public String toString() {
		return "Result [value=" + value + ", succeeded=" + succeeded + ", cause=" + cause + ", desc=" + desc + "]";
	}
	
}
