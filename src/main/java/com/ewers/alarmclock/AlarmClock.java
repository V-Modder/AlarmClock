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
import java.util.function.Predicate;

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
import com.ewers.alarmclock.components.BrowserPanel;
import com.ewers.alarmclock.components.ContextMenu;
import com.ewers.alarmclock.components.MyWebCalender;
import com.ewers.alarmclock.components.RoundedStripesButton;
import com.ewers.alarmclock.components.SettingsPanel;
import com.ewers.alarmclock.display.Display;

public class AlarmClock extends JFrame implements ActionListener, MouseListener, Runnable {

	private static final long serialVersionUID = 1L;

	private JLayeredPane contentPane;
	private JPanel mainPanel;
	private BrowserPanel browserPanel;
	private JLabel timeLabel;
	private RoundedStripesButton configButton;
	private JLabel displayOffButton;
	private Timer timer;
	private WebCalendar calendar;
	private ContextMenu contextMenu;
	private SettingsPanel settingsPanel;
	private AlarmPanel alarmPanel;

	public static void main(String[] args) {

		if (Arrays.stream(args).anyMatch(new Predicate<String>() {
			public boolean test(String arg) {
				return arg.equals("-debug");
			}
		})) {
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
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		if (!Constants.isDebug) {
			setUndecorated(true);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
			setCursor(blankCursor);
		}
		setBounds(0, 0, 800, 480);
		setTitle("AlarmClock");
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("gear.png")).getImage());
		addMouseListener(this);

		timer = new Timer(1000, this);
		timer.start();
		new Thread(this).start();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer && timeLabel != null && calendar != null) {
			String html = "<html><p><span style=\"font-family:trebuchet ms,helvetica,sans-serif;\"><strong><span style=\"font-size: 50px;\"><TIME></span></strong><span style=\"font-size: 25px;\">:<SECONDS></span></span></p></html>";
			SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
			Date date = new Date();
			html = html.replace("<TIME>", sdf.format(date));
			sdf = new SimpleDateFormat("ss");
			html = html.replace("<SECONDS>", sdf.format(date));
			timeLabel.setText(html);
			calendar.setDate(date, true);
			calendar.invalidate();
			return;
		} else if (e.getSource() == configButton) {
			contextMenu.setVisible(true);
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (e.getSource() == displayOffButton) {
			if (Constants.isRaspberryPi) {
				Display.startScrennSaver();
			}
		} else {
			contextMenu.setVisible(false);
		}
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void run() {
		Color transparent = new Color(0, 0, 0, 1);
		contentPane = new JLayeredPane();
		contentPane.setBackground(transparent);
		contentPane.setLayout(null);
		contentPane.setPreferredSize(new Dimension(800, 480));
		contentPane.setBounds(0, 0, 800, 480);
		setContentPane(contentPane);

		mainPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			public Image backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("background.jpg")).getImage();

			public void paintComponent(Graphics g) {
				g.drawImage(backgroundImage, 0, 0, null);
				super.paintComponent(g);
			}
		};
		mainPanel.setBackground(transparent);
		mainPanel.setPreferredSize(new Dimension(800, 480));
		mainPanel.setBounds(0, 0, 800, 480);
		mainPanel.setName("mainPanel");
		contentPane.add(mainPanel);
		contentPane.setLayer(mainPanel, 0);
		mainPanel.setLayout(null);

		configButton = new RoundedStripesButton();
		configButton.setBackground(transparent);
		configButton.setStripeColor(Color.WHITE);
		configButton.setStripeCount(3);
		configButton.setStripeThikness(2);
		configButton.setSize(new Dimension(40, 40));
		configButton.setPreferredSize(new Dimension(40, 40));
		configButton.setMinimumSize(new Dimension(40, 40));
		configButton.setContentAreaFilled(false);
		configButton.setLocation(760, 0);
		configButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		configButton.addActionListener(this);
		mainPanel.add(configButton);

		Image imgLight = new ImageIcon(getClass().getClassLoader().getResource("light.png")).getImage();
		imgLight = ImageHelper.resize(imgLight, 25, 25);
		displayOffButton = new JLabel(new ImageIcon(imgLight));
		displayOffButton.setBackground(transparent);
		displayOffButton.setSize(new Dimension(40, 40));
		displayOffButton.setPreferredSize(new Dimension(40, 40));
		displayOffButton.setMinimumSize(new Dimension(40, 40));
		displayOffButton.setLocation(710, 0);
		displayOffButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		displayOffButton.addMouseListener(this);
		mainPanel.add(displayOffButton);

		timeLabel = new JLabel();
		timeLabel.setBounds(0, 0, 400, 240);
		timeLabel.setBackground(Color.BLACK);
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timeLabel.setVerticalAlignment(SwingConstants.CENTER);
		timeLabel.setForeground(Color.WHITE);
		timeLabel.setText("<html><p><span style=\"font-family:trebuchet ms,helvetica,sans-serif;\"><span style=\"font-size: 14px;\">DAY</span></span></p><p><span style=\"font-family:trebuchet ms,helvetica,sans-serif;\"><strong><span style=\"font-size: 22px;\">TIME</span></strong><span style=\"font-size: 12px;\">:SECONDS</span></span></p><p><span style=\"font-family:trebuchet ms,helvetica,sans-serif;\"><span style=\"font-size: 14px;\">DATE</span></span></p></html>");
		mainPanel.add(timeLabel);

		initCalendar();
		initBrowserPanel();

		settingsPanel = new SettingsPanel();
		settingsPanel.setName("settingsPanel");
		contentPane.add(settingsPanel);
		contentPane.setLayer(settingsPanel, 1);

		contextMenu = new ContextMenu();
		contextMenu.setName("contextMenu");
		contentPane.add(contextMenu);
		contentPane.setLayer(contextMenu, 2);

		alarmPanel = new AlarmPanel();
		contentPane.add(alarmPanel);
		contentPane.setLayer(alarmPanel, 3);
	}

	private void initCalendar() {
		calendar = new MyWebCalender();
		calendar.setBounds(75, 260, 250, 200);
		calendar.setDate(new Date(), true);
		calendar.setDisplayWeekNumbers(false);
		calendar.setHorizontalSlide(false);
		calendar.setEnabled(false);
		calendar.setAnimate(true);
		calendar.setTitleFormat(new SimpleDateFormat("MMMM"));
		mainPanel.add(calendar);
	}

	private void initBrowserPanel() {
		browserPanel = new BrowserPanel();
		// JLabel p = new JLabel();
		// p.setBounds(400, 20, 400, 240);
		// p.setBackground(Color.BLACK);
		// p.setForeground(Color.magenta);
		// p.setHorizontalAlignment(SwingConstants.CENTER);
		// p.setVerticalAlignment(SwingConstants.CENTER);
		// p.setForeground(Color.WHITE);
		// p.setText("jzthjdzthjhztdhthf");
		mainPanel.add(browserPanel);
	}
}
