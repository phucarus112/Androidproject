package com.example.project.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.InfoTourResponse;
import com.example.project.APIConnect.PointStat;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.ReviewList;
import com.example.project.APIConnect.commentList;
import com.example.project.APIConnect.pointResponse;
import com.example.project.APIConnect.reviewResponse;
import com.example.project.Adapter.reviewAdapter;
import com.example.project.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Review.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Review#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Review extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Review() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Review.
     */
    // TODO: Rename and change types and number of parameters
    public static Review newInstance(String param1, String param2) {
        Review fragment = new Review();
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
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_review, null);
        final String token = getActivity().getIntent().getExtras().getString("token");
        final Integer id = getActivity().getIntent().getExtras().getInt("tourId");
        Integer userId=getActivity().getIntent().getExtras().getInt("userId");
        final InfoTourResponse A = new InfoTourResponse();
        Retrofit retrofit = RetrofitClient.getClient();
        final APIService apiService = retrofit.create(APIService.class);
        apiService.getReview(token,id,1,200).enqueue(new Callback<reviewResponse>() {
            @Override
            public void onResponse(Call<reviewResponse> call, Response<reviewResponse> response) {
                ArrayList<ReviewList> list1 = (ArrayList<ReviewList>) response.body().getReviewList();
                RecyclerView listComment;
                listComment=((RecyclerView)view.findViewById(R.id.rvReview));
                reviewAdapter cmadapter = new reviewAdapter(list1);
                listComment.setAdapter(cmadapter);
                listComment.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onFailure(Call<reviewResponse> call, Throwable t) {

            }
        });
        apiService.getPointReview(token,id).enqueue(new Callback<pointResponse>() {
            @Override
            public void onResponse(Call<pointResponse> call, Response<pointResponse> response) {
                TextView tyLe= ((TextView)view.findViewById(R.id.tyLe));
                RatingBar rating=((RatingBar)view.findViewById(R.id.rating));
                TextView Tong=((TextView)view.findViewById(R.id.tongDanhGia));
                ProgressBar pro1=((ProgressBar)view.findViewById(R.id.progressBar1));
                ProgressBar pro2=((ProgressBar)view.findViewById(R.id.progressBar2));
                ProgressBar pro3=((ProgressBar)view.findViewById(R.id.progressBar3));
                ProgressBar pro4=((ProgressBar)view.findViewById(R.id.progressBar4));
                ProgressBar pro5=((ProgressBar)view.findViewById(R.id.progressBar5));

                ArrayList<PointStat> list =(ArrayList<PointStat>) response.body().getPointStats();

                Float tl;
                Integer tg=list.get(0).getTotal()+list.get(1).getTotal()+list.get(2).getTotal()+list.get(3).getTotal()+list.get(4).getTotal();
                Tong.setText(tg.toString());
                tl= (float) ((list.get(0).getPoint() * list.get(0).getTotal()+list.get(1).getPoint() * list.get(1).getTotal()+ list.get(2).getPoint() * list.get(2).getTotal()+list.get(3).getPoint() * list.get(3).getTotal()+list.get(4).getPoint() * list.get(4).getTotal())/tg);
                rating.setRating(tl);
                tyLe.setText(tl.toString()+"/5");

                pro1.setProgress(100*list.get(0).getTotal()/tg);
                pro2.setProgress((int)(100*list.get(1).getTotal()/tg));
                pro3.setProgress((int)100*list.get(2).getTotal()/tg);
                pro4.setProgress((int)100*list.get(3).getTotal()/tg);
                pro5.setProgress((int)100*list.get(4).getTotal()/tg);


            }

            @Override
            public void onFailure(Call<pointResponse> call, Throwable t) {

            }
        });
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
