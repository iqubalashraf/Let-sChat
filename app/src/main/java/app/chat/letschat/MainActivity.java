package app.chat.letschat;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import app.chat.letschat.adapters.AdapterMainActivity;
import app.chat.letschat.dataModel.AppVersion;
import app.chat.letschat.dataModel.Message;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final String SERVER_IP_ADDRESS = "https://vast-dusk-15119.herokuapp.com/";
    //        private final String SERVER_IP_ADDRESS = "http://192.168.0.6:3000/";
    private static final String USER_NAME = "app.chat.letschat.USER_NAME";
    private static final String USER_GENDER = "app.chat.letschat.USER_GENDER";
    private static final int PICK_IMAGE_REQUEST = 121;
    private static final int CAMERA_REQUEST = 122;
    private static final int NOTIFICATION_ID = 786687;
    private static final int NOTIFICATION_UPDATE_ID = 78687;
    private static final int MALE = 0, FEMALE = 1;
    static Socket socket;
    static String mCurrentPhotoPath;
    private static String user_name = "";
    private static int gender = 2;
    private static int age = 27;
    private static int k = 0;
    private static boolean isPartnerConnected = false;
    private static boolean mIsRunning = false;
    private static boolean isGallryVisiable = true;
    private final String USER_AGE = "app.chat.letschat.USER_AGE";
    public Activity activity;
    private Context context;
    List<Message> messages = new ArrayList<>();
    ImageButton button_send, button_send_image, button_send_camera_image;
    EditText message_box;
    boolean isOpened = false;
    private AdapterMainActivity adapterMainActivity;
    private RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.chat_background_2));
        activity = this;
        context = getApplicationContext();
        Intent intent = getIntent();
        user_name = intent.getStringExtra(USER_NAME);
        gender = intent.getIntExtra(USER_GENDER, 2);
        age = intent.getIntExtra(USER_AGE, 27);
        initializeViews();
        initializeSocket();
        initializeAdapter();
        initializeOnClickListner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsRunning = false;
    }

    private void initializeViews() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);
        button_send = (ImageButton) findViewById(R.id.button_send);
        message_box = (EditText) findViewById(R.id.message_box);
        button_send_image = (ImageButton) findViewById(R.id.button_send_image);
        button_send_camera_image = (ImageButton) findViewById(R.id.button_send_camera_image);
    }

    private void initializeAdapter() {
        adapterMainActivity = new AdapterMainActivity(activity, context, messages);
        recycler_view.setAdapter(adapterMainActivity);
    }

    private void initializeSocket() {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = IO.socket(SERVER_IP_ADDRESS);

                        socket.on("connect", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                if (Constants.getBuildVersion())
                                    Log.d("Socket", "Connected to server");
                                socket.emit("join", getObject(), new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        if (Constants.getBuildVersion())
                                            Log.d("Socket", "on Join called successfully  ");
                                    }
                                });

                            }
                        });

                        socket.on("newMessage", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {

                                Message message = new Gson().fromJson(args[0].toString(), Message.class);
                                updateAdapter(message);
                                if (Constants.getBuildVersion())
                                    Log.d("New Message", message.getText());
                            }
                        });

                        socket.on("newImageMessage", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {

                                Message message = new Gson().fromJson(args[0].toString(), Message.class);
                                updateAdapter(message);
                                if (Constants.getBuildVersion())
                                    Log.d("New Image Message", message.getText());
                            }
                        });

                        socket.on("onWaiting", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                Message message = new Gson().fromJson(args[0].toString(), Message.class);
                                updateAdapter(message);
                                if (Constants.getBuildVersion())
                                    Log.d("New Message", message.getText());
                            }
                        });

                        socket.on("onConnected", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                Message message = new Gson().fromJson(args[0].toString(), Message.class);
                                updateAdapter(message);
                                if (Constants.getBuildVersion())
                                    sendNotification("Connected", message.getText(), NOTIFICATION_ID);
                                isPartnerConnected = true;
                                if (Constants.getBuildVersion())
                                    Log.d("New Message", message.getText());
                            }
                        });

                        socket.on("partnerLeft", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                Message message = new Gson().fromJson(args[0].toString(), Message.class);
                                updateAdapter(message);
                                isPartnerConnected = false;
//                                socket.emit("join", getObject(), new Ack() {
//                                    @Override
//                                    public void call(Object... args) {
//                                        Log.d("Socket", "on Join called successfully  ");
//                                    }
//                                });
                                if (Constants.getBuildVersion())
                                    Log.d("New Message", message.getText());
                            }
                        });

                        socket.on("appVersion", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                AppVersion appVersion = new Gson().fromJson(args[0].toString(), AppVersion.class);
                                if (appVersion.getVersionCode() > BuildConfig.VERSION_CODE)
                                    sendUpdateNotification("Update found", "New version available", NOTIFICATION_UPDATE_ID);
                            }
                        });

                        socket.on("disconnect", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                displayDisconnectPopup();
                            }
                        });
                        socket.connect();

                    } catch (URISyntaxException e) {
                        if (Constants.getBuildVersion())
                            e.printStackTrace();
                    }
                }
            });

            thread.start();

        } catch (Exception e1) {
            if (Constants.getBuildVersion())
                e1.printStackTrace();
        }
    }

    private void initializeOnClickListner() {
        button_send.setOnClickListener(this);
        button_send_image.setOnClickListener(this);
        button_send_camera_image.setOnClickListener(this);
        message_box.setOnClickListener(this);
        message_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {

                if (mEdit.toString().trim().length() == 0) {

                    if (!isGallryVisiable) {
                        if (Constants.getBuildVersion())
                            Log.d("Change Text:  ", " Show gallry button");
                        showGallryButton();
                    }
                    isGallryVisiable = true;
                } else {
                    if (isGallryVisiable) {
                        if (Constants.getBuildVersion())
                            Log.d("Change Text:  ", " Hide gallry button");
                        showSendButton();
                    }
                    isGallryVisiable = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private JSONObject getObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", user_name);
            jsonObject.put("gender", gender);
            jsonObject.put("age", age);
            jsonObject.put("unique_id",Constants.getDeviceUniqueId(context));
            jsonObject.put("country",Constants.getCountryCode(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getMessageObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", user_name);
            jsonObject.put("text", message_box.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getImageObject(String image) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", user_name);
            jsonObject.put("text", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("partnerLeft", getObject(), new Ack() {
            @Override
            public void call(Object... args) {
                if (Constants.getBuildVersion())
                    Log.d("SocketEmit", "Left");
            }
        });
        closeApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_reconnect:
                socket.emit("partnerLeft", getObject(), new Ack() {
                    @Override
                    public void call(Object... args) {
                        if (Constants.getBuildVersion())
                            Log.d("SocketEmit", "Left");
                        socket.emit("join", getObject(), new Ack() {
                            @Override
                            public void call(Object... args) {
                                if (Constants.getBuildVersion())
                                    Log.d("Socket", "on Join called successfully  ");
                            }
                        });
                    }
                });
                break;

            case R.id.action_exit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateAdapter(final Message message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(message);
                adapterMainActivity.setMessages(messages);
                adapterMainActivity.notifyDataSetChanged();
                scrollToBottom();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isPartnerConnected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cancel Chat");
            builder.setMessage("Sure to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            k++;
            if (k == 1) {
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            k = 0;
                        }
                    }, 3000);
                } catch (Exception e) {
                    if (Constants.getBuildVersion())
                        e.printStackTrace();
                }
            } else {
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send:
                if (isPartnerConnected) {
                    if (validateMessage()) {
                        socket.emit("createMessage", getMessageObject(), new Ack() {
                            @Override
                            public void call(Object... args) {
                            }
                        });
                        refreshMessageBox();
                    }
                } else
                    Toast.makeText(context, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_send_image:
                if (isPartnerConnected) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                } else
                    Toast.makeText(context, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
                break;

            case R.id.button_send_camera_image:
                if (isPartnerConnected) {
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    dispatchTakePictureIntent();
                } else
                    Toast.makeText(context, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
                break;
            case R.id.message_box:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollToBottom();
                    }
                },600);
                break;
            default:
                break;
        }
    }

    private boolean validateMessage() {
        if (!message_box.getText().toString().trim().equals("")) {
            return true;
        }
        return false;
    }


    private void refreshMessageBox() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                message_box.setText("");
            }
        });
    }

    private void displayDisconnectPopup() {
        try {
            if (mIsRunning)
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Internet problem");
                        builder.setMessage("Disconnected from Server")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
        } catch (Exception e) {
            if (Constants.getBuildVersion())
                e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                uploadImage(bitmap);
            } catch (IOException e) {
                if (Constants.getBuildVersion())
                    e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                uploadImage(setPic());
            } catch (Exception e) {
                if (Constants.getBuildVersion())
                    e.printStackTrace();
            }
        }
    }

    private void sendNotification(String title, String info, int count) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, count /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon()).setWhen(0)
                .setContentTitle(title)
                .setContentText(info)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(info))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor(getString(R.string.brand_color)))
                .setLargeIcon(icon);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(count /* ID of notification */, notificationBuilder.build());
    }

    private void sendUpdateNotification(String title, String info, int count) {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        } catch (android.content.ActivityNotFoundException anfe) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, count /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon()).setWhen(0)
                .setContentTitle(title)
                .setContentText(info)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(info))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor(getString(R.string.brand_color)))
                .setLargeIcon(icon);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(count /* ID of notification */, notificationBuilder.build());
    }

    private void uploadImage(final Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap converetdImage = getResizedBitmap(bitmap, 1000);
        Log.d(TAG, "" + converetdImage.getByteCount());
        Log.d(TAG, "" + bitmap.getByteCount());
        converetdImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Toast.makeText(context, "Sending message in background.", Toast.LENGTH_SHORT).show();
        socket.emit("createImageMessage", getImageObject(imageString), new Ack() {
            @Override
            public void call(Object... args) {
            }
        });
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_forum_white_48px : R.drawable.ic_forum_brand_color_48px;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = getSmallerNumber(width, maxSize);
            height = (int) (width / bitmapRatio);
        } else {
            height = getSmallerNumber(height, maxSize);
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void showGallryButton() {
        button_send_image.setVisibility(View.VISIBLE);
        button_send_camera_image.setVisibility(View.VISIBLE);
        button_send.setVisibility(View.GONE);
    }

    private void showSendButton() {
        button_send_image.setVisibility(View.GONE);
        button_send_camera_image.setVisibility(View.GONE);
        button_send.setVisibility(View.VISIBLE);
    }

    private int getSmallerNumber(int number1, int number2) {
        if (number1 > number2) {
            return number2;
        } else {
            return number1;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(context, "Unable to capture image.", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(context, "Unable to capture image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Unable to capture image.", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "temp";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Bitmap setPic() {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);


        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        return bitmap;
    }

    private void closeApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void scrollToBottom() {
        recycler_view.scrollToPosition(messages.size() - 1);
    }
}
