package com.ewers.alarmclock.music;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

class LinePitcher implements SourceDataLine {
	public static final PitchControl.Type PITCH_CONTROL = new PitchControl.Type();

	private static final class PitchControl extends FloatControl {
		protected PitchControl() {
			super(PITCH_CONTROL, -1.0F, 1.0F, Float.MIN_VALUE, 10, 0.0F, "", "-", "+", "0");
		}

		private static final class Type extends FloatControl.Type {
			private Type() {
				super("Pitch Control");
			}
		}
	}

	private final Object lock = new Object();
	private final SourceDataLine source;
	private final List<Control> controls;
	private final PitchControl ctrl;
	private ByteBuffer bOut;
	private float samplePos, framePos;
	private int frameSize;

	public LinePitcher(SourceDataLine source) {
		this.source = source;
		try {
			source.open();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		List<Control> controls = new ArrayList<Control>(Arrays.asList(source.getControls()));
		ctrl = new PitchControl();
		controls.add(ctrl);
		this.controls = Collections.unmodifiableList(controls);
		source.close();
	}

	public void drain() {
		synchronized (lock) {
			source.drain();
		}
	}

	public void flush() {
		synchronized (lock) {
			source.flush();
		}
	}

	public void start() {
		synchronized (lock) {
			source.start();
		}
	}

	public void stop() {
		synchronized (lock) {
			source.stop();
		}
	}

	public boolean isRunning() {
		return source.isRunning();
	}

	public boolean isActive() {
		return source.isActive();
	}

	public AudioFormat getFormat() {
		return source.getFormat();
	}

	public int getBufferSize() {
		return source.getBufferSize();
	}

	public int available() {
		return source.available();
	}

	public int getFramePosition() {
		return (int) framePos;
	}

	public long getLongFramePosition() {
		return getFramePosition();
	}

	public long getMicrosecondPosition() {
		return source.getMicrosecondPosition();
	}

	public float getLevel() {
		return source.getLevel();
	}

	public javax.sound.sampled.Line.Info getLineInfo() {
		return source.getLineInfo();
	}

	public void open() throws LineUnavailableException {
		open(null);
	}

	public void close() {
		source.close();
	}

	public boolean isOpen() {
		return source.isOpen();
	}

	public Control[] getControls() {
		return controls.toArray(new Control[controls.size()]);
	}

	public boolean isControlSupported(Type control) {
		for (Control c : controls) {
			if (c.getType() == control) {
				return true;
			}
		}
		return false;
	}

	public Control getControl(Type control) {
		for (Control c : controls) {
			if (c.getType() == control) {
				return c;
			}
		}
		return null;
	}

	public void addLineListener(LineListener listener) {
		source.addLineListener(listener);
	}

	public void removeLineListener(LineListener listener) {
		source.removeLineListener(listener);
	}

	public void open(AudioFormat format, int bufferSize) throws LineUnavailableException {
		synchronized (lock) {
			source.open(format, bufferSize);
			bOut = ByteBuffer.wrap(new byte[bufferSize]);
			frameSize = format.getChannels() * ((format.getSampleSizeInBits() + 7) / 8);
			samplePos = 0.0f;
		}
	}

	public void open(AudioFormat format) throws LineUnavailableException {
		if (format == null) {
			source.open();
			format = source.getFormat();
			source.close();
		}
		int bufferSize = format.getChannels() * ((format.getSampleSizeInBits() + 7) / 8);
		bufferSize *= (int) (format.getSampleRate() / 25.0f) * format.getFrameSize();
		open(format, bufferSize);
	}

	public int write(byte[] b, int off, int len) {
		synchronized (lock) {
			ByteBuffer bIn = ByteBuffer.wrap(b, off, len);
			samplePos %= 1.0F;
			int p = 0;
			while (p < len) {
				for (int c = 0; c < frameSize; c++) {
					bOut.put(bIn.get(p + c + off));
				}
				if (bOut.position() == bOut.capacity()) {
					byte[] bb = bOut.array().clone();
					source.write(bb, 0, bOut.capacity());
					bOut.clear();
				}
				float fp = ctrl.getValue() + 1.0F;
				samplePos += fp;
				framePos += fp;
				p = (int) samplePos * frameSize;
			}
			b = bOut.array().clone();
			source.write(b, 0, bOut.position());
			bOut.clear();
			return len;
		}
	}
}