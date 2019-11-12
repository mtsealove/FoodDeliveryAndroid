package com.mtsealove.github.food_delivery.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mtsealove.github.food_delivery.Design.ReviewRecyclerAdapter;
import com.mtsealove.github.food_delivery.Entity.Result;
import com.mtsealove.github.food_delivery.Entity.Review;
import com.mtsealove.github.food_delivery.LoginActivity;
import com.mtsealove.github.food_delivery.R;
import com.mtsealove.github.food_delivery.Restful.RestAPI;
import com.mtsealove.github.food_delivery.StoreActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.nio.channels.ClosedByInterruptException;
import java.util.List;

public class ReviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView reviewRv;
    String managerID;
    String tag = getClass().getSimpleName();
    EditText replyEt;
    Button replyBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReviewFragment() {
        // Required empty public constructor
    }

    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        replyBtn = view.findViewById(R.id.replyBtn);
        replyEt = view.findViewById(R.id.replyEt);
        reviewRv = view.findViewById(R.id.reviewRv);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        reviewRv.setLayoutManager(lm);

        GetReview();
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostReView();
            }
        });
        return view;
    }

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

    private void GetReview() {
        RestAPI restAPI = new RestAPI(getContext());
        Call<List<Review>> call = restAPI.getRetrofitService().GetReview(managerID);
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful()) {
                    ReviewRecyclerAdapter adapter = new ReviewRecyclerAdapter(getContext());
                    for (Review review : response.body()) {
                        Log.d(tag, review.toString());
                        adapter.addItem(review);
                    }
                    reviewRv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });
    }

    private void PostReView() {
        if (replyEt.getText().toString().length() == 0) {
            Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String memberID = null;
            if (LoginActivity.login == null) {
                Toast.makeText(getContext(), "로그인 후 작성하실 수 있습니다", Toast.LENGTH_SHORT).show();
                return;
            } else {
                memberID = LoginActivity.login.getID();
            }
            String content = replyEt.getText().toString();
            RestAPI restAPI = new RestAPI(getContext());
            Call<Result> call = restAPI.getRetrofitService().CreateReview(memberID, managerID, content);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getResult().equals("Ok")) {
                            Toast.makeText(getContext(), "리뷰가 작성되었습니다", Toast.LENGTH_SHORT).show();
                            replyEt.setText("");
                            GetReview();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {

                }
            });
        }
    }
}
