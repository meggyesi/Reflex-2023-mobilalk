package com.example.reflex_2023;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;



import java.util.HashMap;

public class UserDAO {
    private DatabaseReference ref;
    private User currentUser;
    private String userUID, username;
    private FirebaseDatabase db;
    private FirebaseUser user;

    public UserDAO() {
        db = FirebaseDatabase.getInstance("https://reflexgame-b5f86-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = db.getReference(User.class.getSimpleName());

        addListener();
    }

    public void addListener() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userUID = user.getUid();
            ref.child(userUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUser = snapshot.getValue(User.class);
                    Log.i("firebase", "User loaded: " + currentUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("firebase", "loadPost:onCancelled", error.toException());
                }
            });

            Log.i("firebase", "nem null " + user.getEmail() + ", " + currentUser);

        } else {
            Log.i("firebase", "null");
        }
    }


    public Task<Void> add(User user, String userUID) {
        return ref.child(userUID).setValue(user);
    }

    public Task<Void> update(String key, HashMap<String, Object> map) {
        return ref.child(key).updateChildren(map);
    }


    public HashMap<String, Object> getHash() {
        HashMap<String, Object> hs = new HashMap<>();
        hs.put("email", user.getEmail());
        return hs;
    }

    public Task<User> getCurrentUser() {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();

        if (currentUser != null) {
            taskCompletionSource.setResult(currentUser);
        } else {
            ref.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUser = snapshot.getValue(User.class);
                    taskCompletionSource.setResult(currentUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    taskCompletionSource.setException(error.toException());
                }
            });
        }

        return taskCompletionSource.getTask();
    }


    public Task<Void> update() {
        return ref.child("0").setValue(new Point()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                remove("0");
            }
        });
    }

    public Task<Void> remove(String key) {
        return ref.child(key).removeValue();
    }
}
