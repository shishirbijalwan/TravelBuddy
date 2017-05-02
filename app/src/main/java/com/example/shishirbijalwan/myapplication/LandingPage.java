package com.example.shishirbijalwan.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.lambda.model.transform.InvokeRequestMarshaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LandingPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView attractionClick;
    TextView translatorClick;
    TextView MemoriesClick;
    ImageButton editImageButton;
    de.hdodenhof.circleimageview.CircleImageView profilepic;
    @Override
    public void onBackPressed() {
        User.setInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to logout?");
        //builder.setMessage("Do you want to save this? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                LandingPage.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_layout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startService(new Intent(this, BackGroundService.class));

        //variables
            navigationView=(NavigationView)findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);
            attractionClick= (TextView) findViewById(R.id.attraction);
            translatorClick= (TextView) findViewById(R.id.translator);
            MemoriesClick= (TextView) findViewById(R.id.pastMemory);
            editImageButton=(ImageButton)findViewById(R.id.newImageButton);

            MemoriesClick.setBackgroundResource(R.drawable.diary);
            attractionClick.setBackgroundResource(R.drawable.attraction);
            translatorClick.setBackgroundResource(R.drawable.translation);

        //functions
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open ,
                R.string.navigation_drawer_close ){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Menu menu = navigationView.getMenu();
                MenuItem temp = menu.findItem(R.id.temp);
                temp.setTitle(User.getInstance().temperature);
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        attractionClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), RecycleActivity.class); //Replace MainActivity.class with your launcher class from previous assignments
                startActivity(myIntent);
                Toast.makeText(getApplicationContext(),User.getInstance().temperature,Toast.LENGTH_SHORT).show();
            }
        });
        translatorClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), VoiceConverter.class); //Replace MainActivity.class with your launcher class from previous assignments
                startActivity(myIntent);
            }
        });

        MemoriesClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent myIntent = new Intent(getBaseContext(), EmergencyActivity.class); //Replace MainActivity.class with your launcher class from previous assignments
                Intent myIntent = new Intent(getBaseContext(), DiaryRecycleActivity.class);
                startActivity(myIntent);
            }
        });

        // set new profile image
                View hView =  navigationView.getHeaderView(0);
         editImageButton = (ImageButton)hView.findViewById(R.id.newImageButton);
        profilepic=(de.hdodenhof.circleimageview.CircleImageView)hView.findViewById(R.id.imageView);
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent,1);
            }
        });

            TextView nameText=(TextView)hView.findViewById(R.id.headerUsername);
            nameText.setText(User.getInstance().name);
        if(User.getInstance().profilepicture!=null)
        profilepic.setImageBitmap(User.getInstance().profilepicture);
       overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    // setting up three dot meny
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.xxx_menu, menu);
        //  getMenuInflater().inflate(R.menu.activity_main_drawer,menu);
        return true;
    }

//three dot menu
    public boolean onOptionsItemSelected ( MenuItem item ) {
        switch ( item . getItemId ()) {
            case R.id.action_one :
            {

              Intent  intent= new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true ;
            }



            default :
// If we got here , the user 's action was not recognized .
// Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 1:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        profilepic.setImageBitmap(bitmap);
                        User.getInstance().profilepicture=bitmap;
                        new uploadImage().execute("hey");
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }





    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.communicator:
                intent= new Intent(this,VoiceConverter.class);
                startActivity(intent);
                break;
            case R.id.diaryEntry:
                intent = new Intent(this,DiaryRecycleActivity.class);
                startActivity(intent);
                break;
            case R.id.attarctions:
                intent = new Intent(this,RecycleActivity.class);
                startActivity(intent);
                break;
            case R.id.emergency:
                intent = new Intent(this,EmergencyActivity.class);
                startActivity(intent);
                break;
            case R.id.Map:
                intent = new Intent(this,MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                intent= new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private class uploadImage extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String[] params) {

            User.getInstance().sendImagetoServer();
            return "hi";
        }


        @Override
        protected void onPostExecute(String message) {


        }

    }


}
