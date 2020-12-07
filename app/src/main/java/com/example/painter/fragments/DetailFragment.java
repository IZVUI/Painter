package com.example.painter.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.painter.MyColor;
import com.example.painter.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailFragment() {
        // Required empty public constructor
    }


    private long colorId;

    public void setColor(long id) {
        this.colorId = id;
        updateValue();
    }

    public int getColor() {
        MyColor myColor = MyColor.getCOLORS().get((int) colorId);
        return myColor.getColor();
    }

    private void updateValue() {
        View view = getView();
        if (view != null) {
            TextView title = (TextView) view.findViewById(R.id.Title);
            MyColor myColor = MyColor.getCOLORS().get((int) colorId);// Smth.smths[(int) smthId];
            title.setText(myColor.getName());

        }
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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
    //вызывается тогда, когда Android потребуется макет фрагмента
    //в этом методе необходимо сообщить, какой макет должен использоваться данным фрагментом.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
/*
фрагменты не являются виджетами или активностями.
Для поиска виджета - метод getView() фрагмента для получения ссылки на корневой виджет фрагмента,
 а затем - метод findViewById() виджета для получения дочерних виджетов.
*/
        View view = getView();
        if (view != null) {

            TextView title = (TextView) view.findViewById(R.id.Title);
            MyColor myColor = MyColor.getCOLORS().get((int) colorId);
            title.setText(myColor.getName());
            TextView value = (TextView) view.findViewById(R.id.Value);
            value.setBackgroundColor(myColor.getColor());
        }
    }
}
