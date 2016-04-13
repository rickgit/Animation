package edu.ptu.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eftimoff.androipathview.PathView;

public class MainActivity extends AppCompatActivity {

    private PathView pathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        pathView = (PathView) findViewById(R.id.pathView);
        final List<PathView> pathViews=new ArrayList<>(21);
        pathViews.add((PathView) findViewById(R.id.pathView));
        pathViews.add((PathView) findViewById(R.id.pathView2));
        pathViews.add((PathView) findViewById(R.id.pathView3));
        pathViews.add((PathView) findViewById(R.id.pathView4));
        pathViews.add((PathView) findViewById(R.id.pathView5));
        pathViews.add((PathView) findViewById(R.id.pathView6));
        pathViews.add((PathView) findViewById(R.id.pathView7));
        pathViews.add((PathView) findViewById(R.id.pathView8));
        pathViews.add((PathView) findViewById(R.id.pathView9));
        pathViews.add((PathView) findViewById(R.id.pathView10));
        pathViews.add((PathView) findViewById(R.id.pathView11));
        pathViews.add((PathView) findViewById(R.id.pathView12));
        pathViews.add((PathView) findViewById(R.id.pathView13));
        pathViews.add((PathView) findViewById(R.id.pathView14));
        pathViews.add((PathView) findViewById(R.id.pathView15));
        pathViews.add((PathView) findViewById(R.id.pathView16));
        pathViews.add((PathView) findViewById(R.id.pathView17));
        pathViews.add((PathView) findViewById(R.id.pathView18));
        pathViews.add((PathView) findViewById(R.id.pathView19));
        pathViews.add((PathView) findViewById(R.id.pathView20));
        pathViews.add((PathView) findViewById(R.id.pathView21));

        pathView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < pathViews.size(); i++) {
                    pathViews.get(i).getPathAnimator().
                            //pathView.getSequentialPathAnimator().
                                    delay(100).
                            duration(3000).
                            interpolator(new AccelerateDecelerateInterpolator()).
                            start();
                }
//                h.postDelayed(new Runnable(){
//
//                    @Override
//                    public void run() {
//                        pathView.performClick();
//                    }
//                },8000);


            }
        });
        */
    }

}
