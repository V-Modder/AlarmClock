package com.ewers.alarmclock.display;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;

public class Display {

	private static final String WAKEUP_DISPLAY = "xset -display :0 s reset";
	private static final String START_SCREENSAVER = "xset -display :0 s activate";
	private static final String RASPBERRY_PI_MAC_PREFIX = "B8:27:EB";

	public static void wakeupDisplay() {
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(WAKEUP_DISPLAY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void startScrennSaver() {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Runtime rt = Runtime.getRuntime();
				try {
					rt.exec(START_SCREENSAVER);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	public static boolean isRaspberryPi() {
		try {
			Collection<String> interfaces = getAllLocalMacAddresses();
			for (String networkInterface : interfaces) {
				if (networkInterface.startsWith(RASPBERRY_PI_MAC_PREFIX)) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	private static Collection<String> getAllLocalMacAddresses() throws IOException {
		final Enumeration<NetworkInterface> inetAddresses = NetworkInterface.getNetworkInterfaces();
		final Collection<String> addresses = new LinkedList<String>();

		while (inetAddresses.hasMoreElements()) {
			final byte[] macBytes = inetAddresses.nextElement().getHardwareAddress();

			if (macBytes == null)
				continue;

			addresses.add(getMacAddress(macBytes));
		}

		return addresses;
	}

	private static String getMacAddress(byte[] macBytes) {
		final StringBuilder strBuilder = new StringBuilder();

		for (int i = 0; i < macBytes.length; i++) {
			strBuilder.append(String.format("%02X%s", macBytes[i],
					(i < macBytes.length - 1) ? ":" : ""));
		}

		return strBuilder.toString().toUpperCase();
	}

}
