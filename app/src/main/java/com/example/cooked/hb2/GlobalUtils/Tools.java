package com.example.cooked.hb2.GlobalUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.example.cooked.hb2.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.cooked.hb2.MainActivity.context;

public class Tools
{
    public static String moneyFormat(Float value)
    {
        return(String.format(Locale.ENGLISH, "£%.2f", value));
    }

    public static String expenseFormat(Float value)
    {
        if(value==0.00)
            return(String.format(Locale.ENGLISH, "£%.2f", value));
        return(String.format(Locale.ENGLISH, "£%.2f", value*-1));
    }

    public static void showIn(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(200)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }

    public static void initShowOut(final View v) {
        v.setVisibility(View.GONE);
        v.setTranslationY(v.getHeight());
        v.setAlpha(0f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }

    public static void showOut(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }

    public static boolean rotateFab(final View v, boolean rotate) {
        v.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 135f : 0f);
        return rotate;
    }


    public static boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }


    public static boolean toggleArrow(boolean show, View view) {
        return toggleArrow(show, view, true);
    }

    public static void setAsExpanded(View view, boolean delay)
    {
        view.animate().setDuration(delay ? 200 : 0).rotation(180);
    }

    public static void setAsClosed(View view, boolean delay)
    {
        view.animate().setDuration(delay ? 200 : 0).rotation(0);
    }

    public static boolean toggleArrow(boolean show, View view, boolean delay) {
        if (show) {
            setAsExpanded(view, delay);
            return true;
        } else {
            setAsClosed(view, delay);
            return false;
        }
    }

}
