// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class FindbackCCodeFirstActivity_ViewBinding<T extends FindbackCCodeFirstActivity> implements Unbinder {
  protected T target;

  private View view2131296988;

  private View view2131296985;

  private View view2131296982;

  public FindbackCCodeFirstActivity_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    target.radioGroup = finder.findRequiredViewAsType(source, R.id.radio_group, "field 'radioGroup'", RadioGroup.class);
    view = finder.findRequiredView(source, R.id.rb_team, "field 'rbTeam' and method 'onTeam'");
    target.rbTeam = finder.castView(view, R.id.rb_team, "field 'rbTeam'", RadioButton.class);
    view2131296988 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTeam();
      }
    });
    view = finder.findRequiredView(source, R.id.rb_person, "method 'onPerson'");
    view2131296985 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPerson();
      }
    });
    view = finder.findRequiredView(source, R.id.rb_faculty, "method 'onFaculty'");
    view2131296982 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onFaculty();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.radioGroup = null;
    target.rbTeam = null;

    view2131296988.setOnClickListener(null);
    view2131296988 = null;
    view2131296985.setOnClickListener(null);
    view2131296985 = null;
    view2131296982.setOnClickListener(null);
    view2131296982 = null;

    this.target = null;
  }
}
