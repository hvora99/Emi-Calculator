package timnop.srccalc.loncalcul;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class listview extends AppCompatActivity {



    ListView listView;
    String[] provinces = new String[]{
            "सिबिल क्या है? ",
            "मेरे लोन की मंजूरी पाने के लिए मेरा क्रेडिट स्कोर क्यों महत्वपूर्ण हैं?",
            "अपने क्रेडिट स्कोर को मैं कैसे सुधार सकता हूँ? ",
            "मेरे क्रेडिट स्कोर को कौन से प्रमुख कारक प्रभावित करते हैं?",
            "क्या सिबिल मेरे रिकार्डस को मिटा या बदल सकते हैं?",
            "जब मेरा स्कोर एनए या एनएच हो तो उसका क्या अर्थ होता है? ",
            "सिबिल ट्रांसयूनियन स्कोर 2.0 क्या है? ",
    };

    private boolean isAdViewAdded;
    private @Nullable
    NativeBannerAd mNativeBannerAd;
    private NativeAdLayout mNativeBannerAdContainer;
    private LinearLayout mAdView;
    private FrameLayout mAdChoicesContainer;

    private @Nullable
    AdView bannerAdView;
    Boolean Istablate=false;
    private final String TAG = listview.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Select Option");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        loadBnrAdView();

        Load_Native_Banner();


        listView=(ListView) findViewById(R.id.lvProvinceNames);

        listview.CustumAdapter custumAdapter= new listview.CustumAdapter();
        listView.setAdapter(custumAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                //we use the items of the listview as title of the next activity
                String province = provinces[position];

                //we retrieve the description of the juices from an array defined in arrays.xml
                String[] provincedescription = getResources().getStringArray(R.array.provincedescription);
                final String provincedesclabel = provincedescription[position];

                //retrieve content for the dialog
                String[] dialogmessage = getResources().getStringArray(R.array.dialogmessage);
                final String dialogmsg = dialogmessage[position];


                Intent intent = new Intent(getApplicationContext(), creditdetail.class);
                intent.putExtra("province", province);
                intent.putExtra("provincedesclabel", provincedesclabel);
                intent.putExtra("dialogmsg", dialogmsg);
                startActivity(intent);


            }
        });


    }



    public void Load_Native_Banner()
    {
        mNativeBannerAdContainer = findViewById(R.id.nativebanner);
        mNativeBannerAd = new NativeBannerAd(this, getResources().getString(R.string.nb));


        LayoutInflater inflater = LayoutInflater.from(listview.this);
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
                        listview.this,
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
        listview.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

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
        bannerAdView = new AdView(listview.this, getResources().getString(R.string.banner1),
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



    class CustumAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return provinces.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") View view=getLayoutInflater().inflate(R.layout.listlayout, null);
            TextView txt11=(TextView) view.findViewById(R.id.txt1);
            txt11.setText(provinces[position]);


            return view;
        }

    }
}
