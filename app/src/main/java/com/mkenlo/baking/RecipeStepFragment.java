package com.mkenlo.baking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mkenlo.baking.model.RecipeSteps;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepFragment extends Fragment {

    public static String ARG_STEP_ITEM = "recipe step object";

    private RecipeSteps mStep;
    private OnFragmentInteractionListener mListener;
    private SimpleExoPlayer mExoPlayer;
    private boolean mPlayWhenReady = false;
    private long mPlayBackPosition = 0;
    private int mCurrentWindow = 0;
    @BindView(R.id.tv_step_id) TextView mStepIdTextView;
    @BindView(R.id.tv_step_desc) TextView mStepDescTextView;
    @BindView(R.id.bt_next_step) Button mNextStepButton;
    @BindView(R.id.step_video_view) PlayerView mStepPlayerView;


    public RecipeStepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(ARG_STEP_ITEM);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
            mStepIdTextView.setText("Step #" + mStep.getID());
            mStepDescTextView.setText(mStep.getDescription());
            mNextStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onButtonNextStepSelected(mStep.getID()+1);
                }
            });
        }


        initializePlayer(Uri.parse(mStep.getVideoURL()));

        return rootView;
    }


    private void initializePlayer(Uri videoUri){

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

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
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
    private MediaSource buildMediaSource(Uri videoUri){
        //Need to test if the URL is null or not.
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
       void onButtonNextStepSelected(long nextPosition);
    }


}
