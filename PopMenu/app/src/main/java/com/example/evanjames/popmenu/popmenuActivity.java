package com.example.evanjames.popmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class popmenuActivity extends Activity {

    private static final int MAX_DURATION = 1200;
    private RelativeLayout menuparent;
    private int per_duration;
    private int per_width;
    private int screenheight;
    private int childnum;
    private ImageView Gmail;
    private ImageView twitter;
    private ImageView linkedin;
    private ImageView line;
    private ImageView music;
    private ImageView guardian;
    private ArrayList<ImageView> menuview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setEnterTransition(new Fade().setDuration(100));
//        getWindow().setExitTransition(new Fade().setDuration(100));
        setContentView(R.layout.popmenu);


        menuview = new ArrayList<ImageView>();
        menuparent = (RelativeLayout)findViewById(R.id.menuparent);
        Gmail = (ImageView)findViewById(R.id.Gmail);
        menuview.add(Gmail);
        twitter = (ImageView)findViewById(R.id.twitter);
        menuview.add(twitter);
        linkedin = (ImageView)findViewById(R.id.Linkedin);
        menuview.add(linkedin);
        line = (ImageView)findViewById(R.id.Line);
        menuview.add(line);
        music = (ImageView)findViewById(R.id.music);
        menuview.add(music);
        guardian = (ImageView)findViewById(R.id.Guardian);
        menuview.add(guardian);

        AnimatorSet visAnimSet = initAnim_open(menuparent);
        visAnimSet.start();

    }

    private AnimatorSet initAnim_open(ViewGroup parent) {
        ArrayList<Animator> visAnimList = new ArrayList<Animator>();
        AnimatorSet visAnimSet = new AnimatorSet();


        WindowManager wm = this.getWindowManager();


        childnum = 6;
        screenheight = wm.getDefaultDisplay().getHeight();
        per_duration=MAX_DURATION/childnum/2;
        per_width = parent.getWidth()/childnum/2/2;

        for (int i = 0; i < childnum; i++) {
            ObjectAnimator itemAnim = createItemVisAnim(menuview.get(i), i);
            visAnimList.add(itemAnim);
        }
        visAnimSet.playTogether(visAnimList);
        return visAnimSet;
    }

    //显示动画
    private ObjectAnimator createItemVisAnim(final View target, int index) {

        ObjectAnimator invisToVis = null;
        target.setPivotX(per_width*index);
        target.setPivotY(screenheight * 1.2f);
        invisToVis = ObjectAnimator.ofFloat(target, "translationY", screenheight * 1.2f,target.getHeight() / 2);
        invisToVis.setDuration(MAX_DURATION - per_duration * (childnum-1-index));
        invisToVis.setInterpolator(new AnticipateOvershootInterpolator(1f));
        invisToVis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setVisibility(View.VISIBLE);
            }
        });

        return invisToVis;
    }

    //隐藏动画
    private ObjectAnimator createItemInvisAnim(final View target, int index) {
        target.setPivotX(per_width*index);
        target.setPivotY(target.getHeight() / 2);
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(target, "translationY", target.getHeight() / 2,screenheight * 1.2f);
        visToInvis.setDuration(per_duration + per_duration * (childnum-index));
        return visToInvis;
    }


}
