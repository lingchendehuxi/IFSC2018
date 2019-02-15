// Generated code from Butter Knife. Do not modify!
package com.android.incongress.cd.conference;

import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class DialogActivity_ViewBinding<T extends DialogActivity> implements Unbinder {
  protected T target;

  public DialogActivity_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.imageView = finder.findRequiredViewAsType(source, R.id.dialog_img, "field 'imageView'", ImageView.class);
    target.imageBlack = finder.findRequiredViewAsType(source, R.id.dialog_black, "field 'imageBlack'", ImageView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.imageView = null;
    target.imageBlack = null;

    this.target = null;
  }
}
