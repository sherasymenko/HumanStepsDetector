package app.main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.ui.RefineryUtilities;
import app.graph.OpenSheet;
import app.media.PlayVideo;

public class Main {
	private static OpenSheet sheet;
	private static PlayVideo player = new PlayVideo();

	public static void main(String[] args) {
		openSettingsGUI();
	}

	public static void openSheetGUI(String filePath) {
		sheet = new OpenSheet(filePath,"", "");
		RefineryUtilities.centerFrameOnScreen(sheet);
		sheet.setVisible(true);
		sheet.pack();
	}

	public static OpenSheet getSheet() {
		return sheet;
	}

	public static void setSheet(OpenSheet sheet) {
		Main.sheet = sheet;
	}

	public static PlayVideo getPlayer() {
		return player;
	}

	public static void setPlayer(PlayVideo player) {
		Main.player = player;
	}

	public static void openMediaPlayerGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				player.initAndShowGUI();
			}
		});
	}

	private static void openSettingsGUI() {
		SettingPanel settingPanel = new SettingPanel();
		settingPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}