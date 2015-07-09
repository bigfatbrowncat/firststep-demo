package firststep.demo;

import firststep.Canvas;
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
	}

	@Override
	protected void windowSize(int width, int height) {
		animatorsManager.clear();
		animatorsManager.addAnimator(new LineAnimator(0.0f, 10.0f, Aftermath.SAVE, 0f, 0f, width, height));
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