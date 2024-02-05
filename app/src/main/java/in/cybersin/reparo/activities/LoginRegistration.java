
package in.cybersin.reparo.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.cybersin.reparo.R;
import in.cybersin.reparo.common.Common;
import in.cybersin.reparo.model.Customer;
import io.paperdb.Paper;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginRegistration extends AppCompatActivity {
    TextView Signup;
    IOSDialog dialog0;
    Toolbar toolbar;
    ProgressDialog mProgressDialog;

    Button signin;
    FirebaseAuth mAuth;
    EditText Email, Password, Phone, Name;
    TextView signup;
    TextView Forgot;


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
        Forgot = findViewById(R.id.forgotPass);
        mAuth = FirebaseAuth.getInstance();
        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPass();
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { 
                setContentView(R.layout.fragment_register);
                Button create = findViewById(R.id.create);
                signup = findViewById(R.id.signup);
                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(LoginRegistration.this,LoginRegistration.class));
                    }
                });


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
                        if (TextUtils.isEmpty(Name.getText().toString())) {
                            Snackbar.make(parentLayout, "Please enter your Name!", Snackbar.LENGTH_SHORT)
                                    .show();
                            dialog0.dismiss();
                            return;

                        }
                        AuthCredential credential = EmailAuthProvider.getCredential(Email.getText().toString(), Password.getText().toString());
                        mAuth.getCurrentUser().linkWithCredential(credential)
                                .addOnCompleteListener(LoginRegistration.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getUid())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getUid());


                                                            reference.child("name").setValue(Name.getText().toString());
                                                            reference.child("email").setValue(Email.getText().toString());
                                                            reference.child("Phone").setValue(Phone.getText().toString());
                                                            Paper.book().write(Common.userField, Email.getText().toString());
                                                            Paper.book().write(Common.passfield, Password.getText().toString());
                                                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                                            startActivity(intent);
                                                            dialog0.dismiss();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Snackbar.make(parentLayout, "Registration Failed!" + error.getMessage(), Snackbar.LENGTH_SHORT)
                                                                    .show();
                                                            dialog0.dismiss();
                                                        }
                                                    });

                                            Log.d(TAG, "linkWithCredential:success");
                                            FirebaseUser user = task.getResult().getUser();
                                        } else {
                                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                                            Toast.makeText(LoginRegistration.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            Snackbar.make(parentLayout, "Registration Failed!" + task.getException(), Snackbar.LENGTH_SHORT)
                                                    .show();
                                            dialog0.dismiss();
                                        }
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
                    dialog0.dismiss();
                    return;

                }
                if (TextUtils.isEmpty(Password.getText().toString())) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please enter your Password!", Snackbar.LENGTH_SHORT)
                            .show();
                    dialog0.dismiss();
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                View parentLayout = findViewById(R.id.content);

                                Snackbar.make(parentLayout, "Login SuccessFully, Now you are good to go.", Snackbar.LENGTH_SHORT)
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
    private void forgotPass() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginRegistration.this);
        alertDialog.setTitle("Forgot Password");
        alertDialog.setMessage("Please enter your Email Address");

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        View forgot_pwd = inflater.inflate(R.layout.forgot_pwd, null);

        final TextView eml = forgot_pwd.findViewById(R.id.eml);
        alertDialog.setView(forgot_pwd);

        alertDialog.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                mProgressDialog = new ProgressDialog(LoginRegistration.this,R.style.MyAlertDialogStyle);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                mProgressDialog.setContentView(R.layout.progress_bar);
                mProgressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                FirebaseAuth.getInstance().sendPasswordResetEmail(eml.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                mProgressDialog.dismiss();

                                Toast.makeText(getApplication()," Reset password link has been sent" , Toast.LENGTH_LONG)
                                        .show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                mProgressDialog.dismiss();

                                Toast.makeText(getApplication(),"" + e.getMessage(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }
}