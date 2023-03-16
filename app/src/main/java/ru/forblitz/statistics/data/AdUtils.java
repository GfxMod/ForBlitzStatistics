package ru.forblitz.statistics.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;

import java.util.HashMap;
import java.util.Objects;

public class AdUtils {

    private final Activity activity;
    private final HashMap<Integer, Long> banners = new HashMap<>();

    private long dateOfTheLastImpression = System.currentTimeMillis();

    public AdUtils(Activity activity) {
        this.activity = activity;
    }

    public BannerAdView setBanner(Context context, int width, int padding, BannerAdView adView) {

        ConstraintLayout.LayoutParams adViewLayoutParams = new ConstraintLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        adViewLayoutParams.setMargins(0, padding, 0, 0);
        adView.setLayoutParams(adViewLayoutParams);
        adView.setGravity(Gravity.BOTTOM);

        if (!banners.containsKey(adView.getId())) {
            banners.put(adView.getId(), 0L);
            adView.setAdUnitId(Constants.bannerAdUnitId);
            adView.setAdSize(AdSize.stickySize((int) Utils.pxToDp(context, width)));
        }

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
        if (System.currentTimeMillis() - Objects.requireNonNull(banners.get(adView.getId())) >= 2000L) {
            banners.replace(adView.getId(), System.currentTimeMillis());
            adView.loadAd(adRequest);
        }
        return adView;

    }

    public void showInterstitial(Runnable runnable) {

        if (System.currentTimeMillis() - dateOfTheLastImpression >= 30000) {

            // Создание экземпляра InterstitialAd.
            InterstitialAd adView = new InterstitialAd(activity);

            adView.setAdUnitId(Constants.interstitialAdUnitId);

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
