// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ChooseFieldActivity_ViewBinding<T extends ChooseFieldActivity> implements Unbinder {
  protected T target;

  private View view2131624130;

  public ChooseFieldActivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.mIvBack = finder.findRequiredViewAsType(source, R.id.title_back, "field 'mIvBack'", ImageView.class);
    target.mTvTitle = finder.findRequiredViewAsType(source, R.id.title_text, "field 'mTvTitle'", TextView.class);
    view = finder.findRequiredView(source, R.id.tv_confirm, "field 'mTvConfirm' and method 'onConfirmClick'");
    target.mTvConfirm = finder.castView(view, R.id.tv_confirm, "field 'mTvConfirm'", TextView.class);
    view2131624130 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onConfirmClick();
      }
    });
    target.mRvFields = finder.findRequiredViewAsType(source, R.id.rv_field, "field 'mRvFields'", RecyclerView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mIvBack = null;
    target.mTvTitle = null;
    target.mTvConfirm = null;
    target.mRvFields = null;

    view2131624130.setOnClickListener(null);
    view2131624130 = null;

    this.target = null;
  }
}
