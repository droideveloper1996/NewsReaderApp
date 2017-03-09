package scorecard.project.com.newsreaderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abhishek on 06/03/2017.
 */
public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Context context, List<News> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemview = convertView;
        //  News currentItem=listItemview.getItem(position);
        if (listItemview == null) {
            listItemview = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);

        }
        News currentItem = getItem(position);
        TextView heading=(TextView)listItemview.findViewById(R.id.headingTextView);
        heading.setText(currentItem.getTitle());
        TextView author =(TextView)listItemview.findViewById(R.id.authorTextView);
        author.setText(currentItem.getType());
        TextView section=(TextView)listItemview.findViewById(R.id.newsTypeTextView);
        section.setText(currentItem.getSection());
        String webTitleUrl=currentItem.getMwebUrl();

        return listItemview;

    }
}
