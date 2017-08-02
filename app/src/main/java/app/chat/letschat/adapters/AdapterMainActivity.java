package app.chat.letschat.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.chat.letschat.GenralUtils;
import app.chat.letschat.ImageClass;
import app.chat.letschat.R;
import app.chat.letschat.dataModel.Message;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by ashrafiqubal on 14/07/17.
 */

public class AdapterMainActivity extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String IMAGE_STRING = "app.chat.letschat.IMAGE_STRING";
    private final int VIEW_TYPE_INCOMING_MESSAGE = 2;
    private final int VIEW_TYPE_OUTGOING_MESSAGE = 1;
    private final int VIEW_TYPE_OTHER_MESSAGE = 3;
    private final int VIEW_TYPE_INCOMING_IMAGE_MESSAGE = 5;
    private final int VIEW_TYPE_OUTGOING_IMAGE_MESSAGE = 4;
    Context context;
    Activity activity;
    List<Message> messages;

    public AdapterMainActivity(Activity activity, Context context, List<Message> messages) {
        super();
        this.context = context;
        this.activity = activity;
        this.messages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (messages.get(viewType).getViewType()) {
            case VIEW_TYPE_INCOMING_MESSAGE:
                return new ViewHolderIncomingMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_incoming_message, parent, false));
            case VIEW_TYPE_OUTGOING_MESSAGE:
                return new ViewHolderOutgoingMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_outgoing_message, parent, false));
            case VIEW_TYPE_INCOMING_IMAGE_MESSAGE:
                return new ViewHolderIncomingImageMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_incoming_image_message, parent, false));
            case VIEW_TYPE_OUTGOING_IMAGE_MESSAGE:
                return new ViewHolderOutgoingImageMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_outgoing_image_message, parent, false));
            case VIEW_TYPE_OTHER_MESSAGE:
                return new ViewHolderOtherMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_other_message, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderIncomingMessage) {
            ((ViewHolderIncomingMessage) holder).message.setText(messages.get(position).getText());
            ((ViewHolderIncomingMessage) holder).time.setText(GenralUtils.getFormattedTime(messages.get(position).getCreatedAt()));
        } else if (holder instanceof ViewHolderOutgoingMessage) {
            ((ViewHolderOutgoingMessage) holder).message.setText(messages.get(position).getText());
            ((ViewHolderOutgoingMessage) holder).time.setText(GenralUtils.getFormattedTime(messages.get(position).getCreatedAt()));
        } else if (holder instanceof ViewHolderOtherMessage) {
            ((ViewHolderOtherMessage) holder).message.setText(messages.get(position).getText());
        } else if (holder instanceof ViewHolderIncomingImageMessage) {
            ((ViewHolderIncomingImageMessage) holder).message.setImageBitmap(parseStringToBitmap(messages.get(position).getText()));
            ((ViewHolderIncomingImageMessage) holder).message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ImageClass.class);
                    intent.putExtra(IMAGE_STRING, messages.get(position).getText());
                    activity.startActivity(intent);
                }
            });
            ((ViewHolderIncomingImageMessage) holder).time.setText(GenralUtils.getFormattedTime(messages.get(position).getCreatedAt()));
        } else if (holder instanceof ViewHolderOutgoingImageMessage) {
            ((ViewHolderOutgoingImageMessage) holder).message.setImageBitmap(parseStringToBitmap(messages.get(position).getText()));
            ((ViewHolderOutgoingImageMessage) holder).message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ImageClass.class);
                    intent.putExtra(IMAGE_STRING, messages.get(position).getText());
                    activity.startActivity(intent);
                }
            });
            ((ViewHolderOutgoingImageMessage) holder).time.setText(GenralUtils.getFormattedTime(messages.get(position).getCreatedAt()));
        }
//        switch (messages.get(position).getViewType()){
//            case VIEW_TYPE_INCOMING_MESSAGE:
//                ((ViewHolderIncomingMessage) holder).message.setText(messages.get(position).getText());
//                break;
//            case VIEW_TYPE_OUTGOING_MESSAGE:
//                ((ViewHolderOutgoingMessage) holder).message.setText(messages.get(position).getText());
//                break;
//            case VIEW_TYPE_OTHER_MESSAGE:
//                ((ViewHolderOtherMessage) holder).message.setText(messages.get(position).getText());
//                break;
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    private Bitmap parseStringToBitmap(String imageString) {
        Bitmap bitmap = null;
        final String encodedString = "data:image/jpg;base64," + imageString;
        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",") + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bitmap;
    }

    private class ViewHolderIncomingMessage extends RecyclerView.ViewHolder {
        View view;
        TextView time;
        EmojiconTextView message;

        ViewHolderIncomingMessage(View view) {
            super(view);
            this.view = view;
            time = (TextView) view.findViewById(R.id.time);
            message = (EmojiconTextView) view.findViewById(R.id.message);
            message.setEmojiconSize((int) GenralUtils.pxFromDp(context,28));
        }

    }

    private class ViewHolderOutgoingMessage extends RecyclerView.ViewHolder {
        View view;
        EmojiconTextView message;
        TextView time;

        ViewHolderOutgoingMessage(View view) {
            super(view);
            this.view = view;
            message = (EmojiconTextView) view.findViewById(R.id.message);
            message.setEmojiconSize((int) GenralUtils.pxFromDp(context,28));
            time = (TextView) view.findViewById(R.id.time);
        }

    }

    private class ViewHolderIncomingImageMessage extends RecyclerView.ViewHolder {
        View view;
        TextView time;
        ImageView message;

        ViewHolderIncomingImageMessage(View view) {
            super(view);
            this.view = view;
            time = (TextView) view.findViewById(R.id.time);
            message = (ImageView) view.findViewById(R.id.message);
        }

    }

    private class ViewHolderOutgoingImageMessage extends RecyclerView.ViewHolder {
        View view;
        ImageView message;
        TextView time;

        ViewHolderOutgoingImageMessage(View view) {
            super(view);
            this.view = view;
            message = (ImageView) view.findViewById(R.id.message);
            time = (TextView) view.findViewById(R.id.time);
        }

    }

    private class ViewHolderOtherMessage extends RecyclerView.ViewHolder {
        View view;
        TextView message;

        ViewHolderOtherMessage(View view) {
            super(view);
            this.view = view;
            message = (TextView) view.findViewById(R.id.message);
        }
    }
}
