package app.chat.letschat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.chat.letschat.GenralUtils;
import app.chat.letschat.R;
import app.chat.letschat.dataModel.Message;

/**
 * Created by ashrafiqubal on 14/07/17.
 */

public class AdapterMainActivity extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_INCOMING_MESSAGE = 2;
    private final int VIEW_TYPE_OUTGOING_MESSAGE = 1;
    private final int VIEW_TYPE_OTHER_MESSAGE = 3;
    Context context;
    List<Message> messages;

    public AdapterMainActivity(Context context, List<Message> messages) {
        super();
        this.context = context;
        this.messages = messages;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (messages.get(viewType).getViewType()){
            case VIEW_TYPE_INCOMING_MESSAGE:
                return new ViewHolderIncomingMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_incoming_message, parent, false));
            case VIEW_TYPE_OUTGOING_MESSAGE:
                return new ViewHolderOutgoingMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_outgoing_message, parent, false));
            case VIEW_TYPE_OTHER_MESSAGE:
                return new ViewHolderOtherMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_other_message, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderIncomingMessage){
            ((ViewHolderIncomingMessage) holder).message.setText(messages.get(position).getText());
            ((ViewHolderIncomingMessage) holder).time.setText(GenralUtils.getFormattedTime(messages.get(position).getCreatedAt()));
        }else if(holder instanceof ViewHolderOutgoingMessage){
            ((ViewHolderOutgoingMessage) holder).message.setText(messages.get(position).getText());
            ((ViewHolderOutgoingMessage) holder).time.setText(GenralUtils.getFormattedTime(messages.get(position).getCreatedAt()));
        }else if(holder instanceof ViewHolderOtherMessage){
            ((ViewHolderOtherMessage) holder).message.setText(messages.get(position).getText());
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
    public void setMessages (List<Message> messages){
        this.messages = messages;
    }

    private class ViewHolderIncomingMessage extends RecyclerView.ViewHolder {
        View view;
        TextView time;
        TextView message;
        public ViewHolderIncomingMessage(View view){
            super(view);
            this.view = view;
            time = (TextView)view.findViewById(R.id.time);
            message = (TextView)view.findViewById(R.id.message);
        }

    }

    private class ViewHolderOutgoingMessage extends RecyclerView.ViewHolder {
        View view;
        TextView message;
        TextView time;
        public ViewHolderOutgoingMessage(View view){
            super(view);
            this.view = view;
            message = (TextView)view.findViewById(R.id.message);
            time = (TextView)view.findViewById(R.id.time);
        }

    }

    private class ViewHolderOtherMessage extends RecyclerView.ViewHolder {
        View view;
        TextView message;
        public ViewHolderOtherMessage(View view){
            super(view);
            this.view = view;
            message = (TextView)view.findViewById(R.id.message);
        }

    }
}
