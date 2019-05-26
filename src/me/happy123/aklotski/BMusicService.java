package me.happy123.aklotski;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BMusicService extends Service {

	private MediaPlayer player;  
	private final IBinder mBinder = new BMusicBinder(); 
	
    //为了和Activity交互，我们需要定义一个Binder对象  
    class BMusicBinder extends Binder{  
          
        //返回Service对象  
        BMusicService getService(){  
            return BMusicService.this;  
        }  
    }  	

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("BMusicService", "onBind executed");
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	    player = MediaPlayer.create(this, R.raw.bgm1); 
	    player.setLooping(true);
		Log.d("MyService", "onCreate executed");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("BMusicService", "onStartCommand executed");
		new Thread(new Runnable() {  
	        @Override  
	        public void run() {  
	            if(!player.isPlaying()){  
	                player.start();  
	            }  
	        }  
	    }).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
        if(player.isPlaying()){  
            player.stop();  
        }  
        player.release(); 		
		super.onDestroy();
		Log.d("BMusicService", "onDestroy executed");
	}

}
