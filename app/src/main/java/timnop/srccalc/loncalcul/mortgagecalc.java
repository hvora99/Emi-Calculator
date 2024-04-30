package timnop.srccalc.loncalcul;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class mortgagecalc extends AppCompatActivity {

    final Context context = this;

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    private double purchaseAmount = 0.0;
    private double downPaymentAmount = 0.0;
    private double interestRate = 1.0;
    private double value = 0.0;
    private int duration = 1;

    private TextView textViewDuration;

    private boolean isAdViewAdded;
    private @Nullable
    NativeBannerAd mNativeBannerAd;
    private NativeAdLayout mNativeBannerAdContainer;
    private LinearLayout mAdView;
    private FrameLayout mAdChoicesContainer;

    private @Nullable
    AdView bannerAdView;
    Boolean Istablate=false;
    private final String TAG = mortgagecalc.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgagecalc);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Mortgage Value");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadBnrAdView();

        Load_Native_Banner();


        TextView textViewPurchasePrice = (TextView) findViewById(R.id.textViewPurchasePrice);
        TextView textViewDownPaymentAmount = (TextView) findViewById(R.id.textViewDownPaymentAmount);
        TextView textViewInterestRate = (TextView) findViewById(R.id.textViewInterestRate);
        textViewDuration = (TextView) findViewById(R.id.textViewDuration);

        EditText editTextPurchasePrice = (EditText) findViewById(R.id.editTextPurchasePrice);
        editTextPurchasePrice.addTextChangedListener(this.getEditableTextWatcher(textViewPurchasePrice, "PP"));

        EditText editTextDownPaymentAmount = (EditText) findViewById(R.id.editTextDownPaymentAmount);
        editTextDownPaymentAmount.addTextChangedListener(this.getEditableTextWatcher(textViewDownPaymentAmount, "DPA"));

        EditText editTextInterestRate = (EditText) findViewById(R.id.editTextInterestRate);
        editTextInterestRate.addTextChangedListener(this.getEditableTextWatcher(textViewInterestRate, "IR"));

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration = progress;
                textViewDuration.setText(String.valueOf(progress));
                if (progress <= 0) {
                    seekBar.setProgress(1);
                    textViewDuration.setText(String.valueOf(1));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button calculate = (Button) findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double loanAmount = purchaseAmount - downPaymentAmount;
                Loan loan = new Loan(interestRate, duration, loanAmount);

                final AppCompatDialog dialog = new AppCompatDialog(context);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Result");

                TextView monthlyPayment = (TextView) dialog.findViewById(R.id.monthly_payment);
                assert monthlyPayment != null;
                monthlyPayment.setText(currencyFormat.format(loan.getMonthlyPayment()));

                TextView totalPayment = (TextView) dialog.findViewById(R.id.total_payment);
                assert totalPayment != null;
                totalPayment.setText(currencyFormat.format(loan.getTotalPayment()));

                // reset loan data
                loan.setAnnualInterestRate();
                loan.setNumberOfYears();
                loan.setLoanAmount();

                Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
                assert dialogButtonOk != null;
                dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }


    public TextWatcher getEditableTextWatcher(final TextView textView, final String type) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    value = Double.parseDouble(s.toString()) / 100.0;
                    switch (type) {
                        case "PP":
                            purchaseAmount = value;
                            textView.setText(currencyFormat.format(value));
                            break;
                        case "DPA":
                            downPaymentAmount = value;
                            textView.setText(currencyFormat.format(value));
                            break;
                        case "IR":
                            interestRate = value;
                            textView.setText(String.valueOf(value));
                            break;
                    }
                } catch (NumberFormatException e) {
                    textView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }


    public void Load_Native_Banner()
    {
        mNativeBannerAdContainer = findViewById(R.id.nativebanner);
        mNativeBannerAd = new NativeBannerAd(this, getResources().getString(R.string.nb1));


        LayoutInflater inflater = LayoutInflater.from(mortgagecalc.this);
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
                        mortgagecalc.this,
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
        mortgagecalc.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

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
        bannerAdView = new AdView(mortgagecalc.this, getResources().getString(R.string.banner1),
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

}
