package firststep.demo;

import firststep.Canvas;
import firststep.Color;
import firststep.Font;
import firststep.Framebuffer;
import firststep.Framebuffer.DrawListener;
import firststep.Image;
import firststep.Paint;
import firststep.Window;
import firststep.demo.base.Animation.Aftermath;

public class LogoController {

	private Window window;
	private Framebuffer logoFramebuffer;

	private RoundRectAnimation roundRectAnimation;

	private int logoSize = 160;
	private int logoFramebufferSize = (int)(logoSize * 1.05f);
	private float cornerRadius = 30;
	
	private float currentTime;
	
	private boolean isDeleted = false;
	
	public LogoController(Window window, final float foreRed, final float foreGreen, final float foreBlue) {
		this.window = window;
		
		final Framebuffer textFb = window.createFramebuffer(logoSize, logoSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
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
			
		logoFramebuffer = window.createFramebuffer((int)logoFramebufferSize, (int)logoFramebufferSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
		logoFramebuffer.addDependency(textFb);
		logoFramebuffer.setDrawListener(new DrawListener() {
			@Override
			public void draw(Canvas cnv) {
				float timeSinceStartup = currentTime;
				
				float xCenter = logoFramebufferSize / 2;
				float yCenter = logoFramebufferSize / 2;
				
				Paint textFbPaint = cnv.imagePattern(xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, 0, textFb.getImage(), 1.0f);
				
				cnv.fillPaint(textFbPaint);
				cnv.strokeWidth(0.02f * logoSize);
				cnv.beginPath();
				cnv.roundedRect(xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, cornerRadius);
				cnv.fill();
				
				roundRectAnimation = new RoundRectAnimation(0.0f, 2.0f, Aftermath.SAVE, xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, cornerRadius);

				float alphaDelta = (float)( 1.0 - 0.5 * Math.pow(timeSinceStartup, -0.5));
				cnv.strokeColor(Color.fromRGBA(foreRed, foreGreen, foreBlue, alphaDelta));
				roundRectAnimation.doFrame(cnv, 0.4f * timeSinceStartup * timeSinceStartup);

			}
		});

		window.getMainFramebuffer().addDependency(logoFramebuffer);
	}
	
	public void delete() {
		if (!isDeleted) {
			window.getMainFramebuffer().removeDependency(logoFramebuffer);
			isDeleted = true;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		delete();
		super.finalize();
	}
	
	public void setCurrentTime(float currentTime) {
		this.currentTime = currentTime;
	}
	
	public Framebuffer getLogoFramebuffer() {
		return logoFramebuffer;
	}
	
	public int getLogoFramebufferSize() {
		return logoFramebufferSize;
	}
	
	public int getLogoSize() {
		return logoSize;
	}
}
