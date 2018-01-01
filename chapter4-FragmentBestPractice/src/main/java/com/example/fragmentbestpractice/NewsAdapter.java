package com.example.fragmentbestpractice;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by inanitysloth on 2017/2/24.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> mNewsList;
    private boolean isTwoPane;
    private Fragment mFragment;
    public NewsAdapter(Fragment mFragment,List<News> newsList) {
        this.mNewsList = newsList;
        this.mFragment=mFragment;
    }
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFragment.getActivity().findViewById(R.id.news_content_layout) != null) {
            isTwoPane = true; // 可以找到news_content_layout布局时，为双页模式
        } else {
            isTwoPane = false; // 找不到news_content_layout布局时，为单页模式
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News news = mNewsList.get(holder.getAdapterPosition());
                if (isTwoPane) {
                    NewsContentFragment newsContentFragment = (NewsContentFragment)
                            mFragment.getFragmentManager().findFragmentById(R.id.news_content_fragment);
                    newsContentFragment.refresh(news.getTitle(), news.getContent());
                } else {
                    NewsContentActivity.actionStart(mFragment.getActivity(), news.getTitle(), news.getContent());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        News news = mNewsList.get(position);
        holder.newsTitleText.setText(news.getTitle());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitleText;

        public ViewHolder(View view) {
            super(view);
            newsTitleText = (TextView) view.findViewById(R.id.news_title);
        }

    }
}
