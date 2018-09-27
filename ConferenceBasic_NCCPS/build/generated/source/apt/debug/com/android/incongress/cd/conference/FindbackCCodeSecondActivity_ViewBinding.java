// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference;

import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class FindbackCCodeSecondActivity_ViewBinding<T extends FindbackCCodeSecondActivity> implements Unbinder {
  protected T target;

  private View view2131624144;

  public FindbackCCodeSecondActivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.mEtName = finder.findRequiredViewAsType(source, R.id.et_name, "field 'mEtName'", EditText.class);
    target.mEtMobile = finder.findRequiredViewAsType(source, R.id.et_mobile, "field 'mEtMobile'", EditText.class);
    view = finder.findRequiredView(source, R.id.get_ccode, "method 'getCCode'");
    view2131624144 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.getCCode();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mEtName = null;
    target.mEtMobile = null;

    view2131624144.setOnClickListener(null);
    view2131624144 = null;

    this.target = null;
  }
}
