package in.cybersin.reparo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import in.cybersin.reparo.R;
import in.cybersin.reparo.adapter.RequestAdapter;
import in.cybersin.reparo.model.Customer;
import in.cybersin.reparo.model.Request;

public class RequestActivity extends AppCompatActivity {
    Toolbar toolbar;
    ArrayList<Request> list2;
    RecyclerView recyclerView;
    TextView Extra;
    DatabaseReference ref2;


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
        Extra = findViewById(R.id.extra);
        ref2 = FirebaseDatabase.getInstance().getReference().child("Requests").child(FirebaseAuth.getInstance().getUid());
        ref2.keepSynced(true);
        if (ref2 != null) {
            ref2.addValueEventListener(new ValueEventListener() {
                @NonNull
                @Override
                protected Object clone() throws CloneNotSupportedException {
                    return super.clone();
                }

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        Extra.setVisibility(View.GONE);
                        list2 = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            list2.add(ds.getValue(Request.class));
                        }
                        RequestAdapter shopHomeAda = new RequestAdapter(list2);
                        recyclerView.setAdapter(shopHomeAda);
                        Log.e("WAR", "WORKING");
                    }
                    else
                    {
                        Extra.setVisibility(View.VISIBLE);

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