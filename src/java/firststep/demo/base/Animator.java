package firststep.demo.base;

import firststep.Canvas;

public abstract class Animator {
	public enum Aftermath {
		SAVE, REMOVE
	}
	
	private float startTime, duration;
	private Aftermath aftermath;
	
	public void doFrame(Canvas cnv, float currentTime) {
		if (currentTime > startTime) {
			frame(cnv, Math.min(currentTime - startTime, duration));
		}
	}
	
	public boolean isActual(float currentTime) {
		return currentTime < startTime + duration;
	}
	
	protected abstract void frame(Canvas cnv, float timeSinceStart);
	
	public Animator(float startTime, float duration, Aftermath aftermath) {
		this.startTime = startTime;
		this.duration = duration;
		this.aftermath = aftermath;
	}
	
	public float getStartTime() {
		return startTime;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public Aftermath getAftermath() {
		return aftermath;
	}
}
