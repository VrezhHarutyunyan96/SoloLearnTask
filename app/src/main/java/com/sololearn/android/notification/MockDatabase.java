package com.sololearn.android.notification;

import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

import com.sololearn.android.R;

import java.util.ArrayList;

/** Mock data for each of the Notification Style Demos. */
public final class MockDatabase {

    public static BigPictureStyleSocialAppData getBigPictureStyleData() {
        return BigPictureStyleSocialAppData.getInstance();
    }

    /** Represents data needed for BigPictureStyle Notification. */
    public static class BigPictureStyleSocialAppData extends MockNotificationData {

        private static BigPictureStyleSocialAppData sInstance = null;

        // Unique data for this Notification.Style:
        private int mBigImage;
        private String mBigContentTitle;
        private String mSummaryText;

        private CharSequence[] mPossiblePostResponses;

        private ArrayList<String> mParticipants;

        public static BigPictureStyleSocialAppData getInstance() {
            if (sInstance == null) {
                sInstance = getSync();
            }
            return sInstance;
        }

        private static synchronized BigPictureStyleSocialAppData getSync() {
            if (sInstance == null) {
                sInstance = new BigPictureStyleSocialAppData();
            }

            return sInstance;
        }

        private BigPictureStyleSocialAppData() {
            // Standard Notification values:
            // Title/Content for API <16 (4.0 and below) devices.
            mContentTitle = "You have new item";
            mContentText = "[Picture] Like my shot of Earth?";
            mPriority = NotificationCompat.PRIORITY_HIGH;

            // Style notification values:
            mBigImage = R.drawable.earth;
            mBigContentTitle = "You have new item";
            mSummaryText = "Like my shot of Earth?";

            // This would be possible responses based on the contents of the post.
            mPossiblePostResponses = new CharSequence[] {"Yes", "No", "Maybe?"};

            mParticipants = new ArrayList<>();
            mParticipants.add("Bob Smith");

            // Notification channel values (for devices targeting 26 and above):
            mChannelId = "channel_social_1";
            // The user-visible name of the channel.
            mChannelName = "Sample Social";
            // The user-visible description of the channel.
            mChannelDescription = "Sample Social Notifications";
            mChannelImportance = NotificationManager.IMPORTANCE_HIGH;
            mChannelEnableVibrate = true;
            mChannelLockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE;
        }

        public int getBigImage() {
            return mBigImage;
        }

        public String getBigContentTitle() {
            return mBigContentTitle;
        }

        public String getSummaryText() {
            return mSummaryText;
        }

        public CharSequence[] getPossiblePostResponses() {
            return mPossiblePostResponses;
        }

        public ArrayList<String> getParticipants() {
            return mParticipants;
        }

        @Override
        public String toString() {
            return getContentTitle() + " - " + getContentText();
        }
    }


    /** Represents standard data needed for a Notification. */
    public abstract static class MockNotificationData {

        // Standard notification values:
        protected String mContentTitle;
        protected String mContentText;
        protected int mPriority;

        // Notification channel values (O and above):
        protected String mChannelId;
        protected CharSequence mChannelName;
        protected String mChannelDescription;
        protected int mChannelImportance;
        protected boolean mChannelEnableVibrate;
        protected int mChannelLockscreenVisibility;

        // Notification Standard notification get methods:
        public String getContentTitle() {
            return mContentTitle;
        }

        public String getContentText() {
            return mContentText;
        }

        public int getPriority() {
            return mPriority;
        }

        // Channel values (O and above) get methods:
        public String getChannelId() {
            return mChannelId;
        }

        public CharSequence getChannelName() {
            return mChannelName;
        }

        public String getChannelDescription() {
            return mChannelDescription;
        }

        public int getChannelImportance() {
            return mChannelImportance;
        }

        public boolean isChannelEnableVibrate() {
            return mChannelEnableVibrate;
        }

        public int getChannelLockscreenVisibility() {
            return mChannelLockscreenVisibility;
        }
    }
}
