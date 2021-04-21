package eu.siacs.conversations;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


import static eu.siacs.conversations.SelectBusinessRollandType.dataModeltag;

public class AdapterForTag extends RecyclerView.Adapter<AdapterForTag.MyViewHolder> {

    private Context context;
    private ArrayList<String> textselected = new ArrayList<>();


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView text_title;
        ConstraintLayout layoutRoll;


        public MyViewHolder(View view) {
            super(view);

            this.imageView = (ImageView) view.findViewById(R.id.imageView2);
            this.text_title = (TextView) view.findViewById(R.id.tv_title);
            this.layoutRoll = view.findViewById(R.id.layoutRoll);
        }
    }

    public AdapterForTag(Context _context) {
        this.context = _context;
    }

    @Override
    public AdapterForTag.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_tag, parent, false);

        textselected = new ArrayList<>();

        // view.setOnClickListener(MainActivity.myOnClickListener);

        AdapterForTag.MyViewHolder myViewHolder = new AdapterForTag.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterForTag.MyViewHolder holder, final int position) {
        holder.text_title.setText(dataModeltag.get(position).getName());

        if (dataModeltag.get(position).isFlag()) {
            holder.text_title.setTag("selected");
            addToSelectedMap(String.valueOf(position), holder.text_title.getText().toString());
            holder.layoutRoll.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_edittext));
            holder.text_title.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_done), null, null, null);
            holder.text_title.setTextColor(context.getResources().getColor(R.color.green800));
        } else {
            holder.text_title.setTag("unselected");
            holder.text_title.setTextColor(context.getResources().getColor(R.color.white));
            holder.text_title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            removeFromSelectedMap(String.valueOf(position), holder.text_title.getText().toString());
            holder.layoutRoll.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rectangle_small_moreround));
        }


        holder.text_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.text_title.getTag() != null && holder.text_title.getTag().equals("selected")) {
//                    holder.imageView.setVisibility(View.INVISIBLE);
                    holder.text_title.setTag("unselected");
                    dataModeltag.get(position).setFlag(false);
                    holder.text_title.setTextColor(context.getResources().getColor(R.color.white));
                    holder.text_title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    removeFromSelectedMap(String.valueOf(position), holder.text_title.getText().toString());
                    holder.layoutRoll.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rectangle_small_moreround));
                } else {
//                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.text_title.setTag("selected");
                    dataModeltag.get(position).setFlag(true);
                    addToSelectedMap(String.valueOf(position), holder.text_title.getText().toString());
                    holder.layoutRoll.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_edittext));
                    holder.text_title.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_done), null, null, null);
                    holder.text_title.setTextColor(context.getResources().getColor(R.color.green800));
                }
                ((SelectBusinessRollandType)context).checkSlectedDataforTAG();
//                notifyDataSetChanged();
                Log.e("KING1995", "onClick: " + holder.text_title.getText().toString());
                Log.e("KING1995", "onClick: " + textselected.toString());
            }
        });

    }

    private void addToSelectedMap(String key, String value) {

        textselected.add(value);

    }


    private void removeFromSelectedMap(String key, String value) {

        textselected.remove(value);

    }

    @Override
    public int getItemCount() {
        return dataModeltag.size();
    }



}
