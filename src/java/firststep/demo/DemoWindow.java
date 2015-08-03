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
	
	private AnimationsGroup animationsGroup;

	private float getTimeSinceStartup() {
		return (float)((double)System.currentTimeMillis() - startupMoment) / 1000;
	}
	
	Framebuffer textFb, fb2;
	Font boldFont;
	Paint textFbPaint, fb2Paint;

	int xCenter;
	int yCenter;

	int squareSize = 80;
	float cornerRadius = 15;

	@Override
	protected void windowSize(int width, int height) {
		xCenter = width / 2;
		yCenter = height / 2;
		
		float boxLeft = xCenter - squareSize / 2;
		float boxTop = yCenter - squareSize / 2;
		float boxRight = xCenter + squareSize / 2;
		float boxBottom = yCenter + squareSize / 2;

		float tline = 0.3f;
		float tcorner = 0.2f;
		
		LineAnimation[] lineAnims = new LineAnimation[4];
		ArcAnimation[] cornerAnims = new ArcAnimation[4];
		
		lineAnims[0] = new LineAnimation(0.2f, tline * 2, Aftermath.SAVE,
				boxLeft + cornerRadius, 
				boxTop, 
				boxRight - cornerRadius, 
				boxTop);

		cornerAnims[0] = ArcAnimation.after(lineAnims[0], tcorner, Aftermath.SAVE, 
				boxRight - cornerRadius, 
				boxTop + cornerRadius, 
				cornerRadius, -(float)Math.PI / 2, 0, Winding.CW);

		lineAnims[1] = LineAnimation.after(cornerAnims[0], tline, Aftermath.SAVE,
				boxRight, 
				boxTop + cornerRadius,
				boxRight, 
				boxBottom - cornerRadius);

		cornerAnims[1] = ArcAnimation.after(lineAnims[1], tcorner, Aftermath.SAVE, 
				boxRight - cornerRadius, 
				boxBottom - cornerRadius, 
				cornerRadius, 0, (float)Math.PI / 2, Winding.CW);

		lineAnims[2] = LineAnimation.after(cornerAnims[1], tline, Aftermath.SAVE,
				boxRight - cornerRadius, 
				boxBottom,
				boxLeft + cornerRadius, 
				boxBottom);

		cornerAnims[2] = ArcAnimation.after(lineAnims[2], tcorner, Aftermath.SAVE, 
				boxLeft + cornerRadius, 
				boxBottom - cornerRadius, 
				cornerRadius, (float)Math.PI / 2, (float)Math.PI, Winding.CW);

		lineAnims[3] = LineAnimation.after(cornerAnims[2], tline, Aftermath.SAVE,
				boxLeft, 
				boxBottom - cornerRadius,
				boxLeft, 
				boxTop + cornerRadius);

		cornerAnims[3] = ArcAnimation.after(lineAnims[3], tcorner, Aftermath.SAVE, 
				boxLeft + cornerRadius, 
				boxTop + cornerRadius, 
				cornerRadius, -(float)Math.PI, -(float)Math.PI / 2, Winding.CW);
		
		animationsGroup = new AnimationsGroup(2.0f, cornerAnims[3].getStartTime() + cornerAnims[3].getDuration(), Aftermath.SAVE);
		animationsGroup.addAnimation(lineAnims[0]);
		animationsGroup.addAnimation(lineAnims[1]);
		animationsGroup.addAnimation(lineAnims[2]);
		animationsGroup.addAnimation(lineAnims[3]);
		animationsGroup.addAnimation(cornerAnims[0]);
		animationsGroup.addAnimation(cornerAnims[1]);
		animationsGroup.addAnimation(cornerAnims[2]);
		animationsGroup.addAnimation(cornerAnims[3]);
		
		Framebuffer mainFb = getMainFramebuffer(); 

		if (textFb == null) {
			textFb = createFramebuffer(squareSize, squareSize, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
			mainFb.addDependency(textFb);
			textFb.setDrawListener(new DrawListener() {
				@Override
				public void draw(Canvas cnv) {
					float timeSinceStartup = getTimeSinceStartup();
					
					if (boldFont == null) {
						boldFont = cnv.createFont("bold", "JosefinSans-Bold.ttf");
					}

					cnv.fontFace("bold");
					cnv.fontSize(squareSize);
					cnv.beginPath();
					cnv.text(squareSize * 0.7f, squareSize * 0.8f, "f");
					cnv.fillColor(new Color(0.5f, 0.5f, 0.5f, 1.f));
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
					cnv.fill();
				}
			});
		}

		mainFb.setDrawListener(new DrawListener() {
			@Override
			public void draw(Canvas cnv) {
				float timeSinceStartup = getTimeSinceStartup();

				textFbPaint = cnv.imagePattern(xCenter - squareSize / 2, yCenter - squareSize / 2, squareSize, squareSize, 0, textFb.getImage(), 1.0f);
				fb2Paint = cnv.imagePattern(xCenter - squareSize / 2, yCenter - squareSize / 2, squareSize, squareSize, 0, fb2.getImage(), 1.0f);

				//if (!animationsGroup.isActual(timeSinceStartup)) {
					cnv.beginPath();
					cnv.roundedRect(xCenter - squareSize / 2, yCenter - squareSize / 2, squareSize, squareSize, cornerRadius);
					cnv.fillPaint(textFbPaint);
					cnv.fill();
					cnv.fillPaint(fb2Paint);
					cnv.fill();
				//}

				cnv.strokeColor(new Color(255, 255, 192));
				animationsGroup.doFrame(cnv, timeSinceStartup);

			}
		});


	}
	
	public DemoWindow() {
		super (APPNAME, 600, 400, new Color(0.1f, 0.1f, 0.2f, 1.0f));
		startupMoment = System.currentTimeMillis();
	}
	
	public static void main(String... args) {
		new DemoWindow();
		
		Window.loop(fps);
	}
}