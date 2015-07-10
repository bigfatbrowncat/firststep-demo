package firststep.demo;

import firststep.Canvas;
import firststep.Canvas.Winding;
import firststep.Color;
import firststep.Window;
import firststep.demo.base.Animator.Aftermath;
import firststep.demo.base.AnimatorsManager;

public class DemoWindow extends Window {
	
	private static final String APPNAME = "Welcome to firststep";
	
	private static float fps = 25.0f;
	private static long startupMoment;
	
	private AnimatorsManager animatorsManager = new AnimatorsManager();

	@Override
	protected void frame(Canvas cnv) {
		float timeSinceStartup = (float)((double)System.currentTimeMillis() - startupMoment) / 1000;
		animatorsManager.doFrame(cnv, timeSinceStartup);
		
		cnv.beginPath();
		cnv.arc(50, 50, 30, 0, 6.2f, Winding.CW);
		cnv.stroke();
	}

	@Override
	protected void windowSize(int width, int height) {
		animatorsManager.clear();
		int xCenter = width / 2;
		int yCenter = height / 2;
		
		float squareSize = 80;
		float cornerRadius = 15;
		
		float boxLeft = xCenter - squareSize / 2;
		float boxTop = yCenter - squareSize / 2;
		float boxRight = xCenter + squareSize / 2;
		float boxBottom = yCenter + squareSize / 2;

		float tline = 1f;
		float tcorner = 0.5f;
		
		LineAnimator[] lineAnims = new LineAnimator[4];
		ArcAnimator[] cornerAnims = new ArcAnimator[4];
		
		float time = 0; 
		lineAnims[0] = new LineAnimator(time, tline, Aftermath.SAVE,
				boxLeft + cornerRadius, 
				boxTop, 
				boxRight - cornerRadius, 
				boxTop);
		time += tline;

		cornerAnims[0] = new ArcAnimator(time, tcorner, Aftermath.SAVE, 
				boxRight - cornerRadius, 
				boxTop + cornerRadius, 
				cornerRadius, -(float)Math.PI / 2, 0, Winding.CW);
		time += tcorner;

		lineAnims[1] = new LineAnimator(time, tline, Aftermath.SAVE,
				boxLeft, 
				boxBottom - cornerRadius,
				boxLeft, 
				boxTop + cornerRadius);
		time += tline;

		cornerAnims[1] = new ArcAnimator(time, tcorner, Aftermath.SAVE, 
				boxLeft + cornerRadius, 
				boxTop + cornerRadius, 
				cornerRadius, -(float)Math.PI, -(float)Math.PI / 2, Winding.CW);
		time += tcorner;
		
		lineAnims[2] = new LineAnimator(time, tline, Aftermath.SAVE,
				boxRight - cornerRadius, 
				boxBottom,
				boxLeft + cornerRadius, 
				boxBottom);
		time += tline;

		cornerAnims[2] = new ArcAnimator(time, tcorner, Aftermath.SAVE, 
				boxRight - cornerRadius, 
				boxBottom - cornerRadius, 
				cornerRadius, 0, (float)Math.PI / 2, Winding.CW);
		time += tcorner;

		lineAnims[3] = new LineAnimator(time, tline, Aftermath.SAVE,
				boxRight, 
				boxTop + cornerRadius,
				boxRight, 
				boxBottom - cornerRadius);
		time += tline;
		
		cornerAnims[3] = new ArcAnimator(time, tcorner, Aftermath.SAVE, 
				boxLeft + cornerRadius, 
				boxBottom - cornerRadius, 
				cornerRadius, (float)Math.PI / 2, (float)Math.PI, Winding.CW);
		time += tcorner;
		
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