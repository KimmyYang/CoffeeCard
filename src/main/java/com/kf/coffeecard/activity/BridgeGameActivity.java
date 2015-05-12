package com.kf.coffeecard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.kf.coffeecard.GameProxyService;
import com.kf.coffeecard.R;
import com.kf.coffeecard.ServiceManager;
import android.os.Handler;
import android.os.Message;

public class BridgeGameActivity extends Activity {

    final int MAX_CARD_PER_PLAYER = 3;//fake to test
    private ImageView[] mImageViews;
    private ServiceManager mGameProxyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_game);
        initImageView();

        mGameProxyService = new ServiceManager(this , GameProxyService.class , new Handler(){
            @Override
            public void handleMessage(Message msg){

            }
        });
    }
    private void initImageView(){
        mImageViews[1] = (ImageView) findViewById(R.id.iv1);
        mImageViews[2] = (ImageView) findViewById(R.id.iv2);
        mImageViews[3] = (ImageView) findViewById(R.id.iv3);
    }
    private void setImage(){
        mImageViews[1].setImageResource(R.drawable.c1);
        mImageViews[2].setImageResource(R.drawable.c2);
        mImageViews[3].setImageResource(R.drawable.c3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bridge_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
