// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ConferencedDownloadDetailActivity_ViewBinding<T extends ConferencedDownloadDetailActivity> implements Unbinder {
  protected T target;

  private View view2131296363;

  private View view2131296360;

  private View view2131296622;

  public ConferencedDownloadDetailActivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.mIvTopLogo = finder.findRequiredViewAsType(source, R.id.iv_top_logo, "field 'mIvTopLogo'", ImageView.class);
    target.mIvLogo = finder.findRequiredViewAsType(source, R.id.iv_logo, "field 'mIvLogo'", ImageView.class);
    target.mTvTime = finder.findRequiredViewAsType(source, R.id.tv_time, "field 'mTvTime'", TextView.class);
    target.mTvLocation = finder.findRequiredViewAsType(source, R.id.tv_location, "field 'mTvLocation'", TextView.class);
    target.mTvRemark = finder.findRequiredViewAsType(source, R.id.tv_remark, "field 'mTvRemark'", TextView.class);
    view = finder.findRequiredView(source, R.id.bt_handle, "field 'mBtHandle' and method 'onHandleClick'");
    target.mBtHandle = finder.castView(view, R.id.bt_handle, "field 'mBtHandle'", Button.class);
    view2131296363 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onHandleClick();
      }
    });
    view = finder.findRequiredView(source, R.id.bt_delete, "field 'mBtDelete' and method 'onDeleteClick'");
    target.mBtDelete = finder.castView(view, R.id.bt_delete, "field 'mBtDelete'", Button.class);
    view2131296360 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDeleteClick();
      }
    });
    view = finder.findRequiredView(source, R.id.iv_back, "method 'onBackClick'");
    view2131296622 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackClick();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mIvTopLogo = null;
    target.mIvLogo = null;
    target.mTvTime = null;
    target.mTvLocation = null;
    target.mTvRemark = null;
    target.mBtHandle = null;
    target.mBtDelete = null;

    view2131296363.setOnClickListener(null);
    view2131296363 = null;
    view2131296360.setOnClickListener(null);
    view2131296360 = null;
    view2131296622.setOnClickListener(null);
    view2131296622 = null;

    this.target = null;
  }
}
