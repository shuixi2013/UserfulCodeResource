package com.nineoldandroids.animation;

public class TimeAnimator extends ValueAnimator {
    private TimeListener mListener;
    private long mPreviousTime = -1;

    public interface TimeListener {
        void onTimeUpdate(TimeAnimator timeAnimator, long j, long j2);
    }

    boolean animationFrame(long j) {
        long j2 = 0;
        if (this.mPlayingState == 0) {
            this.mPlayingState = 1;
            if (this.mSeekTime < 0) {
                this.mStartTime = j;
            } else {
                this.mStartTime = j - this.mSeekTime;
                this.mSeekTime = -1;
            }
        }
        if (this.mListener != null) {
            long j3 = j - this.mStartTime;
            if (this.mPreviousTime >= 0) {
                j2 = j - this.mPreviousTime;
            }
            this.mPreviousTime = j;
            this.mListener.onTimeUpdate(this, j3, j2);
        }
        return false;
    }

    public void setTimeListener(TimeListener timeListener) {
        this.mListener = timeListener;
    }

    void animateValue(float f) {
    }

    void initAnimation() {
    }
}
