/*
 * Copyright (C) 2017 Florian Dreier
 *
 * This file is part of MyTargets.
 *
 * MyTargets is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation.
 *
 * MyTargets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package de.dreier.mytargets.utils.transitions;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;

/**
 * https://halfthought.wordpress.com/2014/11/07/reveal-transition/
 * <p/>
 * Interrupting Activity transitions can yield an OperationNotSupportedException when the
 * transition tries to pause the animator. Yikes! We can fix this by wrapping the Animator:
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class NoPauseAnimator extends Animator {
    private final Animator mAnimator;
    private final ArrayMap<AnimatorListener, AnimatorListener> mListeners =
            new ArrayMap<>();

    public NoPauseAnimator(Animator animator) {
        mAnimator = animator;
    }

    @Override
    public void addListener(AnimatorListener listener) {
        AnimatorListener wrapper = new AnimatorListenerWrapper(this, listener);
        if (!mListeners.containsKey(listener)) {
            mListeners.put(listener, wrapper);
            mAnimator.addListener(wrapper);
        }
    }

    @Override
    public void cancel() {
        mAnimator.cancel();
    }

    @Override
    public void end() {
        mAnimator.end();
    }

    @Override
    public long getDuration() {
        return mAnimator.getDuration();
    }

    @Override
    public TimeInterpolator getInterpolator() {
        return mAnimator.getInterpolator();
    }

    @Override
    public void setInterpolator(TimeInterpolator timeInterpolator) {
        mAnimator.setInterpolator(timeInterpolator);
    }

    @Override
    public ArrayList<AnimatorListener> getListeners() {
        return new ArrayList<>(mListeners.keySet());
    }

    @Override
    public long getStartDelay() {
        return mAnimator.getStartDelay();
    }

    @Override
    public void setStartDelay(long delayMS) {
        mAnimator.setStartDelay(delayMS);
    }

    @Override
    public boolean isPaused() {
        return mAnimator.isPaused();
    }

    @Override
    public boolean isRunning() {
        return mAnimator.isRunning();
    }

    @Override
    public boolean isStarted() {
        return mAnimator.isStarted();
    }

        /* We don't want to override pause or resume methods because we don't want them
         * to affect mAnimator.
        public void pause();
        public void resume();
        public void addPauseListener(AnimatorPauseListener listener);
        public void removePauseListener(AnimatorPauseListener listener);
        */

    @Override
    public void removeAllListeners() {
        mListeners.clear();
        mAnimator.removeAllListeners();
    }

    @Override
    public void removeListener(AnimatorListener listener) {
        AnimatorListener wrapper = mListeners.get(listener);
        if (wrapper != null) {
            mListeners.remove(listener);
            mAnimator.removeListener(wrapper);
        }
    }

    @Override
    public Animator setDuration(long durationMS) {
        mAnimator.setDuration(durationMS);
        return this;
    }

    @Override
    public void setTarget(Object target) {
        mAnimator.setTarget(target);
    }

    @Override
    public void setupEndValues() {
        mAnimator.setupEndValues();
    }

    @Override
    public void setupStartValues() {
        mAnimator.setupStartValues();
    }

    @Override
    public void start() {
        mAnimator.start();
    }

    private static class AnimatorListenerWrapper implements Animator.AnimatorListener {
        private final Animator mAnimator;
        private final Animator.AnimatorListener mListener;

        public AnimatorListenerWrapper(Animator animator, Animator.AnimatorListener listener) {
            mAnimator = animator;
            mListener = listener;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            mListener.onAnimationStart(mAnimator);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mListener.onAnimationEnd(mAnimator);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mListener.onAnimationCancel(mAnimator);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            mListener.onAnimationRepeat(mAnimator);
        }
    }
}