package com.example.smartcomplaint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Rakshith on 1/14/2016.
 */
public class AnimationActiviy  extends AppCompatActivity implements View.OnClickListener {
    LinearLayout topView, belowView;
    TextView foo, bar;
    int viewHeight;
    boolean noSwap = true;
    private static int ANIMATION_DURATION = 3000;

    ImageView imageView1,imageView2;
    int x1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animan_layout);

        topView = (LinearLayout) findViewById(R.id.top_view);
        belowView = (LinearLayout) findViewById(R.id.below_view);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        foo = (TextView) findViewById(R.id.foo);
        bar = (TextView) findViewById(R.id.bar);

        ImageButton btnSwitch = (ImageButton) findViewById(R.id.switch_btn);

        ViewTreeObserver viewTreeObserver = foo.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    foo.getViewTreeObserver().addOnGlobalLayoutListener(this);
                    viewHeight = foo.getHeight();
                    foo.getLayoutParams();


                     x1=imageView2.getLeft()-imageView1.getLeft();



                }
            });
        }

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (noSwap) {
                    //Log.d("Swap Status", "Not Swapping yet ? " + true);

                    TranslateAnimation ta1 = new TranslateAnimation(0, 0, 0, viewHeight);
                    ta1.setDuration(ANIMATION_DURATION);
                    ta1.setFillAfter(true);
                    topView.startAnimation(ta1);
                    topView.bringToFront();

                    //belowView.setY(0);
                    TranslateAnimation ta2 = new TranslateAnimation(0, 0, 0, -viewHeight);
                    ta2.setDuration(ANIMATION_DURATION);
                    ta2.setFillAfter(true);
                    belowView.startAnimation(ta2);
                    belowView.bringToFront();

                    noSwap = false;
                } else {
                    //Log.d("Swap Status", "Swapped already ? " + true);

                    TranslateAnimation ta1 = new TranslateAnimation(0, 0, viewHeight, 0);
                    ta1.setDuration(ANIMATION_DURATION);
                    ta1.setFillAfter(true);
                    topView.startAnimation(ta1);
                    topView.bringToFront();

                    belowView.setY(0);

                    TranslateAnimation ta2 = new TranslateAnimation(0, 0, 0, viewHeight);
                    ta2.setDuration(ANIMATION_DURATION);
                    ta2.setFillAfter(true);
                    belowView.startAnimation(ta2);
                    belowView.bringToFront();

                    noSwap = true;
                }
            }
        });



        imageView1.setOnClickListener(this);







    }

    @Override
    public void onClick(View v) {

        TranslateAnimation ta1 = new TranslateAnimation(0, x1, 0, 0);
        ta1.setDuration(ANIMATION_DURATION);
        ta1.setFillAfter(true);
        imageView1.startAnimation(ta1);
        ta1.setFillAfter(true);
        imageView1.bringToFront();



        TranslateAnimation ta2 = new TranslateAnimation(0, -x1, 0, 0);
        ta2.setDuration(ANIMATION_DURATION);
        ta2.setFillAfter(true);
        imageView2.startAnimation(ta2);
        imageView2.bringToFront();

        ta2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                TranslateAnimation  ta1 = new TranslateAnimation(x1, -x1, 0, 0);
                ta1.setDuration(ANIMATION_DURATION);
                ta1.setFillAfter(true);
                imageView1.startAnimation(ta1);
                ta1.setFillAfter(true);
                imageView1.bringToFront();



                TranslateAnimation ta2 = new TranslateAnimation(-x1, x1, 0, 0);
                ta2.setDuration(ANIMATION_DURATION);
                ta2.setFillAfter(true);
                imageView2.startAnimation(ta2);
                imageView2.bringToFront();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }
}
