package com.xmu.arac;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.xmu.arac.caprotocol.ClientThread;
import com.xmu.arac.caprotocol.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {



    //Use in the client
    ClientThread clientThread;
    Gson gson;

    //Define the client side information
    private static final int TCP_SERVER_PORT = 30004;
    private static String targetSite  = "192.168.0.100";

    //An UI edit text
    private boolean showInput = false;
    private TextInputLayout inputPortLayout;
    private TextInputLayout inputAddrLayout;
    private TextInputEditText textInputerAddr;
    private TextInputEditText textInputerPort;




    //The Float Button
    private FloatingActionButton fabMain,fabBack,fabDraw,fabWeb,fabText,fabKInput,fabModel,fabMan;
    private Animation fabOpen, fabClose, fabClock, fabAnticlock;
    private boolean fabOpenOrNot = false;

    //OpenGL Initial
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;

    //About Setting
    //final MBRenderer mbRenderer = new MBRenderer(this,,screenHeight,screenWidth);
    CARenderer caRenderer;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    /*private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };*/

    //Use the Handler in main Thread to get the message from server
    //In this handler we do two things
    //1.First we receive the data of the camera and ground from the server
    //2.Then we get the Video From the server
    private static class MainHandler extends Handler {
        private final WeakReference<FullscreenActivity> mActivity;

        public MainHandler(FullscreenActivity activity) {
            mActivity = new WeakReference<FullscreenActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FullscreenActivity activity = mActivity.get();
            if (activity != null) {
                //Is the Message the information
                if (msg.what == Constants.RECEIVE_MASSAGE) {
                    System.out.println("FullScreenActivity::handle the information");

                }
                //Is the Message the File
                else if(msg.what == 2){
                    System.out.println("FullScreenActivity::handle the file");
                }
            }
        }
    }
    //Initialize the Handler
    private final MainHandler mainHandler = new MainHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        //Debug information for start the app
        System.out.println("MainActivity:onStart()");

        //create a caRenderer
        caRenderer = new CARenderer(this);

        //clientThread Initialize
        clientThread = new ClientThread(mainHandler);
        //start in the button
        //clientThread.start();

        //Initialize
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        //Find the text inputer of the UI
        inputAddrLayout = findViewById(R.id.inputAddrLayout);
        inputPortLayout = findViewById(R.id.inputPortLayout);
        textInputerAddr = findViewById(R.id.textinputerAddr);
        textInputerPort = findViewById(R.id.textinputerPort);
        //Close the input
        inputAddrLayout.setVisibility(View.GONE);
        inputPortLayout.setVisibility(View.GONE);
        textInputerAddr.setVisibility(View.GONE);
        textInputerPort.setVisibility(View.GONE);

        //its a text View
        mContentView = findViewById(R.id.fullscreen_content);
        glSurfaceView = findViewById(R.id.surfaceView);

        //Find the Button
        fabMain = findViewById(R.id.floatingActionButtonMain);
        fabBack = findViewById(R.id.floatingActionButtonBack);
        fabDraw = findViewById(R.id.floatingActionButtonDraw);
        fabWeb = findViewById(R.id.floatingActionButtonWeb);
        fabText = findViewById(R.id.floatingActionButtonText);
        fabKInput = findViewById(R.id.floatingActionButtonKInput);
        fabModel = findViewById(R.id.floatingActionButtonModel);
        fabMan = findViewById(R.id.floatingActionButtonMan);

        //get Animation
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fabAnticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        //Init Float Button
        initFab();

        //get Focus
        glSurfaceView.requestFocus();

        //get OpenGL INFO
        final ActivityManager activityManager =
                (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();

        final boolean supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        //Use OpenGL ES 2.0
        if(supportEs2)
        {
            System.out.println("MainActivity:supportEs2");

            //request an OpenGL ES 2.0
            glSurfaceView.setEGLContextClientVersion(2);

            //Assign renderer
            glSurfaceView.setRenderer(caRenderer);
            rendererSet = true;
        }

        //Touch
        if(supportEs2)
            glSurfaceView.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                    if(event!=null) {
                        //Turn to one,not need,but test now
                        final float normalizedX =
                                (event.getX() / (float) v.getWidth()) * 2 - 1;
                        final float normalizedY =
                                -((event.getY() / (float) v.getHeight()) * 2 - 1);

                        //Coords self
                        final float zeroOneX =
                                event.getX()/v.getWidth();
                        final float zeroOneY =
                                event.getY()/v.getHeight();

                        //put int Touch X,Y To initial the model.
                        final float TouchX = event.getX();
                        final float TouchY = event.getY();

                        //Left Down is the  (0,0) ,screen coords
                        final float sX = event.getX();
                        final float sY = v.getHeight()- event.getY();

                        //#only use #ACTION_MASK can Two Finger Work
                        switch (event.getAction() & MotionEvent.ACTION_MASK){
                            case MotionEvent.ACTION_DOWN:
                                glSurfaceView.queueEvent(new Runnable() {
                                    @Override
                                    public void run() {
                                            //mbRenderer.handleTouchPress(
                                            //        TouchX,TouchY);
                                            //mbRenderer.handleTouchPress(
                                            //        normalizedX,normalizedY);
                                        caRenderer.handleTouchPress(
                                                sX, sY);
                                    }
                                });
                            break;

                            //Touch up
                            case MotionEvent.ACTION_UP:
                                glSurfaceView.queueEvent(new Runnable() {
                                        @Override
                                        public void run() {
                                            //mbRenderer.handleTouchUp(
                                            //        TouchX,TouchY);
                                            //mbRenderer.handleTouchUp(
                                            //        normalizedX,normalizedY);
                                            //
                                            caRenderer.handleTouchUp(
                                                    sX, sY);

                                        }
                                    });
                                break;
                            //Touch Move:One Finger or Two
                            case MotionEvent.ACTION_MOVE:
                                glSurfaceView.queueEvent(new Runnable() {
                                    @Override
                                    public void run() {
                                            //mbRenderer.handleTouchMove(
                                            //        TouchX,TouchY);
                                            //Debug
                                            //mbRenderer.handleTouchMove(
                                            //       normalizedX,normalizedY);
                                        caRenderer.handleTouchMove(
                                                sX, sY);
                                    }
                                });

                                break;
                        }
                        return true;
                    }else{
                        return false;
                    }
                }
            });
        // Set up the user interaction to manually show or hide the system UI.
        // Use Click to Show the Action Bar
        /*mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(1000);

    }

    @Override
    protected  void onPause()
    {
        System.out.println("MainActivity:onPause()");
        super.onPause();
        if(rendererSet)
        {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume()
    {
        System.out.println("MainActivity:onResume()");
        super.onResume();
        if(rendererSet)
        {
            glSurfaceView.onResume();
        }
    }

    /*private void toggle() {
        if (mVisible) {
            hide();
        }
    }*/

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mContentView.setVisibility(View.GONE);
        //mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        //mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    void initFab() {

        //fabMain to Open all this thing
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabOpenOrNot) {
                    fabMain.startAnimation(fabAnticlock);
                    fabDraw.startAnimation(fabClose);
                    fabBack.startAnimation(fabClose);
                    fabWeb.startAnimation(fabClose);
                    fabText.startAnimation(fabClose);
                    fabKInput.startAnimation(fabClose);
                    fabModel.startAnimation(fabClose);
                    fabMan.startAnimation(fabClose);

                    fabMain.setClickable(true);
                    fabDraw.setClickable(false);
                    fabBack.setClickable(false);
                    fabWeb.setClickable(false);
                    fabText.setClickable(false);
                    fabKInput.setClickable(false);
                    fabModel.setClickable(false);
                    fabMan.setClickable(false);

                    fabOpenOrNot = false;
                } else {
                    fabMain.startAnimation(fabClock);
                    fabDraw.startAnimation(fabOpen);
                    fabBack.startAnimation(fabOpen);
                    fabWeb.startAnimation(fabOpen);
                    fabText.startAnimation(fabOpen);
                    fabKInput.startAnimation(fabOpen);
                    fabModel.startAnimation(fabOpen);
                    fabMan.startAnimation(fabOpen);

                    fabMain.setClickable(true);
                    fabDraw.setClickable(true);
                    fabBack.setClickable(true);
                    fabWeb.setClickable(true);
                    fabText.setClickable(true);
                    fabKInput.setClickable(true);
                    fabModel.setClickable(true);
                    fabMan.setClickable(true);

                    fabOpenOrNot = true;
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(CARenderer.textureEvent)
                    CARenderer.textureEvent = false;
                else
                    CARenderer.textureEvent = true;
            }
        });

        fabDraw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(CARenderer.touchLineEvent)
                    CARenderer.touchLineEvent = false;
                else
                    CARenderer.touchLineEvent = true;
            }
        });

        fabWeb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("FullscreenActivity::ClickButton and run TCP");
                //Send An message to test the server

                Message message = new Message();
                message.what = Constants.SEND_MSG;
                message.obj = "Button::Test the Input of foregound";
                if(clientThread.getClientHandler()!=null){
                    System.out.println("FullscreenActivity::getClientHandler Success!");
                    clientThread.getClientHandler().sendMessage(message);
                }
            }
        });

        fabText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("FullscreenActivity::Click Fab Text");
                try {
                    System.out.println("FullscreenActivity::Text Value:"
                            + textInputerAddr.getText().toString());
                    //Change the TargetSite with the input
                    //Constants.HOST = textInputerAddr.getText().toString();

                    //GET THE Port Number
                    //Constants.PORT = Integer.parseInt(textInputerPort.getText().toString());

                    //When click the button ,start the thread
                    clientThread.start();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        fabKInput.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(showInput) {
                    inputAddrLayout.setVisibility(View.GONE);
                    inputPortLayout.setVisibility(View.GONE);
                    textInputerAddr.setVisibility(View.GONE);
                    textInputerPort.setVisibility(View.GONE);
                    showInput = false;
                }else{
                    inputAddrLayout.setVisibility(View.VISIBLE);
                    inputPortLayout.setVisibility(View.VISIBLE);
                    textInputerAddr.setVisibility(View.VISIBLE);
                    textInputerPort.setVisibility(View.VISIBLE);
                    showInput = true;
                }
            }
        });

        fabModel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                caRenderer.showRectangle();
            }
        });

        fabMan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(CARenderer.textureManEvent){
                    CARenderer.textureManEvent = false;
                }else{
                    CARenderer.textureManEvent = true;
                }

            }
        });
    }
}
