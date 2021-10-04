package androidRecyclerView;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.List;

import com.i9930.croptrails.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messageList;

    public static final int SENDER = 0;
    public static final int RECIPIENT = 1;

    public MessageAdapter(Context context, List<Message> messages) {
        messageList = messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView,mTextView1,mTextView2;

        public ViewHolder(LinearLayout v, int viewType) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.text);
            if (viewType==1){
                mTextView2 = (TextView) v.findViewById(R.id.text3);
                mTextView1 = (TextView) v.findViewById(R.id.text2);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_chat_item_purple, parent, false);
            ViewHolder vh = new ViewHolder((LinearLayout) v,viewType);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_chat_item_green, parent, false);
            ViewHolder vh = new ViewHolder((LinearLayout) v,viewType);
            return vh;
        }
    }

    public void remove(int pos) {
        int position = pos;
        messageList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, messageList.size());

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String msg=messageList.get(position).getMessage();
        holder.mTextView.setText(msg);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        try {
            if (holder.mTextView1!=null&&msg!=null&&msg.length()>0){
                char c = msg.charAt(0); // c='a'
                int ascii = (int)c;
                holder.mTextView1.setText("Ascii        =>"+ascii);
                float ascii2=ascii;
                if (holder.mTextView2!=null)
                    holder.mTextView2.setText("Ascii/2 =>"+(ascii2/2));
            }

        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        if (message.getSenderName().equals("Me")) {
            return SENDER;
        } else {
            return RECIPIENT;
        }

    }

}