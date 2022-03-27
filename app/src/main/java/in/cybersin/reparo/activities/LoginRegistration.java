package in.cybersin.reparo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import in.cybersin.reparo.common.Common;
import in.cybersin.reparo.model.Customer;
import io.paperdb.Paper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.Maps;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.cybersin.reparo.R;

public class LoginRegistration extends AppCompatActivity {
    TextView Signup;
    IOSDialog dialog0;
    Toolbar toolbar;
    Button signin;
    EditText Email, Password, Phone, Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);
        Paper.init(this);


        String user = Paper.book().read(Common.userField);
        String pwd = Paper.book().read(Common.passfield);
        if (user != null && pwd != null) {
            if (!TextUtils.isEmpty(user) &&
                    !TextUtils.isEmpty(pwd)) {
                autoLogin(user, pwd);
            }
        }
        dialog0 = new IOSDialog.Builder(LoginRegistration.this)
                .build();
        dialog0.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Signup = findViewById(R.id.signup);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.fragment_register);
                Button create=findViewById(R.id.create);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog0 = new IOSDialog.Builder(LoginRegistration.this)
                                .build();
                        dialog0.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog0.show();
                        View parentLayout = findViewById(R.id.linear);

                        Email = findViewById(R.id.email);
                        Password = findViewById(R.id.password);
                        Phone = findViewById(R.id.phone);
                        Name = findViewById(R.id.name);

                        if (TextUtils.isEmpty(Email.getText().toString())) {

                            Snackbar.make(parentLayout, "Please enter your Email-Address!", Snackbar.LENGTH_SHORT)
                                    .show();
                            dialog0.dismiss();
                            return;

                        }
                        if (TextUtils.isEmpty(Password.getText().toString())) {
                            Snackbar.make(parentLayout, "Please enter your Password!", Snackbar.LENGTH_SHORT)
                                    .show();
                            dialog0.dismiss();
                            return;

                        }
                        if (TextUtils.isEmpty(Phone.getText().toString())) {
                            Snackbar.make(parentLayout, "Please enter your Phone Number!", Snackbar.LENGTH_SHORT)
                                    .show();
                            dialog0.dismiss();
                            return;

                        }
                        if (TextUtils.isEmpty(Email.getText().toString())) {
                            Snackbar.make(parentLayout, "Please enter your Email!", Snackbar.LENGTH_SHORT)
                                    .show();
                            dialog0.dismiss();
                            return;

                        }
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Customer customer = new Customer();
                                        customer.setName(Name.getText().toString());
                                        customer.setEmail(Email.getText().toString());
                                        customer.setPhone(Phone.getText().toString());
                                        customer.setUid(FirebaseAuth.getInstance().getUid());
                                        FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(customer)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Snackbar.make(parentLayout, "Registered SuccessFully, Now you are good to go.", Snackbar.LENGTH_SHORT)
                                                                .show();
                                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString())
                                                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                                    @Override
                                                                    public void onSuccess(AuthResult authResult) {
                                                                        Paper.book().write(Common.userField, Email.getText().toString());
                                                                        Paper.book().write(Common.passfield, Password.getText().toString());
                                                                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                                                        startActivity(intent);
                                                                        dialog0.dismiss();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull  Exception e) {

                                                            }
                                                        });
                                                        dialog0.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Snackbar.make(parentLayout, "Registration Failed!" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                                .show();
                                                        dialog0.dismiss();
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(parentLayout, "Registration Failed!" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                                dialog0.dismiss();
                            }
                        });

                        toolbar = (Toolbar) findViewById(R.id.toolbar);
                        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(LoginRegistration.this, LoginRegistration.class));
                            }

                        });

                    }
                });

            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog0.show();
                if (TextUtils.isEmpty(Email.getText().toString())) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please enter your Email-Address!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }
                if (TextUtils.isEmpty(Password.getText().toString())) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please enter your Password!", Snackbar.LENGTH_SHORT)
                    .show();
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                View parentLayout = findViewById(R.id.content);

                                Snackbar.make(parentLayout, "Registered SuccessFully, Now you are good to go.", Snackbar.LENGTH_SHORT)
                                        .show();

                                Paper.book().write(Common.userField, Email.getText().toString());
                                Paper.book().write(Common.passfield, Password.getText().toString());
                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                startActivity(intent);
                                dialog0.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Something went wrong !" + e, Snackbar.LENGTH_SHORT)
                        .show();
                        dialog0.dismiss();
                    }
                });
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        @SuppressLint("UseCompatLoadingForDrawables") android.graphics.drawable.Drawable background = LoginRegistration.this.getResources().getDrawable(R.drawable.background2);
        getWindow().setBackgroundDrawable(background);
    }

    private void autoLogin(String user, String pwd) {

        dialog0 = new IOSDialog.Builder(LoginRegistration.this)
                .build();
        dialog0.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog0.show();
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(user, pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid())
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Customer customer = dataSnapshot.getValue(Customer.class);
                                        startActivity(new Intent(LoginRegistration.this, MapsActivity.class));
                                        dialog0.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }
                });


    }

}