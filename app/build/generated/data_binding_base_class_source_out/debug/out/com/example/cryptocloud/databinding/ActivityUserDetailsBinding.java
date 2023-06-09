// Generated by view binder compiler. Do not edit!
package com.example.cryptocloud.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cryptocloud.R;
import com.google.android.material.card.MaterialCardView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityUserDetailsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialCardView imgCardViewUserDetailsActivity;

  @NonNull
  public final ImageView imgUserDetailsActivity;

  @NonNull
  public final TextView txtEmailUserDetailsActivity;

  @NonNull
  public final TextView txtUserNameUserDetailsActivity;

  private ActivityUserDetailsBinding(@NonNull ConstraintLayout rootView,
      @NonNull MaterialCardView imgCardViewUserDetailsActivity,
      @NonNull ImageView imgUserDetailsActivity, @NonNull TextView txtEmailUserDetailsActivity,
      @NonNull TextView txtUserNameUserDetailsActivity) {
    this.rootView = rootView;
    this.imgCardViewUserDetailsActivity = imgCardViewUserDetailsActivity;
    this.imgUserDetailsActivity = imgUserDetailsActivity;
    this.txtEmailUserDetailsActivity = txtEmailUserDetailsActivity;
    this.txtUserNameUserDetailsActivity = txtUserNameUserDetailsActivity;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUserDetailsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUserDetailsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_user_details, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUserDetailsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imgCardViewUserDetailsActivity;
      MaterialCardView imgCardViewUserDetailsActivity = ViewBindings.findChildViewById(rootView, id);
      if (imgCardViewUserDetailsActivity == null) {
        break missingId;
      }

      id = R.id.imgUserDetailsActivity;
      ImageView imgUserDetailsActivity = ViewBindings.findChildViewById(rootView, id);
      if (imgUserDetailsActivity == null) {
        break missingId;
      }

      id = R.id.txtEmailUserDetailsActivity;
      TextView txtEmailUserDetailsActivity = ViewBindings.findChildViewById(rootView, id);
      if (txtEmailUserDetailsActivity == null) {
        break missingId;
      }

      id = R.id.txtUserNameUserDetailsActivity;
      TextView txtUserNameUserDetailsActivity = ViewBindings.findChildViewById(rootView, id);
      if (txtUserNameUserDetailsActivity == null) {
        break missingId;
      }

      return new ActivityUserDetailsBinding((ConstraintLayout) rootView,
          imgCardViewUserDetailsActivity, imgUserDetailsActivity, txtEmailUserDetailsActivity,
          txtUserNameUserDetailsActivity);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
