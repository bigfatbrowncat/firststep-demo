package firststep.demo;

import firststep.Canvas;
import firststep.Canvas.Winding;
import firststep.Color;
import firststep.Font;
import firststep.Framebuffer;
import firststep.Framebuffer.DrawListener;
import firststep.Image;
import firststep.Paint;
import firststep.Window;
import firststep.demo.base.Animation.Aftermath;
import firststep.demo.base.AnimationsGroup;

public class DemoWindow extends Window {
	
	private static final String APPNAME = "Welcome to firststep";
	
	private static float fps = 25.0f;
	private static long startupMoment;
	
	private RoundRectAnimation roundRectAnimation;

	private float getTimeSinceStartup() {
		return (float)((double)System.currentTimeMillis() - startupMoment) / 1000;
	}
	
	private static float backRed = 0.15f, backGreen = 0.15f, backBlue = 0.1f;
	private static float foreRed = 0.8f, foreGreen = 0.8f, foreBlue = 0.7f;
	
	Framebuffer textFb, fb2;
	Font boldFont, regularFont, lightFont;
	Paint textFbPaint, fb2Paint;

	int squareSize = 80;
	float cornerRadius = 15;

	@Override
	protected void windowSize(int width, int height) {
		
		Framebuffer mainFb = getMainFramebuffer(); 

		if (textFb == null) {
			textFb = createFramebuffer(squareSize, squareSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
			mainFb.addDependency(textFb);
			textFb.setDrawListener(new DrawListener() {
				@Override
				public void draw(Canvas cnv) {
					float timeSinceStartup = getTimeSinceStartup();
					
					if (boldFont == null) {
						boldFont = cnv.createFont("bold", "ClearSans-Bold.ttf");
					}
					if (regularFont == null) {
						regularFont = cnv.createFont("regular", "ClearSans-Regular.ttf");
					}
					if (lightFont == null) {
						lightFont = cnv.createFont("light", "ClearSans-Light.ttf");
					}

					float alphaDelta1 = (float)( 1.0 - 0.5 / timeSinceStartup);// Math.min(Math.sqrt(timeSinceStartup / 3), 1.0);

					float delta1 = (float)( 1.0 + 0.02 / (timeSinceStartup - 1));
					float delta2 = (float) Math.max(2.0 * (2.2 - timeSinceStartup), 1.0);
					
					cnv.fillColor(foreRed, foreGreen, foreBlue, 0.5f * alphaDelta1);
					cnv.beginPath();
					cnv.fontFace("light");
					cnv.fontSize(squareSize * (0.5f*alphaDelta1 + 1));
					cnv.text(squareSize * (-0.1f), delta1 * squareSize * 0.95f, "1");
					cnv.fill();

					cnv.fillColor(foreRed, foreGreen, foreBlue, 0.8f * alphaDelta1);
					cnv.beginPath();
					cnv.fontFace("regular");
					cnv.fontSize(squareSize * 0.65f * (0.5f*alphaDelta1 + 1));
					cnv.text(delta2 * squareSize * 0.35f, squareSize * 0.64f, "st");
					cnv.fill();
				}
			});
		}
			
		if (fb2 == null) {
			fb2 = createFramebuffer(squareSize, squareSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
			mainFb.addDependency(fb2);
			fb2.setDrawListener(new DrawListener() {
				@Override
				public void draw(Canvas cnv) {
					float timeSinceStartup = getTimeSinceStartup();
					cnv.beginPath();
					cnv.rect(0, 0, squareSize / 2, squareSize / 2);
					cnv.fillColor(new Color(128, 255, 128));
					//cnv.fill();
				}
			});
		}

		mainFb.setDrawListener(new DrawListener() {
			@Override
			public void draw(Canvas cnv) {
				float timeSinceStartup = getTimeSinceStartup();

				float xCenter = width / 2;
				float yCenter = height / 2;
				
				textFbPaint = cnv.imagePattern(xCenter - squareSize / 2, yCenter - squareSize / 2, squareSize, squareSize, 0, textFb.getImage(), 1.0f);
				//fb2Paint = cnv.imagePattern(xCenter - squareSize / 2, yCenter - squareSize / 2, squareSize, squareSize, 0, fb2.getImage(), 1.0f);

				//if (!animationsGroup.isActual(timeSinceStartup)) {
				
				cnv.fillPaint(textFbPaint);
				cnv.beginPath();
				cnv.roundedRect(xCenter - squareSize / 2, yCenter - squareSize / 2, squareSize, squareSize, cornerRadius);
				cnv.fill();

				//cnv.fillPaint(fb2Paint);
				//cnv.fill();
				//}
				
				roundRectAnimation = new RoundRectAnimation(0.0f, 2.0f, Aftermath.SAVE, xCenter - squareSize / 2, yCenter - squareSize / 2, squareSize, squareSize, cornerRadius);

				float alphaDelta = (float)( 1.0 - 0.5 * Math.pow(timeSinceStartup, -0.5));
				cnv.strokeColor(new Color(foreRed, foreGreen, foreBlue, alphaDelta));
				roundRectAnimation.doFrame(cnv, 0.4f * timeSinceStartup * timeSinceStartup);

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