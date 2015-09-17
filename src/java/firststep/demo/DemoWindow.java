package firststep.demo;

import firststep.Color;
import firststep.Framebuffer;
import firststep.Transform;
import firststep.Window;
import firststep.contracts.Animatable;
import firststep.contracts.Renderable;
import firststep.demo.base.Animation;

public class DemoWindow extends Window {

	private static final String APPNAME = "Welcome to firststep";
	
	private static float fps = 25.0f;
	private static long startupMoment;
	
	private LogoAnimation logoView;

	private static float backRed = 0.15f, backGreen = 0.15f, backBlue = 0.1f;

	private float getTimeSinceStartup() {
		return (float)((double)System.currentTimeMillis() - startupMoment) / 1000;
	}
	
	static float foreRed = 0.8f, foreGreen = 0.8f, foreBlue = 0.7f;
	public static class TestLogoAnimation extends LogoAnimation {
		public TestLogoAnimation() {
			//super(0, 10, Aftermath.SAVE, 10, 10, 50, 30, 8);
			super(0, 1, 1, 1);
		}
		
		@Override
		public void render(Framebuffer fb) {
			//fb.setTransform(Transform.translating(100, 0));
			//fb.save();
			setSize(fb.getWidth(), fb.getHeight());
			super.render(fb);
			//fb.restore();
		}
		
	}
	
	@Override
	protected void onFrame() {

		logoView = new LogoAnimation(0.0f, foreRed, foreGreen, foreBlue);
		logoView.setSize(getWidth(), getHeight());

		logoView.setCurrentTime(getTimeSinceStartup());
		logoView.render(this);
	}
	
	@Override
	protected void onSizeChange(final int width, final int height) {
		
		
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