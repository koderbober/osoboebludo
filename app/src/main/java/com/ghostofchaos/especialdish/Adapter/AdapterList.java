package com.ghostofchaos.especialdish.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghostofchaos.especialdish.FontCache;
import com.ghostofchaos.especialdish.ListItem;
import com.ghostofchaos.especialdish.MainActivity;
import com.ghostofchaos.especialdish.Objects.Model;
import com.ghostofchaos.especialdish.R;
import com.ghostofchaos.especialdish.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ghost on 28.02.2016.
 */
public class AdapterList extends ArrayAdapter {

    String fontName = "fonts/Proxima Nova Light.otf";
    Typeface typeface;
    Context c;
    int res;
    ArrayList<Model> objs;
    String toolbarTitle;

    public AdapterList(Context context, int resource, ArrayList<Model> objs, String toolbarTitle) {
        super(context, resource, objs);
        this.objs = objs;
        this.res = resource;
        this.c = context;
        this.toolbarTitle = toolbarTitle;
        typeface = FontCache.getTypeface(fontName, context); // Typeface.createFromAsset(context.getAssets(), fontName);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null) {
            typeface = FontCache.getTypeface(fontName, c);
            convertView = View.inflate(c, res, null);
            holder = new ViewHolder();
            holder.cardView = (CardView)convertView.findViewById(R.id.cardView);
            holder.title = (TextView)convertView.findViewById(R.id.tvTextTitle);
            holder.title.setTypeface(typeface);
            holder.intro = (TextView)convertView.findViewById(R.id.tvTextIntro);
            holder.intro.setTypeface(typeface);
            holder.image = (ImageView)convertView.findViewById(R.id.ivImg);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(Html.fromHtml("<b>" + Utils.specCharactersHtmlToString(objs.get(position).getTitle() + "</b>")));
        holder.intro.setText(Utils.specCharactersHtmlToString(objs.get(position).getIntro()));
        Picasso.with(holder.image.getContext())
                .load("http://osoboebludo.com/uploads/content/" + objs.get(position).getImage())
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
                Intent intent = new Intent(c, ListItem.class);
                intent.putExtra("obj", objs.get(position));
                intent.putExtra("title", toolbarTitle);
                c.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        CardView cardView;
        TextView title;
        TextView intro;
        ImageView image;
    }
}
