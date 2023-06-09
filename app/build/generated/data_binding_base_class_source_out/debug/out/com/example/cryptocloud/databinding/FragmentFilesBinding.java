// Generated by view binder compiler. Do not edit!
package com.example.cryptocloud.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cryptocloud.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentFilesBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatImageButton btnAddFilesFragment;

  @NonNull
  public final AppCompatImageButton btnRefreshFilesFragment;

  @NonNull
  public final ImageView btnSearchFilesFragment;

  @NonNull
  public final EditText edtTxtSearchFilesFragment;

  @NonNull
  public final ImageView imgSortFilesFragment;

  @NonNull
  public final RelativeLayout progressBarLayoutFilesFragment;

  @NonNull
  public final RecyclerView recyclerViewFilesFragment;

  @NonNull
  public final TextView txtSortFilesFragment;

  @NonNull
  public final TextView txtUserDataLimit;

  @NonNull
  public final ConstraintLayout userDataLimitLayoutFilesFragment;

  @NonNull
  public final LinearProgressIndicator userDataLimitProgressBarFilesFragment;

  private FragmentFilesBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatImageButton btnAddFilesFragment,
      @NonNull AppCompatImageButton btnRefreshFilesFragment,
      @NonNull ImageView btnSearchFilesFragment, @NonNull EditText edtTxtSearchFilesFragment,
      @NonNull ImageView imgSortFilesFragment,
      @NonNull RelativeLayout progressBarLayoutFilesFragment,
      @NonNull RecyclerView recyclerViewFilesFragment, @NonNull TextView txtSortFilesFragment,
      @NonNull TextView txtUserDataLimit,
      @NonNull ConstraintLayout userDataLimitLayoutFilesFragment,
      @NonNull LinearProgressIndicator userDataLimitProgressBarFilesFragment) {
    this.rootView = rootView;
    this.btnAddFilesFragment = btnAddFilesFragment;
    this.btnRefreshFilesFragment = btnRefreshFilesFragment;
    this.btnSearchFilesFragment = btnSearchFilesFragment;
    this.edtTxtSearchFilesFragment = edtTxtSearchFilesFragment;
    this.imgSortFilesFragment = imgSortFilesFragment;
    this.progressBarLayoutFilesFragment = progressBarLayoutFilesFragment;
    this.recyclerViewFilesFragment = recyclerViewFilesFragment;
    this.txtSortFilesFragment = txtSortFilesFragment;
    this.txtUserDataLimit = txtUserDataLimit;
    this.userDataLimitLayoutFilesFragment = userDataLimitLayoutFilesFragment;
    this.userDataLimitProgressBarFilesFragment = userDataLimitProgressBarFilesFragment;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentFilesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentFilesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_files, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentFilesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAddFilesFragment;
      AppCompatImageButton btnAddFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (btnAddFilesFragment == null) {
        break missingId;
      }

      id = R.id.btnRefreshFilesFragment;
      AppCompatImageButton btnRefreshFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (btnRefreshFilesFragment == null) {
        break missingId;
      }

      id = R.id.btnSearchFilesFragment;
      ImageView btnSearchFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (btnSearchFilesFragment == null) {
        break missingId;
      }

      id = R.id.edtTxtSearchFilesFragment;
      EditText edtTxtSearchFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (edtTxtSearchFilesFragment == null) {
        break missingId;
      }

      id = R.id.imgSortFilesFragment;
      ImageView imgSortFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (imgSortFilesFragment == null) {
        break missingId;
      }

      id = R.id.progressBarLayoutFilesFragment;
      RelativeLayout progressBarLayoutFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (progressBarLayoutFilesFragment == null) {
        break missingId;
      }

      id = R.id.recyclerViewFilesFragment;
      RecyclerView recyclerViewFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewFilesFragment == null) {
        break missingId;
      }

      id = R.id.txtSortFilesFragment;
      TextView txtSortFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (txtSortFilesFragment == null) {
        break missingId;
      }

      id = R.id.txtUserDataLimit;
      TextView txtUserDataLimit = ViewBindings.findChildViewById(rootView, id);
      if (txtUserDataLimit == null) {
        break missingId;
      }

      id = R.id.userDataLimitLayoutFilesFragment;
      ConstraintLayout userDataLimitLayoutFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (userDataLimitLayoutFilesFragment == null) {
        break missingId;
      }

      id = R.id.userDataLimitProgressBarFilesFragment;
      LinearProgressIndicator userDataLimitProgressBarFilesFragment = ViewBindings.findChildViewById(rootView, id);
      if (userDataLimitProgressBarFilesFragment == null) {
        break missingId;
      }

      return new FragmentFilesBinding((ConstraintLayout) rootView, btnAddFilesFragment,
          btnRefreshFilesFragment, btnSearchFilesFragment, edtTxtSearchFilesFragment,
          imgSortFilesFragment, progressBarLayoutFilesFragment, recyclerViewFilesFragment,
          txtSortFilesFragment, txtUserDataLimit, userDataLimitLayoutFilesFragment,
          userDataLimitProgressBarFilesFragment);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
