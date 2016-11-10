package com.kf.coffeecard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Handler;
import android.os.Message;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.view.Gravity;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kf.coffeecard.BridgeGame;
import com.kf.coffeecard.ContractInfo;
import com.kf.coffeecard.Game;
import com.kf.coffeecard.GameConstants;
import com.kf.coffeecard.R;
import com.kf.coffeecard.fragment.PlayerFragment;
import com.kf.coffeecard.service.BridgeGameService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;


public class BridgeGameActivity extends Activity implements PlayerFragment.OnFragmentInteractionListener{

    final boolean DBG = true;
    final boolean VDBG = false;
    final String TAG = "BridgeGameActivity";
    final int DISPALY_GAME_TIME_DELAY = 1000;

    private ImageView mSplashIv;
    private ArrayList<PlayerFragment> mPlayerFragList = null;
    //use in call king dialog
    private Spinner mContractTrickSpinner = null;
    private Spinner mContractSuitSpinner = null;
    private Button mGameButton = null;
    private TextView mGameText = null;

    private Handler mHandler = new ClientHandler();
    private Game mGame = null;
    private Messenger mClient = null;
    private Messenger mService = null;
    private ContractInfo mMainPlayerContractInfo = new ContractInfo();

    @Override
    public void updatePlayerInfoDone(String info) {

    }

    private class ClientHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            Log.d(TAG,"handleMessage msg = "+msg.what);
            switch (msg.what){
                case GameConstants.EVENT_SERVICE_REGISTER_DONE:
                    initGame();
                    mHandler.sendMessageDelayed(obtainMessage(GameConstants.EVENT_CLIENT_DISPLAY_GAME), DISPALY_GAME_TIME_DELAY);
                    break;
                case GameConstants.EVENT_CLIENT_DISPLAY_GAME:
                    displayGame();
                    break;
                case GameConstants.EVENT_CLIENT_DISPLAY_GAME_DONE:
                    startBidContract();
                    break;
                case GameConstants.EVENT_SERVICE_BID_CONTRACT_DONE:
                    updatePlayerInfo();
                    break;
                default:
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mClient = new Messenger(mHandler);
            Message msg = Message.obtain();
            msg.what = GameConstants.EVENT_SERVICE_REGISTER;
            msg.replyTo = mClient;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_game_splash);
        Log.d(TAG,"onCreate");
        //init game, rule and player
        createGame(getIntent().getExtras());
        Intent intent = new Intent(this, BridgeGameService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void createGame(Bundle bundle){
        mGame = BridgeGame.createGame(bundle);
    }

    //arrange, deal card
    private void initGame(){
        sendMessage(GameConstants.EVENT_SERVICE_INIT_GAME);
    }

    //display card view
    private void displayGame(){
        Log.d(TAG, "displayGame");
        setContentView(R.layout.activity_bridge_game);
        setCardImageView();//init cards imageView
        mHandler.sendEmptyMessageDelayed(GameConstants.EVENT_CLIENT_DISPLAY_GAME_DONE, 1000);
    }

    private void updatePlayerInfo(){
        for(PlayerFragment fragment:mPlayerFragList){
            fragment.updatePlayerInfoToView();
        }
        if(((BridgeGame)mGame).getContractInfo().isPass){
            mGameButton.setText(R.string.start_button);//start game
        }else{
            mGameButton.setText(R.string.bid_contract);//bid contract
        }
        mGameText.setText(((BridgeGame)mGame).getContract());
    }

    //show start button and starting the game
    private void startBidContract(){
        //init game button
        mGameButton = new Button(this);
        mGameButton.setText(R.string.bid_contract);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.bridge_game_relative_layout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(350,150);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mGameButton.setLayoutParams(params);
        relativeLayout.addView(mGameButton);//button
        mGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DBG)Log.d(GameConstants.TAG,"mGameButton.getText() = "+mGameButton.getText());
                if (mGameButton.getText().equals(R.string.bid_contract)) {
                    showContractDialog();
                } else if (mGameButton.getText().equals(R.string.start_button)) {
                    //start game
                }
            }
        });
        //init game text
        mGameText = new TextView(this);
        params = new RelativeLayout.LayoutParams(400,150);
        mGameText.setLayoutParams(params);
        relativeLayout.addView(mGameText);//text
    }

    private void showContractDialog(){
        Log.d(GameConstants.TAG, "showContractDialog");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.bid_contract_dialog, null);
        updateContractSpinner(view);//update spinner
        AlertDialog.Builder contractDialog = new AlertDialog.Builder(this);
        contractDialog.setView(view);
        contractDialog.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (DBG) Log.d(GameConstants.TAG, "YES");
                Bundle bundle = new Bundle();
                bundle.putInt(GameConstants.CONTRACT_SUIT, mMainPlayerContractInfo.Suit);
                bundle.putInt(GameConstants.CONTRACT_TRICK, mMainPlayerContractInfo.Trick);
                sendMessage(GameConstants.EVENT_SERVICE_BID_CONTRACT, (Object) bundle);
            }
        });
        contractDialog.setNeutralButton(R.string.no_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //nothing to do
            }
        });
        //pass
        contractDialog.setNegativeButton(R.string.pass_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (VDBG) Log.d(GameConstants.TAG, "PASS");
                Bundle bundle = new Bundle();
                bundle.putInt(GameConstants.CONTRACT_SUIT,0);
                bundle.putInt(GameConstants.CONTRACT_TRICK,0);
                sendMessage(GameConstants.EVENT_SERVICE_BID_CONTRACT,(Object)bundle);
            }
        });
        contractDialog.show();
    }

    private void updateContractSpinner(View view){
        Log.d(GameConstants.TAG,"updateContractSpinner");
        mContractTrickSpinner = (Spinner)view.findViewById(R.id.contract_trick_spinner);
        mContractSuitSpinner = (Spinner)view.findViewById(R.id.contract_suit_spinner);
        mContractTrickSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ((BridgeGame) mGame).getContractTrickList()));
        mContractSuitSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ((BridgeGame) mGame).getContractSuitList()));

        //suit
        mContractSuitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(GameConstants.TAG, "mContractSuitSpinner: position = " + position);
                mMainPlayerContractInfo.Suit = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //trick
        mContractTrickSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(GameConstants.TAG, "mContractTrickSpinner: position = " + position);
                //position start from 0 .. so need to increase one
                mMainPlayerContractInfo.Trick = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setCardImageView(){
        mPlayerFragList = new ArrayList<PlayerFragment>();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for(int i=0; i< mGame.getNumberOfPlayer(); ++i){
            PlayerFragment frag = new PlayerFragment();
            frag.setPlayer(mGame.getPlayer(i));
            //find framelayout
            int resId = getResources().getIdentifier("fragment_container_"+(i+1), "id", getPackageName());
            Log.d(TAG,"initCardImageView: resId = "+resId);
            fragmentTransaction.add(resId, frag);
            mPlayerFragList.add(frag);
            Log.d(TAG,"[initCardImageView] init player "+i+" done");
        }
        fragmentTransaction.commit();
    }

    protected void onStart(){
        super.onStart();
        Log.d(TAG,"onStart");
        mSplashIv = (ImageView) findViewById(R.id.iv_bridge_game_splash);
        mSplashIv.setImageResource(R.drawable.bridge_game_splash);
    }

    protected void onResume(){
        super.onResume();
        Log.d(TAG,"onResume");
    }

    private void sendMessage(int what){
        Message msg = Message.obtain();
        msg.what = what;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int what, Object obj){
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int what, int arg1, int arg2, Object obj){
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
