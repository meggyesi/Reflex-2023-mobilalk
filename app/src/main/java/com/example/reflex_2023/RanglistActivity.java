package com.example.reflex_2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class RanglistActivity extends AppCompatActivity {
    PointsDAO pDAO;
    RecyclerView rv;
    RVAdapter rvAdapter;
    List<Point> ps;
    List<String> pointsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranglist);

        pDAO = new PointsDAO();
        rv = findViewById(R.id.recyleView);

        pDAO.update().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ps = pDAO.getPoints();
                Log.i("points SIZE", String.valueOf(ps.size()));

                pointsString = new ArrayList<>();

                for (Point p : ps) {
                    pointsString.add(p.toString());
                }

                rvAdapter = new RVAdapter(RanglistActivity.this, pointsString);
                rv.setAdapter(rvAdapter);
                rv.setLayoutManager(new LinearLayoutManager(RanglistActivity.this));
            }
        });
    }
    public void backToHome(View view) {
        Intent profileIntent = new Intent(this, MainActivity.class);
        profileIntent.putExtra("secret", 42);
        finish();
        startActivity(profileIntent);
    }
}