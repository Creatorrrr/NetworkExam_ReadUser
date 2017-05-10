package com.example.kosta.networkexam_readuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kosta on 2017-05-09.
 */

public class UserAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;

    private LayoutInflater inflater;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView name = (TextView)convertView.findViewById(R.id.nameText);
        TextView address = (TextView)convertView.findViewById(R.id.addrText);
        TextView hobby = (TextView)convertView.findViewById(R.id.hobbyText);

        name.setText(users.get(position).getName());
        address.setText(users.get(position).getAddress());
        hobby.setText(users.get(position).getHobby());

        return convertView;
    }
}
