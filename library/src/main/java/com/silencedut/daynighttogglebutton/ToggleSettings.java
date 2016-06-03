package com.silencedut.daynighttogglebutton;

import android.graphics.Color;

/**
 * Created by SilenceDut on 16/6/3.
 */

public class ToggleSettings {
    public   static final int BACKGROUND_UNCHECKED_COLOR = Color.parseColor("#7c4dff");
    public  static final int BACKGROUND_CHECKED_COLOR = Color.parseColor("#424242");
    public  static final int TOGGLE_CHECKED_COLOR = Color.WHITE;
    public  static final int TOGGLE_UNCHECKED_COLOR = Color.parseColor("#ff5722");
    public static final int PADDING_DEFAULT = 5;
    public static final int DURATION_DEFAULT = 300;
    public int mBackgroundUnCheckedColor ;
    public int mBackgroundCheckedColor ;
    public int mToggleUnCheckedColor ;
    public int mToggleCheckedColor ;
    public int mPadding ;
    public int mDuration ;

    private ToggleSettings(Builder builder) {
        this.mBackgroundUnCheckedColor = builder.backgroundUncheckedColor;
        this.mBackgroundCheckedColor = builder.backgroundCheckedColor;
        this.mToggleUnCheckedColor = builder.toggleUnCheckedColor;
        this.mToggleCheckedColor = builder.toggleCheckedColor;
        this.mPadding = builder.padding;
        this.mDuration = builder.duration;
        this.mDuration = !builder.withAnimator?1:mDuration;
    }

    public static class Builder {
        int backgroundUncheckedColor = BACKGROUND_UNCHECKED_COLOR;
        int backgroundCheckedColor = BACKGROUND_CHECKED_COLOR;
        int toggleUnCheckedColor = TOGGLE_UNCHECKED_COLOR;
        int toggleCheckedColor =TOGGLE_CHECKED_COLOR;
        int padding = PADDING_DEFAULT;
        int duration = DURATION_DEFAULT;
        boolean withAnimator = true;

        public Builder setBackgroundUncheckedColor(int backgroundUncheckedColor) {
            this.backgroundUncheckedColor = backgroundUncheckedColor;
            return this;
        }
        public Builder setBackgroundCheckedColor(int backgroundCheckedColor) {
            this.backgroundCheckedColor = backgroundCheckedColor;
            return this;
        }
        public Builder setToggleUnCheckedColor(int toggleUnCheckedColor) {
            this.toggleUnCheckedColor = toggleUnCheckedColor;
            return this;
        }
        public Builder setToggleCheckedColor(int toggleCheckedColor) {
            this.toggleCheckedColor = toggleCheckedColor;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder withAnimator(boolean withAnimator) {
            this.withAnimator = withAnimator;
            return this;
        }

        public Builder setPadding(int padding) {
            this.padding = padding;
            return this;
        }

        public ToggleSettings buildSettings() {
            return new ToggleSettings(this);
        }
    }
}
