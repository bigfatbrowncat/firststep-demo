package firststep.demo;

import firststep.Canvas;
import firststep.Canvas.Winding;
import firststep.Color;
import firststep.Font;
import firststep.Framebuffer;
import firststep.Framebuffer.DrawListener;
import firststep.Image;
import firststep.Paint;
import firststep.Transform;
import firststep.Window;
import firststep.demo.base.Animation.Aftermath;
import firststep.demo.base.AnimationsGroup;

public class DemoWindow extends Window {
	
	private static final String APPNAME = "Welcome to firststep";
	
	private static float fps = 25.0f;
	private static long startupMoment;
	
	private LogoController logoController;

	private static float backRed = 0.15f, backGreen = 0.15f, backBlue = 0.1f;

	private float getTimeSinceStartup() {
		return (float)((double)System.currentTimeMillis() - startupMoment) / 1000;
	}
	
	@Override
	protected void frame() {
		logoController.setCurrentTime(getTimeSinceStartup());	// ???
	}
	
	@Override
	protected void windowSize(int width, int height) {
		
		logoController = new LogoController(this);
		
		getMainFramebuffer().setDrawListener(new DrawListener() {
			
			float angleFunction(float time) {
				return (float) (-0.3 + 0.21 * Math.atan(time));
			}
			
			float zoomFunction(float time) {
				return (float) (1 + 0.2 * Math.atan(time - 2));
			}
			
			float blendFunction(float time) {
				return (float)( 1.0 - Math.pow(Math.max(time - 7.7, 0), 1.5));// Math.min(Math.sqrt(timeSinceStartup / 3), 1.0);
			}
			
			@Override
			public void draw(Canvas cnv) {
				float timeSinceStartup = getTimeSinceStartup();

				float xCenter = width / 2;
				float yCenter = height / 2;
				
				float logoPaintSize = logoController.getLogoFramebufferSize() / 2;

				cnv.setTransform(Transform.rotating(angleFunction(timeSinceStartup)).scale(zoomFunction(timeSinceStartup), zoomFunction(timeSinceStartup)).translate(xCenter, yCenter));

				Paint logoFbPaint = cnv.imagePattern(- logoPaintSize / 2, - logoPaintSize / 2, logoPaintSize, logoPaintSize, 0, logoController.getLogoFramebuffer().getImage(), blendFunction(timeSinceStartup));
				cnv.beginPath();
				cnv.fillPaint(logoFbPaint);
				cnv.rect(- logoPaintSize / 2, - logoPaintSize / 2, logoPaintSize, logoPaintSize);
				cnv.fill();
			}
		});


	}
	
	public DemoWindow() {
		super (APPNAME, 600, 400, new Color(backRed, backGreen, backBlue, 1.0f));
		startupMoment = System.currentTimeMillis();
	}
	
	public static void main(String... args) {
		new DemoWindow();
		
		Window.loop(fps);
	}
}