// Generated by view binder compiler. Do not edit!
package com.example.cryptocloud.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.example.cryptocloud.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityForgotPasswordBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView btnSubmitForgotPasswordActivity;

  @NonNull
  public final EditText edtTxtEmailForgotPasswordActivity;

  @NonNull
  public final LottieAnimationView imgForgotPassword;

  @NonNull
  public final LinearLayout middleLayoutForgotPasswordActivity;

  @NonNull
  public final TextView txtForgotPasswordHeading;

  @NonNull
  public final TextView txtLoginForgotPasswordActivity;

  private ActivityForgotPasswordBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView btnSubmitForgotPasswordActivity,
      @NonNull EditText edtTxtEmailForgotPasswordActivity,
      @NonNull LottieAnimationView imgForgotPassword,
      @NonNull LinearLayout middleLayoutForgotPasswordActivity,
      @NonNull TextView txtForgotPasswordHeading,
      @NonNull TextView txtLoginForgotPasswordActivity) {
    this.rootView = rootView;
    this.btnSubmitForgotPasswordActivity = btnSubmitForgotPasswordActivity;
    this.edtTxtEmailForgotPasswordActivity = edtTxtEmailForgotPasswordActivity;
    this.imgForgotPassword = imgForgotPassword;
    this.middleLayoutForgotPasswordActivity = middleLayoutForgotPasswordActivity;
    this.txtForgotPasswordHeading = txtForgotPasswordHeading;
    this.txtLoginForgotPasswordActivity = txtLoginForgotPasswordActivity;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityForgotPasswordBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityForgotPasswordBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_forgot_password, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityForgotPasswordBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnSubmitForgotPasswordActivity;
      ImageView btnSubmitForgotPasswordActivity = ViewBindings.findChildViewById(rootView, id);
      if (btnSubmitForgotPasswordActivity == null) {
        break missingId;
      }

      id = R.id.edtTxtEmailForgotPasswordActivity;
      EditText edtTxtEmailForgotPasswordActivity = ViewBindings.findChildViewById(rootView, id);
      if (edtTxtEmailForgotPasswordActivity == null) {
        break missingId;
      }

      id = R.id.imgForgotPassword;
      LottieAnimationView imgForgotPassword = ViewBindings.findChildViewById(rootView, id);
      if (imgForgotPassword == null) {
        break missingId;
      }

      id = R.id.middleLayoutForgotPasswordActivity;
      LinearLayout middleLayoutForgotPasswordActivity = ViewBindings.findChildViewById(rootView, id);
      if (middleLayoutForgotPasswordActivity == null) {
        break missingId;
      }

      id = R.id.txtForgotPasswordHeading;
      TextView txtForgotPasswordHeading = ViewBindings.findChildViewById(rootView, id);
      if (txtForgotPasswordHeading == null) {
        break missingId;
      }

      id = R.id.txtLoginForgotPasswordActivity;
      TextView txtLoginForgotPasswordActivity = ViewBindings.findChildViewById(rootView, id);
      if (txtLoginForgotPasswordActivity == null) {
        break missingId;
      }

      return new ActivityForgotPasswordBinding((ConstraintLayout) rootView,
          btnSubmitForgotPasswordActivity, edtTxtEmailForgotPasswordActivity, imgForgotPassword,
          middleLayoutForgotPasswordActivity, txtForgotPasswordHeading,
          txtLoginForgotPasswordActivity);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}