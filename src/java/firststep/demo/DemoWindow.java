package firststep.demo;

import firststep.Canvas;
import firststep.Canvas.Winding;
import firststep.Color;
import firststep.Framebuffer;
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
	
	Framebuffer fb, fb2;
	Paint fbPaint, fb2Paint;

	@Override
	protected void frame(Canvas cnv) {
		float timeSinceStartup = (float)((double)System.currentTimeMillis() - startupMoment) / 1000;

		if (fb == null) {
			fb = cnv.createFramebuffer(100, 100, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
		}
		fb.beginDrawing(1.0f);
		//cnv.save();
		cnv.fillColor(new Color(255, 128, 128));
		cnv.beginPath();
		cnv.roundedRect((float)Math.sin(timeSinceStartup) * 50, 0, 50.0f, 50.0f, 10.0f);
		cnv.fill();
		//cnv.restore();
		fb.endDrawing();
			
		if (fb2 == null) {
			fb2 = cnv.createFramebuffer(100, 100, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
		}
		fb2.beginDrawing(1.0f);
		//cnv.save();
		cnv.beginPath();
		cnv.rect((float)Math.sin(timeSinceStartup) * 50, 0, 50.0f, 50.0f);
		cnv.fillColor(new Color(128, 255, 128));
		cnv.fill();
		//cnv.restore();
		fb2.endDrawing();
		
		fbPaint = cnv.imagePattern(0, 0, 50.0f, 50.0f, 0.2f*timeSinceStartup, fb.getImage(), 1.0f);
		fb2Paint = cnv.imagePattern(0, 0, 100.0f, 100.0f, -0.1f*timeSinceStartup, fb2.getImage(), 0.5f);

		Framebuffer mainFb = cnv.getMainFramebuffer(); 
		mainFb.beginDrawing(1.0f);

		if (!animationsGroup.isActual(timeSinceStartup)) {
			cnv.beginPath();
			cnv.roundedRect(xCenter - squareSize / 2, yCenter - squareSize / 2, squareSize, squareSize, cornerRadius);
			cnv.fillPaint(fbPaint);
			cnv.fill();
			cnv.fillPaint(fb2Paint);
			cnv.fill();
		}

		cnv.strokeColor(new Color(255, 255, 192));
		animationsGroup.doFrame(cnv, timeSinceStartup);

		mainFb.endDrawing();

		
	}

	
	int xCenter;
	int yCenter;

	float squareSize = 80;
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
	}
	
	public DemoWindow() {
		super (APPNAME, 600, 400, new Color(0.5f, 0.5f, 0.5f, 1.0f));
		startupMoment = System.currentTimeMillis();
	}
	
	public static void main(String... args) {
		new DemoWindow();
		
		Window.loop(fps);
	}
}