package firststep.demo;

import firststep.Canvas;
import firststep.demo.base.Animator;

public class LineAnimator extends Animator {

	private float x1, y1, x2, y2;
	
	public LineAnimator(float startTime, float duration, Aftermath aftermath, float x1, float y1, float x2, float y2) {
		super(startTime, duration, aftermath);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	@Override
	protected void frame(Canvas cnv, float timeSinceStart) {
		float xc = (x2 - x1) * timeSinceStart / getDuration() + x1;
		float yc = (y2 - y1) * timeSinceStart / getDuration() + y1;
		cnv.beginPath();
		cnv.moveTo(x1, y1);
		cnv.lineTo(xc, yc);
		cnv.strokeColor(1.0f, 0.0f, 1.0f, 50.0f);
		cnv.stroke();
	}
}
