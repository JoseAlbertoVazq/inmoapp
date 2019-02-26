package dam.javazquez.inmoapp.ui.favs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyFavsResponse;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.retrofit.generator.AuthType;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import dam.javazquez.inmoapp.ui.favs.PropertyFavFragment.OnListFragmentInteractionListener;
import dam.javazquez.inmoapp.ui.login.LoginActivity;
import dam.javazquez.inmoapp.util.UtilToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class PropertyFavAdapter extends RecyclerView.Adapter<PropertyFavAdapter.ViewHolder> {

    private final List<PropertyFavsResponse> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context contexto;
    PropertyService service;
    String jwt;

    public PropertyFavAdapter(Context ctx, List<PropertyFavsResponse> items, OnListFragmentInteractionListener listener) {
        this.contexto = ctx;
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
        jwt = UtilToken.getToken(contexto);
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getTitle());
        holder.price.setText(String.valueOf(Math.round(mValues.get(position).getPrice())) + "€");
        holder.size.setText(String.valueOf(Math.round(mValues.get(position).getSize())) + "€");
        holder.city.setText(mValues.get(position).getCity());
        // aquí llamar al método getAll() de las fotos pasando la id del piso
        Glide.with(holder.mView)
                .load("https://http2.mlstatic.com/piso-flotante-alto-transito-manta-zocalo-83-mm-ofertapack-D_NQ_NP_903122-MLA25601513684_052017-F.jpg")
                .centerCrop()
                .into(holder.photo);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });

        holder.fav.setOnClickListener(v -> {


            if (jwt == null) {
                Intent i = new Intent(contexto, LoginActivity.class);
                contexto.startActivity(i);
            } else {

                service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);

                Call<PropertyResponse> call = service.deleteFav(holder.mItem.getId());
                call.enqueue(new Callback<PropertyResponse>() {
                    @Override
                    public void onResponse(Call<PropertyResponse> call, Response<PropertyResponse> response) {
                        if (response.code() != 200) {
//                            Toast.makeText(contexto, "Error in request", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(contexto, "Deleted from favourites", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PropertyResponse> call, Throwable t) {
//                        Toast.makeText(contexto, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            holder.fav.setImageResource(R.drawable.ic_no_fav);

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
        public final ImageView fav;
        public PropertyFavsResponse mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price_property);
            size = view.findViewById(R.id.size);
            city = view.findViewById(R.id.city);
            photo = view.findViewById(R.id.photo);
            fav = view.findViewById(R.id.favFavs);
        }
    }
}
