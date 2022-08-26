package in.cybersin.reparo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.cybersin.reparo.R;
import in.cybersin.reparo.adapter.RequestAdapter;
import in.cybersin.reparo.model.Reqeust;

public class RequestActivity extends AppCompatActivity {
    Toolbar toolbar;
    ArrayList<Reqeust> list;
    RecyclerView recyclerView;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        @SuppressLint("UseCompatLoadingForDrawables") android.graphics.drawable.Drawable background = RequestActivity.this.getResources().getDrawable(R.drawable.background2);
        getWindow().setBackgroundDrawable(background);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        recyclerView = findViewById(R.id.rv);

        ref = FirebaseDatabase.getInstance().getReference("Requests").child(FirebaseAuth.getInstance().getUid());

        if (ref != null) {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @NonNull
                @Override
                protected Object clone() throws CloneNotSupportedException {
                    return super.clone();
                }

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            list.add(ds.getValue(Reqeust.class));
                        }
                        RequestAdapter adapterClass = new RequestAdapter(list);
                        recyclerView.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}