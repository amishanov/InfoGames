package com.example.infogames;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

public class TheoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<String> subTheory;
    private final LayoutInflater inflater;

    public TheoryListAdapter(Context context, List<String> states) {
        this.subTheory = states;
        this.inflater = LayoutInflater.from(context);
    }

    public static class ViewHolderIm extends RecyclerView.ViewHolder {
        final ImageView imageView;
        ViewHolderIm(View view){
            super(view);
            this.imageView = view.findViewById(R.id.theoryImage);

        }
    }

    public static class ViewHolderTx extends RecyclerView.ViewHolder {
        final TextView textView;
        ViewHolderTx(View view){
            super(view);
            this.textView = view.findViewById(R.id.theoryText);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
//        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
//        view = inflater.inflate(R.layout.theory_item_text, parent, false);
//        return new ViewHolderTx(view);
        if (viewType == 0) {
            view = inflater.inflate(R.layout.theory_item_text, parent, false);
            return new ViewHolderTx(view);
        }
        view = inflater.inflate(R.layout.theory_item_image, parent, false);
        return new ViewHolderIm(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        ViewHolderTx viewHolderTx = (ViewHolderTx) holder;
//        viewHolderTx.textView.setText(Html.fromHtml(subTheory.get(position), Html.FROM_HTML_MODE_COMPACT));
        if (holder.getItemViewType() == 0) {
            ViewHolderTx viewHolderTx = (ViewHolderTx) holder;
            viewHolderTx.textView.setText(Html.fromHtml(subTheory.get(position),
                    Html.FROM_HTML_MODE_COMPACT));
        } else if (holder.getItemViewType() == 1) {
            ViewHolderIm viewHolderIm = (ViewHolderIm)  holder;
            viewHolderIm.imageView.setImageResource(R.drawable.ic_profile);
            Picasso.get().load(subTheory.get(position)).error(R.drawable.net_error_ic).into(viewHolderIm.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return subTheory.size();
    }
}
