package dam.javazquez.inmoapp.ui.favs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.ui.favs.PropertyFavFragment.OnListFragmentInteractionListener;
import dam.javazquez.inmoapp.ui.favs.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PropertyFavAdapter extends RecyclerView.Adapter<PropertyFavAdapter.ViewHolder> {

    private final List<PropertyResponse> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context ctx;

    public PropertyFavAdapter(Context ctx, List<PropertyResponse> items, OnListFragmentInteractionListener listener) {
        this.ctx = ctx;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_propertyfav, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getTitle());
        holder.price.setText(String.valueOf(Math.round(mValues.get(position).getPrice())) + "€");
        holder.size.setText(String.valueOf(Math.round(mValues.get(position).getSize())) + "€");
        holder.city.setText(mValues.get(position).getCity());
        // aquí llamar al método getAll() de las fotos pasando la id del piso
        Glide.with(holder.mView).load("https://http2.mlstatic.com/piso-flotante-alto-transito-manta-zocalo-83-mm-ofertapack-D_NQ_NP_903122-MLA25601513684_052017-F.jpg").into(holder.photo);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
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
