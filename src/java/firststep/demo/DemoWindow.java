package firststep.demo;

import java.io.BufferedInputStream;
import java.io.IOException;

import firststep.Canvas;
import firststep.Color;
import firststep.DrawingQueue;
import firststep.Image;
import firststep.Paint;
import firststep.Transform;
import firststep.Window;

public class DemoWindow extends Window {

	private static final String APPNAME = "Welcome to firststep";
	
	private static float fps = 25.0f;
	private static long startupMoment;
	
	private LogoView logoView;

	private static float backRed = 0.15f, backGreen = 0.15f, backBlue = 0.1f;

	private float getTimeSinceStartup() {
		return (float)((double)System.currentTimeMillis() - startupMoment) / 1000;
	}
	
	@Override
	protected void beforeFrame(DrawingQueue queue) {
		logoView.setCurrentTime(getTimeSinceStartup());	// ???
		logoView.appendToQueue(queue);
	}
	
	@Override
	protected void windowSize(final int width, final int height) {
		
		float foreRed = 0.8f, foreGreen = 0.8f, foreBlue = 0.7f;
		
		/*if (logoView == null)*/ logoView = new LogoView(this, foreRed, foreGreen, foreBlue);
	}
	
	public DemoWindow() {
		super (APPNAME, 600, 400, Color.fromRGBA(backRed, backGreen, backBlue, 1.0f));
		startupMoment = System.currentTimeMillis();
	}
	
	public static void main(String... args) {
		new DemoWindow();
		
		Window.loop(fps);
	}
	

	float angleFunction(float time) {
		return (float) (-0.3 + 0.21 * Math.atan(time));
	}
	
	float zoomFunction(float time) {
		return (float) (1 + 0.2 * Math.atan(time - 2));
	}
	
	float blendFunction(float time) {
		return (float)( 1.0 - Math.pow(Math.max(time - 7.7, 0), 1.5));// Math.min(Math.sqrt(timeSinceStartup / 3), 1.0);
	}
	
	private Image image = null;
	private Paint bgPaint = null;
		
	@Override
	public void draw(Canvas cnv) {
		if (image == null) {
			BufferedInputStream is = (BufferedInputStream)this.getClass().getResourceAsStream("/firststep/demo/stars.png");
			try {
				image = cnv.createImage(is, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
				bgPaint = cnv.imagePattern(0, 0, image.getSize().getX(), image.getSize().getY(), 0, image, 0.3f);
			} catch (IOException e) {
				image = null;
			}
		}
		
		float timeSinceStartup = getTimeSinceStartup();

		final float xCenter = DemoWindow.this.getWidth() / 2;
		final float yCenter = DemoWindow.this.getHeight() / 2;
		
		float logoPaintSize = logoView.getImage().getSize().getX() / 2;

		cnv.save();
		cnv.setTransform(
				Transform.rotating(-timeSinceStartup / 20)
				.translate(getWidth() / 2, getHeight() / 2)
		);

		cnv.beginPath();
		cnv.fillPaint(bgPaint);
		int d = Math.max(getWidth(), getHeight());
		cnv.rect(-d, -d, 2*d, 2*d);
		cnv.fill();
		cnv.restore();
		

		cnv.save();
		cnv.setTransform(
				Transform.rotating(angleFunction(timeSinceStartup))
				.scale(zoomFunction(timeSinceStartup), 
				
				zoomFunction(timeSinceStartup)
		).translate(xCenter, yCenter));

		Paint logoFbPaint = cnv.imagePattern(- logoPaintSize / 2, - logoPaintSize / 2, logoPaintSize, logoPaintSize, 0, logoView.getImage(), blendFunction(timeSinceStartup));
		cnv.beginPath();
		cnv.fillPaint(logoFbPaint);
		cnv.rect(- logoPaintSize / 2, - logoPaintSize / 2, logoPaintSize, logoPaintSize);
		cnv.fill();
		cnv.restore();

	}
}