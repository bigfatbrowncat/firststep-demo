package firststep.demo;

import firststep.Canvas;
import firststep.Canvas.Winding;
import firststep.demo.base.Animation;
import firststep.demo.base.Animation.Aftermath;

public class ArcAnimation extends Animation {

	private float cx, cy, radius, a0, a1;
	private Winding dir;
	
	public ArcAnimation(float startTime, float duration, Aftermath aftermath, float cx, float cy, float radius, float a0, float a1, Winding dir) {
		super(startTime, duration, aftermath);
		this.cx = cx;
		this.cy = cy;
		this.radius = radius;
		this.a0 = a0;
		this.a1 = a1;
		this.dir = dir;
	}
	
	protected ArcAnimation(Animation previous, float duration, Aftermath aftermath, float cx, float cy, float radius, float a0, float a1, Winding dir) {
		super(previous, duration, aftermath);
		this.cx = cx;
		this.cy = cy;
		this.radius = radius;
		this.a0 = a0;
		this.a1 = a1;
		this.dir = dir;
	}
	
	public static ArcAnimation after(Animation previous, float duration, Aftermath aftermath, float cx, float cy, float radius, float a0, float a1, Winding dir) {
		return new ArcAnimation(previous, duration, aftermath, cx, cy, radius, a0, a1, dir);
	}
	
	
	@Override
	protected void frame(Canvas cnv, float timeSinceStart) {
		while (a0 < 0) a0 += 2 * Math.PI;
		while (a1 < a0) a1 += 2 * Math.PI;
		
		float ac = (a1 - a0) * timeSinceStart / getDuration() + a0;
		cnv.beginPath();
		cnv.arc(cx, cy, radius, a0, ac, dir);
		cnv.stroke();
	}
}
