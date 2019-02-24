package dam.javazquez.inmoapp.ui.properties;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PhotoResponse;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.ui.properties.PropertyFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 *
 */
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {
    private PhotoResponse photo;
    private final List<PropertyResponse> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context ctx;


    public PropertyAdapter(Context ctx, List<PropertyResponse> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getTitle());
        holder.price.setText(String.valueOf(mValues.get(position).getPrice()));
        holder.size.setText(String.valueOf(mValues.get(position).getSize()));
        holder.city.setText(mValues.get(position).getCity());
        Glide.with(holder.mView).load("https://imgur.com/RtGWDaA").into(holder.photo);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final TextView price;
        public final TextView size;
        public final TextView city;
        public final ImageView photo;
        public PropertyResponse mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            size = view.findViewById(R.id.size);
            city = view.findViewById(R.id.city);
            photo = view.findViewById(R.id.photo);
        }

    }
}
