// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class AdvertisesActivity_ViewBinding<T extends AdvertisesActivity> implements Unbinder {
  protected T target;

  private View view2131296590;

  private View view2131296589;

  private View view2131297398;

  public AdvertisesActivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.rlCardRoot = finder.findRequiredViewAsType(source, R.id.rl_card_root, "field 'rlCardRoot'", RelativeLayout.class);
    view = finder.findRequiredView(source, R.id.imageView_front, "field 'mIvFrontAD' and method 'onFrontClick'");
    target.mIvFrontAD = finder.castView(view, R.id.imageView_front, "field 'mIvFrontAD'", ImageView.class);
    view2131296590 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onFrontClick();
      }
    });
    view = finder.findRequiredView(source, R.id.imageView_back, "field 'mIvBackAD' and method 'onBackClick'");
    target.mIvBackAD = finder.castView(view, R.id.imageView_back, "field 'mIvBackAD'", ImageView.class);
    view2131296589 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackClick();
      }
    });
    view = finder.findRequiredView(source, R.id.tv_skip, "field 'mBtSkip' and method 'onSkipCLick'");
    target.mBtSkip = finder.castView(view, R.id.tv_skip, "field 'mBtSkip'", TextView.class);
    view2131297398 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSkipCLick();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.rlCardRoot = null;
    target.mIvFrontAD = null;
    target.mIvBackAD = null;
    target.mBtSkip = null;

    view2131296590.setOnClickListener(null);
    view2131296590 = null;
    view2131296589.setOnClickListener(null);
    view2131296589 = null;
    view2131297398.setOnClickListener(null);
    view2131297398 = null;

    this.target = null;
  }
}
