package com.ewers.alarmclock.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import com.ewers.alarmclock.Constants;
import com.ewers.alarmclock.config.Alarm;
import com.ewers.alarmclock.config.Configuration;
import com.ewers.alarmclock.display.Display;
import com.ewers.alarmclock.holiday.HolidayChecker;
import com.ewers.alarmclock.modules.common.BackgroundWorker;
import com.ewers.alarmclock.modules.common.BackgroundWorker.Finished;
import com.ewers.alarmclock.modules.common.BackgroundWorker.Parameters;
import com.ewers.alarmclock.modules.common.BackgroundWorker.ProgressChanged;
import com.ewers.alarmclock.modules.common.Worker;
import com.ewers.alarmclock.music.MusicPlayer;

public class AlarmPanel extends JPanel implements Worker, MouseListener {

	private static final long serialVersionUID = 1L;
	private static final String RADIO_21_URL = "http://188.94.97.91:80/radio21.mp3";
	private static final String LOCAL_FILE = "FailOver.mp3";

	private BackgroundWorker backgroundWorker;
	private MusicPlayer musicPlayer;
	private boolean isPlaying;
	private HolidayChecker holidayChecker;

	public AlarmPanel() {
		this.setBounds(0, 0, 800, 480);
		this.setMaximumSize(new Dimension(400, 240));
		this.setBackground(new Color(0, 0, 0, 1));
		this.setVisible(false);
		this.setLayout(null);
		this.setOpaque(false);
		this.addMouseListener(this);

		this.backgroundWorker = new BackgroundWorker(this);
		this.backgroundWorker.runWorkerAsync();
		this.isPlaying = false;
		try {
			this.holidayChecker = new HolidayChecker("j.ewers@setlog.com", "Jowa8856");
		} catch (Exception e) {
			e.printStackTrace();
		}

		InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream(LOCAL_FILE);

		byte[] buffer;
		try {
			buffer = new byte[resourceStream.available()];
			resourceStream.read(buffer);

			File targetFile = new File(LOCAL_FILE);
			OutputStream outStream = new FileOutputStream(targetFile);
			outStream.write(buffer);
			outStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private boolean triggerAlarm(Alarm alarm) {
		if (this.isPlaying) {
			return false;
		}

		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		boolean isHoliday;
		try {
			isHoliday = this.holidayChecker.isHolydayToday();
		} catch (Exception e) {
			e.printStackTrace();
			isHoliday = false;
		}
		if (!isHoliday
				&& (dayOfWeek == Calendar.MONDAY && alarm.onMonday()
						|| dayOfWeek == Calendar.TUESDAY && alarm.onTuesday()
						|| dayOfWeek == Calendar.WEDNESDAY && alarm.onWendsday()
						|| dayOfWeek == Calendar.THURSDAY && alarm.onThursday()
						|| dayOfWeek == Calendar.FRIDAY && alarm.onFriday()
						|| dayOfWeek == Calendar.SATURDAY && alarm.onSaturday()
						|| dayOfWeek == Calendar.SUNDAY && alarm.onSunday())) {
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			if (alarm.getTime().getHours() == hour
					&& alarm.getTime().getMinutes() == minute) {
				return true;
			}
		}

		return false;
	}

	private void playAlarm() {
		if (Constants.isRaspberryPi) {
			Display.wakeupDisplay();
		}
		this.musicPlayer = new MusicPlayer();
		try {
			this.musicPlayer.playStream(RADIO_21_URL);
		} catch (Exception e) {
			try {
				this.musicPlayer.playFile(LOCAL_FILE);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				e1.printStackTrace();
			}
		}

		for (int i = 3; i < 50 && this.isPlaying; i++) {
			this.musicPlayer.setVolume(i);
			this.sleep(400);
		}
		if (this.isPlaying) {
			this.sleep(5 * 60 * 1000);
		}

		this.musicPlayer.stop();
		this.musicPlayer = null;
		if (Constants.isRaspberryPi) {
			Display.startScrennSaver();
		}
	}

	private void sleep(long millis) {
		for (long i = 0; i < millis && this.isPlaying; i += 10) {
			try {
				Thread.sleep(i);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void doWork(Parameters args) {
		Calendar calendar = Calendar.getInstance();
		while (true) {
			calendar.setTime(new Date());
			int ms = calendar.get(Calendar.SECOND) * 1000;
			ms += calendar.get(Calendar.MILLISECOND);
			ms = 60000 - ms;
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<Alarm> alarms = Configuration.getInstance().getAlarms();
			for (Alarm alarm : alarms) {
				if (this.triggerAlarm(alarm)) {
					this.isPlaying = true;
					this.backgroundWorker.reportProgress(100);
					this.playAlarm();
				}
			}
		}
	}

	@Override
	public void workerFinished(Finished args) {
	}

	@Override
	public void progressChanged(ProgressChanged args) {
		this.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		this.isPlaying = false;
		this.setVisible(false);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
