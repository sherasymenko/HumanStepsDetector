package app.media;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

@SuppressWarnings("deprecation")
public class SceneGenerator {
	private final StackPane layout = new StackPane();
	private MediaPlayer player;
	private MediaView mediaView;

	public SceneGenerator() {
		super();
	}

	public MediaView getMediaView() {
		return mediaView;
	}

	public Scene createScene(String videoPath) throws MediaException {
		final File dir = new File(videoPath);
		if (!dir.exists() || dir.isDirectory()) {
			System.out.println("Cannot find video file: " + dir);
		}
		player = createPlayer(("file:///" + videoPath).replace("\\", "/").replaceAll(" ", "%20"));
		mediaView = new MediaView(player);
		mediaView.mediaPlayerProperty().addListener(new ChangeListener<MediaPlayer>() {
			@Override
			public void changed(ObservableValue<? extends MediaPlayer> observableValue, MediaPlayer oldPlayer,
					MediaPlayer newPlayer) {
				setCurrentlyPlaying(newPlayer);
			}
		});
		mediaView.setMediaPlayer(player);
		setCurrentlyPlaying(mediaView.getMediaPlayer());
		layout.setStyle("-fx-background-color: cornsilk; -fx-font-size: 20; -fx-padding: 20; -fx-alignment: center;");
		layout.getChildren().addAll(VBoxBuilder.create().spacing(10).alignment(Pos.CENTER)
				.children(mediaView, HBoxBuilder.create().spacing(10).alignment(Pos.CENTER).build()).build());
		return new Scene(layout, 800, 600);
	}

	private void setCurrentlyPlaying(final MediaPlayer newPlayer) {
		String source = newPlayer.getMedia().getSource();
		source = source.substring(0, source.length() - ".mp4".length());
		source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
	}

	public MediaPlayer createPlayer(String aMediaSrc) throws MediaException {
		Media media = new Media(aMediaSrc);
		final MediaPlayer player = new MediaPlayer(media);
		player.setOnError(new Runnable() {
			@Override
			public void run() {
				System.out.println("Media error occurred: " + player.getError());
			}
		});
		return player;
	}
}