package dam.javazquez.inmoapp.ui.properties;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import dam.javazquez.inmoapp.R;
import dam.javazquez.inmoapp.responses.PropertyResponse;
import dam.javazquez.inmoapp.responses.ResponseContainer;
import dam.javazquez.inmoapp.retrofit.generator.ServiceGenerator;
import dam.javazquez.inmoapp.retrofit.services.PropertyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {}
 * interface.
 */
public class PropertyFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    Context ctx;
    List<PropertyResponse> properties = new ArrayList<>();
    String jwt;
    PropertyService service;
    PropertyAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PropertyFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PropertyFragment newInstance(int columnCount) {
        PropertyFragment fragment = new PropertyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_properties_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            service = ServiceGenerator.createService(PropertyService.class);

            Call<ResponseContainer<PropertyResponse>> call = service.listProperties();
            call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
                @Override
                public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                    if (response.code() != 200) {
                        Toast.makeText(getActivity(), "Error in request", Toast.LENGTH_SHORT).show();
                    } else {
                        properties = response.body().getRows();

                        adapter = new PropertyAdapter(ctx, properties, mListener);
                        recyclerView.setAdapter(adapter);

                    }
                }

                @Override
                public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                    Log.e("NetworkFailure", t.getMessage());
                    Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });

        }
        return view;
    }


        @Override
        public void onAttach (Context context){
            super.onAttach(context);
            if (context instanceof OnListFragmentInteractionListener) {
                mListener = (OnListFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnListFragmentInteractionListener");
            }
        }

        @Override
        public void onDetach () {
            super.onDetach();
            mListener = null;
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p/>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface OnListFragmentInteractionListener {
            // TODO: Update argument type and name
            void onListFragmentInteraction(PropertyResponse item);
        }
    }
