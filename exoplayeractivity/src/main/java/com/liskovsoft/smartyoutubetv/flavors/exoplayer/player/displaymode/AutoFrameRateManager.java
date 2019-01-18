package com.liskovsoft.smartyoutubetv.flavors.exoplayer.player.displaymode;

import android.app.Activity;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.liskovsoft.smartyoutubetv.common.helpers.MessageHelpers;
import com.liskovsoft.smartyoutubetv.flavors.exoplayer.player.ExoPreferences;

public class AutoFrameRateManager {
    private final Activity mContext;
    private final SimpleExoPlayer mPlayer;
    private final DisplaySyncHelper mSyncHelper;
    private final ExoPreferences mPrefs;

    public AutoFrameRateManager(Activity context, SimpleExoPlayer player) {
        mContext = context;
        mPlayer = player;
        mSyncHelper = new DisplaySyncHelper(mContext);
        mPrefs = ExoPreferences.instance(mContext);
    }

    public void apply() {
        if (!getEnabled()) {
            return;
        }

        if (mPlayer == null || mPlayer.getVideoFormat() == null) {
            return;
        }

        Format videoFormat = mPlayer.getVideoFormat();
        float frameRate = videoFormat.frameRate;
        int width = videoFormat.width;
        boolean result = mSyncHelper.syncDisplayMode(mContext.getWindow(), width, frameRate);

        if (!result) {
            MessageHelpers.showMessage(mContext, "Display mode switch unsuccessful. See log for details.");
        }
    }

    public boolean getEnabled() {
        return mPrefs.getAutoframerateChecked();
    }

    public void setEnabled(boolean enabled) {
        mPrefs.setAutoframerateChecked(enabled);
        apply();
    }

    public int getCurrentModeId() {
        return mSyncHelper.getCurrentModeId();
    }

    public void saveOriginalState() {
        if (!getEnabled()) {
            return;
        }

        mSyncHelper.saveOriginalState();
    }

    public void restoreOriginalState() {
        if (!getEnabled()) {
            return;
        }

        mSyncHelper.restoreOriginalState();
    }
}
