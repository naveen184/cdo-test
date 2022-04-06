package com.test.screenvideorecorder;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

public class MonteVideoRecorder {

    private ScreenRecorder screenRecorder;

    /**
     *
     * @param folderPath e.g. "C:\\Videos"
     * @param fileName e.g. "MyVideo"
     * @throws AWTException
     * @throws IOException
     */
    public void startRecording(String folderPath, String fileName) throws IOException, AWTException {
	File file = new File(folderPath);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = screenSize.width;
	int height = screenSize.height;

	Rectangle captureSize = new Rectangle(0, 0, width, height);

	GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
		.getDefaultConfiguration();

	this.screenRecorder = new MonteSpecializedScreenRecorder(gc, captureSize,
		new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
		new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
			CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
			Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
		new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
		null, file, fileName);
	this.screenRecorder.start();

    }

    public void stopRecording() throws IOException {
	this.screenRecorder.stop();
    }

}