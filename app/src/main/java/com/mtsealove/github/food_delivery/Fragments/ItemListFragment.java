package com.mtsealove.github.food_delivery.Fragments;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mtsealove.github.food_delivery.Design.MenuRecyclerAdapter;
import com.mtsealove.github.food_delivery.Entity.Menu;
import com.mtsealove.github.food_delivery.OrderSheetActivity;
import com.mtsealove.github.food_delivery.R;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import com.mtsealove.github.food_delivery.StoreActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String tag = getClass().getSimpleName();
    RecyclerView recyclerView;
    FloatingActionButton confirmBtn;
    public static ArrayList<Integer> orderList;
    private String managerID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ItemListFragment() {
        // Required empty public constructor
    }

    public static ItemListFragment newInstance(String param1, String param2) {
        ItemListFragment fragment = new ItemListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = view.findViewById(R.id.menuRv);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        confirmBtn = view.findViewById(R.id.confirmBtn);
        orderList = new ArrayList<>();
        GetItemList();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
        if (getActivity() != null && getActivity() instanceof StoreActivity) {
            managerID = ((StoreActivity) getActivity()).getManagerID();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void GetItemList() {
        RestAPI restAPI = new RestAPI(getContext());
        Call<List<Menu>> call = restAPI.getRetrofitService().GetMenuList(managerID);
        call.enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                if (response.isSuccessful()) {
                    MenuRecyclerAdapter adapter = new MenuRecyclerAdapter(getContext());
                    for (Menu menu : response.body()) {
                        Log.d(tag, menu.toString());
                        adapter.addItem(menu);
                    }
                    recyclerView.setAdapter(adapter);
                    confirmBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (orderList.size() == 0) {
                                Toast.makeText(getContext(), "선택한 상품이 없어요", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(getContext(), OrderSheetActivity.class);
                                intent.putIntegerArrayListExtra("orderList", orderList);
                                getContext().startActivity(intent);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {

            }
        });
    }
}
