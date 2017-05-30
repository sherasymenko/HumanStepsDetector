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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.jfree.ui.RefineryUtilities;

import app.graph.OpenSheet;
import app.media.PlayVideo;
import javafx.scene.media.MediaException;

@SuppressWarnings("serial")
public class SettingPanel extends JFrame {
	private JLabel errorMessageLine = new JLabel(" ");
	private JLabel errorMessage = new JLabel("");
	private JButton showInfo = new JButton("Instrukcja");
	private JButton selectAccFile = new JButton("Plik z danymi przyspieszenia");
	private JTextField selectedAccFilePath = new JTextField("", 50);
	private JButton selectEulerFile = new JButton("Plik z danymi k¹tów Eulera");
	private JTextField selectedEulerFilePath = new JTextField("", 50);
	private JButton selectVideo = new JButton("Nagranie");
	private JTextField selectedVideoPath = new JTextField("", 50);
	private JLabel speedLabel = new JLabel("Szybkoœæ");
	private JRadioButton speed1 = new JRadioButton("0.1");
	private JRadioButton speed2 = new JRadioButton("0.25");
	private JRadioButton speed3 = new JRadioButton("0.5");
	private JRadioButton speed4 = new JRadioButton("1");
	private JLabel frequencyLabel = new JLabel("Czêstotliwoœæ");
	private JTextField frequency = new JTextField("100", 10);
	private JLabel frequencyMeasurement = new JLabel("Hz");
	private static JButton start = new JButton("Start");
	private JButton resetSetting = new JButton("Wyczyœæ ustawienia");
	private JPanel newPanel = new JPanel();
	private JPanel line1 = new JPanel();
	private JPanel line2 = new JPanel();
	private JPanel line3 = new JPanel();
	private JPanel line4 = new JPanel();
	private JPanel line5 = new JPanel();
	private JPanel line6 = new JPanel();
	private GridBagConstraints constraints = new GridBagConstraints();
	private OpenSheet accChart;
	private OpenSheet eulerChart;
	private PlayVideo player;

	public SettingPanel() {
		super("Ustawienia");
		elementsSetting();
		addElementsToPanel();
		panelSetting();
	}

	private void elementsSetting() {
		errorMessage.setVisible(false);
		errorMessage.setForeground(Color.red);

		showInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(AppText.INFO_TEXT.get());
				JOptionPane.showMessageDialog(null, AppText.INFO_TEXT.get(), "", 1);
				System.out.println(AppText.INFO_TEXT.get());
			}
		});

		selectAccFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
				fileChooser.setFileFilter(getFileFilter(".txt"));
				int result = fileChooser.showOpenDialog(newPanel);
				if (result == JFileChooser.APPROVE_OPTION) {
					if (!selectedAccFilePath.getText().equals(""))
						accChart.resetChart(AppText.ACC_CHART.get());
					File selectedFile = fileChooser.getSelectedFile();
					selectedAccFilePath.setText(selectedFile.getAbsolutePath());
					try {
						accChart = new OpenSheet(selectedFile.getAbsolutePath(), AppText.ACC_CHART.get(),
								new Double(frequency.getText()));
						RefineryUtilities.centerFrameOnScreen(accChart);
						accChart.setVisible(true);
						accChart.pack();
						prepareToStart();
						errorMessage.setVisible(false);
					} catch (Exception ex) {
						errorMessage.setText("Plik jest niepoprawny");
						errorMessage.setVisible(true);
						selectedAccFilePath.setText("");
					}
				}
			}
		});
		selectedAccFilePath.setEditable(false);

		selectEulerFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
				fileChooser.setFileFilter(getFileFilter(".txt"));
				int result = fileChooser.showOpenDialog(newPanel);
				if (result == JFileChooser.APPROVE_OPTION) {
					if (!selectedEulerFilePath.getText().equals(""))
						eulerChart.resetChart(AppText.EULER_CHART.get());
					File selectedFile = fileChooser.getSelectedFile();
					selectedEulerFilePath.setText(selectedFile.getAbsolutePath());
					try {
						eulerChart = new OpenSheet(selectedFile.getAbsolutePath(), AppText.EULER_CHART.get(),
								new Double(frequency.getText()));
						RefineryUtilities.centerFrameOnScreen(eulerChart);
						eulerChart.setVisible(true);
						eulerChart.pack();
						prepareToStart();
						errorMessage.setVisible(false);
					} catch (Exception ex) {
						errorMessage.setText("Plik jest niepoprawny");
						errorMessage.setVisible(true);
						selectedEulerFilePath.setText("");
					}
				}
			}
		});
		selectedEulerFilePath.setEditable(false);

		selectVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
				fileChooser.setFileFilter(getFileFilter(".mp4"));
				int result = fileChooser.showOpenDialog(newPanel);
				if (result == JFileChooser.APPROVE_OPTION) {
					if (!selectedVideoPath.getText().equals(""))
						player.resetPlayer();
					player = new PlayVideo();
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							player.initAndShowGUI();
						}
					});
					File selectedVideo = fileChooser.getSelectedFile();
					selectedVideoPath.setText(selectedVideo.getAbsolutePath());
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							try {
								player.selectVideo(selectedVideoPath.getText());
							} catch (MediaException ex) {
								System.out.println("test12311!!!!!1");
							}
						}
					});

					frequency.setEditable(false);
					prepareToStart();
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

		frequency.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				check();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				check();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				check();
			}

			public void check() {
				try {
					Double frequencyValue = new Double(frequency.getText());
					errorMessage.setText("");
					errorMessage.setVisible(false);
					selectAccFile.setEnabled(true);
					selectEulerFile.setEnabled(true);
					selectedVideoPath.setEnabled(true);
				} catch (NumberFormatException e) {
					errorMessage.setText("Wpisz poprawn¹ liczbê, naprzyk³ad 100.00.");
					errorMessage.setVisible(true);
					selectAccFile.setEnabled(false);
					selectEulerFile.setEnabled(false);
					selectedVideoPath.setEnabled(false);
				}

			}
		});
		start.setEnabled(false);
		start.addActionListener(new ActionListener() {
			List<JRadioButton> radioList = Arrays.asList(speed1, speed2, speed3, speed4);

			public void actionPerformed(ActionEvent e) {
				if (start.getText().equals("Start")) {
					if (!selectedAccFilePath.getText().equals("")) {
						accChart.setEulerFilePath(selectedEulerFilePath.getText());
						try {
							accChart.startDraw(selectedAccFilePath.getText(), false, getSpeed(radioList));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if (!selectedEulerFilePath.getText().equals("")) {
						try {
							eulerChart.startDraw(selectedEulerFilePath.getText(), false, getSpeed(radioList));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if (!selectedVideoPath.getText().equals("")) {
						player.startButton(selectedVideoPath.getText(), false, getSpeed(radioList));
					}
					start.setText("Pauza");
				} else if (start.getText().equals("Pauza")) {
					if (!selectedVideoPath.getText().equals("")) {
						player.pauseButton(selectedVideoPath.getText());
					}
					if (!selectedAccFilePath.getText().equals("")) {
						accChart.pauseDraw(selectedAccFilePath.getText());
					}
					if (!selectedEulerFilePath.getText().equals("")) {
						eulerChart.pauseDraw(selectedEulerFilePath.getText());
					}
					start.setText("Start");
				} else if (start.getText().equals("Restart")) {
					if (!selectedAccFilePath.getText().equals("")) {
						try {
							accChart.startDraw(selectedAccFilePath.getText(), true, getSpeed(radioList));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if (!selectedEulerFilePath.getText().equals("")) {
						try {
							eulerChart.startDraw(selectedEulerFilePath.getText(), true, getSpeed(radioList));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if (!selectedVideoPath.getText().equals("")) {
						player.startButton(selectedVideoPath.getText(), true, getSpeed(radioList));
					}
					start.setText("Pauza");
				}

				selectAccFile.setEnabled(false);
				selectEulerFile.setEnabled(false);
				selectVideo.setEnabled(false);
			}
		});
		resetSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!selectedAccFilePath.getText().equals("")) {
					accChart.resetChart(AppText.ACC_CHART.get());
				}
				if (!selectedEulerFilePath.getText().equals("")) {
					eulerChart.resetChart(AppText.EULER_CHART.get());
				}
				if (!selectedVideoPath.getText().equals("")) {
					player.resetPlayer();
				}
				selectedAccFilePath.setText("");
				selectedEulerFilePath.setText("");
				selectedVideoPath.setText("");
				selectAccFile.setEnabled(true);
				selectEulerFile.setEnabled(true);
				selectVideo.setEnabled(true);
				speed1.setSelected(false);
				speed2.setSelected(false);
				speed3.setSelected(false);
				speed4.setSelected(true);
				speed1.setEnabled(true);
				speed2.setEnabled(true);
				speed3.setEnabled(true);
				speed4.setEnabled(true);
				frequency.setText("100");
				frequency.setEditable(true);
				start.setText("Start");
				start.setEnabled(false);
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
		line1.add(errorMessageLine);
		line1.add(errorMessage);
		line2.add(selectAccFile);
		line2.add(selectedAccFilePath);
		line3.add(selectEulerFile);
		line3.add(selectedEulerFilePath);
		line4.add(selectVideo);
		line4.add(selectedVideoPath);
		line5.add(speedLabel);
		line5.add(speed1);
		line5.add(speed2);
		line5.add(speed3);
		line5.add(speed4);
		line5.add(frequencyLabel);
		line5.add(frequency);
		line5.add(frequencyMeasurement);
		line6.add(start);
		line6.add(resetSetting);
		line6.add(showInfo);
		newPanel.add(line1);
		newPanel.add(line5);
		newPanel.add(line2);
		newPanel.add(line3);
		newPanel.add(line4);
		newPanel.add(line6);
	}

	private void panelSetting() {
		setSize(600, 1000);
		setVisible(true);
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ""));
		add(newPanel);
		pack();
		setLocationRelativeTo(null);
	}

	private FileFilter getFileFilter(String extension) {
		return new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else if (f.getName().endsWith(extension) || f.getName().endsWith(extension.toUpperCase())) {
					return true;
				} else {
					return false;
				}
			}

			public String getDescription() {
				return extension + " files";
			}
		};
	}

	private void prepareToStart() {
		speed1.setEnabled(false);
		speed2.setEnabled(false);
		speed3.setEnabled(false);
		speed4.setEnabled(false);
		if (!selectedAccFilePath.getText().equals("") && !selectedEulerFilePath.getText().equals("")) {
			start.setEnabled(true);
		}
	}

	public static void setRestartButton() {
		start.setText("Restart");
	}

	public double getSpeed(List<JRadioButton> radio) {
		radio = radio.stream().filter(s -> s.isSelected()).collect(Collectors.toList());
		return new Double(radio.get(0).getName());
	}
}