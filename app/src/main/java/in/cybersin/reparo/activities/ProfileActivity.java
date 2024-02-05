package in.cybersin.reparo.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cybersin.reparo.R;
import in.cybersin.reparo.model.Customer;
import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView Name;
    TextView Email;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ProgressDialog mProgressDialog;
    Button signout, Signin;
    TextView Phone;
    CircleImageView ImageView;
    TextView Wrong;
    IOSDialog dialog0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
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

        dialog0 = new IOSDialog.Builder(ProfileActivity.this)
                .build();
        dialog0.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog0.show();


        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);
        ImageView = findViewById(R.id.imageProfile);
        signout = findViewById(R.id.button2);
        Signin = findViewById(R.id.button);
        Wrong = findViewById(R.id.wrong);
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getUid() == null){
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(ProfileActivity.this, LoginRegistration.class));
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
        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).isAnonymous()){
                    chooseImage();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Login to change Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDetail();
            }
        });

        if(FirebaseAuth.getInstance().getUid() !=null){
            FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Customer customer = snapshot.getValue(Customer.class);
                            if (customer != null) {
                                if (customer.getName() != null) {
                                    Name.setText(customer.getName());
                                    Email.setText(customer.getEmail());
                                    Phone.setText(customer.getPhone());
                                    Signin.setVisibility(View.GONE);
                                    dialog0.dismiss();

                                    if (!(customer.getAvatarName() == null)) {
                                        Picasso.get().load(customer.getAvatarName()).into(ImageView);
                                    } else {
                                        Picasso.get().load(R.drawable.person_image);
                                    }
                                } else {
                                    Name.setText("Hello, Guest");
                                    Email.setVisibility(View.GONE);
                                    Phone.setVisibility(View.GONE);
                                    signout.setVisibility(View.GONE);
                                    Wrong.setVisibility(View.GONE);
                                    Picasso.get().load(R.drawable.person_image);
                                    dialog0.dismiss();

                                }
                            } else {
                                Name.setText("Hello, Guest");
                                Email.setVisibility(View.GONE);
                                Phone.setVisibility(View.GONE);
                                signout.setVisibility(View.GONE);
                                Picasso.get().load(R.drawable.person_image);
                                dialog0.dismiss();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

        }
        }

    private void changeDetail() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setTitle("Change Details");
        alertDialog.setMessage("Please enter your Details");

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        View wrongDetails = inflater.inflate(R.layout.wrong_lay, null);

        final EditText Name = wrongDetails.findViewById(R.id.name);
        final EditText Phone = wrongDetails.findViewById(R.id.phone);

        alertDialog.setView(wrongDetails);

        alertDialog.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                mProgressDialog = new ProgressDialog(ProfileActivity.this, R.style.MyAlertDialogStyle);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                mProgressDialog.setContentView(R.layout.progress_bar);
                mProgressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getUid());
                if (TextUtils.isEmpty(Name.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter your Name!", Toast.LENGTH_SHORT)
                            .show();
                    mProgressDialog.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(Name.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter your Phone!", Toast.LENGTH_SHORT)
                            .show();
                    mProgressDialog.dismiss();
                    return;
                }
                    reference.child("name").setValue(Name.getText().toString());
                    reference.child("phone").setValue(Phone.getText().toString());
                    mProgressDialog.dismiss();

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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 9999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9999 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri saveUri = data.getData();
            if (saveUri != null) {
                final ProgressDialog mDialog = new ProgressDialog(ProfileActivity.this);
                mDialog.setMessage("Please Wait !");
                mDialog.show();
                String imageName = UUID.randomUUID().toString();
                final StorageReference imagefolder = storageReference.child("imagesCustomer/" + imageName);

                imagefolder.putFile(saveUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Map<String, Object> avatarUpdate = new HashMap<>();
                                        avatarUpdate.put("avatarName", uri.toString());

                                        DatabaseReference engineerInfo = FirebaseDatabase.getInstance().getReference("CustomerInformation")
                                                .child(FirebaseAuth.getInstance().getUid());
                                        engineerInfo
                                                .updateChildren(avatarUpdate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                                            Picasso.get()
                                                                    .load(uri)
                                                                    .into(ImageView);

                                                        } else
                                                            Toast.makeText(getApplicationContext(), "Upload Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                mDialog.setMessage("Uploading...");

                            }
                        });
            }
        }
    }
}
