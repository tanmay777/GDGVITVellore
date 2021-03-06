package com.gdgvitvellore.gdgvitvellore.Entity.Navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gdgvitvellore.gdgvitvellore.Entity.AboutUs.AboutUsFragment;
import com.gdgvitvellore.gdgvitvellore.Entity.FAQs.FAQsFragment;
import com.gdgvitvellore.gdgvitvellore.Entity.LogIn.LoginActivity;
import com.gdgvitvellore.gdgvitvellore.Entity.Members.TabbedMemberFragment;
import com.gdgvitvellore.gdgvitvellore.Entity.Notification.Services.app.Config;
import com.gdgvitvellore.gdgvitvellore.Entity.Notification.Services.utils.NotificationUtils;
import com.gdgvitvellore.gdgvitvellore.Entity.Project.ProjectFragment;
import com.gdgvitvellore.gdgvitvellore.Entity.BoardMember.BoardFragment;
//import com.example.tanmayjha.gdgvitvellore.Entity.BoardMember.BoardFragment1;
import com.gdgvitvellore.gdgvitvellore.Entity.ContactUs.ContactUsFragment;
import com.gdgvitvellore.gdgvitvellore.Entity.Events.EventsFragment;
//import com.example.tanmayjha.gdgvitvellore.Entity.BoardMember.OrganiserFragment1;
import com.gdgvitvellore.gdgvitvellore.Entity.Timeline.TimelineFragment;
import com.gdgvitvellore.gdgvitvellore.R;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.messaging.FirebaseMessaging;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener{
    String personName="User";
    String title;
    GoogleApiClient mGoogleApiClient;
    String TAG=getClass().getSimpleName();
    String personPhotoUrl;
    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FirebaseMessaging.getInstance().subscribeToTopic("global");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TimelineFragment timelineFragment=new TimelineFragment();
        ft.replace(R.id.container,timelineFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
//        IntentFilter intentFilter=new IntentFilter();
//        intentFilter.addAction(Config.REGISTRATION_COMPLETE);
//        intentFilter.addAction(Config.PUSH_NOTIFICATION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,intentFilter);
        mRegistrationBroadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
//                    Intent intent1 = new Intent(getApplication(), NotificationActivity.class);
//                    intent1.putExtra("type",0);
//                    startActivity(intent1);
                }
                else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)){
                    createdialog(intent);
//                    Intent intent1 = new Intent(getApplication(), NotificationActivity.class);
//                    intent1.putExtra("message",intent.getStringExtra("message"));
//                    intent1.putExtra("type",1);
//                    startActivity(intent1);
                }
            }

        };

        Intent fromLogin=getIntent();
        personName=fromLogin.getStringExtra("personName");
        personPhotoUrl=fromLogin.getStringExtra("personPhotoUrl");
        Log.v("Person's name",personName);
        View hView =  navigationView.getHeaderView(0);
        TextView name=(TextView)hView.findViewById(R.id.person_name);
        CircleImageView personImage=(CircleImageView)hView.findViewById(R.id.person_image);
        name.setText(personName);
        Glide.with(getApplicationContext()).load(personPhotoUrl).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(personImage);
    }

    private void createdialog(Intent intent) {
        alertDialogBuilder=new AlertDialog.Builder(this);
        alertDialogBuilder.setIcon(R.drawable.gdg_logo);
        alertDialogBuilder.setCancelable(true);
        Log.v("HomeActivity",intent.getStringExtra("message"));
        alertDialogBuilder.setMessage(intent.getStringExtra("message"));
        if(intent.getStringExtra("title")!=null)
            alertDialogBuilder.setTitle(intent.getStringExtra("title"));
        else
            alertDialogBuilder.setTitle("Notification:");
        if (intent.getStringExtra("imageUrl")!=null)
        {
            LayoutInflater factory = LayoutInflater.from(this);
            final View view = factory.inflate(R.layout.alert_box_dialog, null);
            Glide.with(getApplicationContext()).load(intent.getStringExtra("imageUrl")).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView)view.findViewById(R.id.dialog_imageview));
            alertDialogBuilder.setView(view);
        }
        else
        {
            ImageView imageView=new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.gdg);
            alertDialogBuilder.setView(imageView);

        }
        alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }



    @Override
    public void onStart()
    {
        super.onStart();
        String title="Timeline";
        getSupportActionBar().setTitle(title);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        OptionalPendingResult<GoogleSignInResult> opr =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            // Users cached credentials are valid, GoogleSignInResult containing ID token
            // is available immediately. This likely means the current ID token is already
            // fresh and can be sent to your server.
            GoogleSignInResult result = opr.get();
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently and get a valid
            // ID token. Cross-device single sign on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                }
            });
        }

        // Timeline code starts here onwards.!
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            Fragment f =getSupportFragmentManager().findFragmentById(R.id.container);
            if(f instanceof TimelineFragment)
            {
                finishAffinity();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        title="";
        id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Fragment f =getSupportFragmentManager().findFragmentById(R.id.container);
                if (id == R.id.timeline) {
                    if(!(f instanceof TimelineFragment))
                    replaceFragment(new TimelineFragment(),"Timeline");
                } else if (id == R.id.events) {
                    if(!(f instanceof EventsFragment))
                    replaceFragment(new EventsFragment(),"Events");
                } else if (id == R.id.projects) {
                    if(!(f instanceof ProjectFragment))
                    replaceFragment(new ProjectFragment(),"Projects");
                } else if (id == R.id.members) {
                    if(!(f instanceof TabbedMemberFragment))
                    replaceFragment(new TabbedMemberFragment(),"Members");
                } else if (id == R.id.organiser) {
                    if(!(f instanceof BoardFragment))
                    replaceFragment(new BoardFragment(),"The Board");
                } else if (id == R.id.about_us) {
                    if(!(f instanceof AboutUsFragment))
                    replaceFragment(new AboutUsFragment(),"About Us");
                } else if (id == R.id.contact_us) {
                    if(!(f instanceof ContactUsFragment))
                    replaceFragment(new ContactUsFragment(),"Contact Us");
                }
                else if (id == R.id.faqs) {
                    if(!(f instanceof FAQsFragment))
                    replaceFragment(new FAQsFragment(),"FAQs");
                }
        /*else if (id == R.id.feedback) {
            FeedbackFragment feedbackFragment=new FeedbackFragment();
            ft.replace(R.id.container,feedbackFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            title="Feedback";
        }
        */
                else if (id == R.id.sign_out) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(@NonNull Status status) {
                                }
                            }
                    );
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        // Handle navigation view item clicks here.
        return true;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    public void replaceFragment(Fragment fragment,String title)
    {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        getSupportActionBar().setTitle(title);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        //register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        //register new push message receiver
        //by doing this, the activity will be notified each time a new message arrives

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        //clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

}
