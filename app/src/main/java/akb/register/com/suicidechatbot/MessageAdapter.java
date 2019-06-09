package akb.register.com.suicidechatbot;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CustomViewHolder> {
    List<ResponseMessage> responseMessages;
    Context context;

    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public CustomViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textMessage);

            textView.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {

                }

                @Override
                public void onDoubleClick(View v) {
                    String google = textView.getText().toString();

                    Pattern p = Pattern.compile(URL_REGEX);
                    Matcher m = p.matcher(google);//replace with string to compare
                    if(m.find()) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(google)));
                    }
                }
            });


            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(textView.getText());
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }


    public MessageAdapter(List<ResponseMessage> responseMessages, Context context) {
        this.responseMessages = responseMessages;
        this.context = context;
    }

    public abstract class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }

    @Override
    public int getItemViewType(int position) {
        if(responseMessages.get(position).isMe()){
            return R.layout.me_bubble;
        }
        return R.layout.bot_bubble;
    }

    @Override
    public int getItemCount() {
        return  responseMessages.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.textView.setText(responseMessages.get(position).getText());
    }
}
