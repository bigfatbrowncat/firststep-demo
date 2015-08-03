package firststep.demo;

import firststep.Canvas.Winding;
import firststep.demo.base.Animation;
import firststep.demo.base.AnimationsGroup;

public class RoundRectAnimation extends AnimationsGroup {

	private float left, top, width, height;
	private float cornerRadius;
	
	private void prepare() {
		
		float boxLeft = left;
		float boxTop = top;
		float boxRight = left + width;
		float boxBottom = top + height;
		
		float tline = getDuration() / 8;
		float tcorner = getDuration() / 8;
		
		LineAnimation[] lineAnims = new LineAnimation[4];
		ArcAnimation[] cornerAnims = new ArcAnimation[4];
		
		lineAnims[0] = new LineAnimation(getStartTime(), tline, Aftermath.SAVE,
				boxLeft + cornerRadius, 
				boxTop, 
				boxRight - cornerRadius, 
				boxTop);

		cornerAnims[0] = ArcAnimation.after(lineAnims[0], tcorner, Aftermath.SAVE, 
				boxRight - cornerRadius, 
				boxTop + cornerRadius, 
				cornerRadius, -(float)Math.PI / 2, 0, Winding.CW);

		lineAnims[1] = LineAnimation.after(cornerAnims[0], tline, Aftermath.SAVE,
				boxRight, 
				boxTop + cornerRadius,
				boxRight, 
				boxBottom - cornerRadius);

		cornerAnims[1] = ArcAnimation.after(lineAnims[1], tcorner, Aftermath.SAVE, 
				boxRight - cornerRadius, 
				boxBottom - cornerRadius, 
				cornerRadius, 0, (float)Math.PI / 2, Winding.CW);

		lineAnims[2] = LineAnimation.after(cornerAnims[1], tline, Aftermath.SAVE,
				boxRight - cornerRadius, 
				boxBottom,
				boxLeft + cornerRadius, 
				boxBottom);

		cornerAnims[2] = ArcAnimation.after(lineAnims[2], tcorner, Aftermath.SAVE, 
				boxLeft + cornerRadius, 
				boxBottom - cornerRadius, 
				cornerRadius, (float)Math.PI / 2, (float)Math.PI, Winding.CW);

		lineAnims[3] = LineAnimation.after(cornerAnims[2], tline, Aftermath.SAVE,
				boxLeft, 
				boxBottom - cornerRadius,
				boxLeft, 
				boxTop + cornerRadius);

		cornerAnims[3] = ArcAnimation.after(lineAnims[3], tcorner, Aftermath.SAVE, 
				boxLeft + cornerRadius, 
				boxTop + cornerRadius, 
				cornerRadius, -(float)Math.PI, -(float)Math.PI / 2, Winding.CW);
		
		addAnimation(lineAnims[0]);
		addAnimation(lineAnims[1]);
		addAnimation(lineAnims[2]);
		addAnimation(lineAnims[3]);
		addAnimation(cornerAnims[0]);
		addAnimation(cornerAnims[1]);
		addAnimation(cornerAnims[2]);
		addAnimation(cornerAnims[3]);

	}
	
	public RoundRectAnimation(Animation previous, float duration, Aftermath aftermath, float left, float top, float width, float height, float cornerRadius) {
		super(previous, duration, aftermath);
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.cornerRadius = cornerRadius;
		prepare();
	}

	public RoundRectAnimation(float startTime, float duration, Aftermath aftermath, float left, float top, float width, float height, float cornerRadius) {
		super(startTime, duration, aftermath);
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.cornerRadius = cornerRadius;
		prepare();
	}
	
}
