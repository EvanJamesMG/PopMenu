package com.example.evanjames.popmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by EvanJames on 2016/1/4.
 */
public class PopMenuDialog {

    private final ImageView quitbtn;
    private Dialog mDialog;

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

    public PopMenuDialog(Context context) {


        mDialog = new Dialog(context, R.style.dialog);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha = 0.9f;//设置透明度
        window.setAttributes(wl);
        mDialog.setContentView(R.layout.popmenu);


        menuview = new ArrayList<ImageView>();
        menuparent = (RelativeLayout)mDialog.findViewById(R.id.menuparent);
        Gmail = (ImageView)mDialog.findViewById(R.id.Gmail);
        menuview.add(Gmail);
        twitter = (ImageView)mDialog.findViewById(R.id.twitter);
        menuview.add(twitter);
        linkedin = (ImageView)mDialog.findViewById(R.id.Linkedin);
        menuview.add(linkedin);
        line = (ImageView)mDialog.findViewById(R.id.Line);
        menuview.add(line);
        music = (ImageView)mDialog.findViewById(R.id.music);
        menuview.add(music);
        guardian = (ImageView)mDialog.findViewById(R.id.Guardian);
        menuview.add(guardian);

        quitbtn = (ImageView)mDialog.findViewById(R.id.SVGbtn);

        AnimatorSet visAnimSet = initAnim_open(menuparent);
        visAnimSet.start();

        quitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitAnimate_start();
                AnimatorSet visAnimSet = initAnim_close(menuparent);
                visAnimSet.start();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mDialog.dismiss();
                        quitbtn.setRotation(0);
                    }
                }, 500);
            }
        });

    }
    private void quitAnimate_start(){
        quitbtn.setRotation(0);
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(quitbtn, "rotation", 0,45);
        visToInvis.setDuration(200);
        visToInvis.setInterpolator(new AccelerateDecelerateInterpolator());
        visToInvis.start();
    }

//    private void animate() {
//        Drawable drawable = quitbtn.getDrawable();
//        if(drawable instanceof Animatable){
//            ((Animatable) drawable).start();
//        }
//    }

    private AnimatorSet initAnim_open(ViewGroup parent) {
        ArrayList<Animator> visAnimList = new ArrayList<Animator>();
        AnimatorSet visAnimSet = new AnimatorSet();


        WindowManager wm =mDialog.getWindow().getWindowManager();


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
    private AnimatorSet initAnim_close(ViewGroup parent) {
        ArrayList<Animator> visAnimList = new ArrayList<Animator>();
        AnimatorSet visAnimSet = new AnimatorSet();


        WindowManager wm =mDialog.getWindow().getWindowManager();


        childnum = 6;
        screenheight = wm.getDefaultDisplay().getHeight();
        per_duration=MAX_DURATION/childnum/2;
        per_width = parent.getWidth()/childnum/2/2;

        for (int i = 0; i < childnum; i++) {
            ObjectAnimator itemAnim = createItemInvisAnim(menuview.get(i), i);
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
        target.setPivotX(per_width * index);
        target.setPivotY(target.getHeight() / 2);
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(target, "translationY", target.getHeight() / 2,screenheight * 1.2f);
        visToInvis.setDuration(per_duration + per_duration * (childnum - index));
        visToInvis.setInterpolator(new AccelerateInterpolator(2f));

        return visToInvis;
    }

    public void show() {
        mDialog.show();
    }
}
