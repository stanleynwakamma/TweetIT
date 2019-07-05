// Generated code from Butter Knife. Do not modify!
package com.codepath.apps.restclienttemplate;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.wafflecopter.charcounttextview.CharCountTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ComposeActivity_ViewBinding<T extends ComposeActivity> implements Unbinder {
  protected T target;

  private View view2131165220;

  @UiThread
  public ComposeActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.etNewTweet = Utils.findRequiredViewAsType(source, R.id.etNewTweet, "field 'etNewTweet'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btnSubmit, "field 'btnSubmit' and method 'submitTweet'");
    target.btnSubmit = Utils.castView(view, R.id.btnSubmit, "field 'btnSubmit'", Button.class);
    view2131165220 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.submitTweet();
      }
    });
    target.tvCharacterCount = Utils.findRequiredViewAsType(source, R.id.tvTextCounter, "field 'tvCharacterCount'", CharCountTextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.etNewTweet = null;
    target.btnSubmit = null;
    target.tvCharacterCount = null;

    view2131165220.setOnClickListener(null);
    view2131165220 = null;

    this.target = null;
  }
}
