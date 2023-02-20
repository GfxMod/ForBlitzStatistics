package ru.forblitz.statistics.data;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;

public class AdUtils {

    private final Activity activity;

    private long dateOfTheLastImpression = System.currentTimeMillis();

    public AdUtils(Activity activity) {
        this.activity = activity;
    }

    public BannerAdView createBanner(ViewGroup parent, int padding) {

        BannerAdView adView = new BannerAdView(activity);
        LinearLayout.LayoutParams adViewLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        adViewLayoutParams.setMargins(0, padding, 0, padding);
        adView.setLayoutParams(adViewLayoutParams);

        adView.setAdUnitId("R-M-2200226-1");
        adView.setAdSize(AdSize.stickySize( (int) Utils.pxToDp(
                activity, parent.getWidth()
        )));

        // Создание объекта таргетирования рекламы.
        final AdRequest adRequest = new AdRequest.Builder().build();

        // Регистрация слушателя для отслеживания событий, происходящих в баннерной рекламе.
        adView.setBannerAdEventListener(new BannerAdEventListener() {

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                Log.e(adRequestError.toString(), adRequestError.getDescription());
            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onLeftApplication() {

            }

            @Override
            public void onReturnedToApplication() {

            }

            @Override
            public void onImpression(@Nullable ImpressionData impressionData) {

            }
        });

        // Загрузка объявления.
        adView.loadAd(adRequest);

        return adView;

    }

    public void showInterstitial(Runnable runnable) {

        if (System.currentTimeMillis() - dateOfTheLastImpression >= 30000) {

            // Создание экземпляра InterstitialAd.
            InterstitialAd adView = new InterstitialAd(activity);
            adView.setAdUnitId("R-M-2200226-2");

            // Создание объекта таргетирования рекламы.
            final AdRequest adRequest = new AdRequest.Builder().build();

            // Регистрация слушателя для отслеживания событий, происходящих в рекламе.
            adView.setInterstitialAdEventListener(new InterstitialAdEventListener() {

                @Override
                public void onAdLoaded() {
                    adView.show();
                }

                @Override
                public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                    Log.e(adRequestError.toString(), adRequestError.getDescription());

                    runnable.run();

                    dateOfTheLastImpression = System.currentTimeMillis();
                }

                @Override
                public void onAdShown() {
                    runnable.run();

                    dateOfTheLastImpression = System.currentTimeMillis();
                }

                @Override
                public void onAdDismissed() {

                }

                @Override
                public void onAdClicked() {

                }

                @Override
                public void onLeftApplication() {

                }

                @Override
                public void onReturnedToApplication() {

                }

                @Override
                public void onImpression(@Nullable ImpressionData impressionData) {

                }

            });

            // Загрузка объявления.
            adView.loadAd(adRequest);

        } else {
            runnable.run();
        }

    }

}
