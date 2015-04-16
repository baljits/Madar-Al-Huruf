package de.vrallev.madar;

import java.util.ArrayList;
import java.util.logging.Level;

import com.tekle.oss.android.animation.AnimationFactory;
import com.tekle.oss.android.animation.AnimationFactory.FlipDirection;

	import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class FlippedView extends Activity {

	private static Bitmap imageOriginal, imageScaled, resizedBitmap, overlayOriginal, overlayScaled, resizedOverlay;
	private static Matrix matrix;

	private ImageView dialer,rotater, alphaImage,menu, menuDetails;
	private EditText writeText;
	private int dialerHeight, dialerWidth;
	private double globalAngle = 0.0;	
	private GestureDetector detector;
	private int letterIndex = 11;
	private boolean isScale = false;
	
	String[] isolatedForm = {"ث","ح","خ","ذ","ش","ص","ض","ط","ظ","ع","غ","ق"};
	String[] beginForm = {"ثـ","حـ","خـ","ذ","شـ","صـ","ضـ","طـ","ظـ","عـ","غـ","قـ"};
	String[] middleForm = {"ـثـ","ـحـ","ـخـ","ـذ","ـشـ","ـصـ","ـضـ","ـطـ","ـظـ","ـعـ","ـغـ","ـقـ"};
	String[] endForm = {"ـث","ـح","ــخ","ـذ","ـش","ـص","ـض","ـط","ـظ","ـع","ـغ","ـق"};
	
	// needed for detecting the inversed rotations
	private boolean[] quadrantTouched;
	public String textString= "";
	public String string = "";
	private boolean allowRotating;
	private ArrayList<Integer> isCharCorrect = new ArrayList<Integer>();
	private ArrayList<Integer> FormLengthArray = new ArrayList<Integer>();
	private String currentLetter = "";
	private int isCorrect = -1;
	private  Drawable[] stringsImages;
	private Animation menuAnimOpen, menuDetailsAnimOpen;
	private Animation menuAnimClose, menuDetailsAnimClose;
	private int isClicked = 0;
	private boolean isDetailWindowOpen = false;
	private int guidePageNum = 0;
	private boolean guideWindow = false;
	
	
	 @Override
	    
	 public void onDestroy()
	    {
		 
	        super.onDestroy();
	        
	        if(imageOriginal!=null)
	        {Log.i("He","Im in ");	imageOriginal.recycle();}
	        if(imageScaled!=null)
	        	imageScaled.recycle();
	        if(resizedBitmap!=null)
	        	resizedBitmap.recycle();
	        
	        if(overlayScaled!=null)
	        	overlayScaled.recycle();
	        
	        if(overlayOriginal!=null)
	        	overlayOriginal.recycle();
			//imageScaled.recycle();
	        imageOriginal = null;
	       imageScaled = null;
	       resizedBitmap = null;
	       overlayScaled = null;
	       overlayOriginal = null;
	       //wordSound.release();
	       menu.clearAnimation();
	       menuDetails.clearAnimation();
	        // imageOriginal.setImageDrawable(null);
	      
			dialer = null;
			rotater = null;
			alphaImage = null;
		
			menu = null;
			menuDetails = null;
			 unbindDrawables(findViewById(R.id.container));
			 isCharCorrect.clear();
			 FormLengthArray.clear();
			
			 System.gc();
	       // Toast.makeText(getApplicationContext(),"16. onDestroy()", Toast.LENGTH_SHORT).show();
	    }
 
 

 @Override
    public void onPause()
    {
	 
        super.onPause();
        
        Log.i("In","Pause");
            unbindDrawables(findViewById(R.id.container));
            System.gc();
        
    }
 
 static void unbindDrawables(View view) {
     try{
     System.out.println("UNBINDING"+view.getBackground());
            if (view.getBackground() != null) {

               // ((Object) view.getBackground()).getBitmap().recycle();


                view.getBackground().setCallback(null);
                view=null;
            }

            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
            ((ViewGroup) view).removeAllViews();
            }

    }catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
    }
 } 
	
	 
	 
	@Override
    public void onCreate(Bundle savedInstanceState) {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
		
		super.onCreate(savedInstanceState);
	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		ImageView imageArray[] = new ImageView[26];
//		
		//imageArray[0] = new ImageView(R.drawable.image0);
		
		
		  DisplayMetrics displaymetrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	        int height = displaymetrics.heightPixels;
	        int width = displaymetrics.widthPixels;
	        Log.i("Density","density"+displaymetrics.densityDpi);
	        Log.i("Width","width"+width);
	        Log.i("Height","height"+height);
	        
	    	DisplayMetrics denm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(denm);
			String resolution = denm.widthPixels + "x" + denm.heightPixels;
			
			 if((resolution.equals("569x320")) && denm.densityDpi==160)
		        {  	
		        	//Toast.makeText(getApplicationContext(),"ININININNI"+resolution+" "+size.y, Toast.LENGTH_SHORT).show();
		        	setContentView(R.layout.flipped_369);
		        }
		        else if(denm.widthPixels>=570 && denm.widthPixels<=640 && denm.heightPixels==480)
		        {
		        	Log.i("Is","it this1?");
		        	setContentView(R.layout.flipped_480_640);
		        }
		        else  if((resolution.equals("455x320")) && denm.densityDpi==160)
		        {  //Toast.makeText(getApplicationContext(),"ININININNI"+resolution+" "+size.y, Toast.LENGTH_SHORT).show();
	        		setContentView(R.layout.flipped_455);
		        }
		        else if(denm.heightPixels>=635 && denm.heightPixels<=645 && denm.widthPixels<=915 && denm.widthPixels>=900)
		        {
		        	Log.i("Is","it this2?");
		        	setContentView(R.layout.flipped_2048_1536);
		        }
		        else if(denm.heightPixels>=635 && denm.heightPixels<=645 && denm.widthPixels>=995 && denm.widthPixels<=1025)
		        {
		        	Log.i("Is","it this3?");
		        	setContentView(R.layout.flipped_1280_752);	
		        }  
		        else if(denm.heightPixels>=790 && denm.heightPixels<=800 && denm.widthPixels>=1190 && denm.widthPixels<=1200 && denm.densityDpi==320)
		        {	
		        	isScale =  true;
		        	Log.i("Is","it 4this?");
		        	setContentView(R.layout.flipped_800_1280_320);	
		        }  
		        else if(denm.heightPixels>=760 && denm.heightPixels<=780 && denm.widthPixels>=1190 && denm.widthPixels<=1200)
		        {
		        	Log.i("Is","it 5this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped_1196_768);	//also nexus 4
		        }
		        else if(denm.heightPixels>=1100 && denm.heightPixels<=1105 && denm.widthPixels>=1920 && denm.densityDpi==320)
		        {
		        	Log.i("Is","it thi6s?");
		        	isScale = true;
		        	setContentView(R.layout.flipped_1200_1920);	
		       }
		        else if(denm.heightPixels>=635 && denm.heightPixels<=645 && denm.widthPixels>=1055 && denm.widthPixels<=1065)
		        {
		        	Log.i("Is","it this7?");
		        	
		        	setContentView(R.layout.flipped_720_1280);	//for density 320
		        }
		        else if(denm.heightPixels>=635 && denm.heightPixels<=645 && denm.widthPixels<=876)
		        {
		        	Log.i("Is","it1 this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped_640);
		        }
		        else if(denm.heightPixels>=765 && denm.heightPixels<=780 && denm.widthPixels>=1215 && denm.widthPixels<=1219)
		        {
		        	isScale = true;
		        	Log.i("Is","it3 this?");
		        	setContentView(R.layout.flipped_1280_768);
		        }
		        else if(denm.heightPixels>=735 && denm.heightPixels<=740 && denm.widthPixels>=1280 && denm.densityDpi==213)
			    {
		        		Log.i("Is","it2 this?");
			        	isScale = true;
			        	setContentView(R.layout.flipped_1280_800_213);
		        }
		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240 && denm.widthPixels>=760 && denm.widthPixels<=765)
		        {Log.i("Is","it44 this?");
		        	setContentView(R.layout.flipped_1280_768);
		        }
		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240 && denm.widthPixels>=760 && denm.widthPixels<=780)
		        {Log.i("Is","it33 this?");
		        	setContentView(R.layout.flipped_600_1024);	//240dpi
		        }
		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240 && denm.widthPixels>=790 && denm.widthPixels<=799)
		        {
		        	Log.i("Is","it 22this?");
		        	setContentView(R.layout.flipped_854_480);	//240dpi
		        }
		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240 && denm.widthPixels>=730 && denm.widthPixels<=740 || denm.widthPixels==800)
		        {
		        	Log.i("Is","it111 this?");
		        	setContentView(R.layout.flipped_480_800);	//for 240
		        }
//		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240)
//		        {			
//		        	Log.i("Is","it this?");
//		        	setContentView(R.layout.flipped_480);
//		        }
//		        else if(denm.heightPixels>=315 && denm.heightPixels<=325)
//		        {
//		        	Log.i("Is","it thi22s?");
//		        	setContentView(R.layout.flipped_480_160);
//		        }
		        else if(denm.heightPixels>=1080 && denm.heightPixels<=1085 && denm.widthPixels==1536)
		        {
		        	Log.i("Is","it 2342this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped); //sw768_hdpi
		        }
		        else if(denm.heightPixels>=1080 && denm.heightPixels<=1085 && denm.widthPixels==1794 && denm.densityDpi==480)
		        {	Log.i("Is","it11§ this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped_1080_1920);
		        }
		        else if(denm.heightPixels>=1080 && denm.heightPixels<=1085 && denm.widthPixels==1920 && denm.densityDpi==480)
		        {	Log.i("Is","it11§ this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped_1080_1920_s4);
		        }
		        else if(denm.heightPixels>=1080 && denm.heightPixels<=1085 && denm.widthPixels==1920 || denm.widthPixels==1152)
		        {	
		        	Log.i("Is","it11§ this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped_1920_1152);
		        }
		        
		        else if(denm.heightPixels>=1125 && denm.heightPixels<=1130 && denm.widthPixels==1920)
		        {
		        	Log.i("Is","22it this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped);	//sw_800_xhdpi
		        }
		        else if(denm.heightPixels>=1440 && denm.heightPixels<=1445 && denm.widthPixels==2048)
		        {
		        	Log.i("Is","it444444 this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped);	//sw_768_xhdpi
		        }
		        else if(denm.heightPixels>=1440 && denm.heightPixels<=1445 && denm.widthPixels==2560) 
		        {Log.i("Is","it99 this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped_1536_2560);	
		        }
		        else if(denm.heightPixels>=1500 && denm.heightPixels<=1600 && denm.widthPixels==2560)
		        {	Log.i("Is","it 23412this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped_1600_2560);	
		        }
		        else if(denm.heightPixels>=600 && denm.heightPixels<=605 && denm.widthPixels==961)
		        {
		        	Log.i("Is","it 23412this?");
		        	isScale = true;
		        	setContentView(R.layout.flipped_600_1024);	
		        }
		        else if(denm.heightPixels>=750 && denm.heightPixels<=768 && denm.widthPixels==1280 && denm.densityDpi==160)
		        {
		        	isScale = true;
		        	Log.i("Is","it 23412this?");
		        	
		        	  setContentView(R.layout.flipped_800_1280_160);	
		        }
		        else	
		        	setContentView(R.layout.flipped);

	        final Button home = (Button) findViewById(R.id.home);
		    home.setBackgroundColor(Color.TRANSPARENT);
		    home.setVisibility(View.GONE);
		    home.setText("");
		    final Button madar = (Button)findViewById(R.id.madar);
		    madar.setText("");
		    madar.setVisibility(View.GONE);
		    madar.setBackgroundColor(Color.TRANSPARENT);
		    final Button qfi = (Button)findViewById(R.id.qfi);
		    qfi.setBackgroundColor(Color.TRANSPARENT);
		    qfi.setText("");
		    qfi.setVisibility(View.GONE);
		    final Button qcri = (Button)findViewById(R.id.qcri);
		    qcri.setBackgroundColor(Color.TRANSPARENT);
		    qcri.setText("");
		    qcri.setVisibility(View.GONE);
		    final Button invent = (Button)findViewById(R.id.invent);
		    invent.setText("");
		    invent.setBackgroundColor(Color.TRANSPARENT);
		    invent.setVisibility(View.GONE);
		    final Button guide = (Button)findViewById(R.id.guide);
		    guide.setBackgroundColor(Color.TRANSPARENT);
		    guide.setText("");
		    guide.setVisibility(View.GONE);
	        findViewById(R.id.container).setBackgroundColor(Color.WHITE);
	       // setContentView(R.layout.flipped);
	        
	        final Button warningButton = (Button)findViewById(R.id.warning);
	        Log.i("mem", "mem"+(Runtime.getRuntime().totalMemory()));
	       // overridePendingTransition(R.anim.anim_left, android.R.anim.slide_in_left);
	        menuAnimOpen = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
	        menuAnimClose = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
	       // AnimationFactory.flipTransition(viewFlipper, FlipDirection.LEFT_RIGHT);
	        menuDetailsAnimOpen = AnimationUtils.loadAnimation(this, R.anim.anim_left);
	        menuDetailsAnimClose = AnimationUtils.loadAnimation(this, R.anim.anim_right);
	       
	      
	        menuAnimOpen.setDuration(300);
	        menuAnimClose.setDuration(100);
	        
	        menuAnimOpen.setInterpolator(new AccelerateInterpolator());
	        menuAnimClose.setInterpolator(new AccelerateInterpolator());
	        menuDetailsAnimOpen.setInterpolator(new AccelerateInterpolator());
	        menuDetailsAnimClose.setInterpolator(new AccelerateInterpolator());
	       // android.R.anim.
	      
	        menu = (ImageView) findViewById(R.id.imageView2);
	        
	       // menu.setImageResource(R.drawable.menu_list_1);
	        
	       
	       
	        menuDetails = (ImageView) findViewById(R.id.imageView3);
	        
//	        menuDetails.setImageResource(R.drawable.icon);
	     
	     
	        Bundle bundle = this.getIntent().getExtras();
	       textString = bundle.getString("textBar");
	       isCharCorrect = bundle.getIntegerArrayList("isCharCorrect");//("textBar");
	       FormLengthArray = bundle.getIntegerArrayList("FormLengthArray");
	       if(bundle.getString("warning").equals("red"))
	    	   warningButton.setBackgroundColor(Color.RED);
	       else
	    	   warningButton.setBackgroundColor(Color.GRAY);
	       
	        menu.setVisibility(View.GONE);
	        menuDetails.setVisibility(View.GONE);
	        
	        final int[] wordSounds ={R.raw.back0,R.raw.back1,R.raw.back2,R.raw.back3,R.raw.back4,R.raw.back5,
		    		  R.raw.back6,R.raw.back7,R.raw.back8,R.raw.back9,R.raw.back10,R.raw.back11
		    	};
	       
	        final int[] letterSounds ={R.raw.back_letter0,R.raw.back_letter1,R.raw.back_letter2,
		    		  R.raw.back_letter3, R.raw.back_letter4,R.raw.back_letter5, R.raw.back_letter6,
		    		  R.raw.back_letter7,R.raw.back_letter8, R.raw.back_letter9, R.raw.back_letter10,
		    		  R.raw.back_letter11
		    	};
	        
	       stringsImages = new Drawable[]{
		            getResources().getDrawable(R.drawable.imageback0_small),
		            getResources().getDrawable(R.drawable.imageback1_small),
		            getResources().getDrawable(R.drawable.imageback2_small),
		            getResources().getDrawable(R.drawable.imageback3_small),
		            getResources().getDrawable(R.drawable.imageback4_small),
		            getResources().getDrawable(R.drawable.imageback5_small),
		            getResources().getDrawable(R.drawable.imageback6_small),
		            getResources().getDrawable(R.drawable.imageback7_small),
		            getResources().getDrawable(R.drawable.imageback8_small),
		            getResources().getDrawable(R.drawable.imageback9_small),
		            getResources().getDrawable(R.drawable.imageback10_small),
		            getResources().getDrawable(R.drawable.imageback11_small)
		            
		    };
	 
	      
        alphaImage = (ImageView) findViewById(R.id.imageView1);
        if(isScale)
        {
        	 alphaImage.setBackgroundDrawable(stringsImages[11]);
        	// CLabel.setBackgroundDrawable(getResources().getDrawable(R.drawable.image23_1_small));
        	 menu.setBackgroundResource(R.drawable.menu_list_1_1);
        }
       else
        {	
        	alphaImage.setImageDrawable(stringsImages[11]);
        	menu.setImageResource(R.drawable.menu_list_1_1);
        }
        
      //  alphaImage.setImageDrawable(stringsImages[11]);
        if(denm.heightPixels==480 && denm.widthPixels==737 && denm.densityDpi==240)
        { 
        	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)alphaImage.getLayoutParams();
        	params.setMargins(0, 0, 10, 0); //substitute parameters for left, top, right, bottom
        	alphaImage.setLayoutParams(params);
        }
        writeText = (EditText) findViewById(R.id.editText1);
        writeText.setBackgroundResource(R.drawable.menu_bar);
        writeText.setTextColor(Color.BLACK);
        writeText.setKeyListener(null);
        writeText.setText(textString);
        // load the image only once
        if (imageOriginal == null && overlayOriginal == null) {
        	imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.backwheel1);
        	overlayOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.back_wheel_overlay1);
        	Log.i("In","In");
            //resizedBitmap=Bitmap.createScaledBitmap(imageOriginal, imageOriginal.getWidth(), imageOriginal.getHeight(), true);
            //resizedOverlay = Bitmap.createScaledBitmap(overlayOriginal, imageOriginal.getWidth(), imageOriginal.getHeight(), true);
        }
        
        // initialize the matrix only once
        if (matrix == null) {
        	matrix = new Matrix();
        } else {
        	// not needed, you can also post the matrix immediately to restore the old state
        	matrix.reset();
        }

        detector = new GestureDetector(this, new MyGestureDetector());
        
        // there is no 0th quadrant, to keep it simple the first value gets ignored
        quadrantTouched = new boolean[] { false, false, false, false, false };
        
        allowRotating = true;
        
        Log.i("Sure","sure");
        dialer = (ImageView) findViewById(R.id.imageView_ring);
        //dialer.setOnTouchListener(new MyOnTouchListener());
        Log.i("Hey","Hey");
        Log.i("IsEMpty","Sure: "+dialer);
        rotater = (ImageView) findViewById(R.id.imageView_ring1);
        rotater.setOnTouchListener(new MyOnTouchListener());
       
        
        dialer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

        	@Override
			public void onGlobalLayout() {
        		// method called more than once, but the values only need to be initialized one time
        		if (dialerHeight == 0 || dialerWidth == 0) {
        			dialerHeight = dialer.getHeight()+300;
        			dialerWidth = dialer.getWidth()+300;
        			
        			// resize
					Matrix resize = new Matrix();
					
					resize.postScale((float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getWidth(), (float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getHeight());
					
				//	Log.i("Here","Width"+imageOriginal.getWidth() + "and"+imageOriginal.getHeight());
				//	Log.i("Here","Width"+overlayOriginal.getWidth() + "and"+overlayOriginal.getHeight());
					
					imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0,imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);
					overlayScaled = Bitmap.createBitmap(overlayOriginal, 0, 0,overlayOriginal.getWidth(), overlayOriginal.getHeight(), resize, false);

					// translate to the image view's center
					float translateX = dialerWidth / 2 - imageScaled.getWidth() / 2;
					float translateY = dialerHeight / 2 - imageScaled.getHeight() / 2;
					
					//translateX = translateX - 100;
				
					
					matrix.postTranslate(translateX, translateY);
					
					dialer.setImageBitmap(imageScaled);
					rotater.setImageBitmap(overlayScaled);
				//	alphaImage.setImageBitmap(alphaImageBitmap);
					//alphaImage.setImageMatrix(imageResize);
					
					rotater.setImageMatrix(matrix);
					dialer.setImageMatrix(matrix);
					
					
        		}
			}
		});
    
        
        menuDetails.setOnTouchListener(new OnSwipeTouchListener(null) {
	        public void onSwipeTop() {
	           // Toast.makeText(FlippedView.this, "top", Toast.LENGTH_SHORT).show();
	        }
	        public void onSwipeRight() {
	        	menuDetails.setVisibility(View.GONE);
	        	if(isScale)
	        		menu.setBackgroundResource(R.drawable.menu_list_1_1);
	        	else
	        		menu.setImageResource(R.drawable.menu_list_1_1);
	           // Toast.makeText(FlippedView.this, "right", Toast.LENGTH_SHORT).show();
	        }
	        public void onSwipeLeft() {
	        	
	        	if(guideWindow)
	        	{
	        		
	        		guidePageNum++;
	        		//Toast.makeText(TutorialActivity.this, "left "+guidePageNum , Toast.LENGTH_SHORT).show();
	        		if(guidePageNum == 1)
	        			if(isScale)
	        				menuDetails.setBackgroundResource(R.drawable.guide_details_1);
	        			else
	        				menuDetails.setImageResource(R.drawable.guide_details_1);
	        		else if(guidePageNum == 2)	
	        		{	
	        			//Toast.makeText(FlippedView.this, "left "+guidePageNum , Toast.LENGTH_SHORT).show();
	        			if(isScale)
	        				menuDetails.setBackgroundResource(R.drawable.guide_details_2);
	        			else
	        				menuDetails.setImageResource(R.drawable.guide_details_2);
	        		}
	        		else if(guidePageNum == 3)
	        		{
	        			if(isScale)
	        				menuDetails.setBackgroundResource(R.drawable.guide_details_3);
	        			else
	        				menuDetails.setImageResource(R.drawable.guide_details_3);
	        		}
	        		else if(guidePageNum == 4)
	        		{	
	        			menuDetails.setBackgroundResource(0);
	        			if(isScale)
	        				menuDetails.setBackgroundResource(R.drawable.guide_details_4);
	        			else
	        				menuDetails.setImageResource(R.drawable.guide_details_4);
	        			guidePageNum = 0;
	        		}
	        		
	        	}
	            
	        }
	        public void onSwipeBottom() {
	           // Toast.makeText(FlippedView.this, "bottom", Toast.LENGTH_SHORT).show();
	        }
	    });
	    
	    //home.setVisibility(View.GONE);
	    //madar.setVisibility(View.GONE);
	    //qfi.setVisibility(View.GONE);
	    
	    
	    home.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		 menuDetails.setVisibility(View.GONE);
	    		 home.setVisibility(View.GONE);
	    		 madar.setVisibility(View.GONE);
	    		 qcri.setVisibility(View.GONE);
	    		 qfi.setVisibility(View.GONE);
	    		 invent.setVisibility(View.GONE);
	    		 guide.setVisibility(View.GONE);
	    			guidePageNum = 1;
	    			menuDetails.setAnimation(menuDetailsAnimClose);
	    			guideWindow = false;
//	    			menu.setAnimation(animDown);
//	    			animDown.startNow();
//	    		
	    			if(isScale)
		    		{
	    				menu.setBackgroundResource(0);
	    				menuDetails.setBackgroundResource(0);
		    			menu.setBackgroundResource(R.drawable.menu_list_1_1);
		    		
		    		}
		    		else
		    		{
		    			menuDetails.setBackgroundResource(0);
		    			menu.setImageResource(0);
		    			menu.setImageResource(R.drawable.menu_list_1_1);
		    			
		    		}
	    			
	    			menu.setVisibility(View.GONE);
	    			if(isDetailWindowOpen)
	    			{	
	    				isDetailWindowOpen = false;
	    				menuDetails.setVisibility(View.GONE);
	    			}
	    			
	    			
	    			
	    			isClicked = 0;
	    	}
	    });
	    
	    madar.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		
	    		isDetailWindowOpen = true;
	    		guideWindow = false;
	    		menuDetails.setVisibility(View.VISIBLE);
	    		if(isScale)
	    		{
	    			menu.setBackgroundResource(0);
	    			menuDetails.setBackgroundResource(0);
	    			menu.setBackgroundResource(R.drawable.madar_menu_click);
	    			
	    			menuDetails.setBackgroundResource(R.drawable.madar_details);
	    		}
	    		else
	    		{
	    			menu.setImageResource(0);
	    			menuDetails.setImageResource(0);
	    			menu.setImageResource(R.drawable.madar_menu_click);
	    			menuDetails.setImageResource(R.drawable.madar_details);
	    		}
	    	
	    		menuDetails.setAnimation(menuDetailsAnimOpen);
	    		menuDetailsAnimOpen.start();
	    	}
	    });
	  		   
	    guide.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		guidePageNum = 1;
	    		guideWindow = true;
	    		isDetailWindowOpen = true;
	    		menuDetails.setVisibility(View.VISIBLE);
	    		if(isScale)
	    		{	
	    			menu.setBackgroundResource(0);
	    			menuDetails.setBackgroundResource(0);
	    			menu.setBackgroundResource(R.drawable.guide_menu_click);
	    			menuDetails.setBackgroundResource(R.drawable.guide_details_1);
	    		}
	    		else
	    		{	
	    			menu.setImageResource(0);
    				menuDetails.setImageResource(0);
	    			menu.setImageResource(R.drawable.guide_menu_click);
	    			menuDetails.setImageResource(R.drawable.guide_details_1);
	    		}
	    		//menuDetails.setBackgroundResource(R.drawable.guide_menu_click);
	    		menuDetails.setAnimation(menuDetailsAnimOpen);
	    		menuDetailsAnimOpen.start();
	    	}
	    });
	    
	    qfi.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		
	    		isDetailWindowOpen = true;
	    		guideWindow = false;
	    		menuDetails.setVisibility(View.VISIBLE);
	    		if(isScale)
	    		{	
	    			menu.setBackgroundResource(0);
	    			menuDetails.setBackgroundResource(0);
	    			
	    			menu.setBackgroundResource(R.drawable.qfi_menu_click);
	    			menuDetails.setBackgroundResource(R.drawable.qfi_details);
	    		}
	    		else
	    		{	
	    			menu.setImageResource(0);
    				menuDetails.setImageResource(0);
	    			menu.setImageResource(R.drawable.qfi_menu_click);
	    			menuDetails.setImageResource(R.drawable.qfi_details);
	    		}
	    		//menuDetails.setBackgroundResource(R.drawable.guide_menu_click);
	    		menuDetails.setAnimation(menuDetailsAnimOpen);
	    		menuDetailsAnimOpen.start();
	    	}
	    });
	    
	    qcri.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		guideWindow = false;
	    		isDetailWindowOpen = true;
	    		menuDetails.setVisibility(View.VISIBLE);
	    		if(isScale)
	    		{	
	    			menu.setBackgroundResource(0);
	    			menuDetails.setBackgroundResource(0);
	    			menu.setBackgroundResource(R.drawable.qcri_menu_click);
	    			menuDetails.setBackgroundResource(R.drawable.qcri_details);
	    		}
	    		else
	    		{	
	    			menu.setImageResource(0);
    				menuDetails.setImageResource(0);
	    			menu.setImageResource(R.drawable.qcri_menu_click);
	    			menuDetails.setImageResource(R.drawable.qcri_details);
	    		}
	    		//menuDetails.setBackgroundResource(R.drawable.guide_menu_click);
	    		menuDetails.setAnimation(menuDetailsAnimOpen);
	    		menuDetailsAnimOpen.start();
	    	}
	    });
	    
	    invent.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		guideWindow = false;
	    		isDetailWindowOpen = true;
	    		menuDetails.setVisibility(View.VISIBLE);
	    		if(isScale)
	    		{	
	    			menu.setBackgroundResource(0);
	    			menuDetails.setBackgroundResource(0);
	    			menu.setBackgroundResource(R.drawable.qcri_menu_click);
	    			menuDetails.setBackgroundResource(R.drawable.invent_details);
	    		}
	    		else
	    		{	
	    			menu.setImageResource(0);
	    			menuDetails.setImageResource(0);
	    			menu.setImageResource(R.drawable.qcri_menu_click);
	    			menuDetails.setImageResource(R.drawable.invent_details);
	    		}
	    		//menuDetails.setBackgroundResource(R.drawable.guide_menu_click);
	    		menuDetails.setAnimation(menuDetailsAnimOpen);
	    		menuDetailsAnimOpen.start();
	    	}
	    });
	   
        Button flipButton = (Button) findViewById(R.id.flip);
        flipButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v)
        	{
        		Intent intent = new Intent();
        		Bundle bundle = new Bundle();
        		bundle.putString("textBar",textString);
        		bundle.putIntegerArrayList("isCharCorrect", isCharCorrect);
        		bundle.putIntegerArrayList("FormLengthArray", FormLengthArray);
        		
        		if(isCharCorrect.contains(1))
        			bundle.putString("warning", "red");
        		else
        			bundle.putString("warning", "grey");
        		intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
        	}
        });
        
        Button letterSound = (Button) findViewById(R.id.letterSound);
        letterSound.setBackgroundColor(Color.TRANSPARENT);
        letterSound.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v)
        	{
        		AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        		amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        		
        		final MediaPlayer wordSound = MediaPlayer.create(getApplicationContext(), letterSounds[letterIndex]);
        		wordSound.setVolume(1.0f, 1.0f);
				
        		wordSound.start();
        	//	wordSound.release();
        	}
        });
        
        Button wordButton = (Button) findViewById(R.id.wordPlay);
        wordButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v)
        	{
        		AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        		amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);

        		final MediaPlayer wordSound = MediaPlayer.create(getApplicationContext(), wordSounds[letterIndex]);
        		wordSound.setVolume(1.0f, 1.0f);
				
        		wordSound.start();
        		//wordSound.release();
        	}
        });
        
        Button clickButton = (Button) findViewById(R.id.button4);
	    clickButton.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v)
	    	{
	    		 home.setVisibility(View.VISIBLE);
	    		 madar.setVisibility(View.VISIBLE);
	    		 qcri.setVisibility(View.VISIBLE);
	    		 qfi.setVisibility(View.VISIBLE);
	    		 invent.setVisibility(View.VISIBLE);
	    		 guide.setVisibility(View.VISIBLE);
	    		
	    		if(isClicked == 0)
	    		{
	    			Log.i("Hi","Button Clicked !");
	    			menu.setVisibility(View.VISIBLE);
	    			menu.setAnimation(menuAnimOpen);
	    			//menu.setBackgroundColor(Color.rgb(255, 100, 255));
	    			menuAnimOpen.startNow();
	    			isClicked = 1;
	    		}
	    		else
	    		{
	    			menu.setVisibility(View.GONE);
	    			//menu.setAnimation(animDown);
	    			//animDown.startNow();
	    			
	    			isClicked = 0;
	    			
	    		}
	    	}
	    });
	    
	    
	    
	    Button clearButton = (Button) findViewById(R.id.Button04);
        clearButton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v)
        	{
        		int sum = 0;
        		if(textString.length() > 0)
        		{
        			isCharCorrect.remove(isCharCorrect.size()-1);
        			for(int k=0;k<isCharCorrect.size();k++)
        			{	
        				sum = sum + isCharCorrect.get(k);
        			}

        			if(sum == 0)
        				warningButton.setBackgroundColor(Color.GRAY);
        			textString = textString.substring(0,textString.length()-FormLengthArray.get(FormLengthArray.size()-1));

        			FormLengthArray.remove(FormLengthArray.size()-1);
        			
        			writeText.setText(textString);
        		}
        		else
        		{
        			warningButton.setBackgroundColor(Color.GRAY);
        			textString = "";
        			writeText.setText(textString);
        		}
        	}
        });
        
        
        Button btn1 = (Button) findViewById(R.id.button1);
        
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(textString.length()!=0)
            		if(textString.charAt(textString.length()-1) == ' ')
            			isCorrect = 1;
            		else
            			isCorrect = 0;
            	else if(textString.length() == 0)
            		isCorrect = 1;
            	else
            		isCorrect = 0;
            	
            	if(isCorrect == 1)
            	{	
            		isCharCorrect.add(0);
            		
            		if(letterIndex == 3)
            		{
            			currentLetter = " "+beginForm[letterIndex]+" ";
            			  if(letterIndex!=-1)
                    		  textString+= " " +beginForm[letterIndex]+" ";
                    	  else if(letterIndex == -1)
                    		  textString+=" "+beginForm[11]+" ";
            			
            		}
            		else
            		{
            			currentLetter = " "+beginForm[letterIndex];
            			  if(letterIndex!=-1)
                    		  textString+= " " +beginForm[letterIndex];
                    	  else if(letterIndex == -1)
                    		  textString+=" "+beginForm[11];
            			//string = string + " "+beginForm[letterIndex];
            		}
            	
            		FormLengthArray.add(currentLetter.length());
            		//console.log("string"+string.charAt(string.length-1));
            	}
            	else
            	{
            		isCharCorrect.add(1);
            		
            		if(letterIndex == 3)
            		{
            			currentLetter = " "+beginForm[letterIndex]+" ";
            			  if(letterIndex!=-1)
                    		  textString+= " " +beginForm[letterIndex]+" ";
                    	  else if(letterIndex == -1)
                    		  textString+=" "+beginForm[11]+" ";
            			//string = string + " "+beginForm[letterIndex]+" ";
            		}
            		else
            		{
            			currentLetter = " "+beginForm[letterIndex];
            			  if(letterIndex!=-1)
                    		  textString+= " " +beginForm[letterIndex];
                    	  else if(letterIndex == -1)
                    		  textString+=" "+beginForm[11];
            			  Vibrator vibrateWrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                   		 // Vibrate for 500 milliseconds
                   		 vibrateWrong.vibrate(500);
            			//string = string + " "+beginForm[letterIndex];
            		}
            		FormLengthArray.add(currentLetter.length());
            		//console.log("string"+string.charAt(string.length-1));
            		warningButton.setBackgroundColor(Color.RED);
            	}
            	 writeText.setText(textString);
            	    
            }
        });
        
Button btn2 = (Button) findViewById(R.id.Button01);
        
        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	if(textString.length()!=0)
            		if(textString.charAt(textString.length()-1) == 'ـ')
            			isCorrect = 1;
            		else
            			isCorrect = 0;
            	else
            		isCorrect = 0;
             
            	  
             if(isCorrect == 1)
          	 {	
          		isCharCorrect.add(0);
          		if(letterIndex == 3)
          		{
          			currentLetter = middleForm[letterIndex] + " ";
          			 if(letterIndex!=-1)
               		  textString+= middleForm[letterIndex]+" ";
               	  else if(letterIndex == -1)
               		  textString+=middleForm[11]+" ";
          		}
          		else
          		{
          			currentLetter = middleForm[letterIndex];
          			 if(letterIndex!=-1)
               		  textString+= middleForm[letterIndex];
               	  else if(letterIndex == -1)
               		  textString+=middleForm[11];
          		}
          		
          		FormLengthArray.add(currentLetter.length());
          	}
          	else
          	{
          		isCharCorrect.add(1);
          		if(letterIndex == 3)
          		{
          			currentLetter = middleForm[letterIndex] + " ";
          			 if(letterIndex!=-1)
               		  textString+= middleForm[letterIndex]+" ";
               	  else if(letterIndex == -1)
               		  textString+=middleForm[11]+" ";
          		}
          		else
          		{
          			currentLetter = middleForm[letterIndex];
          			 if(letterIndex!=-1)
               		  textString+= middleForm[letterIndex];
               	  else if(letterIndex == -1)
               		  textString+=middleForm[11];
          		}
          		Vibrator vibrateWrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
          		 // Vibrate for 500 milliseconds
          		 vibrateWrong.vibrate(500);
          		warningButton.setBackgroundColor(Color.RED);
          		FormLengthArray.add(currentLetter.length());
          	}	
            	  
                  writeText.setText(textString);
            	
                 Log.i("ey","clicked B");
               
            }
        });
        
Button btn3 = (Button) findViewById(R.id.Button02);
        
        btn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(textString.length()!=0)
            		if(textString.charAt(textString.length()-1) == 'ـ')
            			isCorrect = 1;
            		else
            			isCorrect = 0;
            	else
            		isCorrect = 0;
            	
            	if(isCorrect == 1)
          		{	
          			isCharCorrect.add(0);
          			 if(letterIndex!=-1)
	            		  textString+= endForm[letterIndex]+" ";
	            	  else if(letterIndex == -1)
	            		  textString+=endForm[11]+" ";
	                 
          			//string = string + endForm[alphabet] + " ";
          			currentLetter = endForm[letterIndex] + " ";
          			FormLengthArray.add(currentLetter.length());
          		}

          		else
          		{
          			textString = textString + endForm[letterIndex] + " ";
          			currentLetter = endForm[letterIndex] + " ";
          			FormLengthArray.add(currentLetter.length());
          			warningButton.setBackgroundColor(Color.RED);
          			isCharCorrect.add(1);
          			Vibrator vibrateWrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
             		 // Vibrate for 500 milliseconds
             		 vibrateWrong.vibrate(500);
          		}
                  writeText.setText(textString);
                Log.i("ey","clicked C");
                
            }
        });
        
Button btn4 = (Button) findViewById(R.id.Button03);
        
        btn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	   /* not sure whether null temrinator is right */
            	if(textString.length()!=0)
            		if(textString.substring(textString.length()-1).equals("") || textString.charAt(textString.length()-1) == ' ')
            			isCorrect = 1;
            		else
            			isCorrect = 0;
            	else
            		isCorrect = 1;
                if(isCorrect == 1)
              	{
                	 if(letterIndex!=-1)
                  		  textString+= " "+isolatedForm[letterIndex]+" ";
                   	 else if(letterIndex == -1)
                  		  textString+=" "+isolatedForm[11]+" ";
                	
              		isCharCorrect.add(0);
              		//string = string + " "+isolatedForm[letterIndex]+" ";
              		currentLetter = " "+isolatedForm[letterIndex]+" ";
              		FormLengthArray.add(currentLetter.length());
              	}
              	else
              	{
              		if(letterIndex!=-1)
                		  textString+= " "+isolatedForm[letterIndex]+" ";
                 	 else if(letterIndex == -1)
                		  textString+=" "+isolatedForm[11]+" ";
              		
              		//string = string + " "+isolatedForm[letterIndex]+" ";
              		currentLetter = " "+isolatedForm[letterIndex]+" ";
              		FormLengthArray.add(currentLetter.length());
              		warningButton.setBackgroundColor(Color.RED);
              		isCharCorrect.add(1);
              		Vibrator vibrateWrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
             		 // Vibrate for 500 milliseconds
             		 vibrateWrong.vibrate(500);
              	}
                writeText.setText(textString);
                Log.i("ey","clicked D");
                
            }
        });
        
    }
	
	/**
	 * Rotate the dialer.
	 * 
	 * @param degrees The degrees, the dialer should get rotated.
	 */
	
	private void rotateDialer(float degrees) {
		globalAngle += degrees;
		globalAngle %= 360;
		if(globalAngle<0)
			globalAngle = (globalAngle % 360 + 360) % 360;
		
		matrix.postRotate(degrees, dialerWidth / 2, dialerHeight / 2);
		
		letterIndex = (int)Math.floor((globalAngle/30));
		letterIndex = letterIndex%12;
			Drawable getImage = stringsImages[letterIndex];
			if(isScale)
	        {
	        	alphaImage.setBackgroundDrawable(getImage);
	        }
	        else
	        {	
	        	alphaImage.setImageDrawable(getImage);
	        }
			//alphaImage.setImageDrawable(getImage);
		
		
		
		//letterIndex = letterIndex-1;
		dialer.setImageMatrix(matrix);
	}
	
	/**
	 * @return The angle of the unit circle with the image view's center
	 */
	private double getAngle(double xTouch, double yTouch) {
		double x = xTouch - (dialerWidth / 2d);
		double y = dialerHeight - yTouch - (dialerHeight / 2d);
//			return Math.atan2(y, x) * (180 / Math.PI);
			//angle = (angle+360)%360;
		switch (getQuadrant(x, y)) {
			case 1:
				return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
			
			case 2:
			case 3:
				return 180 - (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
			
			case 4:
				return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
			
			default:
				// ignore, does not happen
				return 0;
		}
	}
	
	/**
	 * @return The selected quadrant.
	 */
	private static int getQuadrant(double x, double y) {
		if (x >= 0) {
			return y >= 0 ? 1 : 4;
		} else {
			return y >= 0 ? 2 : 3;
		}
	}
	
	/**
	 * Simple implementation of an {@link OnTouchListener} for registering the dialer's touch events. 
	 */
	private class MyOnTouchListener implements OnTouchListener {
		
		private double startAngle;
		private double currentAngle;
		private double diff = 0.0;
		private double old = 0.0;
	
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
				
				case MotionEvent.ACTION_DOWN:
					
					// reset the touched quadrants
					for (int i = 0; i < quadrantTouched.length; i++) {
						quadrantTouched[i] = false;
					}
					
					allowRotating = false;
					
					startAngle = getAngle(event.getX(), event.getY());
					startAngle = (startAngle+360)%360;
					
					//beginAngle = startAngle;
					
					break;
					
				case MotionEvent.ACTION_MOVE:
					
					//Log.i("Letter","let "+(globalAngle%13.86));
					currentAngle = getAngle(event.getX(), event.getY());
					diff = startAngle - currentAngle;
					rotateDialer((float)diff);
					letterIndex = (int) Math.floor(globalAngle/30);
					startAngle = currentAngle;
					startAngle = (startAngle+360)%360;
					break;
					
				case MotionEvent.ACTION_UP:
					
					old = currentAngle %360;
					
					allowRotating = true;
					float extra = (float) (globalAngle%30);
					if(extra < (30/2))
						extra = -1*extra;
					else
						extra = (float) (30 - extra);
					//Toast.makeText(getApplicationContext(),"Extra"+extra+" "+globalAngle, Toast.LENGTH_SHORT).show();
					//Toast.makeText(getApplicationContext(),"Global"+globalAngle, Toast.LENGTH_SHORT).show();

					rotateDialer((float)extra);
					
					break;
			}
			
			// set the touched quadrant to true
			quadrantTouched[getQuadrant(event.getX() - (dialerWidth / 2), dialerHeight - event.getY() - (dialerHeight / 2))] = true;
			
			detector.onTouchEvent(event);
			
			return true;
		}
	}
	
	/**
	 * Simple implementation of a {@link SimpleOnGestureListener} for detecting a fling event. 
	 */
	private class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			
			// get the quadrant of the start and the end of the fling
			int q1 = getQuadrant(e1.getX() - (dialerWidth / 2), dialerHeight - e1.getY() - (dialerHeight / 2));
			int q2 = getQuadrant(e2.getX() - (dialerWidth / 2), dialerHeight - e2.getY() - (dialerHeight / 2));

			// the inversed rotations
			if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math.abs(velocityY))
					|| (q1 == 3 && q2 == 3)
					|| (q1 == 1 && q2 == 3)
					|| (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math.abs(velocityY))
					|| ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
					|| ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
					|| (q1 == 2 && q2 == 4 && quadrantTouched[3])
					|| (q1 == 4 && q2 == 2 && quadrantTouched[3])) {
			
				dialer.post(new FlingRunnable(-1 * (velocityX + velocityY)));
			} else {
				// the normal rotation
				dialer.post(new FlingRunnable(velocityX + velocityY));
			}

			return true;
		}
	}
	
	/**
	 * A {@link Runnable} for animating the the dialer's fling.
	 */
	private class FlingRunnable implements Runnable {

		private float velocity;

		public FlingRunnable(float velocity) {
			this.velocity = velocity;
		}

		@Override
		public void run() {
			if (Math.abs(velocity) > 5 && allowRotating) {
				rotateDialer(velocity / 75);
				velocity /= 1.0666F;

				// post this instance again
				dialer.post(this);
			}
		}
	}
}
