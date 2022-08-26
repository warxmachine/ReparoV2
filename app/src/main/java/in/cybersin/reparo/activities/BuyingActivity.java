package in.cybersin.reparo.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import in.cybersin.reparo.R;
import in.cybersin.reparo.model.Subscriptions;

public class BuyingActivity extends AppCompatActivity implements PaymentResultListener {
    String id = "";
    Toolbar toolbar;
    Button Buy;
    ImageView Card;
    TextView High1,High2,High3,High,Price;
    ProgressBar progressBar ;
    int amount ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        @SuppressLint("UseCompatLoadingForDrawables") android.graphics.drawable.Drawable background = BuyingActivity.this.getResources().getDrawable(R.drawable.background2);
        getWindow().setBackgroundDrawable(background);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        Card = findViewById(R.id.card);

        High  = findViewById(R.id.high);

        High1  = findViewById(R.id.high1);

        High2  = findViewById(R.id.high2);

        High3  = findViewById(R.id.high3);

        progressBar = findViewById(R.id.progressbar);

        Price = findViewById(R.id.price);

        id = getIntent().getStringExtra("id");

        FirebaseDatabase.getInstance().getReference("Subscription").child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Subscriptions subscriptions = snapshot.getValue(Subscriptions.class);
                        Price.setText("₹ "+ new DecimalFormat("##,##,##0").format(Integer.parseInt(subscriptions.getPrice())));
                        amount = Integer.parseInt(subscriptions.getPrice());
                        if(!subscriptions.getHigh().equals("")){
                            High.setText('\u2022'+ " " +subscriptions.getHigh());
                        }else{
                            High.setVisibility(View.GONE);
                        }

                        if(!subscriptions.getHigh1().equals("")){
                            High1.setText('\u2022'+ " " +subscriptions.getHigh1());
                        }else{
                            High1.setVisibility(View.GONE);
                        }

                        if(!subscriptions.getHigh2().equals("")){
                            High2.setText('\u2022'+ " " +subscriptions.getHigh2());
                        }else{
                            High2.setVisibility(View.GONE);
                        }

                        if(!subscriptions.getHigh3().equals("")){
                            High3.setText('\u2022'+ " " +subscriptions.getHigh3());
                        }else{
                            High3.setVisibility(View.GONE);
                        }

                        Picasso.get()
                                .load(subscriptions.getImg())
                                .into(Card, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        Buy = findViewById(R.id.buying);
        Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkout checkout = new Checkout();

                // set your id as below
                checkout.setKeyID("rzp_live_BeXyXoeRQg2ZR5");

                // set image
                //    checkout.setImage(R.drawable.gfgimage);

                // initialize json object
                JSONObject object = new JSONObject();
                try {
                    // to put name
                    object.put("name", "REPARO");
                    object.put("image", "https://docs.google.com/uc?export=download&id=1H_A3SZ4WaZqsaCJY-hy_8FedHYZKSAYv");

                    // put description
                    object.put("description", "Payments");

                    // to set theme color
                    object.put("theme.color", "#193661");

                    // put the currency
                    object.put("currency", "INR");


                    object.put("amount",amount*100);



                    // open razorpay to checkout activity
                    checkout.open(BuyingActivity.this, object );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getUid());
        ref.child("subscriptionDetails").setValue(id);
        ref.child("subscription").setValue(true);
        ref.child("TransactionID").setValue(s);

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("PaymentUpdates").child(FirebaseAuth.getInstance().getUid());
        ref2.child("TransactionID").setValue(s);
        ref2.child("subscriptionDetails").setValue(id);
        Toast.makeText(BuyingActivity.this, "Payment Successful", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getUid());
        ref.child("subscription").setValue(false);
        ref.child("subscriptionDetails").setValue(id);
        ref.child("TransactionID").setValue(s + i);

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("PaymentUpdates").child(FirebaseAuth.getInstance().getUid());
        ref2.child("TransactionID").setValue(s);
        ref2.child("subscriptionDetails").setValue(id);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}