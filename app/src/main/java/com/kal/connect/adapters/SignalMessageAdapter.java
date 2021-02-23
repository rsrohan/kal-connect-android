package com.kal.connect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.kal.connect.R;
import com.kal.connect.models.SignalMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SignalMessageAdapter extends ArrayAdapter<SignalMessage> {

    public static final int VIEW_TYPE_LOCAL = 0;
    public static final int VIEW_TYPE_REMOTE = 1;
    private static final Map<Integer, Integer> viewTypes;

    static {
        Map<Integer, Integer> aMap = new HashMap<Integer, Integer>();
        aMap.put(VIEW_TYPE_REMOTE, R.layout.adapter_chat_left);
        aMap.put(VIEW_TYPE_LOCAL, R.layout.adapter_chat_right);
        viewTypes = Collections.unmodifiableMap(aMap);
    }

    public SignalMessageAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SignalMessage message = getItem(position);

        if (convertView == null) {
            int type = getItemViewType(position);
            convertView = LayoutInflater.from(getContext()).inflate(viewTypes.get(type), null);
        }

        TextView messageTextView = (TextView) convertView.findViewById(R.id.txtMsg);
        TextView name = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        name.setVisibility(View.GONE);
        if (messageTextView != null) {
            messageTextView.setText(message.getMessageText());
            name.setText("");
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        SignalMessage message = getItem(position);
        return message.isRemote() ? VIEW_TYPE_REMOTE : VIEW_TYPE_LOCAL;
    }

    @Override
    public int getViewTypeCount() {
        return viewTypes.size();
    }
}
