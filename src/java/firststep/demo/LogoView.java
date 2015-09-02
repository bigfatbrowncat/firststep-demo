package firststep.demo;

import java.io.BufferedInputStream;
import java.io.IOException;

import firststep.Color;
import firststep.Font;
import firststep.Framebuffer;
import firststep.Image;
import firststep.Paint;
import firststep.Window;
import firststep.demo.base.Animation.Aftermath;

public class LogoView {

	private static Font boldFont, regularFont, lightFont;

	private RoundRectAnimation roundRectAnimation;

	private final Framebuffer oneStFramebuffer;
	private Framebuffer logoFramebuffer;
	
	private int logoSize = 160;
	private int logoFramebufferSize = (int)(logoSize * 1.05f);
	private float cornerRadius = 30;

	private final float foreRed, foreGreen, foreBlue;
	
	private float currentTime;
	
	public LogoView(Window window, final float foreRed, final float foreGreen, final float foreBlue) {
		this.foreRed = foreRed;
		this.foreGreen = foreGreen;
		this.foreBlue = foreBlue;
		
		oneStFramebuffer = window.createFramebuffer(logoSize, logoSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
		logoFramebuffer = window.createFramebuffer((int)logoFramebufferSize, (int)logoFramebufferSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
	}

	private void drawOneSt() {
		float timeSinceStartup = currentTime;
		oneStFramebuffer.beginDrawing();

		if (boldFont == null) {
			try {
				BufferedInputStream is = (BufferedInputStream)this.getClass().getResourceAsStream("/firststep/demo/ClearSans-Bold.ttf");
				boldFont = oneStFramebuffer.createOrFindFont("bold", is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (regularFont == null) {
			try {
				BufferedInputStream is = (BufferedInputStream)this.getClass().getResourceAsStream("/firststep/demo/ClearSans-Regular.ttf");
				regularFont = oneStFramebuffer.createOrFindFont("regular", is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (lightFont == null) {
			try {
				BufferedInputStream is = (BufferedInputStream)this.getClass().getResourceAsStream("/firststep/demo/ClearSans-Light.ttf");
				lightFont = oneStFramebuffer.createOrFindFont("light", is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		float alphaDelta1 = (float)( 1.0 - 0.5 / (Math.max(timeSinceStartup - 1.5, 0)));// Math.min(Math.sqrt(timeSinceStartup / 3), 1.0);

		float delta1 = (float)( 1.0 + 0.05 / (Math.max(timeSinceStartup - 2, 0)));
		float delta2 = (float)( 1.0 + 0.1 / (Math.max(timeSinceStartup - 2.5, 0)));
		
		oneStFramebuffer.fillColor(foreRed, foreGreen, foreBlue, 0.9f * alphaDelta1);
		oneStFramebuffer.beginPath();
		oneStFramebuffer.fontFace("light");
		oneStFramebuffer.fontSize(logoSize * (0.5f*alphaDelta1 + 1));
		oneStFramebuffer.text(logoSize * (-0.1f), delta1 * logoSize * 0.95f, "1");
		oneStFramebuffer.fill();

		oneStFramebuffer.fillColor(foreRed, foreGreen, foreBlue, 0.5f * alphaDelta1);
		oneStFramebuffer.beginPath();
		oneStFramebuffer.fontFace("regular");
		oneStFramebuffer.fontSize(logoSize * 0.65f * (0.5f*alphaDelta1 + 1));
		oneStFramebuffer.text(delta2 * logoSize * 0.35f, logoSize * 0.70f, "st");
		oneStFramebuffer.fill();
		
		oneStFramebuffer.endDrawing();
	}
	
	private void drawLogo() {
		logoFramebuffer.beginDrawing();
		
		float timeSinceStartup = currentTime;
		
		float xCenter = logoFramebufferSize / 2;
		float yCenter = logoFramebufferSize / 2;
		
		Paint textFbPaint = logoFramebuffer.imagePattern(xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, 0, oneStFramebuffer.getImage(), 1.0f);
		
		logoFramebuffer.fillPaint(textFbPaint);
		logoFramebuffer.strokeWidth(0.02f * logoSize);
		logoFramebuffer.beginPath();
		logoFramebuffer.roundedRect(xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, cornerRadius);
		logoFramebuffer.fill();
		
		roundRectAnimation = new RoundRectAnimation(0.0f, 2.0f, Aftermath.SAVE, xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, cornerRadius);

		float alphaDelta = (float)( 1.0 - 0.5 * Math.pow(timeSinceStartup, -0.5));
		logoFramebuffer.strokeColor(Color.fromRGBA(foreRed, foreGreen, foreBlue, alphaDelta));
		roundRectAnimation.doFrame(logoFramebuffer, 0.4f * timeSinceStartup * timeSinceStartup);

		logoFramebuffer.endDrawing();
	}
	
	public void draw() {
		drawOneSt();
		drawLogo();
	}

	public void setCurrentTime(float currentTime) {
		this.currentTime = currentTime;
	}
	
	public Image getImage() {
		return logoFramebuffer.getImage();
	}
}
