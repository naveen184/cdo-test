package com.test.screenvideorecorder;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.aventstack.extentreports.Status;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.utils.SystemProperties;

public class VideoRecorderSession {

    /**
     * To compare the recording options from the system.properties file
     */
    private enum RecordingOptions {
        ALL("ALL"), // All tests executions will be recorded
        NONE("NONE"), // None Tests executions will be recorded
        ONLY_FAILED("ONLY_FAILED"), // Failed and FailedWithProgress executions
        // will be recorded
        ALL_EXCEPT_PASSED("ALL_EXCEPT_PASSED"); // All tests executions (Except
        // Passed) will be recorded

        private String strategy;

        RecordingOptions(String strategy) {
            this.strategy = strategy;
        }

        public String getStrategy() {
            return this.strategy;
        }

    }

    private static final String SEPPARATOR = File.separator;
    private static final String AVI_EXTENTION = ".avi";

    private static final String DEFAULT_PATH = File.separator +  "TestReport" + File.separator + "Videos";

    private static HashMap<Long, MonteVideoRecorder> videoMap = new HashMap<>();
	private static final String EXCEPTION_MSG = "Exception Caught : ";

    /**
     * RecordingOptions.ALL / RecordingOptions.NONE --> Will not need to pass by this method.
     *
     * @param fileName
     * @param status
     */
    public static void afterStopRecordingDeleteVideoFileIfNeeded(String fileName, Status status) {
        if (isVideoCandidateToDeleteAfterTestExecution(status)) {
            forceDeleteVideoFile(getStorePathLocation(), fileName);
        }
    }

    private static void forceDeleteVideoFile(String path, String fileName) {
        String fullPath = path + SEPPARATOR + fileName + AVI_EXTENTION;
        Reporting.logReporter(Status.DEBUG, "videoRecord - Call to Delete Video if same path/Name File exist");

        try {
            FileUtils.forceDelete(FileUtils.getFile(fullPath));
            Reporting.logReporter(Status.DEBUG, "videoRecord - Success to delete Video - " + fullPath);
        } catch (IOException e) {
            Reporting.logReporter(Status.DEBUG, "videoRecord - Error cannot delete Video - " + fullPath);
            Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
        }

    }

    private static String getStorePathLocation() {
        if ("DEFAULT".equalsIgnoreCase(SystemProperties.VIDEO_FOLDER_PATH)) {
            return System.getProperty("user.dir") + DEFAULT_PATH;
        } else {
            return SystemProperties.VIDEO_FOLDER_PATH;
        }
    }

    /**
     * OBJECTIVE: Get the videoRecorder instance so it can be reused during the script.
     */
    private static MonteVideoRecorder getVideoRecordSession() {
        MonteVideoRecorder toReturn = videoMap.get(Thread.currentThread().getId());
        if (toReturn == null) {
            loadNewVideoRecordSession();
            toReturn = videoMap.get(Thread.currentThread().getId());
        }
        return toReturn;
    }

    /**
     * ALL and NONE will return false, as we do not need to delete with any of both
     *
     * @param status
     * @return
     */
    private static boolean isVideoCandidateToDeleteAfterTestExecution(Status status) {
        if (SystemProperties.VIDEO_RECORDING.equals(RecordingOptions.ALL_EXCEPT_PASSED.getStrategy()) && Status.PASS.equals(status)) {
            return true;
        }

        else if (SystemProperties.VIDEO_RECORDING.equals(RecordingOptions.ONLY_FAILED.getStrategy()) && !Status.FAIL.equals(status)) {
            return true;
        }
        return false;
    }

    /**
     * OBJECTIVE: Load a NEW session of videoRecorder when there is not an existing to work on.
     */
    private static void loadNewVideoRecordSession() {
        MonteVideoRecorder videoRecord = new MonteVideoRecorder();
        Reporting.logReporter(Status.DEBUG, "videoRecord - Session ID saved to MAP");
        videoMap.put(Thread.currentThread().getId(), videoRecord);
    }

    private static void startRecord(String fileName) {
        MonteVideoRecorder videoRecord = getVideoRecordSession();
        String fullPath = getStorePathLocation() + SEPPARATOR + fileName + AVI_EXTENTION;

        try {
            videoRecord.startRecording(getStorePathLocation(), fileName);
            Reporting.logReporter(Status.DEBUG, "videoRecord - has started as expected");
            Reporting.logReporter(Status.DEBUG, "videoRecord - Stored at: " + fullPath + "   Note: Depending on your filters the video might not be available.");

        } catch (RuntimeException | IOException | AWTException e) {
            Reporting.logReporter(Status.DEBUG, "videoRecord - Error - Can not Start to record video.");
            Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
        }
    }

    /**
     * OBJECTIVE: Start the recording: Before to start the recording will check if there is a file with the same name under the expected path, if the file is found it will delete
     * it, to record and save a new one instead
     */
    public static void startRecording(String fileName) {
        if (!SystemProperties.VIDEO_RECORDING.equals(RecordingOptions.NONE.getStrategy())) {

            // Delete video if already exist in same path
            String fullPath = getStorePathLocation() + SEPPARATOR + fileName + AVI_EXTENTION;
            File tempFile = new File(fullPath);
            if (tempFile.exists()) {
                forceDeleteVideoFile(getStorePathLocation(), fileName);
            }

            startRecord(fileName);
        }
    }

    private static void stopRecord() {
        MonteVideoRecorder videoRecord = getVideoRecordSession();
        try {
            videoRecord.stopRecording();
            Reporting.logReporter(Status.DEBUG, "videoRecord - has Stopped as expected");
        } catch (RuntimeException | IOException e) {
            Reporting.logReporter(Status.DEBUG, "videoRecord - Error - Cannot stop video");
            Logging.logReporter(Status.DEBUG, EXCEPTION_MSG + e);
        } finally {
            Reporting.logReporter(Status.DEBUG, "videoRecord - Session ID removed from MAP");

            videoMap.remove(Thread.currentThread().getId());
        }
    }

    /**
     * OBJECTIVE: Stop the recording
     */
    public static void stopRecording() {
        if (!SystemProperties.VIDEO_RECORDING.equals(RecordingOptions.NONE.getStrategy())) {
            stopRecord();
        }
    }

}
