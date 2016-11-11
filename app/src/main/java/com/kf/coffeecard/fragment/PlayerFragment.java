package com.kf.coffeecard.fragment;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kf.coffeecard.Card;
import com.kf.coffeecard.GameConstants;
import com.kf.coffeecard.R;
import com.kf.coffeecard.Player;
import com.kf.coffeecard.activity.MainActivity;

import java.util.ArrayList;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //
    private final boolean DBG = true;
    private final boolean VDBG = false;
    private static final String TAG = "PlayerFragment";

    // TODO: Rename and change types of parameters
    private Player mPlayer = null;
    private ArrayList<ImageView> mCardImageViews = null;
    private TextView mText = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static PlayerFragment newInstance(String param1, String param2) {
        PlayerFragment fragment = new PlayerFragment();
        return fragment;
    }

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[onCreate]");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "[onCreateView]");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_player, container, false);
        initView(view);
        setImageView(mPlayer.getID());
        setTextView(mPlayer.getID());
        return view;
    }

    private void initView(View view){
        //imageView
        if(VDBG)Log.d(TAG,"initView: numberOfCards = "+mPlayer.getNumberOfCards());
        mCardImageViews = new ArrayList<ImageView>();
        mCardImageViews.clear();

        String wt = "iv";
        for(int i=0; i<mPlayer.getNumberOfCards(); ++i){
            int ivId = i+1;
            int resID = getResources().getIdentifier(wt + ivId, "id", MainActivity.PACKAGE_NAME);
            ImageView iv = (ImageView) view.findViewById(resID);
            if(iv == null){
                Log.e(TAG,"ImageView is null, resID="+resID);
                continue;
            }
            mCardImageViews.add(iv);
        }
        //textView
        mText =  (TextView)view.findViewById(R.id.player_info_text);
    }

    private void setImageView(int index){
        if(VDBG)Log.d(GameConstants.TAG,"setImageView: "+index);
        String wt = "c";
        Vector<Card> cards = mPlayer.getCards();

        if(index == 0){//player1
            int margin_start = 40;
            int cnt = 0;
            if(DBG)Log.d(GameConstants.TAG,"setImageView: Player1 cardSize = "+cards.size());
            for(Card card: cards){
                int resID = getResources().getIdentifier(wt+card.getId(),"drawable",MainActivity.PACKAGE_NAME);
                if(VDBG)Log.d(GameConstants.TAG,"card Name/card id/resID = "+(wt+card.getId())+"/"+card.getId()+"/"+resID);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                if(DBG)Log.d(GameConstants.TAG,"setImageView: cnt = "+cnt);
                ImageView imageView = mCardImageViews.get(cnt++);
                params.setMarginStart(margin_start);
                params.setMargins(0, 100, 0, 0);
                imageView.setLayoutParams(params);
                imageView.setImageResource(resID);
                margin_start+=55;
            }
        }
        else{//player2~4
            int margin_top = 30;
            int margin_left = 50;
            for(int i=0; i<mPlayer.getNumberOfCards(); ++i){
                mCardImageViews.get(i).setImageResource(R.drawable.back);
                ImageView imageView = mCardImageViews.get(i);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                if(index == 1){
                    //imageView
                    params.setMargins(margin_left, margin_top, 0, 0);//left,top,right,bottom
                    imageView.setRotation(90);
                    margin_top+=55;
                }
                else if(index == 2){
                    params.setMargins(margin_left, margin_top, 0, 0);
                    imageView.setRotation(180);
                    margin_left+=55;
                }
                else if(index == 3){
                    //imageView
                    params.setMargins(150, margin_top, 0, 0);
                    imageView.setRotation(270);
                    margin_top+=56;
                    //text

                }
                imageView.setLayoutParams(params);
            }
        }
    }

    private void setTextView(int index){
        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        if(index == 0){
            textParams.gravity = Gravity.TOP|Gravity.CENTER;
            mText.setLayoutParams(textParams);
        }else{
            textParams = new FrameLayout.LayoutParams(500,300);
            if(index == 1){
                mText.setRotation(90);
                textParams.setMargins(150, 0, 0, 0);//left,top,right,bottom
                textParams.gravity=Gravity.CENTER;
                mText.setGravity(Gravity.CENTER);
            }else if(index == 2){
                textParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                textParams.gravity = Gravity.BOTTOM|Gravity.CENTER;
            }else if(index == 3){
                mText.setRotation(270);
                textParams.setMargins(0, 0, 150, 0);
                textParams.gravity=Gravity.CENTER;
                mText.setGravity(Gravity.CENTER);
            }
            mText.setLayoutParams(textParams);
        }
        updatePlayerInfoToView();
    }

    public void setPlayer(Player player){
        mPlayer = player;
    }

    public void updatePlayerInfoToView(){
        mText.setText(mPlayer.getPlayerInfo());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        Log.d(TAG,"[onActivityCreated] bundle = "+bundle);
        if (bundle != null) {
            Log.d(TAG, "para1 = " + bundle.getString("kimmy"));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"[onAttach]");
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void updatePlayerInfoDone(String info);
    }

}
