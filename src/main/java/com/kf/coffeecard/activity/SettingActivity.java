package com.kf.coffeecard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.os.Message;
import android.content.Intent;
import android.widget.EditText;
import com.kf.coffeecard.GameProxyService;
import com.kf.coffeecard.R;
import com.kf.coffeecard.ServiceManager;
import com.kf.coffeecard.Game.GameType;
import com.kf.coffeecard.Game;


public class SettingActivity extends Activity {

    final static String TAG = "SettingActivity";
    private GameType mGameType;
    private int mNumPlays;
    private String mName;
    private ServiceManager mGameProxyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initSpinner();

        mGameProxyService = new ServiceManager(this, GameProxyService.class, null , new Handler() {
            @Override
            public void handleMessage(Message msg) {

            }
        });

        Button button = (Button) findViewById(R.id.setting_ok_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mNumPlays > 0) {
                    Log.d(TAG, "start the game");
                    mName = getEditText();

                    Intent intent = new Intent(getBaseContext(), GameProxyService.class);
                    intent.putExtra("name", mName);
                    intent.putExtra("gametype", mGameType);
                    intent.putExtra("players", mNumPlays);
                    mGameProxyService.start(intent);

                    Intent intent2 = new Intent (getApplicationContext(), BridgeGameActivity.class);
                    startActivity(intent2);
                }

            }
        });
    }

    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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

    private String getEditText() {
        Log.d(TAG, "getEditText");
        EditText text = (EditText) findViewById(R.id.editText_name);
        return text.getText().toString();
    }

    private void initSpinner() {
        Spinner SpnNumPlayer = (Spinner) findViewById(R.id.num_player_spinner);
        ArrayAdapter<CharSequence> np_adapter = ArrayAdapter.createFromResource(this, R.array.num_players_list,
                android.R.layout.simple_spinner_item);
        np_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpnNumPlayer.setAdapter(np_adapter);
        SpnNumPlayer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                mNumPlays = Integer.valueOf(parent.getItemAtPosition(position).toString());
                Log.d(TAG, "Number of Playser = " + mNumPlays);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner SpnGameType = (Spinner) findViewById(R.id.game_type_spinner);
        ArrayAdapter<CharSequence> gt_adapter = ArrayAdapter.createFromResource(this, R.array.game_type_list,
                android.R.layout.simple_spinner_item);
        gt_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpnGameType.setAdapter(gt_adapter);
        SpnGameType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (Game.isVaild(position)) {
                    if (position == 0) return;
                    mGameType = GameType.values()[position - 1];
                } else {
                    Log.wtf(TAG, "Invaild Game Type index");
                }
                Log.d(TAG, "Game Type = " + mGameType + " index = " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mGameProxyService.stop();
        } catch (Throwable t) {
            Log.e("MainActivity", "Failed to unbind from the service", t);
        }

    }
}
