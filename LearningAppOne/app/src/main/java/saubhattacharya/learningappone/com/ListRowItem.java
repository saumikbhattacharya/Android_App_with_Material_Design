package saubhattacharya.learningappone.com;

import android.graphics.Bitmap;

public class ListRowItem {

    String over_view,release_date,original_title,vote_avg;
    Bitmap poster;

    public String getOverview()
    {
        return over_view;
    }

    public String getReleasedate()
    {
        return release_date;
    }

    public String getOriginaltitle()
    {
        return original_title;
    }

    public String getVoteavg()
    {
        return vote_avg;
    }

    public Bitmap getPoster()
    {
        return poster;
    }

    public void setOverview(String m_over_view)
    {
        this.over_view = m_over_view;
    }

    public void setReleasedate(String m_release_date)
    {
        this.release_date = m_release_date;
    }

    public void setOriginaltitle(String m_original_title)
    {
        this.original_title = m_original_title;
    }

    public void setVoteavg(String m_vote_avg)
    {
        this.vote_avg = m_vote_avg;
    }

    public void setPoster(Bitmap m_poster)
    {
        this.poster = m_poster;
    }
}
