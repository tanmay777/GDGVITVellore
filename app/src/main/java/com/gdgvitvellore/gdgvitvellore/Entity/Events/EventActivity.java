package com.gdgvitvellore.gdgvitvellore.Entity.Events;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdgvitvellore.gdgvitvellore.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class EventActivity extends AppCompatActivity {

    Firebase mRef;
    Intent intent;
    String eventName,eventDescription,eventDate,eventTime,eventVenue,eventPic;
    TextView eventDescriptionView,eventDateView,eventTimeView,eventVenueView,eventDateHeading,eventTimeHeading,eventVenueHeading,eventDescriptionHeading;
    CollapsingToolbarLayout collapsingToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        eventDescriptionView=(TextView)findViewById(R.id.event_content_description);
        eventDateView=(TextView)findViewById(R.id.event_content_date);
        eventTimeView=(TextView)findViewById(R.id.event_content_time);
        eventVenueView=(TextView)findViewById(R.id.event_content_venue);

        eventDateHeading=(TextView)findViewById(R.id.event_date);
        eventDescriptionHeading=(TextView)findViewById(R.id.event_description);
        eventTimeHeading=(TextView)findViewById(R.id.event_time);
        eventVenueHeading=(TextView)findViewById(R.id.event_venue);


        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        eventDescriptionView.setTypeface(custom_font);
        eventDateView.setTypeface(custom_font);
        eventTimeView.setTypeface(custom_font);
        eventVenueView.setTypeface(custom_font);

        eventDateHeading.setTypeface(custom_font);
        eventDescriptionHeading.setTypeface(custom_font);
        eventTimeHeading.setTypeface(custom_font);
        eventVenueHeading.setTypeface(custom_font);


        intent=getIntent();
        mRef=new Firebase("https://gdg-vit-vellore-af543.firebaseio.com/events/"+intent.getIntExtra("position",0));
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot eventdateSnapshot=dataSnapshot.child("/eventDate");
                DataSnapshot eventNameSnapshot=dataSnapshot.child("/eventName");
                DataSnapshot eventDescriptionSnapshot=dataSnapshot.child("/eventDescription");
                DataSnapshot eventTimeSnapshot=dataSnapshot.child("/eventTime");
                DataSnapshot eventVenueSnapshot=dataSnapshot.child("/eventVenue");
                DataSnapshot eventPicSnapshot=dataSnapshot.child("/eventpic");
                eventDate=eventdateSnapshot.getValue(String.class);
                eventName=eventNameSnapshot.getValue(String.class);
                eventVenue=eventVenueSnapshot.getValue(String.class);
                eventTime=eventTimeSnapshot.getValue(String.class);
                eventPic=eventPicSnapshot.getValue(String.class);
                eventDescription=eventDescriptionSnapshot.getValue(String.class);
                setValues();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    }

    public void setValues() {
        collapsingToolbar.setTitle(eventName);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        loadEventBackdrop();
        eventDescriptionView.setText(eventDescription);
        eventDateView.setText(eventDate);
        eventTimeView.setText(eventTime);
        eventVenueView.setText(eventVenue);
    }

    private void loadEventBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.event_backdrop);
        Glide.with(this).load(eventPic).fitCenter().into(imageView);
    }
}
