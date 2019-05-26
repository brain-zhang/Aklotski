package me.happy123.aklotski;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LevelsActivity extends Activity implements OnClickListener {
	
	private ArrayList<Button> buttons = new ArrayList<Button>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        
        this.buttons.add((Button)findViewById(R.id.level0_button));
        this.buttons.add((Button)findViewById(R.id.level1_button));
        this.buttons.add((Button)findViewById(R.id.level2_button));
        this.buttons.add((Button)findViewById(R.id.level3_button));
        this.buttons.add((Button)findViewById(R.id.level4_button));
        this.buttons.add((Button)findViewById(R.id.level5_button));
        this.buttons.add((Button)findViewById(R.id.level6_button));
        this.buttons.add((Button)findViewById(R.id.level7_button));
        
        for(int i=0; i<this.buttons.size(); i++) {
        	buttons.get(i).setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onClick(View v) {
    	int level = 7;
    	switch (v.getId()) {
    	case R.id.level0_button:
    		level = 0;
    		break;    	
    	case R.id.level1_button:
    		level = 1;
    		break;
    	case R.id.level2_button:
    		level = 2;
    		break;
    	case R.id.level3_button:
    		level = 3;
    		break;
    	case R.id.level4_button:
    		level = 4;
    		break;
    	case R.id.level5_button:
    		level = 5;
    		break;
    	case R.id.level6_button:
    		level = 6;
    		break;
    	case R.id.level7_button:
    		level = 7;
    		break;  
    	default:
    		break;
    	}
		Intent intent = new Intent(LevelsActivity.this, WarsActivity.class);
		intent.putExtra("level", level);
		startActivity(intent);    	
    }
    
}
