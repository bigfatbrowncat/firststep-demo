package firststep.demo;

import firststep.Canvas;
import firststep.Framebuffer;
import firststep.demo.base.Animation;

public class LineAnimation extends Animation {

	private float x1, y1, x2, y2;
	
	public LineAnimation(float startTime, float duration, Aftermath aftermath, float x1, float y1, float x2, float y2) {
		super(startTime, duration, aftermath);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	protected LineAnimation(Animation previous, float duration, Aftermath aftermath, float x1, float y1, float x2, float y2) {
		super(previous, duration, aftermath);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public static LineAnimation after(Animation previous, float duration, Aftermath aftermath, float x1, float y1, float x2, float y2) {
		return new LineAnimation(previous, duration, aftermath, x1, y1, x2, y2);
	}
	
	@Override
	protected void frame(Canvas fb, float timeSinceStart) {
		float xc = (x2 - x1) * timeSinceStart / getDuration() + x1;
		float yc = (y2 - y1) * timeSinceStart / getDuration() + y1;
		fb.beginPath();
		fb.moveTo(x1, y1);
		fb.lineTo(xc, yc);
		fb.stroke();
	}
}
