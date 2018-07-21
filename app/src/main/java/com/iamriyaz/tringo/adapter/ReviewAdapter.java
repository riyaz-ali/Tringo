package com.iamriyaz.tringo.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.iamriyaz.tringo.R;
import com.iamriyaz.tringo.data.Review;
import com.pascalwelsch.arrayadapter.ArrayAdapter;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Created on 10 Jul, 2018
 *
 * @author Riyaz
 */
public class ReviewAdapter extends ArrayAdapter<Review, ReviewAdapter.ViewHolder> {

  //ViewHolder impl
  static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.review_author) TextView author;
    @BindView(R.id.review_content) TextView content;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bind(@NonNull Review review){
      author.setText(review.getAuthor());
      content.setText(review.getContent());
    }
  }

  public ReviewAdapter(@NonNull List<Review> objects) {
    super(objects);
  }

  @Nullable @Override public Object getItemId(@NonNull Review item) {
    return item.getId();
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.adapter_review_item, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(requireNonNull(getItem(position)));
  }
}
