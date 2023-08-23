package com.example.news.list;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.ArticleActivity;
import com.example.news.R;
import com.example.news.models.ArticleModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context context;
    private List<ArticleModel> articleList;

    public ArticleAdapter(Context context, List<ArticleModel> itemList) {
        this.context = context;
        this.articleList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArticleModel item = articleList.get(position);

        holder.itemTitle.setText(item.getTitle());
        Picasso.get().load(item.getUrlToImage()).into(holder.itemImage);
        holder.itemAuthor.setText(item.getAuthor());

        // CLICK
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("author", item.getAuthor());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("url", item.getUrl());
                intent.putExtra("urlToImage", item.getUrlToImage());
                intent.putExtra("publishedAt", item.getPublishedAt());
                intent.putExtra("content", item.getContent());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.articleImage);
            itemTitle = itemView.findViewById(R.id.articleItitle);
            itemAuthor = itemView.findViewById(R.id.articleAuthor);
        }
    }


}
