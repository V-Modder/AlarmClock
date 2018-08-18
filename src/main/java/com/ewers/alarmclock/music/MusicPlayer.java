package com.ewers.alarmclock.music;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
	private static final AudioFormat MIXER_FORMAT = new AudioFormat(Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, true);

	private FloatControl volumeValue;
	private Thread playerThread;
	private boolean play;

	public void playStream(String url) throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.play(new URL(url));
	}

	public void playFile(String path) throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {
		File f = new File(path);

		this.play(f.toURI().toURL());
	}

	public void stop() {
		if (this.playerThread != null) {
			this.playerThread.interrupt();
			this.play = false;
			this.playerThread.isInterrupted();
		}
	}

	public void setVolume(int percent) {
		if (percent > 100 || percent < 0) {
			throw new IllegalArgumentException("Value percent must be in range inclusive of 0 - 100");
		}

		float minimum = this.dBToLinear(this.volumeValue.getMinimum());
		float range = this.dBToLinear(this.volumeValue.getMaximum()) - minimum;
		this.volumeValue.setValue(this.linearToDB(percent / 100.0f * range + minimum));
	}

	private float dBToLinear(float f) {
		float f1 = (float) Math.pow(10.0, f / 20.0);
		return f1;
	}

	private float linearToDB(float f) {
		float f1 = (float) ((Math.log((f != 0.0) ? f : 1.0E-14) / Math.log(10.0)) * 20.0);
		return f1;
	}

	private void play(URL url) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		this.play = true;
		AudioInputStream aisBefore = AudioSystem.getAudioInputStream(url);
		final AudioInputStream audioInputStream = toMixerFormat(aisBefore);
		final LinePitcher linePitcher = new LinePitcher(AudioSystem.getSourceDataLine(audioInputStream.getFormat()));
		volumeValue = (FloatControl) linePitcher.getControl(FloatControl.Type.MASTER_GAIN);
		volumeValue.setValue(volumeValue.getMinimum());
		try {
			linePitcher.open();
			linePitcher.start();
			this.playerThread = new Thread() {
				byte[] buf = new byte[linePitcher.getBufferSize()];

				@Override
				public void run() {
					try {
						int r = 0;
						while ((r = audioInputStream.read(buf)) != -1 && !isInterrupted() && MusicPlayer.this.play) {
							linePitcher.write(buf, 0, r);
						}
					} catch (IOException e) {
						e.printStackTrace();
						interrupt();
					}
					linePitcher.drain();
					linePitcher.close();
				}
			};
			this.playerThread.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	private static final AudioInputStream toMixerFormat(AudioInputStream source) {
		source = ensurePCM(source);
		AudioFormat audioFormat = source.getFormat();
		if (!audioFormat.matches(MIXER_FORMAT)) {
			source = AudioSystem.getAudioInputStream(MIXER_FORMAT, source);
		}
		return source;
	}

	private static final AudioInputStream ensurePCM(AudioInputStream source) {
		AudioFormat audioFormat = source.getFormat();
		Encoding encoding = audioFormat.getEncoding();
		if (!Encoding.PCM_SIGNED.equals(encoding) && !Encoding.PCM_UNSIGNED.equals(encoding)) {
			int sampleSizeBits = audioFormat.getSampleSizeInBits();
			int channelCount = audioFormat.getChannels();
			if (sampleSizeBits < 8) {
				sampleSizeBits = 8;
			}
			int minFrames = channelCount * sampleSizeBits / 8;
			int frameSize = audioFormat.getFrameSize();
			if (frameSize < minFrames) {
				frameSize = minFrames;
			}
			audioFormat = new AudioFormat(Encoding.PCM_SIGNED, audioFormat.getSampleRate(), sampleSizeBits * 2, audioFormat.getChannels(), frameSize * 2, audioFormat.getSampleRate(), audioFormat.isBigEndian());
			source = AudioSystem.getAudioInputStream(audioFormat, source);
		}
		return source;
	}
}
