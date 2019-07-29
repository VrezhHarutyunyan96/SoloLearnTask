package com.sololearn.android.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.sololearn.android.AppApplication;
import com.sololearn.android.R;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.helper.SharedHelper;
import com.sololearn.android.home.view.activity.MainActivity;

public class SoloLearnNotification {
    private NotificationManagerCompat mNotificationManagerCompat;
    public static final int NOTIFICATION_ID = 888;
    private Context context;

    public SoloLearnNotification(Context context) {
        this.context = context;
    }

    public void show(String title, String body) {
        create();
        boolean areNotificationsEnabled = mNotificationManagerCompat.areNotificationsEnabled();
        if (!areNotificationsEnabled) {
            // Because the user took an action to create a notification, we create a prompt to let
            // the user re-enable notifications for this application again.
            Toast.makeText(context, "You need to enable notifications for this app", Toast.LENGTH_SHORT).show();
            return;
        }
        new LoadBitmap().execute();
    }

    class LoadBitmap extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            String imageUrl = SharedHelper.getKey(context, AppConstants.NEW_ITEM_IMAGE);
            RequestBuilder<Bitmap> requestBuilder = Glide.with(context)
                    .asBitmap()
                    .load(imageUrl);
            try {
                return requestBuilder.submit().get();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            generateBigPictureStyleNotification(bitmap);
        }
    }

    private void generateBigPictureStyleNotification(Bitmap NotificationBitmap) {
        // Main steps for building a BIG_PICTURE_STYLE notification:
        //      0. Get your data
        //      1. Create/Retrieve Notification Channel for O and beyond devices (26+)
        //      2. Build the BIG_PICTURE_STYLE
        //      3. Set up main Intent for notification
        //      4. Set up RemoteInput, so users can input (keyboard and voice) from notification
        //      5. Build and issue the notification

        // 0. Get your data (everything unique per Notification).
        MockDatabase.BigPictureStyleSocialAppData bigPictureStyleSocialAppData =
                MockDatabase.getBigPictureStyleData();

        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        String notificationChannelId =
                NotificationUtil.createNotificationChannel(context, bigPictureStyleSocialAppData);
        NotificationCompat.BigPictureStyle bigPictureStyle;
        if (NotificationBitmap != null) {
            // 2. Build the BIG_PICTURE_STYLE.
            bigPictureStyle = new NotificationCompat.BigPictureStyle()
                    // Provides the bitmap for the BigPicture notification.
                    .bigPicture(
                            NotificationBitmap)
                    // Overrides ContentTitle in the big form of the template.
                    .setBigContentTitle(bigPictureStyleSocialAppData.getBigContentTitle())
                    // Summary line after the detail section in the big form of the template.
                    .setSummaryText(bigPictureStyleSocialAppData.getSummaryText());
        } else {
            bigPictureStyle = new NotificationCompat.BigPictureStyle()
                    // Provides the bitmap for the BigPicture notification.
                    .bigPicture(
                            BitmapFactory.decodeResource(
                                    context.getResources(),
                                    bigPictureStyleSocialAppData.getBigImage()))// TODO: set solo lear image
                    // Overrides ContentTitle in the big form of the template.
                    .setBigContentTitle(bigPictureStyleSocialAppData.getBigContentTitle())
                    // Summary line after the detail section in the big form of the template.
                    .setSummaryText(bigPictureStyleSocialAppData.getSummaryText());
        }

        // 3. Set up main Intent for notification.
        Intent mainIntent = new Intent(context, MainActivity.class);

        // When creating your Intent, you need to take into account the back state, i.e., what
        // happens after your Activity launches and the user presses the back button.

        // There are two options:
        //      1. Regular activity - You're starting an Activity that's part of the application's
        //      normal workflow.

        //      2. Special activity - The user only sees this Activity if it's started from a
        //      notification. In a sense, the Activity extends the notification by providing
        //      information that would be hard to display in the notification itself.

        // Even though this sample's MainActivity doesn't link to the Activity this Notification
        // launches directly, i.e., it isn't part of the normal workflow, a social app generally
        // always links to individual posts as part of the app flow, so we will follow option 1.

        // For an example of option 2, check out the BIG_TEXT_STYLE example.

        // For more information, check out our dev article:
        // https://developer.android.com/training/notify-user/navigation.html

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack.
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent to the top of the stack.
        stackBuilder.addNextIntent(mainIntent);
        // Gets a PendingIntent containing the entire back stack.
        PendingIntent mainPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // 4. Set up RemoteInput, so users can input (keyboard and voice) from notification.

        // Note: For API <24 (M and below) we need to use an Activity, so the lock-screen presents
        // the auth challenge. For API 24+ (N and above), we use a Service (could be a
        // BroadcastReceiver), so the user can input from Notification or lock-screen (they have
        // choice to allow) without leaving the notification.

        // Create the RemoteInput.
        String replyLabel = "Label";
        RemoteInput remoteInput =
                new RemoteInput.Builder("EXTRA_COMMENT")
                        .setLabel(replyLabel)
                        // List of quick response choices for any wearables paired with the phone
                        .setChoices(bigPictureStyleSocialAppData.getPossiblePostResponses())
                        .build();

        // Pending intent =
        //      API <24 (M and below): activity so the lock-screen presents the auth challenge
        //      API 24+ (N and above): this should be a Service or BroadcastReceiver
        PendingIntent replyActionPendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction("ACTION_COMMENT");
            replyActionPendingIntent = PendingIntent.getService(context, 0, intent, 0);

        } else {
            replyActionPendingIntent = mainPendingIntent;
        }

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(
                        android.R.drawable.stat_notify_chat,
                        replyLabel,
                        replyActionPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // 5. Build and issue the notification.

        // Because we want this to be a new notification (not updating a previous notification), we
        // create a new Builder. Later, we use the same global builder to get back the notification
        // we built here for a comment on the post.

        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(AppApplication.appApplication, notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        notificationCompatBuilder
                // BIG_PICTURE_STYLE sets title and content for API 16 (4.1 and after).
                .setStyle(bigPictureStyle)
                // Title for API <16 (4.0 and below) devices.
                .setContentTitle(bigPictureStyleSocialAppData.getContentTitle())
                // Content for API <24 (7.0 and below) devices.
                .setContentText(SharedHelper.getKey(null,AppConstants.NEW_ITEM_TITLE))
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(),
                        android.R.drawable.ic_dialog_email))
                .setContentIntent(mainPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                // Set primary color (important for Wear 2.0 Notifications).
                .setColor(ContextCompat.getColor(AppApplication.appApplication, R.color.colorPrimary))

                // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
                // devices and all Wear devices. If you have more than one notification and
                // you prefer a different summary notification, set a group key and create a
                // summary notification via
                // .setGroupSummary(true)
                // .setGroup(GROUP_KEY_YOUR_NAME_HERE)

                .setSubText(Integer.toString(1))
                .addAction(replyAction)
                .setCategory(Notification.CATEGORY_SOCIAL)

                // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
                // 'importance' which is set in the NotificationChannel. The integers representing
                // 'priority' are different from 'importance', so make sure you don't mix them.
                .setPriority(bigPictureStyleSocialAppData.getPriority())

                // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
                // visibility is set in the NotificationChannel.
                .setVisibility(bigPictureStyleSocialAppData.getChannelLockscreenVisibility());

        // If the phone is in "Do not disturb mode, the user will still be notified if
        // the sender(s) is starred as a favorite.
        for (String name : bigPictureStyleSocialAppData.getParticipants()) {
            notificationCompatBuilder.addPerson(name);
        }

        Notification notification = notificationCompatBuilder.build();

        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    private void create() {
        mNotificationManagerCompat = NotificationManagerCompat.from(AppApplication.appApplication);

    }


}
