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
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import app.chat.letschat.adapters.AdapterMainActivity;
import app.chat.letschat.dataModel.Message;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "MainActivity";
    private final String SERVER_IP_ADDRESS = "https://vast-dusk-15119.herokuapp.com/";
    //    private final String SERVER_IP_ADDRESS = "http://192.168.0.4:3000/";
    private final String USER_NAME = "app.chat.letschat.USER_NAME";
    private final String USER_GENDER = "app.chat.letschat.USER_GENDER";
    private final int PICK_IMAGE_REQUEST = 121;
    private final int NOTIFICATION_ID = 786687;
    private final int MALE = 0, FEMALE = 1;
    private String user_name = "";
    private int gender = 2;
    Socket socket;
    private AdapterMainActivity adapterMainActivity;
    private RecyclerView recycler_view;
    private LinearLayoutManager llmProgressRC;
    List<Message> messages = new ArrayList<>();
    public Activity activity;
    ImageButton button_send, button_send_image;
    EditText message_box;
    private boolean isPartnerConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        Intent intent = getIntent();
        user_name = intent.getStringExtra(USER_NAME);
        gender = intent.getIntExtra(USER_GENDER, 2);
        initializeViews();
        initializeSocket();
        initializeAdapter();
        initializeOnClickListner();
    }

    private void initializeViews() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
//        llmProgressRC = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);
        button_send = (ImageButton) findViewById(R.id.button_send);
        message_box = (EditText) findViewById(R.id.message_box);
        button_send_image = (ImageButton) findViewById(R.id.button_send_image);
    }

    private void initializeAdapter() {
        adapterMainActivity = new AdapterMainActivity(getApplicationContext(), messages);
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
//                                sendNotification("Connected",message.getText(),NOTIFICATION_ID);
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
                                socket.emit("join", getObject(), new Ack() {
                                    @Override
                                    public void call(Object... args) {
                                        Log.d("Socket", "on Join called successfully  ");
                                    }
                                });
                                if (Constants.getBuildVersion())
                                    Log.d("New Message", message.getText());
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
    }

    private JSONObject getObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", user_name);
            jsonObject.put("gender", gender);
            jsonObject.put("country", "India");
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
                Log.d("SocketEmit", "Left");
            }
        });
    }

    private void updateAdapter(final Message message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(message);
                adapterMainActivity.setMessages(messages);
                adapterMainActivity.notifyDataSetChanged();
                recycler_view.scrollToPosition(messages.size() - 1);
            }
        });

    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
//        finish();
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
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send:
                if (validateMessage()) {
                    refreshMessageBox();
                    socket.emit("createMessage", getMessageObject(), new Ack() {
                        @Override
                        public void call(Object... args) {
                        }
                    });
                }
                break;
            case R.id.button_send_image:
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
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
        }
    }

    private void sendNotification(String title, String info, int count) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        Bitmap converetdImage = getResizedBitmap(bitmap, 800);
        Log.d(TAG, ""+converetdImage.getByteCount());
        Log.d(TAG, ""+bitmap.getByteCount());
        converetdImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Toast.makeText(getApplicationContext(),"Uploading message in background.",Toast.LENGTH_SHORT).show();
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

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
