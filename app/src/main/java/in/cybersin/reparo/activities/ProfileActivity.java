package in.cybersin.reparo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cybersin.reparo.R;
import in.cybersin.reparo.model.Customer;
import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView Name;
    TextView Email;
    Button signout,Signin;
    TextView Phone;
    CircleImageView ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Paper.init(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        @SuppressLint("UseCompatLoadingForDrawables") android.graphics.drawable.Drawable background = ProfileActivity.this.getResources().getDrawable(R.drawable.background2);
        getWindow().setBackgroundDrawable(background);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);
        ImageView = findViewById(R.id.imageProfile);
        signout = findViewById(R.id.button2);
        Signin = findViewById(R.id.button);
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,LoginRegistration.class));
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Paper.book().destroy();
                startActivity(new Intent(ProfileActivity.this, LoginRegistration.class));
            }
        });


        if (!(FirebaseAuth.getInstance().getCurrentUser() == null)) {
            FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Customer customer = snapshot.getValue(Customer.class);
                            if (customer != null) {
                                Name.setText(customer.getName());
                                Email.setText(customer.getEmail());
                                Phone.setText(customer.getPhone());
                                Signin.setVisibility(View.GONE);
                                if (!(customer.getAvatarName() == null)) {
                                    Picasso.get().load(customer.getAvatarName()).into(ImageView);
                                } else {
                                    Picasso.get().load(R.drawable.person_image);
                                }
                            }else{
                                Name.setText("Hello, Guest");
                                Email.setVisibility(View.GONE);
                                Phone.setVisibility(View.GONE);
                                signout.setVisibility(View.GONE);
                                Picasso.get().load(R.drawable.person_image);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } else {
            Name.setText("Hello, Guest");
            Email.setVisibility(View.GONE);
            Phone.setVisibility(View.GONE);
            signout.setVisibility(View.GONE);
            Picasso.get().load(R.drawable.person_image);
        }
    }

}
