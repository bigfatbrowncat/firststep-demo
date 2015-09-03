package firststep.demo;

import firststep.Color;
import firststep.Window;

public class DemoWindow extends Window {

	private static final String APPNAME = "Welcome to firststep";
	
	private static float fps = 25.0f;
	private static long startupMoment;
	
	private LogoAnimation logoView;

	private static float backRed = 0.15f, backGreen = 0.15f, backBlue = 0.1f;

	private float getTimeSinceStartup() {
		return (float)((double)System.currentTimeMillis() - startupMoment) / 1000;
	}
	
	@Override
	protected void frame() {
		float foreRed = 0.8f, foreGreen = 0.8f, foreBlue = 0.7f;

		logoView = new LogoAnimation(0.0f, foreRed, foreGreen, foreBlue);
		logoView.setSize(getWidth(), getHeight());
		
		//if (logoView.isActual(getTimeSinceStartup())) {
			logoView.doFrame(getRootFramebuffer(), getTimeSinceStartup());
		//}
	}
	
	@Override
	protected void windowSize(final int width, final int height) {
		
		
		/*if (logoView == null)*/ 
		//logoView.setSize(width, height);
	}
	
	public DemoWindow() {
		super (APPNAME, 600, 400, Color.fromRGBA(backRed, backGreen, backBlue, 1.0f));
		startupMoment = System.currentTimeMillis();
	}
	
	public static void main(String... args) {
		new DemoWindow();
		
		Window.loop(fps);
	}
}