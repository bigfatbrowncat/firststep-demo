package firststep.demo;

import firststep.Color;
import firststep.Framebuffer;
import firststep.Window;
import firststep.internal.portaudio.BlockingStream;
import firststep.internal.portaudio.PortAudio;
import firststep.internal.portaudio.StreamParameters;

public class DemoWindow extends Window {

	private class SoundThread extends Thread {
		private volatile boolean stopFlag = false;
		
		public void stopSound() { stopFlag = true; }
		public void run() {
			PortAudio.initialize();
			
			int channels = 2;
			int frames = 100;

			StreamParameters isp = new StreamParameters();
			isp.channelCount = channels;
			isp.device = PortAudio.getDefaultOutputDevice();
			isp.sampleFormat = PortAudio.FORMAT_FLOAT_32;
			
			BlockingStream bs = PortAudio.openStream(null, isp, 44100, frames, 0);
			bs.start();
			float[] buf = new float[frames * channels];
			int p = 0;
			for (int k = 0; k < 44100 * 5 / frames; k++) {
				for (int i = 0; i < buf.length; i += 2) {
					buf[i] = (float) (0.05f * Math.sin(2 * Math.PI * p / 44100 * 200));
					buf[i + 1] = (float) (0.05f * Math.sin(2 * Math.PI * p / 44100 * 200));
					p++;
				}
				if (stopFlag) break;
				bs.write(buf, frames);
			}
			bs.stop();
		}
	};
	
	private SoundThread soundThread = new SoundThread();
	
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
	
	@Override
	public void onClose() {
		soundThread.stopSound();
		super.onClose();
	}
	
	public DemoWindow() {
		super (APPNAME, 600, 400, Color.fromRGBA(backRed, backGreen, backBlue, 1.0f));
		startupMoment = System.currentTimeMillis();
		soundThread.start();
	}
	
	
	
	public static void main(String... args) {
		new DemoWindow();
		
		Window.loop(fps);
	}
}