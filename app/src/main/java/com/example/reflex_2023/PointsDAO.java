package com.example.reflex_2023;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PointsDAO {

    private DatabaseReference ref;
    private FirebaseDatabase db;
    private Point p;
    private List<Point> ps;

    public PointsDAO() {
        db = FirebaseDatabase.getInstance("https://reflexgame-b5f86-default-rtdb.europe-west1.firebasedatabase.app/");
        ref = db.getReference("Points");
        p = new Point();
        ps = new ArrayList<>();

        addListener();
    }

    public Task<Void> add(Point p) {
        return ref.child(String.valueOf(System.currentTimeMillis())).setValue(p);
    }


    public Task<Void> remove(String key) {
        return ref.child(key).removeValue();
    }

    public Task<Void> update() {
        return ref.child("0").setValue(new Point()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                remove("0");
            }
        });
    }


    public void addListener() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot point : snapshot.getChildren()) {
                    Point tmp = point.getValue(Point.class);
                    Log.i("firebaseize", tmp.toString());

                    ps.add(point.getValue(Point.class));
                }

                Log.i("firebase", "Points loaded!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "loadPost:onCancelled", error.toException());
            }
        });
    }

    public List<Point> getPoints() {
        return ps;
    }


}
