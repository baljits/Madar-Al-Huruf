package de.vrallev.madar;
	
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
	
	
	public class TutorialActivity extends Activity {
		
		private static Bitmap imageOriginal, imageScaled, resizedBitmap, overlayOriginal, overlayScaled, resizedOverlay;
		private static Matrix matrix;
	
		private ImageView dialer,rotater, alphaImage,menu, menuDetails,CLabel;
		private EditText writeText;
		private int dialerHeight, dialerWidth;
		private double globalAngle = 0.0;	
//		private GestureDetector detector;
		private int letterIndex = 25;
		private Drawable getImage = null;
		Button cButton1, cButton2, cButton3, cButton4;
		MediaPlayer wordSound = null;
		DisplayMetrics denm = new DisplayMetrics();
		private boolean isScale = false;
		private boolean isDetailWindowOpen = false;
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
		private int guidePageNum = 0;
		private boolean guideWindow = false;
		
		final int[] wordSounds = {R.raw.front0,R.raw.front1,R.raw.front2,R.raw.front3,R.raw.front4,R.raw.front5,
	    		  R.raw.front6,R.raw.front7,R.raw.front8,R.raw.front9,R.raw.front10,R.raw.front11,R.raw.front12,
	    		  R.raw.front13, R.raw.front14,R.raw.front15,R.raw.front16,R.raw.front17,R.raw.front18,R.raw.front19,
	    		  R.raw.front20,R.raw.front21,R.raw.front22,R.raw.front23,R.raw.front24,R.raw.front25
	    	};
	      
	      final int[] letterSounds ={R.raw.front_letter0,R.raw.front_letter1,R.raw.front_letter2,
	    		  R.raw.front_letter3, R.raw.front_letter4,R.raw.front_letter5, R.raw.front_letter6,
	    		  R.raw.front_letter7,R.raw.front_letter8, R.raw.front_letter9, R.raw.front_letter10,
	    		  R.raw.front_letter11,R.raw.front_letter12, R.raw.front_letter13, R.raw.front_letter14,
	    		  R.raw.front_letter15,R.raw.front_letter16, R.raw.front_letter17,R.raw.front_letter18,
	    		  R.raw.front_letter19, R.raw.front_letter20, R.raw.front_letter21,R.raw.front_letter22,
	    		  R.raw.front_letter23,R.raw.front_letter24, R.raw.front_letter25
	    	};

		
		String[] isolatedForm = {"ز","ي","كس","و","ف","و","ت","س","ر","ك","ب","و","ن","م","ل","ك","ج","ي","ه","غ","ف","ي","د","س","ب","ا"};
		String[] beginForm = {"ز","يـ","كسـ","و","فـ","و","تـ","سـ","ر","كـ","بـ","و","نـ","مـ","لـ","كـ","جـ","يـ","هـ","غـ","فـ","يـ","د","سـ","بـ","ا"};
		String[] middleForm = {"ـز","ـيـ","ـكسـ","ـو","ـفـ","ـو","ـتـ","ـسـ","ـر","ـكـ","ـبـ","ـو","ـنـ","ـمـ","ـلـ","ـكـ","ـجـ","ـيـ","ـهـ","ـﻐـ","ـفـ","ـيـ","ـد","ـسـ","ـبـ","ـا"};
		String[] endForm = {"ـز","ـي","ـكس","ـو","ـف","ـو","ـت","ـس","ـر","ـك","ـب","ـو","ـن","ـم","ـل","ـك","ـج","ـي","ـه","ـغ","ـف","ـي","ـد","ـس","ـب","ـا"};
		
		// needed for detecting the inversed rotations
		
		
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// Toast.makeText(getApplicationContext(),"Btw", Toast.LENGTH_SHORT).show();
			  if (requestCode == 0) {

			     if(resultCode == RESULT_OK){      
			    	 writeText = (EditText) findViewById(R.id.editText1);
				       // writeText.setBackgroundResource(R.drawable.menu_bar);
				        
				        writeText.setTextColor(Color.BLACK);
				        writeText.setKeyListener(null);
				       textString=data.getStringExtra("textBar"); 
				        writeText.setText(textString);
			    	
				    // Button warningButton = (Button)findViewById(R.id.warning);
					       isCharCorrect = data.getIntegerArrayListExtra("isCharCorrect");//("textBar");
					       FormLengthArray = data.getIntegerArrayListExtra("FormLengthArray");
					       Button warningButton = (Button) findViewById(R.id.warning);
					       if(data.getStringExtra("warning").equals("red"))
					    	   warningButton.setBackgroundColor(Color.RED);
					       else
					    	   warningButton.setBackgroundColor(Color.GRAY);
			    	
			         
			         //Toast.makeText(getApplicationContext(),"Reached", Toast.LENGTH_SHORT).show();
			     }
			     if (resultCode == RESULT_CANCELED) {    
			         //Write your code if there's no result
			     }
			  }
			}//onActivityResult
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
		 
		 
//		 
		 @Override
		    public void onPause()
		    {
			 
		        super.onPause();
		        
		        Log.i("In","Pause");
		      
		            unbindDrawables(findViewById(R.id.container));
		           // wordSound.release();
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
			//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        
			
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
			DisplayMetrics dm = new DisplayMetrics(); 
			Point size = new Point();
			//getWindowManager().getDefaultDisplay().getSize(size);;

			Display display = getWindowManager().getDefaultDisplay();
			//Point size = new Point();
			display.getSize(size);
			int width = size.x;
			
		int	density=dm.densityDpi; 
		//float	width=(density/160)*dm.widthPixels; 
		//float	height=(density/160)*dm.heightPixels;
		     
		
		getWindowManager().getDefaultDisplay().getMetrics(denm);
		String resolution = denm.widthPixels + "x" + denm.heightPixels;
		Log.i("Density","density"+denm.densityDpi);
		        Log.i("Width","width"+size.x);
		        Log.i("Height","height"+size.y);
		      //  Toast.makeText(getApplicationContext(),"Btw"+resolution+" "+denm.densityDpi, Toast.LENGTH_SHORT).show();
		        int screenLayout = getResources().getConfiguration().screenLayout;
		        //screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;   
		        
		        Log.i("Screen layout","screen"+screenLayout);
		        
		        if((resolution.equals("569x320")) && denm.densityDpi==160)
		        {  	
		        	Toast.makeText(getApplicationContext(),"ININININNI"+resolution+" "+size.y, Toast.LENGTH_SHORT).show();
		        	setContentView(R.layout.main_369);
		        }
		        else if(denm.widthPixels>=570 && denm.widthPixels<=640 && denm.heightPixels==480)
		        {
		        	Log.i("Is","it this1?");
		        	setContentView(R.layout.main_480_640);
		        }
		        else  if((resolution.equals("455x320")) && denm.densityDpi==160)
		        {  //Toast.makeText(getApplicationContext(),"ININININNI"+resolution+" "+size.y, Toast.LENGTH_SHORT).show();
	        		setContentView(R.layout.main_455);
		        }
		        else if(denm.heightPixels>=635 && denm.heightPixels<=645 && denm.widthPixels<=915 && denm.widthPixels>=900)
		        {
		        	Log.i("Is","it this2?");
		        	setContentView(R.layout.main_2048_1536);
		        }
		        else if(denm.heightPixels>=635 && denm.heightPixels<=645 && denm.widthPixels>=995 && denm.widthPixels<=1025)
		        {
		        	Log.i("Is","it this3?");
		        	setContentView(R.layout.main_1280_752);	
		        }  
		        else if(denm.heightPixels>=790 && denm.heightPixels<=800 && denm.widthPixels>=1190 && denm.widthPixels<=1200 && denm.densityDpi==320)
		        {	
		        	isScale =  true;
		        	Log.i("Is","it 4this?");
		        	setContentView(R.layout.main_800_1280_320);	
		        }  
		        else if(denm.heightPixels>=760 && denm.heightPixels<=780 && denm.widthPixels>=1190 && denm.widthPixels<=1200)
		        {
		        	Log.i("Is","it 5this?");
		        	isScale = true;
		        	setContentView(R.layout.main_1196_768);	//also nexus 4
		        }
		        else if(denm.heightPixels>=1100 && denm.heightPixels<=1105 && denm.widthPixels>=1920 && denm.densityDpi==320)
		        {
		        	Log.i("Is","it thi6s?");
		        	isScale = true;
		        	setContentView(R.layout.main_1200_1920);	
		       }
//		        else if(denm.heightPixels>=635 && denm.heightPixels<=645 && denm.widthPixels>=1055 && denm.widthPixels<=1065)
//		        {
//		        	Log.i("Is","it this7?");
//		        	
//		        	setContentView(R.layout.main_720_1280);	//for density 320
//		        }
		        else if(denm.heightPixels>=635 && denm.heightPixels<=645 && denm.widthPixels<=876)
		        {
		        	Log.i("Is","it1 this?");
		        	isScale = true;
		        	setContentView(R.layout.main_640);
		        }
		        else if(denm.heightPixels>=765 && denm.heightPixels<=780 && denm.widthPixels>=1215 && denm.widthPixels<=1219)
		        {
		        	isScale = true;
		        	Log.i("Is","it3 this?");
		        	setContentView(R.layout.main_1280_768);
		        }
		        else if(denm.heightPixels>=735 && denm.heightPixels<=740 && denm.widthPixels>=1280 && denm.densityDpi==213)
			    {
		        		Log.i("Is","it2 this?");
			        	isScale = true;
			        	setContentView(R.layout.main_1280_800_213);
		        }
		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240 && denm.widthPixels>=760 && denm.widthPixels<=765)
		        {
		        	Log.i("Is","it44 this?");
		        	setContentView(R.layout.main_1280_768);
		        }
		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240 && denm.widthPixels>=760 && denm.widthPixels<=780)
		        {Log.i("Is","it33 this?");
		        	setContentView(R.layout.main_600_1024);	//240dpi
		        }
		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240 && denm.widthPixels>=790 && denm.widthPixels<=799)
		        {
		        	Log.i("Is","it 22this?");
		        	setContentView(R.layout.main_854_480);	//240dpi
		        }
		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240 && denm.widthPixels>=730 && denm.widthPixels<=740 || denm.widthPixels==800)
		        {
		        	Log.i("Is","it111 this?");
		        	setContentView(R.layout.main_480_800);	//for 240
		        }
//		        else if(denm.heightPixels>=475 && denm.heightPixels<=485 && denm.densityDpi==240)
//		        {			
//		        	Log.i("Is","it this?");
//		        	setContentView(R.layout.main_480);
//		        }
//		        else if(denm.heightPixels>=315 && denm.heightPixels<=325)
//		        {
//		        	Log.i("Is","it thi22s?");
//		        	setContentView(R.layout.main_480_160);
//		        }
		        else if(denm.heightPixels>=1080 && denm.heightPixels<=1085 && denm.widthPixels==1536)
		        {
		        	Log.i("Is","it 2342this?");
		        	isScale = true;
		        	setContentView(R.layout.main); //sw768_hdpi
		        }
		        else if(denm.heightPixels>=1080 && denm.heightPixels<=1085 && denm.widthPixels==1794 && denm.densityDpi==480)
		        {	Log.i("Is","it11§ this?");
		        	isScale = true;
		        	setContentView(R.layout.main_1080_1920);
		        }
		        else if(denm.heightPixels>=1080 && denm.heightPixels<=1085 && denm.widthPixels==1920 && denm.densityDpi==480)
		        {	Log.i("Is","it11§ this?");
		        	isScale = true;
		        	setContentView(R.layout.main_1080_1920_s4);
		        }
		        else if(denm.heightPixels>=1080 && denm.heightPixels<=1085 && denm.widthPixels==1920 || denm.widthPixels==1152)
		        {	
		        	Log.i("Is","it11§ this?");
		        	isScale = true;
		        	setContentView(R.layout.main_1920_1152);
		        }
		        
		        else if(denm.heightPixels>=1125 && denm.heightPixels<=1130 && denm.widthPixels==1920)
		        {
		        	Log.i("Is","22it this?");
		        	isScale = true;
		        	setContentView(R.layout.main);	//sw_800_xhdpi
		        }
		        else if(denm.heightPixels>=1440 && denm.heightPixels<=1445 && denm.widthPixels==2048)
		        {
		        	Log.i("Is","it444444 this?");
		        	isScale = true;
		        	setContentView(R.layout.main);	//sw_768_xhdpi
		        }
		        else if(denm.heightPixels>=1440 && denm.heightPixels<=1445 && denm.widthPixels==2560) 
		        {Log.i("Is","it99 this?");
		        	isScale = true;
		        	setContentView(R.layout.main_1536_2560);	
		        }
		        else if(denm.heightPixels>=1500 && denm.heightPixels<=1600 && denm.widthPixels==2560)
		        {	Log.i("Is","it 23412this?");
		        	isScale = true;
		        	setContentView(R.layout.main_1600_2560);	
		        }
		        else if(denm.heightPixels>=600 && denm.heightPixels<=605 && denm.widthPixels==961)
		        {
		        	Log.i("Is","it 23412this?");
		        	isScale = true;
		        	setContentView(R.layout.main_600_1024);	
		        }
		        else if(denm.heightPixels>=750 && denm.heightPixels<=768 && denm.widthPixels==1280 && denm.densityDpi==160)
		        {
		        	isScale = true;
		        	Log.i("Is","it 23412this?");
		        	
		        	  setContentView(R.layout.main_800_1280_160);	
		        }
		        else	
		        	setContentView(R.layout.main);
		      
		        //clickButton.setVisibility(View.GONE);
		        
		        
//		        final FrameLayout splashScreen = (FrameLayout)findViewById(R.id.splash);
//		      //  splashScreen.setBackgroundResource(R.drawable.pop_up);
//		       	 final Button splashClose = (Button)findViewById(R.id.splashButton);
//		        
//		        splashScreen.bringToFront();
//		        splashScreen.requestLayout();
//		        splashClose.setOnClickListener(new OnClickListener(){
//		        	@Override
//		        	public void onClick(View v)
//		        	{
//		        		splashClose.setVisibility(View.GONE);
//		        		splashScreen.setVisibility(View.GONE);
//		        	}
//		        });
		        
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
		        Button clickButton = (Button) findViewById(R.id.button4);

		        CLabel = (ImageView) findViewById(R.id.imageViewC);
		         
		       
			       cButton1 = (Button)findViewById(R.id.button1C);
			       cButton2 = (Button)findViewById(R.id.Button01C);
			       cButton3 = (Button)findViewById(R.id.Button02C);
			       cButton4 = (Button)findViewById(R.id.Button03C);
		    
			       cButton1.setBackgroundColor(Color.TRANSPARENT);
		       	   cButton2.setBackgroundColor(Color.TRANSPARENT);
		       	   cButton3.setBackgroundColor(Color.TRANSPARENT);
			       cButton4.setBackgroundColor(Color.TRANSPARENT);
			       
		   
			    CLabel.setVisibility(View.GONE);
		        cButton1.setVisibility(View.GONE);
		        cButton4.setVisibility(View.GONE);
		        cButton2.setVisibility(View.GONE);
		        cButton3.setVisibility(View.GONE);
		        
		   
		        final Button warningButton = (Button) findViewById(R.id.warning);
		        
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
		    
		     
		      
		       
		        menuDetails = (ImageView) findViewById(R.id.imageView3);
		       // menuDetails.setBackgroundResource(R.drawable.madar_details_small);
		        //menuDetails.setImageResource(R.drawable.icon);
		     
		        menuDetails.setVisibility(View.GONE);
		        menu.setVisibility(View.GONE);
		        

		     
		       stringsImages = new Drawable[]{
			            getResources().getDrawable(R.drawable.image0_small),
			            getResources().getDrawable(R.drawable.image1_small),
			            getResources().getDrawable(R.drawable.image2_small),
			            getResources().getDrawable(R.drawable.image3_small),
			            getResources().getDrawable(R.drawable.image4_small),
			            getResources().getDrawable(R.drawable.image5_small),
			            getResources().getDrawable(R.drawable.image6_small),
			            getResources().getDrawable(R.drawable.image7_small),
			            getResources().getDrawable(R.drawable.image8_small),
			            getResources().getDrawable(R.drawable.image9_small),
			            getResources().getDrawable(R.drawable.image10_small),
			            getResources().getDrawable(R.drawable.image11_small),
			            getResources().getDrawable(R.drawable.image12_small),
			            getResources().getDrawable(R.drawable.image13_small),
			            getResources().getDrawable(R.drawable.image14_small),
			            getResources().getDrawable(R.drawable.image15_small),
			            getResources().getDrawable(R.drawable.image16_small),
			            getResources().getDrawable(R.drawable.image17_small),
			            getResources().getDrawable(R.drawable.image18_small),
			            getResources().getDrawable(R.drawable.image19_small),
			            getResources().getDrawable(R.drawable.image20_small),
			            getResources().getDrawable(R.drawable.image21_small),
			            getResources().getDrawable(R.drawable.image22_small),
			            getResources().getDrawable(R.drawable.image23_small),
			            getResources().getDrawable(R.drawable.image24_small),
			            getResources().getDrawable(R.drawable.image25_small)
			    };
		 
	  
		    
		       alphaImage = (ImageView) findViewById(R.id.imageView1);
	     
	        if(isScale)
	        {
	        	alphaImage.setBackgroundDrawable(stringsImages[25]);
	        	 CLabel.setBackgroundDrawable(getResources().getDrawable(R.drawable.image23_1_small));
	        	 menu.setBackgroundResource(R.drawable.menu_list_1_1);
	        }
	       else
	        {	
	        	alphaImage.setImageDrawable(stringsImages[25]);
	        	CLabel.setImageDrawable(getResources().getDrawable(R.drawable.image23_1_small));
	        	menu.setImageResource(R.drawable.menu_list_1_1);
	        }
	        if(denm.heightPixels==480 && denm.widthPixels==737 && denm.densityDpi==240)
	        { 
	        	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)alphaImage.getLayoutParams();
	        	params.setMargins(0, 0, 10, 0); //substitute parameters for left, top, right, botto
	        	alphaImage.setLayoutParams(params);
	        }
	        // alphaImage.setLeft(20);
	       
	        writeText = (EditText) findViewById(R.id.editText1);
	        writeText.setBackgroundResource(R.drawable.menu_bar_9);
	        writeText.setTextColor(Color.BLACK);
	        //writeText.setTextSize(size);
	        writeText.setKeyListener(null);
	        findViewById(R.id.container).setBackgroundColor(Color.WHITE);
	       // writeText.setBackgroundColor(Color.RED);
	        
	      

	        
	       
//	        
//	        if(this.getIntent().getExtras()!=null)
//	        { Bundle bundle = this.getIntent().getExtras();
//	        
//	        textString = bundle.getString("hey");
//	        writeText.setText(textString);}
//	        
	        // load the image only once
	        if (imageOriginal == null && resizedBitmap == null) {
	        	
	        	imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.edit);
	        	overlayOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.front_wheel_overlay1);
	        	// Log.i("ALPHA : ","alpha "+alphaImage.setLeft());
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
	
	    //    detector = new GestureDetector(this, new MyGestureDetector());
	        
	        // there is no 0th quadrant, to keep it simple the first value gets ignored
	        quadrantTouched = new boolean[] { false, false, false, false, false };
	        
	        allowRotating = true;
	        
	        dialer = (ImageView) findViewById(R.id.imageView_ring);
	        //dialer.setOnTouchListener(new MyOnTouchListener());
	      //  dialer.set
	        rotater = (ImageView) findViewById(R.id.imageView_ring1);
	        rotater.setOnTouchListener(new MyOnTouchListener());
	       
	    
	        dialer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	       
	        	@Override
				public void onGlobalLayout() {
	        		// method called more than once, but the values only need to be initialized one time
	        		if (dialerHeight == 0 || dialerWidth == 0) {
	        			dialerHeight = dialer.getHeight()+300;
	        			dialerWidth = dialer.getWidth()+300;
	        			
	        			//resize
						Matrix resize = new Matrix();

						resize.postScale((float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getWidth(), (float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getHeight());
				
						Log.i("Here","Width"+imageOriginal.getWidth() + "and"+imageOriginal.getHeight());
						imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0,imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);
						overlayScaled = Bitmap.createBitmap(overlayOriginal, 0, 0,imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);
	
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
	    
	       
	        
	        Button flipButton = (Button) findViewById(R.id.flip);
	        flipButton.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v)
	        	{
	        		Log.i("In","flip");
	        		Bundle bundle = new Bundle();
	        		bundle.putString("textBar", textString);
	        		bundle.putIntegerArrayList("isCharCorrect", isCharCorrect);
	        		bundle.putIntegerArrayList("FormLengthArray", FormLengthArray);
	        		
	        		if(isCharCorrect.contains(1))
	        			bundle.putString("warning", "red");
	        		else
	        			bundle.putString("warning", "grey");
	        	
	        		 Intent myIntent = new Intent(v.getContext(), FlippedView.class);
	        		 myIntent.putExtras(bundle);
	                 startActivityForResult(myIntent, 0);
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
	   
	        Button letterCSound = (Button) findViewById(R.id.letterSoundC);
	        letterCSound.setBackgroundColor(Color.TRANSPARENT);
	        letterCSound.setVisibility(View.GONE);
	        letterCSound.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v)
	        	{
	        		
	        		AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	        		amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
	        	
	        		final MediaPlayer wordSound = MediaPlayer.create(getApplicationContext(), R.raw.front_letter23_1);
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
	        	//	wordSound.release();
	        	}
	        });
	        
	        
	        
	      
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
		    
		    
		    
		    
		    
		  
		    
		    menuDetails.setOnTouchListener(new OnSwipeTouchListener(null) {
		        public void onSwipeTop() {
		           // Toast.makeText(TutorialActivity.this, "top", Toast.LENGTH_SHORT).show();
		        }
		        public void onSwipeRight() {
		        	menuDetails.setVisibility(View.GONE);
		        	if(isScale)
		        		menu.setBackgroundResource(R.drawable.menu_list_1_1);
		        	else
		        		menu.setImageResource(R.drawable.menu_list_1_1);
		            //Toast.makeText(TutorialActivity.this, "right", Toast.LENGTH_SHORT).show();
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
		        			//Toast.makeText(TutorialActivity.this, "left "+guidePageNum , Toast.LENGTH_SHORT).show();
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
		            //Toast.makeText(TutorialActivity.this, "bottom", Toast.LENGTH_SHORT).show();
		        }
		    });
		    
		    //home.setVisibility(View.GONE);
		    //madar.setVisibility(View.GONE);
		    //qfi.setVisibility(View.GONE);
		    
		    
		    home.setOnClickListener(new OnClickListener(){
		    	@Override
		    	public void onClick(View v)
		    	{	 menuDetails.setVisibility(View.GONE);
		    		 home.setVisibility(View.GONE);
		    		 madar.setVisibility(View.GONE);
		    		 qcri.setVisibility(View.GONE);
		    		 qfi.setVisibility(View.GONE);
		    		 invent.setVisibility(View.GONE);
		    		 guide.setVisibility(View.GONE);
		    		 
		    			guidePageNum = 1;
		    			menuDetails.setAnimation(menuDetailsAnimClose);
		    			guideWindow = false;
//		    			menu.setAnimation(animDown);
//		    			animDown.startNow();
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
		    			menuDetails.setImageResource(R.drawable.guide_details_2);
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
		    			menu.setBackgroundResource(R.drawable.invent_menu_click);
		    			menuDetails.setBackgroundResource(R.drawable.invent_details);
		    		}
		    		else
		    		{	
		    			menu.setImageResource(0);
		    			menuDetails.setImageResource(0);
		    			menu.setImageResource(R.drawable.invent_menu_click);
		    			menuDetails.setImageResource(R.drawable.invent_details);
		    		}
		    		//menuDetails.setBackgroundResource(R.drawable.guide_menu_click);
		    		menuDetails.setAnimation(menuDetailsAnimOpen);
		    		menuDetailsAnimOpen.start();
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
	        
	        CLabel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	        		amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);

	        		wordSound = MediaPlayer.create(getApplicationContext(), R.raw.front23_1);
	        		wordSound.setVolume(1.0f, 1.0f);
					
	        		wordSound.start();
					
				}
	        	
	        });
	        cButton1.setOnClickListener(new OnClickListener(){
	        	@Override
	        	public void onClick(View v){
	        		if(textString.length()!=0)
	            		if(textString.substring(textString.length()-1).equals("") || textString.charAt(textString.length()-1) == ' ')
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
	        			currentLetter = " "+beginForm[15];
          			  if(letterIndex!=-1)
                  		  textString+= " " +beginForm[15];
          			  FormLengthArray.add(currentLetter.length());
	        			
	        		}
	        		else
	        		{
	        			currentLetter = " "+beginForm[15];
          			  if(letterIndex!=-1)
                  		  textString+= " " +beginForm[15];
          			  FormLengthArray.add(currentLetter.length());
	        		}
	        		
	        		 writeText.setText(textString);
	        	}
	        });
	        
	        cButton2.setOnClickListener(new OnClickListener(){
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
		          		
		          			currentLetter = middleForm[15];
		          			 if(letterIndex!=-1)
		               		  textString+= middleForm[15];
		               	FormLengthArray.add(currentLetter.length());
		          	}
		          	else
		          	{
		          		isCharCorrect.add(1);
		          	
		          			currentLetter = middleForm[15];
		          			 if(letterIndex!=-1)
		               		  textString+= middleForm[15];
		               	
		          		warningButton.setBackgroundColor(Color.RED);
		          		FormLengthArray.add(currentLetter.length());
		          	}	
		            	  
		                  writeText.setText(textString);
		           
	        	 }
	        });
	        
	        
	        cButton3.setOnClickListener(new OnClickListener() {
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
		            		  textString+= endForm[15]+" ";
		            	
	          			
	          			currentLetter = endForm[15] + " ";
	          			FormLengthArray.add(currentLetter.length());
	          		}

	          		else
	          		{
	          			textString = textString + endForm[15] + " ";
	          			currentLetter = endForm[15] + " ";
	          			FormLengthArray.add(currentLetter.length());
	          			warningButton.setBackgroundColor(Color.RED);
	          			isCharCorrect.add(1);
	          		}
	                  writeText.setText(textString);
	            }
	        });
	        
	        
	        cButton4.setOnClickListener(new OnClickListener() {
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
	                  		  textString+= " "+isolatedForm[15]+" ";
	                   
	              		isCharCorrect.add(0);
	              		//string = string + " "+isolatedForm[letterIndex]+" ";
	              		currentLetter = " "+isolatedForm[15]+" ";
	              		FormLengthArray.add(currentLetter.length());
	              		
	              	}
	              	else
	              	{
	              		if(letterIndex!=-1)
	                		  textString+= " "+isolatedForm[15]+" ";
	                 
	              		
	              		//string = string + " "+isolatedForm[letterIndex]+" ";
	              		currentLetter = " "+isolatedForm[15]+" ";
	              		FormLengthArray.add(currentLetter.length());
	              		warningButton.setBackgroundColor(Color.RED);
	              		isCharCorrect.add(1);
	              	}
	                writeText.setText(textString);
	               
	                
	            }
	        });
	        
	        
	        Button btn1 = (Button) findViewById(R.id.button1);
	       
	        btn1.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	if(textString.length()!=0)
	            		if(textString.substring(textString.length()-1).equals("") || textString.charAt(textString.length()-1) == ' ')
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
	            		
	            		if(letterIndex == 0  ||  letterIndex == 8 || letterIndex == 22 || letterIndex == 25 || letterIndex == 3 || letterIndex == 5 || letterIndex == 11 )
	            		{
	            			currentLetter = " "+beginForm[letterIndex]+" ";
	            			  if(letterIndex!=-1)
	                    		  textString+= " " +beginForm[letterIndex]+" ";
	                    	  else if(letterIndex == -1)
	                    		  textString+=" "+beginForm[25]+" ";
	            			
	            		}
	            		else
	            		{
	            			currentLetter = " "+beginForm[letterIndex];
	            			  if(letterIndex!=-1)
	                    		  textString+= " " +beginForm[letterIndex];
	                    	  else if(letterIndex == -1)
	                    		  textString+=" "+beginForm[25];
	            			//string = string + " "+beginForm[letterIndex];
	            		}
	            		FormLengthArray.add(currentLetter.length());
	            	
	            		//console.log("string"+string.charAt(string.length-1));
	            	}
	            	else
	            	{
	            		isCharCorrect.add(1);
	            		warningButton.setBackgroundColor(Color.RED);
	            		if(letterIndex == 0  ||  letterIndex == 8 || letterIndex == 22 || letterIndex == 25 || letterIndex == 3 || letterIndex == 5 || letterIndex == 11 )
	            		{
	            			currentLetter = " "+beginForm[letterIndex]+" ";
	            			  if(letterIndex!=-1)
	                    		  textString+= " " +beginForm[letterIndex]+" ";
	                    	  else if(letterIndex == -1)
	                    		  textString+=" "+beginForm[25]+" ";
	            			//string = string + " "+beginForm[letterIndex]+" ";
	            		}
	            		else
	            		{
	            			currentLetter = " "+beginForm[letterIndex];
	            			  if(letterIndex!=-1)
	                    		  textString+= " " +beginForm[letterIndex];
	                    	  else if(letterIndex == -1)
	                    		  textString+=" "+beginForm[25];
	            			//string = string + " "+beginForm[letterIndex];
	            		}
	            		Vibrator vibrateWrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	             		 // Vibrate for 500 milliseconds
	             		 vibrateWrong.vibrate(500);
	            		FormLengthArray.add(currentLetter.length());
	            		//console.log("string"+string.charAt(string.length-1));
	            	
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
	          		if((letterIndex == 0 || letterIndex == 3 || letterIndex == 5 || letterIndex == 11 || letterIndex == 8 || letterIndex == 22 || letterIndex==25))
	          		{
	          			currentLetter = middleForm[letterIndex] + " ";
	          			 if(letterIndex!=-1)
	               		  textString+= middleForm[letterIndex]+" ";
	               	  else if(letterIndex == -1)
	               		  textString+=middleForm[25]+" ";
	          		}
	          		else
	          		{
	          			currentLetter = middleForm[letterIndex];
	          			 if(letterIndex!=-1)
	               		  textString+= middleForm[letterIndex];
	               	  else if(letterIndex == -1)
	               		  textString+=middleForm[25];
	          		}
	          	
	          		FormLengthArray.add(currentLetter.length());
	          	}
	          	else
	          	{
	          		isCharCorrect.add(1);
	          		if((letterIndex == 0 || letterIndex == 3 || letterIndex == 5 || letterIndex == 11 || letterIndex == 8 || letterIndex == 22 || letterIndex==25))
	          		{
	          			currentLetter = middleForm[letterIndex] + " ";
	          			 if(letterIndex!=-1)
	               		  textString+= middleForm[letterIndex]+" ";
	               	  else if(letterIndex == -1)
	               		  textString+=middleForm[25]+" ";
	          		}
	          		else
	          		{
	          			currentLetter = middleForm[letterIndex];
	          			 if(letterIndex!=-1)
	               		  textString+= middleForm[letterIndex];
	               	  else if(letterIndex == -1)
	               		  textString+=middleForm[25];
	          		}
	          		warningButton.setBackgroundColor(Color.RED);
	          		Vibrator vibrateWrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	          		 // Vibrate for 500 milliseconds
	          		 vibrateWrong.vibrate(500);
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
		            		  textString+=endForm[25]+" ";
		                 
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
	          			Vibrator vibrateWrong = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	             		 // Vibrate for 500 milliseconds
	             		 vibrateWrong.vibrate(500);
	          			isCharCorrect.add(1);
	          		}
	                  writeText.setText(textString);
	                Log.i("ey","clicked C");
	                
	            }
	        });
	
	       Button btn4 = (Button) findViewById(R.id.Button03);
//	btn1.setBackgroundColor(Color.RED);
//	 btn2.setBackgroundColor(Color.GRAY);
//	 btn3.setBackgroundColor(Color.BLUE);
//	 btn4.setBackgroundColor(Color.CYAN);
//	 cButton1.setBackgroundColor(Color.RED);
//	 cButton2.setBackgroundColor(Color.GRAY);
//	 cButton3.setBackgroundColor(Color.BLUE);
//	 cButton4.setBackgroundColor(Color.CYAN);
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
	                  		  textString+=" "+isolatedForm[25]+" ";
	                	 
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
	                		  textString+=" "+isolatedForm[25]+" ";
	              		
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
			
//			if(isUp)
//			{	
//				matrix.postRotate(degrees, dialerWidth / 2, dialerHeight / 2);
//				dialer.setImageMatrix(matrix);
//				return;
////				float extra = (float) (degrees%13.8461);
////				degrees -= extra;
//			}
			globalAngle += degrees;
			globalAngle %= 360;
			if(globalAngle<0)
				globalAngle = (globalAngle % 360 + 360) % 360;
			
		
			matrix.postRotate(degrees, dialerWidth / 2, dialerHeight / 2);
			
			
			
			letterIndex = (int)Math.floor((globalAngle/(360/26.0)));
			letterIndex = letterIndex%26;
				getImage = stringsImages[letterIndex];
				if(isScale)
		        {
		        	alphaImage.setBackgroundDrawable(getImage);
		        }
		        else
		        {	
		        	alphaImage.setImageDrawable(getImage);
		        }
				//alphaImage.setImageDrawable(getImage);
			
			if(letterIndex == 23)
			{	CLabel.setVisibility(View.VISIBLE);
			//letterCSound.setVisibility(View.VISIBLE);
			}
			else
				CLabel.setVisibility(View.GONE);
			
			//Log.i("Letter","letter : "+letterIndex);
//			if(letterIndex>0)
//				letterIndex = letterIndex-1;
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
		boolean isUp = true;
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
						isUp = false;
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
						if(letterIndex>0)
							letterIndex = (int) Math.floor(globalAngle/13.84);
						startAngle = currentAngle;
						startAngle = (startAngle+360)%360;
						isUp = false;
						break;
						
					case MotionEvent.ACTION_UP:
						
						old = currentAngle %360;
						
						isUp = true;
						
						if(letterIndex==23)
						{

					        CLabel.setVisibility(View.VISIBLE);
					        cButton1.setVisibility(View.VISIBLE);
					        cButton2.setVisibility(View.VISIBLE);
					        cButton3.setVisibility(View.VISIBLE);
					        cButton4.setVisibility(View.VISIBLE);
					        
						}
						else
						{
							CLabel.setVisibility(View.GONE);
					        cButton1.setVisibility(View.GONE);
					        cButton2.setVisibility(View.GONE);
					        cButton3.setVisibility(View.GONE);
					        cButton4.setVisibility(View.GONE);
						}
						float extra = (float) (globalAngle%13.84);
						if(extra < (13.84/2))
							extra = -1*extra;
						else
							extra = (float) (13.84 - extra);
						//Toast.makeText(getApplicationContext(),"Extra"+extra+" "+globalAngle, Toast.LENGTH_SHORT).show();
						//Toast.makeText(getApplicationContext(),"Global"+globalAngle, Toast.LENGTH_SHORT).show();

						rotateDialer((float)extra);
//						int newLetter = (int)Math.floor((old-6.92)/13.84)+1;
//						int currentLetter = 65 + -newLetter;
//						int difference = 65-currentLetter;
//						
//						currentAngle = 13.84*difference%360;
//						matrix.postRotate((float) currentAngle, dialerWidth / 2, dialerHeight / 2);
						//rotateDialer((float) (currentAngle%360));
						
						allowRotating = true;
						break;
				}
				
				// set the touched quadrant to true
				quadrantTouched[getQuadrant(event.getX() - (dialerWidth / 2), dialerHeight - event.getY() - (dialerHeight / 2))] = true;
				
				//detector.onTouchEvent(event);
				
				return true;
			}
		}
		
		/**
		 * Simple implementation of a {@link SimpleOnGestureListener} for detecting a fling event. 
		 */
//		private class MyGestureDetector extends SimpleOnGestureListener {
//			@Override
//			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//				
//				// get the quadrant of the start and the end of the fling
//				int q1 = getQuadrant(e1.getX() - (dialerWidth / 2), dialerHeight - e1.getY() - (dialerHeight / 2));
//				int q2 = getQuadrant(e2.getX() - (dialerWidth / 2), dialerHeight - e2.getY() - (dialerHeight / 2));
//	
//				// the inversed rotations
//				if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math.abs(velocityY))
//						|| (q1 == 3 && q2 == 3)
//						|| (q1 == 1 && q2 == 3)
//						|| (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math.abs(velocityY))
//						|| ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
//						|| ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
//						|| (q1 == 2 && q2 == 4 && quadrantTouched[3])
//						|| (q1 == 4 && q2 == 2 && quadrantTouched[3])) {
//				
//					dialer.post(new FlingRunnable(-1 * (velocityX + velocityY)));
//				} else {
//					// the normal rotation
//					dialer.post(new FlingRunnable(velocityX + velocityY));
//				}
//	
//				return true;
//			}
//		}
//		
//		/**
//		 * A {@link Runnable} for animating the the dialer's fling.
//		 */
//		private class FlingRunnable implements Runnable {
//	
//			private float velocity;
//	
//			public FlingRunnable(float velocity) {
//				this.velocity = velocity;
//			}
//	
//			@Override
//			public void run() {
//				if (Math.abs(velocity) > 5 && allowRotating) {
//					rotateDialer(velocity / 75);
//					velocity /= 1.0666F;
//	
//					// post this instance again
//					dialer.post(this);
//				}
//			}
//		}
	}