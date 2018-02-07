package com.smartadserver.android.instreamsdk.plugin;

import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.smartadserver.android.instreamsdk.SVSContentPlayerPlugin;

/**
 * Implementation of the {@link SVSContentPlayerPlugin} interface for VideoView player.
 * source : https://github.com/smartadserver/smart-instream-android-plugins/
 */
public class SVSVideoViewPlugin implements SVSContentPlayerPlugin {

    private Handler mainHandler;
    private ViewGroup contentPlayerContainer;
    private VideoView videoView;
    private MediaController controls;
    private boolean isLiveContent;
    private boolean hasCompleted;

    /**
     * Constructor
     * @param videoView the VideoView handled by this plugin
     * @param contentPlayerContainer the ViewGroup containing the VideoView
     */
    public SVSVideoViewPlugin(VideoView videoView, MediaController controls, ViewGroup contentPlayerContainer, boolean isLiveContent) {
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.videoView = videoView;
        this.controls = controls;
        this.contentPlayerContainer = contentPlayerContainer;
        this.isLiveContent = isLiveContent;
        this.hasCompleted = false;
    }

    /**
     * Performs any action necessary when the ad playback has finished, including
     * resuming the content playback
     */
    @Override
    public void adBreakEnded() {
        if (videoView.isPlaying() || hasCompleted) return;

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                videoView.start();
            }
        });
    }

    /**
     * Performs any action necessary when the ad playback is about to start, including
     * pausing the content playback
     */
    @Override
    public void adBreakStarted() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // ensure that controls are hidden to to appear over the ad player.
                controls.hide();
                videoView.pause();
            }
        });
    }


    /**
     * Returns whether the content media is currently being played
     */
    @Override
    public boolean isPlaying() {
        return videoView.isPlaying();
    }

    /**
     * Returns the content media duration
     */
    @Override
    public long getContentDuration() {
        return isLiveContent ? -1 : videoView.getDuration();
    }


    /**
     * Returns the current position in the content media
     */
    @Override
    public long getCurrentPosition() {
        long currentPosition = videoView.getCurrentPosition();
        long contentDuration = videoView.getDuration();
        // since the underlying MediaPlayer does not report a position that matches content duration
        // when the playback has completed, assume that a position within 150ms of duration is the end.
        if (contentDuration > 0 && currentPosition >= getContentDuration() - 150) {
            currentPosition = getContentDuration();
            hasCompleted = true;
        } else {
            hasCompleted = false;
        }
        return currentPosition;
    }


    /**
     * Returns the {@link ViewGroup} component that contains the content player
     */
    @Override
    public ViewGroup getContentPlayerContainer() {
        return contentPlayerContainer;
    }
}
