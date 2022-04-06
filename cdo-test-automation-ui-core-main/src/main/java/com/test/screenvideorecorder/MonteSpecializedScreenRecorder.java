package com.test.screenvideorecorder;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.screenrecorder.ScreenRecorder;

public class MonteSpecializedScreenRecorder extends ScreenRecorder {

    private String name;

    public MonteSpecializedScreenRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
	    Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
	    throws IOException, AWTException {
	super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
	this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
	if (!this.movieFolder.exists()) {
	    this.movieFolder.mkdirs();
	} else if (!this.movieFolder.isDirectory()) {
	    throw new IOException("\"" + this.movieFolder + "\" is not a directory.");
	}

	return new File(this.movieFolder, this.name + "." + Registry.getInstance().getExtension(fileFormat));
    }
}