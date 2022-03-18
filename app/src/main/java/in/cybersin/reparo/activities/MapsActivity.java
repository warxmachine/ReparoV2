package in.cybersin.reparo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.arsy.maps_library.MapRipple;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cybersin.reparo.R;
import in.cybersin.reparo.model.Customer;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, LocationListener {
    public static boolean mMapIsTouched = false;

    GoogleMap mGoogleMap;
    MapRipple mapRipple;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    LinearLayout rl;
    Location mLastLocation;
    boolean ispc = true;
    boolean ismo = true;
    LinearLayout container;
    EditText Proble, device, company;
    String type;
    boolean iselec = true;
    CircleImageView profile;
    ImageView next;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    CircleImageView computer, electrician, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        changeStatusBarColor();
        profile = findViewById(R.id.profile);
        FirebaseDatabase.getInstance().getReference("CustomerInformation").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.getValue(Customer.class);
                if (!(customer.getAvatarName() == null)) {
                    Picasso.get().load(customer.getAvatarName()).into(profile);
                } else {
                    Picasso.get().load(R.drawable.person_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ProfileActivity.class));

            }
        });
        type = "";
        electrician = findViewById(R.id.electic);
        next = findViewById(R.id.next);
        rl = findViewById(R.id.click);
        next.setVisibility(View.GONE);
        Proble = findViewById(R.id.problem);
        company = findViewById(R.id.company);
        device = findViewById(R.id.device);
        computer = findViewById(R.id.computer);
        container = findViewById(R.id.containet);
        container.setVisibility(View.GONE);
        mobile = findViewById(R.id.smartphone);
        LinearLayout linearLayout = findViewById(R.id.click);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (computer.getBorderColor() == Color.WHITE) {
                    type = "computer";
                    computer.setBorderColor(Color.GREEN);
                    electrician.setBorderColor(Color.WHITE);
                    rl.animate().translationX(-20).setDuration(400);
                    next.animate()
                            .alpha(1.0f)
                            .setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            next.setVisibility(View.VISIBLE);
                        }

                    });

                    mobile.setBorderColor(Color.WHITE);

                } else {
                    computer.setBorderColor(Color.WHITE);
                    rl.animate().translationX(0).setDuration(400);
                    if (mapRipple.isAnimationRunning()) {
                        mapRipple.stopRippleMapAnimation();
                    }
                    next.animate()

                            .alpha(0.0f)
                            .setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            next.setVisibility(View.GONE);
                            container.setVisibility(View.GONE);

                        }
                    });


                }

            }
        });

        electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (electrician.getBorderColor() == Color.WHITE) {
                    type = "electrician";

                    electrician.setBorderColor(Color.GREEN);
                    mobile.setBorderColor(Color.WHITE);
                    rl.animate().translationX(-20).setDuration(400);
                    next.animate()
                            .alpha(1.0f)
                            .setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            next.setVisibility(View.VISIBLE);

                        }

                    });
                    computer.setBorderColor(Color.WHITE);
                } else {
                    rl.animate().translationX(0).setDuration(400);
                    if (mapRipple.isAnimationRunning()) {
                        mapRipple.stopRippleMapAnimation();
                    }
                    next.animate()

                            .alpha(0.0f)
                            .setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            next.setVisibility(View.GONE);
                            container.setVisibility(View.GONE);


                        }
                    });
                    electrician.setBorderColor(Color.WHITE);
                }

            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getBorderColor() == Color.WHITE) {
                    type = "mobile";

                    mobile.setBorderColor(Color.GREEN);
                    rl.animate().translationX(-20).setDuration(400);
                    next.animate()
                            .alpha(1.0f)
                            .setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            next.setVisibility(View.VISIBLE);
                        }

                    });
                    computer.setBorderColor(Color.WHITE);
                    electrician.setBorderColor(Color.WHITE);
                } else {
                    rl.animate().translationX(0).setDuration(400);
                    if (mapRipple.isAnimationRunning()) {
                        mapRipple.stopRippleMapAnimation();
                    }
                    next.animate()
                            .alpha(0.0f)
                            .setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            next.setVisibility(View.GONE);
                            container.setVisibility(View.GONE);
                        }
                    });
                    mobile.setBorderColor(Color.WHITE);
                }

            }
        });


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            boolean isSuccess = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle)

            );
            if (!isSuccess)
                Log.e("ERROR", "Map style not found!!!");
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
        }
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(false);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(false);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                int height = 150;
                int width = 150;
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.marker2);
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                //Place current location marker
                // mMap is GoogleMap object, latLng is the location on map from which ripple should start

                //in onMapReadyCallBack
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mapRipple = new MapRipple(mGoogleMap, latLng, getBaseContext());
                mapRipple.withNumberOfRipples(3);
                mapRipple.withStrokeColor(Color.DKGRAY);
                mapRipple.withStrokewidth(10);// 10dp
                mapRipple.withDistance(500);// 2000 metres radius
                mapRipple.withRippleDuration(6000);//12000ms
                mapRipple.withTransparency(0.2f);

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mapRipple.isAnimationRunning()) {
                            mapRipple.startRippleMapAnimation();
                        }
                        switch (type) {
                            case "mobile":
                                device.setFocusable(true);
                                device.setFocusableInTouchMode(true);
                                device.requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(device, InputMethodManager.SHOW_IMPLICIT);
                                device.setVisibility(View.VISIBLE);
                                company.setVisibility(View.VISIBLE);
                                break;
                            case "electrician":
                                device.setVisibility(View.GONE);
                                Proble.setFocusable(true);
                                Proble.setFocusableInTouchMode(true);
                                device.requestFocus();
                                InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm3.showSoftInput(Proble, InputMethodManager.SHOW_IMPLICIT);
                                company.setVisibility(View.GONE);
                                break;
                            case "computer":
                                device.setVisibility(View.VISIBLE);
                                device.setFocusable(true);
                                device.setFocusableInTouchMode(true);
                                device.requestFocus();
                                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm2.showSoftInput(device, InputMethodManager.SHOW_IMPLICIT);
                                company.setVisibility(View.VISIBLE);
                                break;

                        }

                        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.slide);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        slidingUpPanelLayout.setAnchorPoint(15);
                        container.setVisibility(View.VISIBLE);
                       /* final IOSDialog dialog0 = new IOSDialog.Builder(MapsActivity.this)
                                .build();
                        dialog0.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog0.show();*/
                    }
                });
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(smallMarkerIcon);
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng) // Sets the center of the map to Geolocation
                        .zoom(16.6f)// Sets the zoom
                        .tilt(45)// Sets the tilt of the camera to 30 degrees
                        .build();// Creates a CameraPosition from the builder

                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        SlidingUpPanelLayout layou = findViewById(R.id.slide);
                        layou.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    }
                });
                mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        SlidingUpPanelLayout layou = findViewById(R.id.slide);
                        layou.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                });

            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
  /* private void loadAllAvailableEngineer(final LatLng location) {
        {

            DatabaseReference engineersLocation = null;
            if(ispc) {
                engineersLocation= FirebaseDatabase.getInstance().getReference("EngineersInformation").child("Computer");
            } else if(ismo) {
                engineersLocation= FirebaseDatabase.getInstance().getReference("EngineersInformation").child("Mobile");
            } else if(iselec) {
                engineersLocation= FirebaseDatabase.getInstance().getReference("EngineersInformation").child("Electrician");
            }

            GeoFire gfEngineers = new GeoFire(engineersLocation);
            GeoQuery geoQuery = gfEngineers.queryAtLocation(new GeoLocation(location.latitude, location.longitude), 6);
            geoQuery.removeAllListeners();
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, final GeoLocation location) {

                    FirebaseDatabase.getInstance().getReference("EngineersInformation")
                            .child(key)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    final Customer customer = dataSnapshot.getValue(Customer.class);

                                    mGoogleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(location.latitude, location.longitude))
                                            .draggable(true)
                                            .title(customer.getName())
                                            .snippet(customer.getType())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.www)));

                                    mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {
                                            Intent intent= new Intent(getBaseContext(),EngineerCalls.class);
                                            intent.putExtra("engineerId",dataSnapshot.getKey());
                                            intent.putExtra("type",customer.getType());
                                            startActivity(intent);
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                }
                @Override
                public void onKeyExited(String key) {

                }
                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }
                @Override
                public void onGeoQueryReady() {
                    if (distance <= LIMIT) {
                        distance++;
                        loadAllAvailableEngineer(location);
                    }
                }
                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });
        }*/


}