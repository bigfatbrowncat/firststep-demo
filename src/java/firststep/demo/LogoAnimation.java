package firststep.demo;

import java.io.IOException;
import java.io.InputStream;

import firststep.Canvas;
import firststep.Color;
import firststep.Font;
import firststep.Framebuffer;
import firststep.Image;
import firststep.Paint;
import firststep.Transform;
import firststep.demo.base.Animation;

public class LogoAnimation extends Animation {

	private static final float DURATION = 10;
	
	private static Font boldFont, regularFont, lightFont;

	private RoundRectAnimation roundRectAnimation;

	private Framebuffer oneStFramebuffer;
	private Framebuffer logoFramebuffer;
	
	private int logoSize = 160;
	private int logoFramebufferSize = (int)(logoSize * 1.05f);
	private float cornerRadius = 30;

	private float foreRed, foreGreen, foreBlue;
	private int width, height;
	
	private void init(final float foreRed, final float foreGreen, final float foreBlue) {
		this.foreRed = foreRed;
		this.foreGreen = foreGreen;
		this.foreBlue = foreBlue;
		
		oneStFramebuffer = new Framebuffer(logoSize, logoSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
		logoFramebuffer = new Framebuffer((int)logoFramebufferSize, (int)logoFramebufferSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
	}
	
	public LogoAnimation(Animation previous, final float foreRed, final float foreGreen, final float foreBlue) {
		super(previous, DURATION, Aftermath.REMOVE);
		init(foreRed, foreGreen, foreBlue);
	}

	public LogoAnimation(float startTime, final float foreRed, final float foreGreen, final float foreBlue) {
		super(startTime, DURATION, Aftermath.REMOVE);
		init(foreRed, foreGreen, foreBlue);
	}

	
	private void drawOneSt(float timeSinceStart) {
		oneStFramebuffer.beginDrawing();

		if (boldFont == null) {
			try {
				InputStream is = this.getClass().getResourceAsStream("/firststep/demo/ClearSans-Bold.ttf");
				boldFont = Font.createOrFindFont("bold", is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (regularFont == null) {
			try {
				InputStream is = this.getClass().getResourceAsStream("/firststep/demo/ClearSans-Regular.ttf");
				regularFont = Font.createOrFindFont("regular", is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (lightFont == null) {
			try {
				InputStream is = this.getClass().getResourceAsStream("/firststep/demo/ClearSans-Light.ttf");
				lightFont = Font.createOrFindFont("light", is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		float alphaDelta1 = (float)( 1.0 - 0.5 / (Math.max(timeSinceStart - 1.5, 0)));// Math.min(Math.sqrt(timeSinceStartup / 3), 1.0);

		float delta1 = (float)( 1.0 + 0.05 / (Math.max(timeSinceStart - 2, 0)));
		float delta2 = (float)( 1.0 + 0.1 / (Math.max(timeSinceStart - 2.5, 0)));
		
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
	
	private void drawLogo(float timeSinceStart) {
		logoFramebuffer.beginDrawing();
		
		float xCenter = logoFramebufferSize / 2;
		float yCenter = logoFramebufferSize / 2;
		
		Paint textFbPaint = logoFramebuffer.imagePattern(xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, 0, oneStFramebuffer.getImage(), 1.0f);
		
		logoFramebuffer.fillPaint(textFbPaint);
		logoFramebuffer.strokeWidth(0.02f * logoSize);
		logoFramebuffer.beginPath();
		logoFramebuffer.roundedRect(xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, cornerRadius);
		logoFramebuffer.fill();
		
		roundRectAnimation = new RoundRectAnimation(0.0f, 2.0f, Aftermath.SAVE, xCenter - logoSize / 2, yCenter - logoSize / 2, logoSize, logoSize, cornerRadius);

		float alphaDelta = (float)( 1.0 - 0.5 * Math.pow(timeSinceStart, -0.5));
		logoFramebuffer.strokeColor(Color.fromRGBA(foreRed, foreGreen, foreBlue, alphaDelta));
		roundRectAnimation.setCurrentTime(0.4f * timeSinceStart * timeSinceStart);
		roundRectAnimation.render(logoFramebuffer);

		logoFramebuffer.endDrawing();
	}
	
	private float angleFunction(float time) {
		return (float) (-0.3 + 0.21 * Math.atan(time));
	}
	
	private float zoomFunction(float time) {
		return (float) (1 + 0.2 * Math.atan(time - 2));
	}
	
	private float blendFunction(float time) {
		return (float)( 1.0 - Math.pow(Math.max(time - 7.7, 0), 1.5));// Math.min(Math.sqrt(timeSinceStartup / 3), 1.0);
	}
	
	private Image image = null;
	private Paint bgPaint = null;
		
	private void drawMain(Canvas rootFb, float timeSinceStartup, int width, int height) {
		if (image == null) {
			InputStream is = this.getClass().getResourceAsStream("/firststep/demo/stars.png");
			try {
				image = new Image(is, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
				bgPaint = rootFb.imagePattern(0, 0, image.getSize().getX(), image.getSize().getY(), 0, image, 0.3f);
			} catch (IOException e) {
				e.printStackTrace();
				image = null;
			}
		}
		
		final float xCenter = width / 2;
		final float yCenter = height / 2;
		
		float logoPaintSize = logoFramebuffer.getImage().getSize().getX() / 2;

		rootFb.save();
		rootFb.setTransform(rootFb.getTransform().rotate(-timeSinceStartup / 20)
				.translate(width / 2, height / 2)
		);

		rootFb.beginPath();
		rootFb.fillPaint(bgPaint);
		int d = Math.max(width, height);
		rootFb.rect(-d, -d, 2*d, 2*d);
		rootFb.fill();
		rootFb.restore();
		

		rootFb.save();
		rootFb.setTransform(
				Transform.rotating(angleFunction(timeSinceStartup))
				.scale(zoomFunction(timeSinceStartup), 
				zoomFunction(timeSinceStartup)
		).translate(xCenter, yCenter));

		Paint logoFbPaint = rootFb.imagePattern(- logoPaintSize / 2, - logoPaintSize / 2, 
				logoPaintSize, logoPaintSize, 
				0, 
				logoFramebuffer.getImage(), 
				blendFunction(timeSinceStartup)
		);
		rootFb.beginPath();
		rootFb.fillPaint(logoFbPaint);
		rootFb.rect(- logoPaintSize / 2, - logoPaintSize / 2, logoPaintSize, logoPaintSize);
		rootFb.fill();
		rootFb.restore();
	}

	public Image getImage() {
		return logoFramebuffer.getImage();
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	protected void frame(Framebuffer fb, float timeSinceStart) {
		if (timeSinceStart >= 0 && timeSinceStart < this.getDuration()) {
			drawOneSt(timeSinceStart);
			drawLogo(timeSinceStart);
			drawMain(fb, timeSinceStart, width, height);
		}
	}
}

	