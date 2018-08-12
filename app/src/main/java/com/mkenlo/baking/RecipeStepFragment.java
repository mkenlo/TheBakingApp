package com.mkenlo.baking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mkenlo.baking.db.model.Steps;
import com.mkenlo.baking.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepFragment extends Fragment {


    public static String STATE_PLAYBACK_POSITION = "playback_position";
    private Steps mStep;
    private boolean mIsLastStep;
    private OnFragmentInteractionListener mListener;
    private SimpleExoPlayer mExoPlayer;
    private boolean mPlayWhenReady = true;
    private long mPlayBackPosition = 0;
    private int mCurrentWindow = 0;

    @BindView(R.id.step_video_view) PlayerView mStepPlayerView;


    public RecipeStepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(Constants.KEY_ITEM_STEP);
            mIsLastStep = getArguments().getBoolean(Constants.KEY_ITEM_LAST_STEP);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if(mStep.getVideoURL()!=null)
                initializePlayer(Uri.parse(mStep.getVideoURL()));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null) && mStep.getVideoURL()!=null) {
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        }
        hideSystemUi();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);

        if(mStep!=null){

            /* This layout only exists in Portrait Mode because the video takes up fullscreen
                on Landscape Mode on Phone
            */
            if(rootView.findViewById(R.id.layout_step_detail)!=null){

                ((TextView)rootView.findViewById(R.id.tv_step_id)).setText("Step #" + mStep.getId());
                ((TextView)rootView.findViewById(R.id.tv_step_desc)).setText(mStep.getDescription());

                Button mNextStepButton = rootView.findViewById(R.id.bt_next_step);
                mNextStepButton.setEnabled(!mIsLastStep);
                if(mIsLastStep){
                    mNextStepButton.setBackgroundResource(R.color.colorPrimaryLight);
                    mNextStepButton.setText("No more steps");
                }
                else{
                    mNextStepButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onButtonNextStepClicked(mStep.getId()+1);
                        }
                    });}
            }


        }

        return rootView;
    }

    private void initializePlayer(Uri videoUri){

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                new DefaultTrackSelector());

        mStepPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        mExoPlayer.seekTo(mPlayBackPosition);
        mExoPlayer.prepare(buildMediaSource(videoUri));
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayBackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mStepPlayerView.setPlayer(null);
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private MediaSource buildMediaSource(Uri videoUri){

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(Util.getUserAgent(getContext(), "TheBakingApp"))).
            createMediaSource(videoUri);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
       void onButtonNextStepClicked(int nextPosition);
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mStepPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null)
            mPlayBackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(STATE_PLAYBACK_POSITION, mExoPlayer.getCurrentPosition());
        super.onSaveInstanceState(outState);


    }
}
