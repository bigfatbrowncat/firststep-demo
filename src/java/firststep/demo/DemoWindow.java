package firststep.demo;

import firststep.Canvas;
import firststep.Canvas.Winding;
import firststep.Color;
import firststep.Window;
import firststep.demo.base.Animation.Aftermath;
import firststep.demo.base.AnimationsGroup;

public class DemoWindow extends Window {
	
	private static final String APPNAME = "Welcome to firststep";
	
	private static float fps = 25.0f;
	private static long startupMoment;
	
	private AnimationsGroup animatorsManager;

	@Override
	protected void frame(Canvas cnv) {
		float timeSinceStartup = (float)((double)System.currentTimeMillis() - startupMoment) / 1000;

		cnv.strokeColor(new Color(255, 255, 192));
		animatorsManager.doFrame(cnv, timeSinceStartup);
	}

	@Override
	protected void windowSize(int width, int height) {
		int xCenter = width / 2;
		int yCenter = height / 2;
		
		float squareSize = 80;
		float cornerRadius = 15;
		
		float boxLeft = xCenter - squareSize / 2;
		float boxTop = yCenter - squareSize / 2;
		float boxRight = xCenter + squareSize / 2;
		float boxBottom = yCenter + squareSize / 2;

		float tline = 0.3f;
		float tcorner = 0.2f;
		
		LineAnimation[] lineAnims = new LineAnimation[4];
		ArcAnimation[] cornerAnims = new ArcAnimation[4];
		
		lineAnims[0] = new LineAnimation(1.0f, tline * 2, Aftermath.SAVE,
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
		
		animatorsManager = new AnimationsGroup(2.0f, cornerAnims[3].getStartTime() + cornerAnims[3].getDuration(), Aftermath.SAVE);
		animatorsManager.addAnimator(lineAnims[0]);
		animatorsManager.addAnimator(lineAnims[1]);
		animatorsManager.addAnimator(lineAnims[2]);
		animatorsManager.addAnimator(lineAnims[3]);
		animatorsManager.addAnimator(cornerAnims[0]);
		animatorsManager.addAnimator(cornerAnims[1]);
		animatorsManager.addAnimator(cornerAnims[2]);
		animatorsManager.addAnimator(cornerAnims[3]);
	}
	
	public DemoWindow() {
		super (APPNAME, 600, 400, new Color(0.5f, 0.5f, 0.5f, 1.0f));
		startupMoment = System.currentTimeMillis();
	}
	
	public static void main(String... args) {
		new DemoWindow();
		
		Window.loop(fps);
	}
}