package firststep.demo;

import firststep.Canvas;
import firststep.Color;
import firststep.Font;
import firststep.Framebuffer;
import firststep.Image;
import firststep.Paint;
import firststep.Window;
import firststep.Framebuffer.DrawListener;
import firststep.demo.base.Animation;
import firststep.demo.base.Animation.Aftermath;

public class LogoController {

	private RoundRectAnimation roundRectAnimation;
	private static float foreRed = 0.8f, foreGreen = 0.8f, foreBlue = 0.7f;
	
	private Framebuffer logoFb;

	int logoSize = 160;
	int logoFbSize = (int)(logoSize * 1.05f);

	float cornerRadius = 30;
	
	float currentTime;

	private void prepare(Window window) {
		Framebuffer textFb = window.createFramebuffer(logoSize, logoSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
		textFb.setDrawListener(new DrawListener() {

			private Font boldFont, regularFont, lightFont;

			@Override
			public void draw(Canvas cnv) {
				float timeSinceStartup = currentTime;
				
				if (boldFont == null) {
					boldFont = cnv.createFont("bold", "ClearSans-Bold.ttf");
				}
				if (regularFont == null) {
					regularFont = cnv.createFont("regular", "ClearSans-Regular.ttf");
				}
				if (lightFont == null) {
					lightFont = cnv.createFont("light", "ClearSans-Light.ttf");
				}

				float alphaDelta1 = (float)( 1.0 - 0.5 / (Math.max(timeSinceStartup - 1.5, 0)));// Math.min(Math.sqrt(timeSinceStartup / 3), 1.0);

				float delta1 = (float)( 1.0 + 0.05 / (Math.max(timeSinceStartup - 2, 0)));
				float delta2 = (float)( 1.0 + 0.1 / (Math.max(timeSinceStartup - 2.5, 0)));
				
				cnv.fillColor(foreRed, foreGreen, foreBlue, 0.9f * alphaDelta1);
				cnv.beginPath();
				cnv.fontFace("light");
				cnv.fontSize(logoSize * (0.5f*alphaDelta1 + 1));
				cnv.text(logoSize * (-0.1f), delta1 * logoSize * 0.95f, "1");
				cnv.fill();

				cnv.fillColor(foreRed, foreGreen, foreBlue, 0.5f * alphaDelta1);
				cnv.beginPath();
				cnv.fontFace("regular");
				cnv.fontSize(logoSize * 0.65f * (0.5f*alphaDelta1 + 1));
				cnv.text(delta2 * logoSize * 0.35f, logoSize * 0.70f, "st");
				cnv.fill();
			}
		});
			
		logoFb = window.createFramebuffer((int)logoFbSize, (int)logoFbSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
		logoFb.addDependency(textFb);
		logoFb.setDrawListener(new DrawListener() {
			@Override
			public void draw(Canvas cnv) {
				float timeSinceStartup = currentTime;
				
				float xCenter = logoFbSize / 2;
				float yCenter = logoFbSize / 2;
				
				Paint textFbPaint = cnv.imagePattern(xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, 0, textFb.getImage(), 1.0f);
				
				cnv.fillPaint(textFbPaint);
				cnv.strokeWidth(0.02f * logoSize);
				cnv.beginPath();
				cnv.roundedRect(xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, cornerRadius);
				cnv.fill();
				
				roundRectAnimation = new RoundRectAnimation(0.0f, 2.0f, Aftermath.SAVE, xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, cornerRadius);

				float alphaDelta = (float)( 1.0 - 0.5 * Math.pow(timeSinceStartup, -0.5));
				cnv.strokeColor(new Color(foreRed, foreGreen, foreBlue, alphaDelta));
				roundRectAnimation.doFrame(cnv, 0.4f * timeSinceStartup * timeSinceStartup);

			}
		});

		window.getMainFramebuffer().addDependency(logoFb);

	}
	
	public LogoController(Window window) {
		prepare(window);
	}
	
	public void setCurrentTime(float currentTime) {
		this.currentTime = currentTime;
	}
	
	public Framebuffer getLogoFramebuffer() {
		return logoFb;
	}
	
	public int getLogoFramebufferSize() {
		return logoFbSize;
	}
	
	public int getLogoSize() {
		return logoSize;
	}
}
