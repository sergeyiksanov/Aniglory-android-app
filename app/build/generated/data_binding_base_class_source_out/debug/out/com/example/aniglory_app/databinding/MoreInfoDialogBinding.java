// Generated by view binder compiler. Do not edit!
package com.example.aniglory_app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.aniglory_app.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class MoreInfoDialogBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView cntEpisodes;

  @NonNull
  public final TextView episode;

  @NonNull
  public final TextView genres;

  @NonNull
  public final TextView status;

  @NonNull
  public final TextView studio;

  @NonNull
  public final TextView titleOtherTXt;

  @NonNull
  public final TextView titleTXt;

  @NonNull
  public final TextView type;

  @NonNull
  public final TextView year;

  private MoreInfoDialogBinding(@NonNull CardView rootView, @NonNull TextView cntEpisodes,
      @NonNull TextView episode, @NonNull TextView genres, @NonNull TextView status,
      @NonNull TextView studio, @NonNull TextView titleOtherTXt, @NonNull TextView titleTXt,
      @NonNull TextView type, @NonNull TextView year) {
    this.rootView = rootView;
    this.cntEpisodes = cntEpisodes;
    this.episode = episode;
    this.genres = genres;
    this.status = status;
    this.studio = studio;
    this.titleOtherTXt = titleOtherTXt;
    this.titleTXt = titleTXt;
    this.type = type;
    this.year = year;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static MoreInfoDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static MoreInfoDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.more_info_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static MoreInfoDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cntEpisodes;
      TextView cntEpisodes = ViewBindings.findChildViewById(rootView, id);
      if (cntEpisodes == null) {
        break missingId;
      }

      id = R.id.episode;
      TextView episode = ViewBindings.findChildViewById(rootView, id);
      if (episode == null) {
        break missingId;
      }

      id = R.id.genres;
      TextView genres = ViewBindings.findChildViewById(rootView, id);
      if (genres == null) {
        break missingId;
      }

      id = R.id.status;
      TextView status = ViewBindings.findChildViewById(rootView, id);
      if (status == null) {
        break missingId;
      }

      id = R.id.studio;
      TextView studio = ViewBindings.findChildViewById(rootView, id);
      if (studio == null) {
        break missingId;
      }

      id = R.id.titleOtherTXt;
      TextView titleOtherTXt = ViewBindings.findChildViewById(rootView, id);
      if (titleOtherTXt == null) {
        break missingId;
      }

      id = R.id.titleTXt;
      TextView titleTXt = ViewBindings.findChildViewById(rootView, id);
      if (titleTXt == null) {
        break missingId;
      }

      id = R.id.type;
      TextView type = ViewBindings.findChildViewById(rootView, id);
      if (type == null) {
        break missingId;
      }

      id = R.id.year;
      TextView year = ViewBindings.findChildViewById(rootView, id);
      if (year == null) {
        break missingId;
      }

      return new MoreInfoDialogBinding((CardView) rootView, cntEpisodes, episode, genres, status,
          studio, titleOtherTXt, titleTXt, type, year);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
