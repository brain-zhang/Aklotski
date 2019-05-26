package me.happy123.aklotski;

import java.util.ArrayList;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import me.happy123.aklotski.R;
import me.happy123.aklotski.DataBaseHelper;
import me.happy123.klotski.ChessBoard;
import me.happy123.klotski.ChessControl;
import me.happy123.klotski.ChessPiece;
import me.happy123.klotski.Position;

public class ChessBoardActivity extends Activity  implements OnClickListener {
	
	//set database handler
	private DataBaseHelper databaseHelper;
	
	//set core chess control
	private int warId;
	private ChessBoard board;
	private ChessControl chessControl;
	
	//set layout unit position
	private double chessSizeUnit = 72.0;
	private int windowWidth = 320, windowHeight = 480;
	private RelativeLayout layoutChessBoard;
	private LinearLayout layoutUpper, layoutDowner, layoutBody, layoutBottom;
	private RelativeLayout layoutHeader;
	
	//set bmusic control button
    private Button bmusicButton;
    private boolean chessAudioPlayed = true, bmusicPlayed = true;
		
	//set background music
	private BMusicService bmusicService;
    private ServiceConnection conn = new ServiceConnection() {  
          
        @Override  
        public void onServiceDisconnected(ComponentName name) {  
            // TODO Auto-generated method stub  
        	bmusicService = null;  
        }  
          
        @Override  
        public void onServiceConnected(ComponentName name, IBinder binder) {  
            //这里我们实例化audioService,通过binder来实现  
        	bmusicService = ((BMusicService.BMusicBinder)binder).getService();  
              
        }  
    }; 
		
	//set play sound effect
	private MediaPlayer moveMediaPlayer, goodjobMediaPlayer;
	
	/*
	 * create a new chess game view
	 */
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chessboard);
        
        this.databaseHelper = DataBaseHelper.getInstance(this);
        
    	int resID = getResources().getIdentifier("move", "raw", getPackageName());
    	moveMediaPlayer = MediaPlayer.create(this, resID);
    	resID = getResources().getIdentifier("good", "raw", getPackageName());
    	goodjobMediaPlayer = MediaPlayer.create(this, resID);
        
        layoutUpper = (LinearLayout)findViewById(R.id.upper);
        layoutDowner = (LinearLayout)findViewById(R.id.downer);
        layoutHeader = (RelativeLayout)findViewById(R.id.header);
        layoutBody = (LinearLayout)findViewById(R.id.body);
        layoutBottom = (LinearLayout)findViewById(R.id.bottom);
        
        bmusicButton = (Button)findViewById(R.id.btn_backgruound_muisc);
        bmusicButton.setOnClickListener((OnClickListener) this);
        
        bmusicPlayed = this.databaseHelper.getBMusic();
        
        //get default single chess size
        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        try {
        	Point size = new Point();
        	display.getSize(size);
        	windowWidth = size.x;
        	windowHeight = size.y;
        } catch (NoSuchMethodError e) {
        	windowWidth = display.getWidth();
        	windowHeight = display.getHeight();
        }
        chessSizeUnit = windowWidth*0.925/4;
        
        /* if phone screen width:height is not 320X480, resize layout
         * android's layout is very  strange, when you resize parent layout, his children views will not auto scale
         * see:http://stackoverflow.com/questions/5852758/views-inside-a-custom-viewgroup-not-rendering-after-a-size-change
         *     http://stackoverflow.com/questions/6262265/how-to-set-size-and-layout-in-onsizechanged
         */
        if(windowHeight > (int)(windowWidth * 480.0 / 320.0)) {
        	//set upper height      	
        	LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)layoutUpper.getLayoutParams();
        	params.height = (int)(windowWidth * 480.0 / 320.0); 
        	
        	//set downer position and height
        	params = new LinearLayout.LayoutParams(windowWidth, windowHeight - (int)(windowWidth * 480.0 / 320.0));
        	params.setMargins(0, (int)(windowWidth * 480.0 / 320.0), 0, 0);
        	layoutDowner.setLayoutParams(params);
        	
        	//set header position and height
        	params = new LinearLayout.LayoutParams(windowWidth, (int)(windowWidth * 480.0 / 320.0 * 65.0 / 480.0));
        	params.setMargins(0, 0, 0, 0);
        	layoutHeader.setLayoutParams(params);
        	
        	//set body  position and height
        	params = new LinearLayout.LayoutParams(windowWidth, (int)(windowWidth * 480.0 / 320.0 * 370.0 / 480.0));
        	params.setMargins(0, 0, 0, 0);
        	layoutBody.setLayoutParams(params);
        	
        	//set body  position and height
        	params = new LinearLayout.LayoutParams(windowWidth, (int)(windowWidth * 480.0 / 320.0 * 45.0 / 480.0));
        	params.setMargins(0, 0, 0, 0);
        	layoutBottom.setLayoutParams(params);
        	
        } else {
        	layoutUpper.setVisibility(View.GONE);
        }
        
        //OK,let's begin chess game
        Intent intent = getIntent();
        this.warId = intent.getIntExtra("WARID", 13);
        String layout = DataBaseHelper.getInstance(this).getWar(this.warId).getLayout();
        ArrayList<ChessPiece> allChessPieces = WarRecord.convertToChessLayout(layout);
        layoutChessBoard = (RelativeLayout) findViewById(R.id.chessboard_body);
		this.board = new ChessBoard(4 ,5);
		this.chessControl = new ChessControl(board);
        
        for(int i=0; i< allChessPieces.size(); i++) {
        	ChessPiece piece = allChessPieces.get(i);
        	this.chessControl.addPiece(piece);
	        ImageView imageView = createImageView(layoutChessBoard.getContext(),
	        									   piece.getPosition().getPosx(),
	        									   piece.getPosition().getPosy(),
	        									   piece.getWidth(),
	        									   piece.getHeight(),
	        									   piece.getId());
	        imageView.setOnClickListener((OnClickListener) this);
	        layoutChessBoard.addView(imageView);
        }
        
        //redraw chess pieces
        layoutChessBoard.requestLayout();
    }
    
    @Override
    public void onPause() {
        Intent intent = new Intent();  
        intent.setClass(this, BMusicService.class);          
        stopService(intent);
        databaseHelper.setBMusic(this.bmusicPlayed);
        super.onPause();
    }
    
    @Override
    public void onStart() {
    	autoPlayBMusic();
    	super.onStart();
    }
        
    /*
     * relayout when chess piece is clicked
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();  
        	
        if(id == R.id.btn_backgruound_muisc){  
        	bmusicPlayed = !bmusicPlayed;
        	autoPlayBMusic();           		
        } else {
	    	Position pos = this.chessControl.movePiece(v.getTag().toString());
	    	if(pos != null) {
	            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(v.getWidth(), v.getHeight());
	            
	            params.setMargins((int)(pos.getPosx()*chessSizeUnit),
	            		          (int)(pos.getPosy()*chessSizeUnit), 0, 0);
	            v.setLayoutParams(params);
	            if(bmusicPlayed) {
	            	moveMediaPlayer.start();
	            }
	            
	        	if(v.getTag() == "cao" && pos.getPosx() == 1 && pos.getPosy() == 3) {
	        		databaseHelper.updateWarStatus(warId, 1);
	        		if(bmusicPlayed) {
	        			goodjobMediaPlayer.start();
	        		}
	        		LevelupDialog cdd = new LevelupDialog(ChessBoardActivity.this);
	        		cdd.show();
	        	}
	    	}
        }
    }
    
    public void autoPlayBMusic() {
        Intent intent = new Intent();  
        intent.setClass(this, BMusicService.class);
    	
    	if(bmusicPlayed) {
    		startService(intent);
    		Toast.makeText(this, "背景音乐开", Toast.LENGTH_SHORT).show();
    		changeBackground(bmusicButton, R.drawable.button_bmusic_enable);
    	} else {
    		stopService(intent);
    		Toast.makeText(this, "背景音乐关", Toast.LENGTH_SHORT).show();
    		changeBackground(bmusicButton, R.drawable.button_bmusic_disable);
    	} 	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /*
     * utility function, create imageview for different chess type
     */
    public ImageView createImageView(Context context, int x, int y, int w, int h, String drawName) {
        ImageView img = new ImageView (this);
        img.setVisibility(View.VISIBLE);
        int drawId = this.getResources().getIdentifier(drawName, "drawable", this.getPackageName());
        Drawable drawable = getResources().getDrawable(drawId);
        img.setImageDrawable(drawable);
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(w*chessSizeUnit),
        		                                                             (int)(h*chessSizeUnit));
        
        params.setMargins((int)(x*chessSizeUnit), (int)(y*chessSizeUnit), 0, 0);
        img.setLayoutParams(params);
        img.setTag(drawName);
        
        return img;
    }   
    
    @SuppressLint("NewApi") public void reload() {
	    Intent intent = getIntent();
	    intent.putExtra("WARID", DataBaseHelper.getInstance(this).getNextWar(this.warId).getId());
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();
	    overridePendingTransition(0, 0);

	    startActivity(intent);
	    overridePendingTransition(0, 0);
    }
    
    @SuppressLint("NewApi") public void changeBackground(android.widget.Button button, int resId) {
    	int sdk = android.os.Build.VERSION.SDK_INT;
    	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
    	    button.setBackgroundDrawable( getResources().getDrawable(resId) );
    	} else {
    	    button.setBackground( getResources().getDrawable(resId));
    	}    	
    }
}
