package vs.voiceapp.com.voiceapp;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{

    TextView mStatusView;
    MediaRecorder mRecorder;
    Thread runner;
    public static int count=0;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    final Runnable updater = new Runnable(){
        public void run(){
            updateTv();
        }
    };
    final Handler mHandler = new Handler();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               setContentView(R.layout.activity_main);
        //for pop up
        ImageView image = new ImageView(MainActivity.this);
        image.setImageResource(R.drawable.pp);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(image);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setLayout(100,100);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //Controlling width and height.
        alertDialog.show();

        //----------end-----------
            final ImageButton imageButton = (ImageButton) findViewById(R.id.btnSpeak);
            mStatusView = (TextView) findViewById(R.id.txvResult);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (count % 2 == 0) {
                        if (count == 0) {
                            Toast.makeText(getApplicationContext(), "Sound START !", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Tap again to STOP !", Toast.LENGTH_SHORT).show();
                            startThread();
                            startRecorder();
                            count++;
                        } else {
                            Toast.makeText(getApplicationContext(), "Sound START !", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Tap again to STOP !", Toast.LENGTH_SHORT).show();
                            onAgainResume();
                            count++;
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), " Sound ''slowly'' STOP !", Toast.LENGTH_SHORT).show();
                        count++;
                        stopRecorder();
                    }
                }
            });

    }
    public void startThread() {
        if (runner == null) {
            runner = new Thread() {
                public void run() {
                    while (runner != null) {
                        try {
                            Thread.sleep(1000);
                            Log.i("Voice", "boom");
                        } catch (InterruptedException e) {
                        }
                        ;
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Voice", "start runner()");
        }
    }
    //This is my baby don't delete this code
//    public void minimizeApp()
//    {
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(startMain);
//    }
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
    public void onAgainResume()
    {
        startRecorder();
    }
    public void startRecorder(){
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                Log.e("[Error]", "IOException: " +
                        Log.getStackTraceString(ioe));

            }catch (SecurityException e) {
                Log.e("[Error]", "SecurityException: " +
                        Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (SecurityException e) {
                Log.e("[Error]", "SecurityException: " +
                        Log.getStackTraceString(e));
            }

            //mEMA = 0.0;
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double updateTv(){
//        mStatusView.setText(Double.toString((getAmplitudeEMA())) + " dB");
//       System.out.println("App Running"+String.format("%.2f",getAmplitudeEMA()));
        mStatusView.setText( String.format("%.2f",getAmplitudeEMA())+ " dB");
        return getAmplitudeEMA();
    }
    public double soundDb(double ampl){
        return  20 * Math.log10(getAmplitudeEMA() / ampl);
    }
    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
}