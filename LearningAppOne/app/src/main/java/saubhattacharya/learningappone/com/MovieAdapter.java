package saubhattacharya.learningappone.com;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>{

    ArrayList<ListRowItem> movieList;
    //LayoutInflater inflater;
    ListRowItem currentListItem;
    Context context;

    public MovieAdapter (ArrayList<ListRowItem> m_movieList,Context ba_context)
    {
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.movieList = m_movieList;
        this.context = ba_context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View each_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_movie_item, null);

        MyViewHolder myViewHolder = new MyViewHolder(each_item);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        currentListItem = movieList.get(position);
        holder.posterView.setImageBitmap(currentListItem.getPoster());
        holder.title.setText(currentListItem.getOriginaltitle());
        holder.rel_date.setText(currentListItem.getReleasedate());
        holder.vote.setText(currentListItem.getVoteavg());
        holder.overview.setText(currentListItem.getOverview());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title,rel_date,vote,overview;
        ImageView posterView;

        public MyViewHolder (View view)
        {
            super(view);
            posterView = (ImageView)view.findViewById(R.id.imageView);
            title = (TextView)view.findViewById(R.id.textView);
            rel_date = (TextView)view.findViewById(R.id.textView3);
            vote = (TextView)view.findViewById(R.id.textView5);
            overview = (TextView)view.findViewById(R.id.textView6);
        }
    }
}
