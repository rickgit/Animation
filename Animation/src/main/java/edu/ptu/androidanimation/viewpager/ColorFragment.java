package edu.ptu.androidanimation.viewpager;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import edu.ptu.androidanimation.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ColorFragment extends android.support.v4.app.Fragment {


    public ColorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        Random random = new Random();
        byte[] buf = new byte[3];
        random.nextBytes(buf);
        textView.setBackgroundColor(0xff<<24&buf[0]<< 16 & buf[1] << 8 & buf[2]);
        return textView;
    }


}
