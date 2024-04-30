package timnop.srccalc.loncalcul;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;

import java.util.ArrayList;
import java.util.List;

public class loncalc extends AppCompatActivity {

    Button emiCalcBtn;

    private boolean isAdViewAdded;
    private @Nullable
    NativeBannerAd mNativeBannerAd;
    private NativeAdLayout mNativeBannerAdContainer;
    private LinearLayout mAdView;
    private FrameLayout mAdChoicesContainer;

    private @Nullable
    AdView bannerAdView;
    Boolean Istablate=false;
    private final String TAG = loncalc.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loncalc);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Loan Calculator");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        loadBnrAdView();

        Load_Native_Banner();


        final EditText P = (EditText)findViewById(R.id.principal);
        final EditText I = (EditText)findViewById(R.id.interest);
        final EditText Y = (EditText)findViewById(R.id.years);
        final EditText TI = (EditText)findViewById(R.id.interest_total);




        final EditText result = (EditText)findViewById(R.id.emi) ;


        emiCalcBtn = (Button) findViewById(R.id.btn_calculate2);

        emiCalcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String st1 = P.getText().toString();
                String st2 = I.getText().toString();
                String st3 = Y.getText().toString();

                if (TextUtils.isEmpty(st1)) {
                    P.setError("Enter Prncipal Amount");
                    P.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(st2)) {
                    I.setError("Enter Interest Rate");
                    I.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(st3)) {
                    Y.setError("Enter Years");
                    Y.requestFocus();
                    return;
                }

                float p = Float.parseFloat(st1);
                float i = Float.parseFloat(st2);
                float y = Float.parseFloat(st3);

                float Principal = calPric(p);

                float Rate = calInt(i);

                float Months = calMonth(y);

                float Dvdnt = calDvdnt( Rate, Months);

                float FD = calFinalDvdnt (Principal, Rate, Dvdnt);

                float D = calDivider(Dvdnt);

                float emi = calEmi(FD, D);

                float TA = calTa (emi, Months);

                float ti = calTotalInt(TA, Principal);



                result.setText(String.valueOf(emi));




                TI.setText(String.valueOf(ti));

                String emi_result = TI.getText().toString();
                String total_intrest = result.getText().toString();

                TI.setVisibility(View.INVISIBLE);
                result.setVisibility(View.INVISIBLE);

                Intent ii = new Intent(loncalc.this, Loancalcfinal.class);
                Bundle b = new Bundle();
                b.putString("Key", emi_result);
                b.putString("Key1",total_intrest);
                ii.putExtras(b);
                startActivity(ii);



            }
        });
    }


    public void Load_Native_Banner()
    {
        mNativeBannerAdContainer = findViewById(R.id.nativebanner);
        mNativeBannerAd = new NativeBannerAd(this, getResources().getString(R.string.nb1));


        LayoutInflater inflater = LayoutInflater.from(loncalc.this);
        mAdView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_unit, mNativeBannerAdContainer, false);

        mAdChoicesContainer = mAdView.findViewById(R.id.ad_choices_container);

        mNativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {



            }

            @Override
            public void onAdLoaded(Ad ad) {



                if (mNativeBannerAd == null || mNativeBannerAd != ad) {
                    // Race condition, load() called again before last ad was displayed
                    return;
                }
                if (!isAdViewAdded) {
                    isAdViewAdded = true;
                    mNativeBannerAdContainer.addView(mAdView);
                }
                // Unregister last ad
                mNativeBannerAd.unregisterView();

                AdOptionsView adOptionsView = new AdOptionsView(
                        loncalc.this,
                        mNativeBannerAd,
                        mNativeBannerAdContainer,
                        AdOptionsView.Orientation.HORIZONTAL,
                        20);
                mAdChoicesContainer.removeAllViews();
                mAdChoicesContainer.addView(adOptionsView);

                inflateAd(mNativeBannerAd, mAdView);

                mNativeBannerAd.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            int i = view.getId();
                            if (i == R.id.native_ad_call_to_action) {
                                Log.d(TAG, "Call to action button clicked");
                            } else if (i == R.id.native_icon_view) {
                                Log.d(TAG, "Main image clicked");
                            } else {
                                Log.d(TAG, "Other ad component clicked");
                            }
                        }
                        return false;
                    }
                });

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        mNativeBannerAd.loadAd(NativeAdBase.MediaCacheFlag.ALL);
    }


    private void inflateAd(NativeBannerAd nativeBannerAd, View adView) {
        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());

        // You can use the following to specify the clickable areas.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(
                mNativeBannerAdContainer,
                nativeAdIconView,
                clickableViews);


    }



    private void loadBnrAdView() {

        DisplayMetrics metrics = new DisplayMetrics();
        loncalc.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

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
        bannerAdView = new AdView(loncalc.this, getResources().getString(R.string.banner1),
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

        if (mNativeBannerAd != null) {
            mNativeBannerAd.destroy();
            mNativeBannerAd = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public  float calPric(float p) {

        return (float) (p);

    }

    public  float calInt(float i) {

        return (float) (i/12/100);

    }

    public  float calMonth(float y) {

        return (float) (y * 12);

    }

    public  float calDvdnt(float Rate, float Months) {

        return (float) (Math.pow(1+Rate, Months));

    }

    public  float calFinalDvdnt(float Principal, float Rate, float Dvdnt) {

        return (float) (Principal * Rate * Dvdnt);

    }

    public  float calDivider(float Dvdnt) {

        return (float) (Dvdnt-1);

    }

    public  float calEmi(float FD, Float D) {

        return (float) (FD/D);

    }

    public  float calTa(float emi, Float Months) {

        return (float) (emi*Months);

    }

    public  float calTotalInt(float TA, float Principal) {

        return (float) (TA - Principal);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            loncalc.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
