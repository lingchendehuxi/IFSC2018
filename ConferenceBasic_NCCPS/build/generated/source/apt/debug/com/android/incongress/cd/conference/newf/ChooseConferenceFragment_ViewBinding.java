// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference.newf;

import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ChooseConferenceFragment_ViewBinding<T extends ChooseConferenceFragment> implements Unbinder {
  protected T target;

  public ChooseConferenceFragment_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.mRecyclerView = finder.findRequiredViewAsType(source, R.id.recyclerview, "field 'mRecyclerView'", XRecyclerView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mRecyclerView = null;

    this.target = null;
  }
}
