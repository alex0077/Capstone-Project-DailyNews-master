package com.alex.dailynews.adapters;

import android.animation.Animator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.alex.dailynews.R;
import com.alex.dailynews.data.NewsSQLite;
import com.alex.dailynews.model.NewsArticle;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<NewsArticle> newsArticleArrayList, newsArticleArrayListCopy;
    private final Context mContext;
    private NewsSQLite newsSQLite;
    private final int mType;
    private LottieAnimationView animationView;

    public NewsAdapter(Context activityContext, int type, ArrayList<NewsArticle> newsArticleList) {
        mType = type;
        mContext = activityContext;
        switch (type) {
            case 2:             //TopHeadlines
                newsSQLite = new NewsSQLite(activityContext);
                this.newsArticleArrayList = newsArticleList;    // Call getAllUnread() here to get only those who haven't been read

                newsArticleArrayListCopy = new ArrayList<>();
                newsArticleArrayListCopy.addAll(newsArticleArrayList);
                break;
            case 3:        //Favorite
                newsSQLite = new NewsSQLite(activityContext);
                this.newsArticleArrayList = newsArticleList;
                break;
            case 1:
                this.newsArticleArrayList = newsArticleList;
                break;
        }
    }

    public NewsAdapter(Context activityContext, int type, ArrayList<NewsArticle> newsArticleList, LottieAnimationView animationView) {
        mType = type;
        this.animationView = animationView;
        mContext = activityContext;
        if (type == 2) {            //TopHeadlines
            newsSQLite = new NewsSQLite(activityContext);
            this.newsArticleArrayList = newsArticleList;    // Call getAllUnread() here to get only those who haven't been read
            newsArticleArrayListCopy = new ArrayList<>();
            newsArticleArrayListCopy.addAll(newsArticleArrayList);
        } else if (type == 3) {       //Favorite
            newsSQLite = new NewsSQLite(activityContext);
            this.newsArticleArrayList = newsArticleList;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.title.setText(newsArticleArrayList.get(position).getTitle());
        holder.source.setText(newsArticleArrayList.get(position).getSourceInfo().getName());
        holder.description.setText(newsArticleArrayList.get(position).getDescription());

        Glide.with(mContext.getApplicationContext())
                .load(newsArticleArrayList.get(position).getUrlToImage())
                .into(holder.imageView);

        switch (mType) {
            case 3:
                holder.imageButton.setImageResource(R.drawable.ic_favorite_black_18px);
                break;
            case 2:
                holder.imageButton.setImageResource(R.drawable.ic_favorite_border_black_18px);
                break;
            case 1:
                holder.imageButton.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (newsArticleArrayList == null) {
            return 0;
        } else {
            return newsArticleArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView description;
        final TextView source;
final CardView cardView;
        final ImageView imageView;
        final ImageView imageButton;
        final RelativeLayout favLayout;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.article_image);
            cardView = itemView.findViewById(R.id.news_card_view);
            title = itemView.findViewById(R.id.article_title);
            source = itemView.findViewById(R.id.article_source);
            imageButton = itemView.findViewById(R.id.favorite_button);
            favLayout = itemView.findViewById(R.id.fav_button_layout);
            description = itemView.findViewById(R.id.article_description);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = newsArticleArrayList.get(getAdapterPosition()).getUrl();
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.addDefaultShareMenuItem();
                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.mipmap.ic_arrow_back_white_24dp));
                    builder.setToolbarColor(mContext.getResources().getColor(R.color.colorPrimary));
                    builder.setStartAnimations(mContext, R.anim.slide_in_right, R.anim.slide_out_left);
                    builder.setExitAnimations(mContext, R.anim.slide_in_left, R.anim.slide_out_right);
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(mContext, Uri.parse(url));
                }
            });

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mType == 2) {
                        ArrayList<NewsArticle> newsList = newsSQLite.getRowByPosition(newsArticleArrayList.size() - getAdapterPosition() - 1);
                        newsSQLite.addNewFavoriteRow(newsList);
                        animationView.setVisibility(View.VISIBLE);
                        animationView.setAnimation("TwitterHeart.json");
                        animationView.loop(false);
                        animationView.playAnimation();
                        animationView.addAnimatorListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animationView.cancelAnimation();
                                animationView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    } else {
                        newsSQLite.deleteFavoriteRow(newsArticleArrayList.get(getAdapterPosition()).getTitle());
                        newsArticleArrayList.remove(getAdapterPosition());
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}