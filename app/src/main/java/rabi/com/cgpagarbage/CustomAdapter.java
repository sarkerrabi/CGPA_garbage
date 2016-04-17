package rabi.com.cgpagarbage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by RABI on 10-Apr-16.
 */
public class CustomAdapter extends ArrayAdapter<ListItem> {
    ListItem li;
    public CustomAdapter(Context context, ListItem[] courses) {
        super(context,R.layout.custom_row,courses);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_row,parent,false);

        boolean b = false;

        li = getItem(position);

        TextView crsText = (TextView)customView.findViewById(R.id.item_course);
        TextView creText = (TextView)customView.findViewById(R.id.item_credit);
        TextView gradText = (TextView)customView.findViewById(R.id.item_grade);
     
        crsText.setText(li.getCourse());
        creText.setText(li.getCredit());
        gradText.setText(li.getGrade());
        String a =li.getGrade();
        if(li.converter(a)<2){
            gradText.setBackgroundColor(Color.RED);

        }




        return customView;
    }

}
