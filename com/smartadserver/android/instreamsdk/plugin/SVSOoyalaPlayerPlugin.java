package com.smartadserver.android.instreamsdk.plugin;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import com.ooyala.android.OoyalaNotification;
import com.ooyala.android.OoyalaPlayer;
import com.ooyala.android.OoyalaPlayerLayout;
import com.smartadserver.android.instreamsdk.SVSContentPlayerPlugin;

import java.util.Observable;
import java.util.Observer;

/**
 * Implementation of the {@link SVSContentPlayerPlugin} interface for Ooyala player.
 * source : https://github.com/smartadserver/smart-instream-android-plugins/
 */
public class SVSOoyalaPlayerPlugin implements SVSContentPlayerPlugin {

    private Handler mainHandler;
    private ViewGroup contentPlayerContainer;
    private OoyalaPlayer ooyalaPlayer;
    private OoyalaPlayerLayout ooyalaPlayerLayout;
    private boolean isLiveContent;
    boolean contentHasCompleted;

    /**
     * Constructor
     * @param ooyalaPlayer the OoyalaPlayer object
     * @param ooyalaPlayerLayout the OoyalaPlayerLayout object
     * @param contentPlayerContainer the ViewGroup containing the OoyalaPlayerLayout object
     */
    public SVSOoyalaPlayerPlugin(final OoyalaPlayer ooyalaPlayer, OoyalaPlayerLayout ooyalaPlayerLayout, ViewGroup contentPlayerContainer, boolean isLiveContent) {
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.ooyalaPlayer = ooyalaPlayer;
        this.ooyalaPlayerLayout = ooyalaPlayerLayout;
        this.contentPlayerContainer = contentPlayerContainer;
        this.isLiveContent = isLiveContent;
        this.contentHasCompleted = false;
        ooyalaPlayer.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (arg instanceof OoyalaNotification) {
                    String eventName = ((OoyalaNotification)arg).getName();

                    if (eventName == null) {
                        return;
                    }

                    switch (eventName) {
                        case OoyalaPlayer.PLAY_COMPLETED_NOTIFICATION_NAME:
                            contentHasCompleted = true;
                            break;
                        case OoyalaPlayer.SEEK_COMPLETED_NOTIFICATION_NAME:
                            contentHasCompleted = false;
                            break;
                    }
                }
            }
        });

    }

    /**
     * Performs any action necessary when the ad playback has finished, including
     * resuming the content playback
     */
    @Override
    public void adBreakEnded() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                ooyalaPlayerLayout.setVisibility(View.VISIBLE); //make sure that ooyalaPlayer is visible
                if (!contentHasCompleted && !isPlaying()) {
                    ooyalaPlayer.play();
                }
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
                if (isPlaying()) {
                    ooyalaPlayer.pause();
                }
                ooyalaPlayerLayout.setVisibility(View.INVISIBLE); //hide ooyalaPlayer to display our adPlayer
            }
        });
    }

    /**
     * Returns whether the content media is currently being played
     */
    @Override
    public boolean isPlaying() {
        return ooyalaPlayer.isPlaying();
    }

    /**
     * Returns the content media duration
     */
    @Override
    public long getContentDuration() {
        return isLiveContent ? -1 : ooyalaPlayer.getDuration();
    }

    /**
     * Returns the current position in the content media
     */
    @Override
    public long getCurrentPosition() {
        return ooyalaPlayer.getPlayheadTime();
    }

    /**
     * Returns the {@link ViewGroup} component that contains the content player
     */
    @Override
    public ViewGroup getContentPlayerContainer() {
        return contentPlayerContainer;
    }
}
