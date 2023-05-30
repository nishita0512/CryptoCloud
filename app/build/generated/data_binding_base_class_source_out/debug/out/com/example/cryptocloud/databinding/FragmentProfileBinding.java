// Generated by view binder compiler. Do not edit!
package com.example.cryptocloud.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cryptocloud.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentProfileBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout changePasswordCardViewProfileFragment;

  @NonNull
  public final ConstraintLayout deleteAccountCardViewProfileFragment;

  @NonNull
  public final ConstraintLayout logoutCardViewProfileFragment;

  @NonNull
  public final TextView txtHelloUsernameProfileFragment;

  @NonNull
  public final ConstraintLayout viewProfileCardViewProfileFragment;

  private FragmentProfileBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout changePasswordCardViewProfileFragment,
      @NonNull ConstraintLayout deleteAccountCardViewProfileFragment,
      @NonNull ConstraintLayout logoutCardViewProfileFragment,
      @NonNull TextView txtHelloUsernameProfileFragment,
      @NonNull ConstraintLayout viewProfileCardViewProfileFragment) {
    this.rootView = rootView;
    this.changePasswordCardViewProfileFragment = changePasswordCardViewProfileFragment;
    this.deleteAccountCardViewProfileFragment = deleteAccountCardViewProfileFragment;
    this.logoutCardViewProfileFragment = logoutCardViewProfileFragment;
    this.txtHelloUsernameProfileFragment = txtHelloUsernameProfileFragment;
    this.viewProfileCardViewProfileFragment = viewProfileCardViewProfileFragment;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.changePasswordCardViewProfileFragment;
      ConstraintLayout changePasswordCardViewProfileFragment = ViewBindings.findChildViewById(rootView, id);
      if (changePasswordCardViewProfileFragment == null) {
        break missingId;
      }

      id = R.id.deleteAccountCardViewProfileFragment;
      ConstraintLayout deleteAccountCardViewProfileFragment = ViewBindings.findChildViewById(rootView, id);
      if (deleteAccountCardViewProfileFragment == null) {
        break missingId;
      }

      id = R.id.logoutCardViewProfileFragment;
      ConstraintLayout logoutCardViewProfileFragment = ViewBindings.findChildViewById(rootView, id);
      if (logoutCardViewProfileFragment == null) {
        break missingId;
      }

      id = R.id.txtHelloUsernameProfileFragment;
      TextView txtHelloUsernameProfileFragment = ViewBindings.findChildViewById(rootView, id);
      if (txtHelloUsernameProfileFragment == null) {
        break missingId;
      }

      id = R.id.viewProfileCardViewProfileFragment;
      ConstraintLayout viewProfileCardViewProfileFragment = ViewBindings.findChildViewById(rootView, id);
      if (viewProfileCardViewProfileFragment == null) {
        break missingId;
      }

      return new FragmentProfileBinding((ConstraintLayout) rootView,
          changePasswordCardViewProfileFragment, deleteAccountCardViewProfileFragment,
          logoutCardViewProfileFragment, txtHelloUsernameProfileFragment,
          viewProfileCardViewProfileFragment);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}