package com.app.amur.amur.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.amur.amur.ProfileGeneral;
import com.app.amur.amur.R;
import com.app.amur.amur.User;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Digriz on 15.02.2018.
 */

public class RecyclerViewForShowAllUsersAdapter extends RecyclerView.Adapter<RecyclerViewForShowAllUsersAdapter.ViewHolder>{

    ArrayList<String> values;
    ArrayList<String> listOfMainPhotoMain;
    Context context1;
    ArrayList<User> mUsers;


    public RecyclerViewForShowAllUsersAdapter(Context context2,ArrayList<String> values2, ArrayList<String> listOfMainPhoto, ArrayList<User> users){

        values = values2;
        listOfMainPhotoMain = listOfMainPhoto;
        context1 = context2;
        mUsers = users;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        TextView mTextNames;
        CircularImageView  circleImageView;

        public ViewHolder(View v){

            super(v);



          circleImageView = (CircularImageView) v.findViewById(R.id.picture);
            mTextNames = (TextView) v.findViewById(R.id.names);
        }
    }

    @Override
    public RecyclerViewForShowAllUsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){



        View view1 = LayoutInflater.from(context1).inflate(R.layout.circle_layout_images,parent,false);

        if (view1 == null) {
            view1 = LayoutInflater.from(context1).inflate(R.layout.circle_layout_images, parent, false);
        }

        ViewHolder viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position){
        User user = mUsers.get(position);




           String userId = user.getUserId();
        if (user.getStatusUser().equals("Online") & user.getUserId().equals(userId)){
            Vholder.circleImageView.setBorderColor(Color.parseColor("#009688"));
            Vholder.mTextNames.setText(user.getNameOfUser());
            Glide.with(context1).load(user.getUrlOfPhoto()).into(Vholder.circleImageView);

        } else {
            Vholder.circleImageView.setBorderColor(Color.parseColor("#9E9E9E"));
            Vholder.mTextNames.setText(user.getNameOfUser());
            Glide.with(context1).load(user.getUrlOfPhoto()).into(Vholder.circleImageView);

        }





    }

    @Override
    public int getItemCount(){

        return mUsers.size();
    }
}
