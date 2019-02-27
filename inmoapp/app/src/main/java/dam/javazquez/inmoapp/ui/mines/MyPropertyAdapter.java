package dam.javazquez.inmoapp.ui.mines;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyFavsResponse;
import dam.javazquez.inmoapp.retrofit.generator.AuthType;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import dam.javazquez.inmoapp.ui.mines.MyPropertyFragment.OnListFragmentInteractionListener;
import dam.javazquez.inmoapp.util.UtilToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**

 */
public class MyPropertyAdapter extends RecyclerView.Adapter<MyPropertyAdapter.ViewHolder> {

    private final List<PropertyFavsResponse> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context contexto;
    String jwt;
    PropertyService service;

    public MyPropertyAdapter(Context ctx, List<PropertyFavsResponse> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        contexto = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_myproperty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        jwt = UtilToken.getToken(contexto);
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getTitle());
        holder.price.setText(String.valueOf(Math.round(mValues.get(position).getPrice())) + "€");
        holder.size.setText(String.valueOf(Math.round(mValues.get(position).getSize())) + "/m2");
        holder.city.setText(mValues.get(position).getCity());
        if (holder.mItem.getPhotos().size() != 0) {
            Glide.with(holder.mView).load(holder.mItem.getPhotos().get(0))
                    .centerCrop()
                    .into(holder.photo);
        } else {
            Glide.with(holder.mView).load("https://www.esm.rochester.edu/uploads/NoPhotoAvailable.jpg")
                    .centerCrop()
                    .into(holder.photo);
        }

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });

        holder.delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
            builder.setTitle(R.string.title_add).setMessage(R.string.message_delete);
            builder.setPositiveButton(R.string.go, (dialog, which) ->
                    deleteProperty(holder));
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
                Log.d("Back", "Going back");
            });
            AlertDialog dialog = builder.create();

            dialog.show();

        });
    }

    public void deleteProperty(final ViewHolder holder) {
        String id = holder.mItem.getId();
        System.out.println(id);
        service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);
        Call<PropertyFavsResponse> callDelete = service.delete(id);
        callDelete.enqueue(new Callback<PropertyFavsResponse>() {
            @Override
            public void onResponse(Call<PropertyFavsResponse> call, Response<PropertyFavsResponse> response) {
                if(response.code() == 200 || response.code() == 204) {
                    Toast.makeText(contexto, "Property deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(contexto, "Error while deleting", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PropertyFavsResponse> call, Throwable t) {
                Toast.makeText(contexto, "Failure", Toast.LENGTH_SHORT).show();
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
        public final ImageButton edit;
        public final ImageButton delete;
        public PropertyFavsResponse mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price_property);
            size = view.findViewById(R.id.size);
            city = view.findViewById(R.id.city);
            photo = view.findViewById(R.id.photo);
            edit = view.findViewById(R.id.editButton);
            delete = view.findViewById(R.id.deleteButton);
        }
    }
}
