package com.test.memo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.test.memo.db.Memo;
import com.test.memo.db.MemoType;
import com.test.memo.functions.Function;
import com.test.memo.observable.ObservableManager;
import com.test.memo.service.LongRunningService;
import com.test.memo.util.MemoAdapter;
import com.test.memo.util.MutiLayoutAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.View.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeftFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeftFragment extends Fragment implements OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private Button button;

    private ListView list_left_drawer;
    private ArrayList<Object> mData = null;
    private MutiLayoutAdapter myAdapter = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LeftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeftFragment newInstance(String param1, String param2) {
        LeftFragment fragment = new LeftFragment();
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
        View view=inflater.inflate(R.layout.fragment_left, container, false);
        bindView(view);
        button=view.findViewById(R.id.plus);
        button.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.plus:
                mData.add(mData.size(),"123");
                myAdapter.notifyDataSetChanged();
                button.setVisibility(GONE);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MemoType memoType=(MemoType)myAdapter.getItem(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("确认删除吗");
        builder.setTitle("提示");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                MemoType memoType=(MemoType)myAdapter.getItem(position);
                DataSupport.deleteAll(Memo.class, "memotype_id = ?", String.valueOf(memoType.getId()));
                memoType.delete();
                myAdapter.remove(memoType);
                mListener.onFragmentInteraction(Uri.parse("content://" + "com.fengge.demo" + "/people"));

//                getActivity().findViewById(R.id.content)
                arg0.dismiss();
            }
        });
        builder.create().show();
        return false;
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
    public void bindView(View view){

        List<MemoType> memoTypes= DataSupport.findAll(MemoType.class);
        Log.i("123",String.valueOf(memoTypes.size()));
        mData=new ArrayList<>();
        mData.addAll(memoTypes);



        list_left_drawer=view.findViewById(R.id.list_left_drawer);
        list_left_drawer.setOnItemLongClickListener(this);
        myAdapter = new MutiLayoutAdapter(getActivity(),mData);
        list_left_drawer.setAdapter(myAdapter);
    }
    public void updata(){
        myAdapter.clear();
        List<MemoType> memoTypes= DataSupport.findAll(MemoType.class);
    }



}
