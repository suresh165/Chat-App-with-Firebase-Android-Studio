package com.suresh.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;

    private ImageView img_toolbar,imgSend;
    private MessageAdapter messageAdapter;

    private ArrayList<Message> messages;

    String usernameofTheRoommet,emailofroomate,chatRoomateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        usernameofTheRoommet = getIntent().getStringExtra("username_of_roommate");
        emailofroomate = getIntent().getStringExtra("email_of_roommate");
        chatRoomateId = getIntent().getStringExtra("img_of_roommate");

        recyclerView = findViewById(R.id.recyclerMessage);
        edtMessageInput = findViewById(R.id.edtText);
        txtChattingWith = findViewById(R.id.txtChattingWith);
        progressBar = findViewById(R.id.progressMessage);
        img_toolbar = findViewById(R.id.img_toolbar);
        imgSend = findViewById(R.id.imgSenderMessage);

        txtChattingWith.setText(usernameofTheRoommet);
        messages =new  ArrayList<>();

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("messages/"+chatRoomateId).push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),emailofroomate,edtMessageInput.getText().toString()));
                edtMessageInput.setText("");
            }
        });

        messageAdapter = new MessageAdapter(messages,getIntent().getStringExtra("my_img"),getIntent().getStringExtra("img_of_roommate"),MessageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("img_of_roommate")).placeholder(R.drawable.account_img).error(R.drawable.account_img).into(img_toolbar);
        setUpchatRoom();
    }

    private void setUpchatRoom(){
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = snapshot.getValue(User.class).getUsername();
                if (usernameofTheRoommet.compareTo(myUsername)>0){
                    chatRoomateId = myUsername + usernameofTheRoommet;
                }else if (usernameofTheRoommet.compareTo(myUsername) == 0){
                    chatRoomateId = myUsername + usernameofTheRoommet;
                }else {
                    chatRoomateId = usernameofTheRoommet + myUsername;
                }
                attachMessageListener(chatRoomateId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void attachMessageListener(String chatRoomateId){
        FirebaseDatabase.getInstance().getReference("messages/"+chatRoomateId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}