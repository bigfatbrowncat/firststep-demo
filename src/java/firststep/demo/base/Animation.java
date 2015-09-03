package firststep.demo.base;

import firststep.Framebuffer;

public abstract class Animation {
	public enum Aftermath {
		SAVE, REMOVE
	}

	private float startTime, duration;
	private Aftermath aftermath;
	
	public void doFrame(Framebuffer fb, float currentTime) {
		if (currentTime > startTime) {
			if (currentTime < startTime + duration || aftermath == Aftermath.SAVE) {
				frame(fb, Math.min(currentTime - startTime, duration));
			}
		}
	}
	
	public boolean isActual(float currentTime) {
		return currentTime < startTime + duration;
	}
	
	protected abstract void frame(Framebuffer fb, float timeSinceStart);
	
	public Animation(float startTime, float duration, Aftermath aftermath) {
		this.startTime = startTime;
		this.duration = duration;
		this.aftermath = aftermath;
	}
	
	/**
	 * Creates one animation after another
	 * @param previous
	 * @param duration
	 * @param aftermath
	 */
	protected Animation(Animation previous, float duration, Aftermath aftermath) {
		this(previous.startTime + previous.duration, duration, aftermath);
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
