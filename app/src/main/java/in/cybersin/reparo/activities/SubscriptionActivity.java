package in.cybersin.reparo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toolbar;

import in.cybersin.reparo.R;

public class SubscriptionActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView Bronze,Silver,Gold,Platinum,Diamond,BronzeBuy,SilverBuy,GoldBuy,PlatinumBuy,DiamondBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        @SuppressLint("UseCompatLoadingForDrawables") android.graphics.drawable.Drawable background = SubscriptionActivity.this.getResources().getDrawable(R.drawable.background2);
        getWindow().setBackgroundDrawable(background);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        Bronze = findViewById(R.id.bronze);
        Silver = findViewById(R.id.silver);
        Gold = findViewById(R.id.gold);
        Platinum = findViewById(R.id.plat);
        Diamond = findViewById(R.id.diamond);


        BronzeBuy = findViewById(R.id.bronzeBuy);
        SilverBuy = findViewById(R.id.silverbuy);
        GoldBuy = findViewById(R.id.goldbuy);
        PlatinumBuy= findViewById(R.id.platibuy);
        DiamondBuy = findViewById(R.id.diamondbuy);

        BronzeBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionActivity.this,BuyingActivity.class);
                intent.putExtra("id","Bronze");
                startActivity(intent);
            }
        });
        SilverBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionActivity.this,BuyingActivity.class);
                intent.putExtra("id","Silver");
                startActivity(intent);
            }
        });
        GoldBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionActivity.this,BuyingActivity.class);
                intent.putExtra("id","Gold");
                startActivity(intent);
            }
        });
        PlatinumBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionActivity.this,BuyingActivity.class);
                intent.putExtra("id","Platinum");
                startActivity(intent);
            }
        });
        DiamondBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionActivity.this,BuyingActivity.class);
                intent.putExtra("id","Diamond");
                startActivity(intent);
            }
        });
        Bronze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bronze.getText().equals("One Seasonal AC Service, No labour charges on appliances, Smartphones and Computers and Extra 5% off.")){
                    Bronze.setText("Bronze");
                    Bronze.setTextSize(15);
                }
                else{
                    Bronze.setText("One Seasonal AC Service, No labour charges on appliances, Smartphones and Computers and Extra 5% off.");
                    Bronze.setTextSize(12);
                }

            }

        });

        Silver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Silver.getText().equals("Two Seasonal AC Services, No labour charges on appliances, Smartphones and Computers and Extra 10% off.")){
                    Silver.setText("Silver");
                    Silver.setTextSize(15);

                }
              else{
                    Silver.setText("Two Seasonal AC Services, No labour charges on appliances, Smartphones and Computers and Extra 10% off.");
                    Silver.setTextSize(12);
                }

            }
        });
        Gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Gold.getText().equals("Four Seasonal AC Services, No labour charges on appliances, Smartphones and Computers and Extra 15% off.")){
                    Gold.setText("Gold");
                    Gold.setTextSize(15);
                }
                else{
                    Gold.setText("Four Seasonal AC Services, No labour charges on appliances, Smartphones and Computers and Extra 15% off.");
                    Gold.setTextSize(12);
                }
            }
        });
        Platinum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Platinum.getText().equals("Six AC Services, No labour charges on appliances, Smartphones and Computers, Extra 20% off.")){
                    Platinum.setText("Platinum");
                    Platinum.setTextColor(0XC8D3D3D3);
                    Platinum.setTextSize(15);

                }
                else{
                    Platinum.setText("Six AC Services, No labour charges on appliances, Smartphones and Computers, Extra 20% off.");
                    Platinum.setTextSize(12);
                    Platinum.setTextColor(Color.BLACK);
                }
            }
        });
        Diamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Diamond.getText().equals("Eight AC Services, No labour charges on appliances, Smartphones and Computers and Extra 25% off.")){
                    Diamond.setText("Diamond");
                    Diamond.setTextSize(15);
                }
                else{
                    Diamond.setText("Eight AC Services, No labour charges on appliances, Smartphones and Computers and Extra 25% off.");
                    Diamond.setTextSize(12);
                }
            }
        });
    }
}