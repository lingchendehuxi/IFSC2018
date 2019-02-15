// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference;

import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class SplashActivity_ViewBinding<T extends SplashActivity> implements Unbinder {
  protected T target;

  public SplashActivity_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.mPbh = finder.findRequiredViewAsType(source, R.id.splash_pbh, "field 'mPbh'", ProgressBar.class);
    target.mTv = finder.findRequiredViewAsType(source, R.id.splash_text, "field 'mTv'", TextView.class);
    target.mTvDots = finder.findRequiredViewAsType(source, R.id.tv_dots, "field 'mTvDots'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mPbh = null;
    target.mTv = null;
    target.mTvDots = null;

    this.target = null;
  }
}
