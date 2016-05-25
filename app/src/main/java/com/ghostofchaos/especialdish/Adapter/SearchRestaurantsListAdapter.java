package com.ghostofchaos.especialdish.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghostofchaos.especialdish.FontCache;
import com.ghostofchaos.especialdish.ListItem;
import com.ghostofchaos.especialdish.MainActivity;
import com.ghostofchaos.especialdish.Objects.RestaurantsModel;
import com.ghostofchaos.especialdish.R;
import com.ghostofchaos.especialdish.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ghost on 29.03.2016.
 */
public class SearchRestaurantsListAdapter extends ArrayAdapter {

    String fontName = "fonts/Proxima Nova Light.otf";
    Typeface typeface;
    Context c;
    int res;
    ArrayList<Object> objs;
    String toolbarTitle;

    public SearchRestaurantsListAdapter(Context context, int resource, ArrayList<Object> objs, String toolbarTitle) {
        super(context, resource, objs);
        this.objs = objs;
        this.res = resource;
        this.c = context;
        this.toolbarTitle = toolbarTitle;
        typeface = FontCache.getTypeface(fontName, context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView==null) {
            typeface = FontCache.getTypeface(fontName, c);
            convertView = View.inflate(c, res, null);
            holder = new ViewHolder();
            holder.cardView = (CardView)convertView.findViewById(R.id.cardView);
            holder.type = (TextView)convertView.findViewById(R.id.tvTextType);
            holder.type.setTypeface(typeface);
            holder.title = (TextView)convertView.findViewById(R.id.tvTextTitle);
            holder.title.setTypeface(typeface);
            holder.intro = (TextView)convertView.findViewById(R.id.tvTextIntro);
            holder.intro.setTypeface(typeface);
            holder.address = (TextView)convertView.findViewById(R.id.tvTextAddress);
            holder.address.setTypeface(typeface);
            holder.image = (ImageView)convertView.findViewById(R.id.ivImg);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RestaurantsModel model = (RestaurantsModel) objs.get(position);
        holder.title.setText(Html.fromHtml("<b>" + Utils.specCharactersHtmlToString(model.getTitle() + "</b>")));
        holder.intro.setText(Utils.specCharactersHtmlToString(model.getIntro()));
        holder.type.setText(Html.fromHtml("<i>" + Utils.specCharactersHtmlToString(model.getRestaurants_type()) + " " + "</i>"));
        holder.address.setText(Html.fromHtml("<i>" + Utils.specCharactersHtmlToString(model.getAddress()) + " " + "</i>"));
        Picasso.with(holder.image.getContext())
                .load("http://osoboebludo.com/" + model.getPhoto_small_path())
                .resize(MainActivity.width, 0)
                .into(holder.image);
        convertView.setTag(holder);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (holder.intro.getText().toString().equals(objs.get(position).getIntro())) {
                    holder.intro.setText(objs.get(position).getDescription());
                } else {
                    holder.intro.setText(objs.get(position).getIntro());
                }*/
                /*Intent intent = new Intent(c, ListItem.class);
                intent.putExtra("obj", objs.get(position));
                intent.putExtra("title", toolbarTitle);
                c.startActivity(intent);*/
            }
        });

        return convertView;
    }

    static class ViewHolder {
        CardView cardView;
        TextView title, intro, address, type;
        ImageView image;
    }
}
