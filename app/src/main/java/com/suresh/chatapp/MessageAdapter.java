package com.suresh.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter <MessageAdapter.Messageholder>{

    private ArrayList<Message> messages;
    private String senderImg,receiverImg;
    private Context context;

    public MessageAdapter(ArrayList<Message> messages, String senderImg, String receiverImg, Context context) {
        this.messages = messages;
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
        this.context = context;
    }

    @NonNull
    @Override
    public Messageholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.messsage_holder,parent,false);
        return new Messageholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Messageholder holder, int position) {
        holder.txtMessage.setText(messages.get(position).getContent());

        ConstraintLayout constraintLayout = holder.ccll;

        if (messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            Glide.with(context).load(senderImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profileImage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profile_cardview,ConstraintSet.LEFT);
            constraintSet.clear(R.id.txtMessageContent,ConstraintSet.LEFT);
            constraintSet.connect(R.id.profile_cardview,ConstraintSet.RIGHT,R.id.ccLayout,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.txtMessageContent,ConstraintSet.RIGHT,R.id.profile_cardview,ConstraintSet.LEFT,0);
            constraintSet.applyTo(constraintLayout);
        }else {
            Glide.with(context).load(receiverImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profileImage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profile_cardview,ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txtMessageContent,ConstraintSet.RIGHT);
            constraintSet.connect(R.id.profile_cardview,ConstraintSet.LEFT,R.id.ccLayout,ConstraintSet.LEFT,0);
            constraintSet.connect(R.id.txtMessageContent,ConstraintSet.LEFT,R.id.profile_cardview,ConstraintSet.RIGHT,0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class Messageholder extends RecyclerView.ViewHolder{
        ConstraintLayout ccll;
        TextView txtMessage;
        ImageView profileImage;
        public Messageholder(@NonNull View itemView) {
            super(itemView);

            ccll = itemView.findViewById(R.id.ccLayout);
            txtMessage = itemView.findViewById(R.id.txtMessageContent);
            profileImage = itemView.findViewById(R.id.small_profile_img);
        }
    }
}
