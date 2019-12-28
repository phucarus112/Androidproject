package com.example.project.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.DnsResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.IforStopPoint;
import com.example.project.APIConnect.InfoTourResponse;
import com.example.project.APIConnect.InforComment;
import com.example.project.APIConnect.ListToursResponse;
import com.example.project.APIConnect.MyMember;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.StopPointObjectEdit;
import com.example.project.APIConnect.Tour;
import com.example.project.APIConnect.commentList;
import com.example.project.APIConnect.listCommentResponse;
import com.example.project.Adapter.commentAdapter;
import com.example.project.Adapter.memberAdapter;
import com.example.project.Adapter.stopPointAdapter;
import com.example.project.R;

import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.getIntent;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Detail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Detail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Detail.
     */
    // TODO: Rename and change types and number of parameters
    public static Detail newInstance(String param1, String param2) {
        Detail fragment = new Detail();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_detail, null);
        final String token = getActivity().getIntent().getExtras().getString("token");
        final Integer id = getActivity().getIntent().getExtras().getInt("tourId");
        Integer userId=getActivity().getIntent().getExtras().getInt("userId");
        // Toast.makeText(getActivity(),id.toString(),Toast.LENGTH_SHORT).show();

        final InfoTourResponse A = new InfoTourResponse();
        Retrofit retrofit = RetrofitClient.getClient();
        final APIService apiService = retrofit.create(APIService.class);
        apiService.getResponseInfoTour(token,id).enqueue(new Callback<InfoTourResponse>() {
                                                             @Override
                                                             public void onResponse(Call<InfoTourResponse> call, Response<InfoTourResponse> response) {
                                                                 ((TextView)view.findViewById(R.id.tvName1)).setText(response.body().getName());

                                                                 ((TextView)view.findViewById(R.id.tvLich)).setText(response.body().getStartDate()+" - "+response.body().getEndDate());
                                                                 ((TextView)view.findViewById(R.id.tvNguoi1)).setText(response.body().getAdults().toString());
                                                                 ((TextView)view.findViewById(R.id.tvNguoi2)).setText(response.body().getChilds().toString());

                                                                 ((TextView)view.findViewById(R.id.tvGia)).setText(response.body().getMinCost()+" - "+response.body().getMaxCost());
                                                                 apiService.getComment(token,id,1,200).enqueue(new Callback<listCommentResponse>() {
                                                                     @Override
                                                                     public void onResponse(Call<listCommentResponse> call, Response<listCommentResponse> response) {

                                                                         if(response.isSuccessful())
                                                                         {
                                                                             final ArrayList<commentList> list;
                                                                             list = (ArrayList<commentList>) response.body().getCommentList();
                                                                             ArrayList<commentList> list1 = new ArrayList<commentList>();
                                                                             int j=0;
                                                                             for(int i=0;i<list.size();i++)
                                                                             {
                                                                                 if(list.get(i).getComment().isEmpty()==false)
                                                                                 {

                                                                                     if(list.get(i).getName().isEmpty()==false)
                                                                                     { list1.add(j,list.get(i));
                                                                                         j++;}
                                                                                 }
                                                                             }
                                                                             RecyclerView listComment;
                                                                             listComment=((RecyclerView)view.findViewById(R.id.rvComment));
                                                                             commentAdapter cmadapter = new commentAdapter(list1);
                                                                             listComment.setAdapter(cmadapter);
                                                                             listComment.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                                             Log.e("Response", "kkk");
                                                                             // Log.e("Response", "kkk");
                                                                             // Toast.makeText(getActivity(),list1.get(0).getComment(),Toast.LENGTH_SHORT).show();
                                                                         }
                                                                     }

                                                                     @Override
                                                                     public void onFailure(Call<listCommentResponse> call, Throwable t) {

                                                                     }
                                                                 });

                                                                 final RecyclerView listMember;
                                                                 listMember=((RecyclerView)view.findViewById(R.id.rvTuyChon));
                                                                 Button btMember=((Button)view.findViewById(R.id.btMember));
                                                                 Button btStopPoint=((Button)view.findViewById(R.id.btstopPointList));
                                                                 final  ArrayList< MyMember> list2=(ArrayList<MyMember>) response.body().getMembers();
                                                                 final  ArrayList<StopPointObjectEdit> list3=(ArrayList<StopPointObjectEdit>) response.body().getStopPoints();

                                                                 btStopPoint.setOnClickListener(new OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {
                                                                         stopPointAdapter cmadapter = new stopPointAdapter(list3,getContext());
                                                                         listMember.setAdapter(cmadapter);
                                                                         listMember.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                                     }
                                                                 });
                                                                 btMember.setOnClickListener(new OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {
                                                                         memberAdapter cmadapter = new memberAdapter(list2);
                                                                         listMember.setAdapter(cmadapter);
                                                                         listMember.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                                     }
                                                                 });

                                                                 final EditText edtext=((EditText)view.findViewById(R.id.edComment));
                                                                 TextView OurName=((TextView)view.findViewById(R.id.tvNamecmour));
                                                                 //
                                                                 ImageButton ButtonSend=((ImageButton)view.findViewById(R.id.imageButtoncm));
                                                                 ButtonSend.setOnClickListener(new OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {
                                                                         String X=edtext.getText().toString();
                                                                         edtext.setText("");
                                                                         Retrofit retrofit1 = RetrofitClient.getClient();
                                                                         final APIService apiService1 = retrofit1.create(APIService.class);
                                                                         apiService1.sendComment(token,id.toString(),"336",X).enqueue(new Callback<String>() {
                                                                             @Override
                                                                             public void onResponse(Call<String> call, Response<String> response) {


                                                                                 if(response.isSuccessful())
                                                                                 {
                                                                                 }
                                                                             }
                                                                             @Override
                                                                             public void onFailure(Call<String> call, Throwable t) {

                                                                             }
                                                                         });
                                                                         apiService1.getComment(token,id,1,200).enqueue(new Callback<listCommentResponse>() {
                                                                             @Override
                                                                             public void onResponse(Call<listCommentResponse> call, Response<listCommentResponse> response) {

                                                                                 if(response.isSuccessful())
                                                                                 {


                                                                                     final ArrayList<commentList> list;
                                                                                     list = (ArrayList<commentList>) response.body().getCommentList();
                                                                                     ArrayList<commentList> list1 = new ArrayList<commentList>();
                                                                                     int j=0;
                                                                                     for(int i=0;i<list.size();i++)
                                                                                     {

                                                                                         if(list.get(i).getComment().isEmpty()==false)
                                                                                         {

                                                                                             if(list.get(i).getName().isEmpty()==false)
                                                                                             { list1.add(j,list.get(i));
                                                                                                 j++;}
                                                                                         }
                                                                                     }
                                                                                     RecyclerView listComment;
                                                                                     listComment=((RecyclerView)view.findViewById(R.id.rvComment));
                                                                                     commentAdapter cmadapter = new commentAdapter(list1);
                                                                                     listComment.setAdapter(cmadapter);
                                                                                     listComment.setLayoutManager(new LinearLayoutManager(getActivity()));

                                                                                     // Toast.makeText(getActivity(),list1.get(0).getComment(),Toast.LENGTH_SHORT).show();
                                                                                 }
                                                                             }

                                                                             @Override
                                                                             public void onFailure(Call<listCommentResponse> call, Throwable t) {

                                                                             }
                                                                         });
                                                                     }
                                                                 });
                                                                 //  Toast.makeText(getActivity(),response.body().getId().toString(),Toast.LENGTH_SHORT).show();

                                                             }

                                                             @Override
                                                             public void onFailure(Call<InfoTourResponse> call, Throwable t) {

                                                             }
                                                         }
        );

        return  view;
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