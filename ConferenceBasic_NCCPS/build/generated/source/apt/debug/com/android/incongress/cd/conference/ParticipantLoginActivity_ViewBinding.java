// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ParticipantLoginActivity_ViewBinding<T extends ParticipantLoginActivity> implements Unbinder {
  protected T target;

  private View view2131296760;

  private View view2131296381;

  private View view2131296516;

  private View view2131296855;

  public ParticipantLoginActivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.mEtName = finder.findRequiredViewAsType(source, R.id.et_name, "field 'mEtName'", EditText.class);
    target.mEtMobile = finder.findRequiredViewAsType(source, R.id.et_mobile, "field 'mEtMobile'", EditText.class);
    target.mEtCode = finder.findRequiredViewAsType(source, R.id.et_ccode, "field 'mEtCode'", EditText.class);
    target.nameTips = finder.findRequiredViewAsType(source, R.id.name_tips, "field 'nameTips'", TextView.class);
    target.mobileTips = finder.findRequiredViewAsType(source, R.id.mobile_tips, "field 'mobileTips'", TextView.class);
    view = finder.findRequiredView(source, R.id.ll_ccode, "field 'mLlCode' and method 'dismissCCode'");
    target.mLlCode = finder.castView(view, R.id.ll_ccode, "field 'mLlCode'", LinearLayout.class);
    view2131296760 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.dismissCCode();
      }
    });
    target.mIvCodeTips = finder.findRequiredViewAsType(source, R.id.ccode, "field 'mIvCodeTips'", ImageView.class);
    view = finder.findRequiredView(source, R.id.ccode_info, "field 'mTvCodeTips' and method 'onCCodeInfoClick'");
    target.mTvCodeTips = finder.castView(view, R.id.ccode_info, "field 'mTvCodeTips'", TextView.class);
    view2131296381 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCCodeInfoClick();
      }
    });
    view = finder.findRequiredView(source, R.id.find_back_ccode, "method 'findBackCCode'");
    view2131296516 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.findBackCCode();
      }
    });
    view = finder.findRequiredView(source, R.id.login, "method 'login'");
    view2131296855 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.login();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mEtName = null;
    target.mEtMobile = null;
    target.mEtCode = null;
    target.nameTips = null;
    target.mobileTips = null;
    target.mLlCode = null;
    target.mIvCodeTips = null;
    target.mTvCodeTips = null;

    view2131296760.setOnClickListener(null);
    view2131296760 = null;
    view2131296381.setOnClickListener(null);
    view2131296381 = null;
    view2131296516.setOnClickListener(null);
    view2131296516 = null;
    view2131296855.setOnClickListener(null);
    view2131296855 = null;

    this.target = null;
  }
}
