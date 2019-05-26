package me.happy123.aklotski;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WarsActivity extends Activity implements OnClickListener {
	
	private int levelId = 1;
	private String levelName;
	private DataBaseHelper databaseHelper;
	private ListView warsListView;
	private TextView levelNameView;
	private ArrayList<WarRecord> wars = new ArrayList<WarRecord>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wars);
        
        databaseHelper = DataBaseHelper.getInstance(this);
        
        //get context from prev active
        Intent intent = getIntent();
        this.levelId = intent.getIntExtra("level", 1);
        int levelNameResId = this.getResources().getIdentifier("level"+this.levelId, "string", this.getPackageName());
        this.levelName = getResources().getString(levelNameResId);
        
        //set wars_level_name TextView name
        this.levelNameView = (TextView)findViewById(R.id.wars_text_level); 
        this.levelNameView.setText(this.levelName);
        
        //get more info of wars
        this.wars = databaseHelper.getWars(this.levelId);
        
        WarAdapter adapter = new WarAdapter(WarsActivity.this, R.layout.listview_waritem, this.wars);
        this.warsListView = (ListView) findViewById(R.id.wars_listview);
        this.warsListView.setAdapter(adapter);
        
		this.warsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				WarRecord war = wars.get(position);
				
				Intent intent = new Intent(WarsActivity.this, ChessBoardActivity.class);
				intent.putExtra("WARID", war.getId());
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
    public void onClick(View v) {
    	
    }
    
}
