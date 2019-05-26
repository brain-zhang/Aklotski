package me.happy123.aklotski;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button startButton, levelsButton, helpButton;
	private DataBaseHelper databaseHelper;
    
    private long waitTime = 2000, touchTime = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        databaseHelper = DataBaseHelper.getInstance(this);
        
        startButton = (Button)findViewById(R.id.main_start_button);
        levelsButton = (Button)findViewById(R.id.main_levels_button);
        helpButton = (Button)findViewById(R.id.main_help_button);
        
        startButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		WarRecord war = databaseHelper.getWar();                
        		Intent intent = new Intent(MainActivity.this, ChessBoardActivity.class);
        		intent.putExtra("WARID", war.getId());
        		startActivity(intent);
        	}
        });
        
        levelsButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent(MainActivity.this, LevelsActivity.class);
        		startActivity(intent);
        	}
        });
        
        helpButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        		startActivity(intent);
        	}
        });        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if((currentTime-touchTime)>=waitTime) {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		}else {
			finish();
		}
	}    
    
}
