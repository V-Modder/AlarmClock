package com.ewers.alarmclock;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.alee.extended.date.WebCalendar;
import com.alee.laf.WebLookAndFeel;
import com.ewers.alarmclock.components.AlarmPanel;
import com.ewers.alarmclock.components.ContextMenu;
import com.ewers.alarmclock.components.MyWebCalender;
import com.ewers.alarmclock.components.RoundedPanel;
import com.ewers.alarmclock.components.RoundedStripesButton;
import com.ewers.alarmclock.components.SettingsPanel;
import com.ewers.alarmclock.components.WeatherPanel;
import com.ewers.alarmclock.display.Display;

public class AlarmClock extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private JLayeredPane contentPane;
	private JPanel mainPanel;
	private RoundedPanel weatherpanel;
	private JLabel timeLabel;
	private RoundedStripesButton configButton;
	private JLabel displayOffButton;
	private Timer timer;
	private WebCalendar calendar;
	private ContextMenu contextMenu;
	private SettingsPanel settingsPanel;
	private AlarmPanel alarmPanel;

	public static void main(String[] args) {
		if (Arrays.stream(args).anyMatch(arg -> arg.equals("-debug"))) {
			Constants.isDebug = true;
		}
		Constants.isRaspberryPi = Display.isRaspberryPi();
		WebLookAndFeel.install();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AlarmClock frame = new AlarmClock();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AlarmClock() throws IOException {
		Color transparent = new Color(0, 0, 0, 1);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.getContentPane().setLayout(null);
		if (!Constants.isDebug) {
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
			this.setCursor(blankCursor);
		}
		this.setBounds(0, 0, 800, 480);
		this.setTitle("AlarmClock");
		this.setIconImage(new ImageIcon(this.getClass().getClassLoader().getResource("gear.png")).getImage());
		this.addMouseListener(this);

		this.timer = new Timer(1000, this);
		this.timer.start();

		this.contentPane = new JLayeredPane();
		this.contentPane.setBackground(transparent);
		this.contentPane.setLayout(null);
		this.contentPane.setPreferredSize(new Dimension(800, 480));
		this.contentPane.setBounds(0, 0, 800, 480);
		this.setContentPane(contentPane);

		this.mainPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			public Image backgroundImage = new ImageIcon(this.getClass().getClassLoader().getResource("background.jpg")).getImage();

			public void paintComponent(Graphics g) {
				g.drawImage(backgroundImage, 0, 0, null);
				super.paintComponent(g);
			}
		};
		this.mainPanel.setBackground(transparent);
		this.mainPanel.setPreferredSize(new Dimension(800, 480));
		this.mainPanel.setBounds(0, 0, 800, 480);
		this.mainPanel.setName("mainPanel");
		this.contentPane.add(this.mainPanel);
		this.contentPane.setLayer(this.mainPanel, 0);
		this.mainPanel.setLayout(null);

		// Image img = new ImageIcon(this.getClass().getClassLoader().getResource("gear.png")).getImage();
		// img = ImageHelper.resize(img, 25, 25);
		this.configButton = new RoundedStripesButton();
		this.configButton.setBackground(transparent);
		this.configButton.setStripeColor(Color.WHITE);
		this.configButton.setStripeCount(3);
		this.configButton.setStripeThikness(2);
		this.configButton.setSize(new Dimension(40, 40));
		this.configButton.setPreferredSize(new Dimension(40, 40));
		this.configButton.setMinimumSize(new Dimension(40, 40));
		this.configButton.setContentAreaFilled(false);
		this.configButton.setLocation(760, 0);
		this.configButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.configButton.addActionListener(this);
		this.mainPanel.add(this.configButton);

		Image imgLight = new ImageIcon(this.getClass().getClassLoader().getResource("light.png")).getImage();
		imgLight = ImageHelper.resize(imgLight, 25, 25);
		this.displayOffButton = new JLabel(new ImageIcon(imgLight));
		this.displayOffButton.setBackground(transparent);
		this.displayOffButton.setSize(new Dimension(40, 40));
		this.displayOffButton.setPreferredSize(new Dimension(40, 40));
		this.displayOffButton.setMinimumSize(new Dimension(40, 40));
		this.displayOffButton.setLocation(710, 0);
		this.displayOffButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.displayOffButton.addMouseListener(this);
		this.mainPanel.add(this.displayOffButton);

		this.weatherpanel = new WeatherPanel();
		this.mainPanel.add(this.weatherpanel);

		this.timeLabel = new JLabel();
		this.timeLabel.setBounds(0, 0, 400, 240);
		this.timeLabel.setBackground(Color.BLACK);
		this.timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.timeLabel.setVerticalAlignment(SwingConstants.CENTER);
		this.timeLabel.setForeground(Color.WHITE);
		this.timeLabel.setText("<html><p><span style=\"font-family:trebuchet ms,helvetica,sans-serif;\"><span style=\"font-size: 14px;\">DAY</span></span></p><p><span style=\"font-family:trebuchet ms,helvetica,sans-serif;\"><strong><span style=\"font-size: 22px;\">TIME</span></strong><span style=\"font-size: 12px;\">:SECONDS</span></span></p><p><span style=\"font-family:trebuchet ms,helvetica,sans-serif;\"><span style=\"font-size: 14px;\">DATE</span></span></p></html>");
		this.mainPanel.add(this.timeLabel);

		this.calendar = new MyWebCalender();
		this.calendar.setBounds(75, 260, 250, 200);
		this.calendar.setDate(new Date(), true);
		this.calendar.setDisplayWeekNumbers(false);
		this.calendar.setHorizontalSlide(false);
		this.calendar.setEnabled(false);
		this.calendar.setAnimate(true);
		this.calendar.setTitleFormat(new SimpleDateFormat("MMMM"));
		this.mainPanel.add(this.calendar);

		this.settingsPanel = new SettingsPanel();
		this.settingsPanel.setName("settingsPanel");
		this.contentPane.add(this.settingsPanel);
		this.contentPane.setLayer(this.settingsPanel, 1);

		this.contextMenu = new ContextMenu();
		this.contextMenu.setName("contextMenu");
		this.contentPane.add(this.contextMenu);
		this.contentPane.setLayer(this.contextMenu, 2);

		this.alarmPanel = new AlarmPanel();
		this.contentPane.add(this.alarmPanel);
		this.contentPane.setLayer(this.alarmPanel, 3);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.timer && this.timeLabel != null && this.calendar != null) {
			String html = "<html><p><span style=\"font-family:trebuchet ms,helvetica,sans-serif;\"><strong><span style=\"font-size: 50px;\"><TIME></span></strong><span style=\"font-size: 25px;\">:<SECONDS></span></span></p></html>";
			SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
			Date date = new Date();
			html = html.replace("<TIME>", sdf.format(date));
			sdf = new SimpleDateFormat("ss");
			html = html.replace("<SECONDS>", sdf.format(date));
			this.timeLabel.setText(html);
			this.calendar.setDate(date, true);
			this.calendar.invalidate();
			return;
		} else if (e.getSource() == this.configButton) {
			this.contextMenu.setVisible(true);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == this.displayOffButton) {
			if (Constants.isRaspberryPi) {
				Display.startScrennSaver();
			}
		} else {
			this.contextMenu.setVisible(false);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
