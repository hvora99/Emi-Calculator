package timnop.srccalc.loncalcul;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
 import com.facebook.ads.NativeAdListener;

import java.util.Objects;

public class Loancalcfinal extends AppCompatActivity {



    private @Nullable
    AdView bannerAdView;
    Boolean Istablate=false;

    private @Nullable AdView rectangleAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loancalcfinal);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Final Payment");



        DisplayMetrics metrics = new DisplayMetrics();
        Loancalcfinal.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        Istablate = diagonalInches >= 7.5;



        loadBnrAdView();

        Load_Rectangular_Banner();

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView emi_re = (TextView) findViewById(R.id.emi);
        TextView intrest_re = (TextView) findViewById(R.id.interest_total);
        Intent intent=getIntent();

        String title2 = Objects.requireNonNull(intent.getExtras()).getString("Key");
        String title1 = intent.getExtras().getString("Key1");


        emi_re.setText(title1);
        intrest_re.setText(title2);

        emi_re.setEnabled(false);
        intrest_re.setEnabled(false);

    }


    public void Load_Rectangular_Banner()
    {

        RelativeLayout rectangleAdContainer = (RelativeLayout) findViewById(R.id.rectangleAdContainer);
        if (rectangleAdView != null) {
            rectangleAdView.destroy();
            rectangleAdView = null;
        }

        // Update progress message


        // Create a banner's ad view with a unique placement ID (generate your own on the Facebook
        // app settings). Use different ID for each ad placement in your app.
        rectangleAdView = new AdView(this, getResources().getString(R.string.mediumrec1),
                AdSize.RECTANGLE_HEIGHT_250);

        // Reposition the ad and add it to the view hierarchy.
        rectangleAdContainer.addView(rectangleAdView);

        // Set a listener to get notified on changes or when the user interact with the ad.
        rectangleAdView.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        // Initiate a request to load an ad.
        rectangleAdView.loadAd();



    }

    private void loadBnrAdView() {

        DisplayMetrics metrics = new DisplayMetrics();
        Loancalcfinal.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        Istablate = diagonalInches >= 7.5;

        RelativeLayout banneradscon = (RelativeLayout) findViewById(R.id.mybanner);

        if (bannerAdView != null) {
            bannerAdView.destroy();
            bannerAdView = null;
        }



        // Create a banner's ad view with a unique placement ID (generate your own on the Facebook
        // app settings). Use different ID for each ad placement in your app.
        bannerAdView = new AdView(Loancalcfinal.this, getResources().getString(R.string.banner),
                Istablate ? AdSize.BANNER_HEIGHT_90 : AdSize.BANNER_HEIGHT_50);

        // Reposition the ad and add it to the view hierarchy.
        banneradscon.addView(bannerAdView);

        // Set a listener to get notified on changes or when the user interact with the ad.

        // Initiate a request to load an ad.
        bannerAdView.loadAd();


    }

    @Override
    public void onDestroy() {
        if (bannerAdView != null) {
            bannerAdView.destroy();
            bannerAdView = null;
        }
        super.onDestroy();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            Loancalcfinal.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
