package app.main;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import app.graph.OpenSheet;
import app.media.PlayVideo;
import javafx.scene.media.MediaException;

@SuppressWarnings("serial")
public class SettingPanel extends JFrame {
	private JLabel errorMessage = new JLabel("Znaleziono b³êdy: ");
	private JButton selectFile = new JButton("wybierz plik...");
	private JTextField selectedFilePath = new JTextField("", 50);
	private JButton selectVideo = new JButton("wybierz filmik...");
	private JTextField selectedVideoPath = new JTextField("", 50);
	private JLabel speedLabel = new JLabel("Szybkoœæ");
	private JRadioButton speed1 = new JRadioButton("0.1");
	private JRadioButton speed2 = new JRadioButton("0.25");
	private JRadioButton speed3 = new JRadioButton("0.5");
	private JRadioButton speed4 = new JRadioButton("1");
	private JLabel frequencyLabel = new JLabel("Czêstotliwoœæ");
	private JTextField frequency = new JTextField("", 10);
	private JLabel frequencyMeasurement = new JLabel("Hz");
	private static JButton start = new JButton("Start");
	private JButton resetSetting = new JButton("Wyczyœæ ustawienia");
	private JPanel newPanel = new JPanel();
	private JPanel line1 = new JPanel();
	private JPanel line2 = new JPanel();
	private JPanel line3 = new JPanel();
	private JPanel line4 = new JPanel();
	private JPanel line5 = new JPanel();
	private GridBagConstraints constraints = new GridBagConstraints();
	private OpenSheet chart;
	private PlayVideo player;

	public SettingPanel() {
		super("Panel sterowania");
		elementsSetting();
		addElementsToPanel();
		panelSetting();
	}

	private void elementsSetting() {
		errorMessage.setVisible(false);
		errorMessage.setForeground(Color.red);

		selectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(newPanel);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					selectedFilePath.setText(selectedFile.getAbsolutePath());
					Main.openSheetGUI(selectedFile.getAbsolutePath());
				}
			}
		});
		selectedFilePath.setEditable(false);

		selectVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(newPanel);
				if (result == JFileChooser.APPROVE_OPTION) {
					Main.openMediaPlayerGUI();
					File selectedVideo = fileChooser.getSelectedFile();
					selectedVideoPath.setText(selectedVideo.getAbsolutePath());
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							try {
								player.selectVideo(selectedVideoPath.getText());
							} catch (MediaException ex) {
							}
						}
					});

				}
			}
		});
		selectedVideoPath.setEditable(false);
		speed1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (speed1.isSelected()) {
					speed2.setSelected(false);
					speed3.setSelected(false);
					speed4.setSelected(false);
				}
			}
		});
		speed2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (speed2.isSelected()) {
					speed1.setSelected(false);
					speed3.setSelected(false);
					speed4.setSelected(false);
				}
			}
		});
		speed3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (speed3.isSelected()) {
					speed1.setSelected(false);
					speed2.setSelected(false);
					speed4.setSelected(false);
				}
			}
		});
		speed4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (speed4.isSelected()) {
					speed1.setSelected(false);
					speed2.setSelected(false);
					speed3.setSelected(false);
				}
			}
		});

		start.addActionListener(new ActionListener() {
			List<JRadioButton> radioList = Arrays.asList(speed1, speed2, speed3, speed4);
			public void actionPerformed(ActionEvent e) {
				if (start.getText().equals("Start")) {
					chart = Main.getSheet();
					if (!selectedFilePath.getText().equals(""))
						chart.startDraw(selectedFilePath.getText(), false, getSpeed(radioList));
					if (!selectedVideoPath.getText().equals(""))
						player.startButton(selectedVideoPath.getText(), false, getSpeed(radioList));
					start.setText("Pauza");
				} else if (start.getText().equals("Pauza")) {
					if (!selectedFilePath.getText().equals(""))
						player.pauseButton(selectedVideoPath.getText());
					if (!selectedFilePath.getText().equals(""))
						chart.pauseDraw(selectedFilePath.getText());
					start.setText("Start");
				} else if (start.getText().equals("Reset")) {
					if (!selectedFilePath.getText().equals(""))
						chart.startDraw(selectedFilePath.getText(), true, getSpeed(radioList));
					if (!selectedFilePath.getText().equals(""))
						player.startButton(selectedVideoPath.getText(), true, getSpeed(radioList));
					start.setText("Pauza");
				}
			}
		});
		resetSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.resetChart();
				player.resetPlayer();
				selectedFilePath.setText("");
				selectedVideoPath.setText("");
				speed1.setSelected(false);
				speed2.setSelected(false);
				speed3.setSelected(false);
				speed4.setSelected(true);
				frequency.setText("");
			}
		});
	}

	private void addElementsToPanel() {
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
		speed1.setName("0.1");
		speed2.setName("0.25");
		speed3.setName("0.5");
		speed4.setName("1");
		speed4.setSelected(true);
		line1.add(errorMessage);
		line2.add(selectFile);
		line2.add(selectedFilePath);
		line3.add(selectVideo);
		line3.add(selectedVideoPath);
		line4.add(speedLabel);
		line4.add(speed1);
		line4.add(speed2);
		line4.add(speed3);
		line4.add(speed4);
		line4.add(frequencyLabel);
		line4.add(frequency);
		line4.add(frequencyMeasurement);
		line5.add(start);
		line5.add(resetSetting);
		newPanel.add(line1);
		newPanel.add(line2);
		newPanel.add(line3);
		newPanel.add(line4);
		newPanel.add(line5);
	}

	private void panelSetting() {
		setSize(600, 700);
		setVisible(true);
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ustawienia"));
		add(newPanel);
		pack();
		setLocationRelativeTo(null);
	}

	public static void setStartButton() {
		start.setText("Start");
	}

	public double getSpeed(List<JRadioButton> radio) {
		radio = radio.stream().filter(s -> s.isSelected()).collect(Collectors.toList());
		return new Double(radio.get(0).getName());
	}
}