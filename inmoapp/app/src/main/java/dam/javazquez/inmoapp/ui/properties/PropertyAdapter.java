package dam.javazquez.inmoapp.ui.properties;

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

import org.w3c.dom.Text;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PhotoResponse;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import dam.javazquez.inmoapp.retrofit.generator.AuthType;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import dam.javazquez.inmoapp.ui.login.LoginActivity;
import dam.javazquez.inmoapp.ui.properties.PropertyFragment.OnListFragmentInteractionListener;
import dam.javazquez.inmoapp.util.UtilToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static java.lang.Math.round;

/**
 *
 */
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {
    private PhotoResponse photo;
    private final List<PropertyResponse> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context contexto;
    String jwt;
    PropertyService service;

    public PropertyAdapter(Context ctx, List<PropertyResponse> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        contexto = ctx;
        jwt = UtilToken.getToken(ctx);
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
        holder.price.setText(String.valueOf(Math.round(mValues.get(position).getPrice()))+"€");
        holder.size.setText(String.valueOf(Math.round(mValues.get(position).getSize()))+"/m2");
        holder.city.setText(mValues.get(position).getCity());
        Glide.with(holder.mView).load("https://http2.mlstatic.com/piso-flotante-alto-transito-manta-zocalo-83-mm-ofertapack-D_NQ_NP_903122-MLA25601513684_052017-F.jpg").into(holder.photo);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
        holder.fav.setOnClickListener(v -> {
            if(holder.fav.getDrawable().getConstantState().equals(R.drawable.ic_no_fav)){
                if (jwt == null) {
                    Intent i = new Intent(contexto, LoginActivity.class);
                    contexto.startActivity(i);
                } else {
                    service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);

                    Call<PropertyResponse> call = service.addFav(holder.mItem.getId());
                    call.enqueue(new Callback<PropertyResponse>() {
                        @Override
                        public void onResponse(Call<PropertyResponse> call, Response<PropertyResponse> response) {
                            if (response.code() != 201) {
                                Toast.makeText(contexto, "Error in request", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(contexto, "Property created", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<PropertyResponse> call, Throwable t) {
                            Toast.makeText(contexto, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
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
        public final ImageView fav;
        public PropertyResponse mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            size = view.findViewById(R.id.size);
            city = view.findViewById(R.id.city);
            photo = view.findViewById(R.id.photo);
            fav = view.findViewById(R.id.favPrincipal);
        }

    }
}
