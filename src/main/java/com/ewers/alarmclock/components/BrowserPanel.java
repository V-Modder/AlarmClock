package com.ewers.alarmclock.components;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

public class BrowserPanel extends JFXPanel implements ChangeListener<State> {

	private static final long serialVersionUID = 1L;
	private static final String libreelecUrl = "http://libreelec";

	private WebView browser;

	public BrowserPanel() {
		setBounds(360, 45, 450, 400);
		setLayout(null);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX();
				loadUri(libreelecUrl);
			}
		});
	}

	private void initFX() {
		Scene scene = createScene();
		setScene(scene);
	}

	private Scene createScene() {
		browser = new WebView();
		browser.getEngine().setUserStyleSheetLocation(getClass().getResource("/style.css").toString());
		browser.getEngine().getLoadWorker().stateProperty().addListener(this);
		Scene scene = new Scene(browser);

		return scene;
	}

	@Override
	public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
		if (newValue == State.SUCCEEDED) {
			browser.getEngine().executeScript("$(\"body\").first.css(\"overflow\", \"hidden\");");
		} else if (newValue == State.FAILED) {
			String uri = getClass().getResource("/404NotFound.html").toExternalForm();
			loadUri(uri);
			new Thread(() -> {
				loadAfterSleep();
			});
		}
	}

	private void loadAfterSleep() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
		loadUri(libreelecUrl);
	}

	private void loadUri(String uri) {
		browser.getEngine().load(uri);
	}
}
