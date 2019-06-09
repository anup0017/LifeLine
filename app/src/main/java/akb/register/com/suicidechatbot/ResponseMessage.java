package akb.register.com.suicidechatbot;

import android.content.ClipboardManager;
import android.content.Context;

public class ResponseMessage {

        String text;
        boolean isMe;

        public ResponseMessage(String text, boolean isMe) {
            this.text = text;
            this.isMe = isMe;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isMe() {
            return isMe;
        }

        public void setMe(boolean me) {
            isMe = me;
        }
    }
