package com.example.forblitzstatistics.data;

import static android.graphics.Typeface.BOLD;
import static android.view.Gravity.CENTER;
import static androidx.core.widget.TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.forblitzstatistics.R;

import java.util.Objects;

/**
 * Object containing all the vehicle statistical data
 */
public class Vehicle extends StatisticsData {

    private String id;
    private String name;
    private String nation;
    private String tier;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Creates a layout with all the information about the vehicle
     * @param activity required to get resources
     */
    @SuppressLint("RestrictedApi")
    public void create(Activity activity) {

        int displayWidth = Utils.getX(activity);
        int padding = displayWidth / 1000 * 25;
        LinearLayout tanksListLayout = activity.findViewById(R.id.tanks_list_layout);

        int battlesColor;
        if (Integer.parseInt(getBattles()) < 100) {
            battlesColor = ContextCompat.getColor(activity, R.color.white);
        } else if (Integer.parseInt(getBattles()) < 250) {
            battlesColor = ContextCompat.getColor(activity, R.color.green);
        } else if (Integer.parseInt(getBattles()) < 500) {
            battlesColor = ContextCompat.getColor(activity, R.color.blue);
        } else {
            battlesColor = ContextCompat.getColor(activity, R.color.violet);
        }

        int averageDamageColor;
        if (Objects.equals(tier, "10")) {
            if (Double.parseDouble(getAverageDamage()) < 2000) {
                averageDamageColor = ContextCompat.getColor(activity, R.color.white);
            } else if (Double.parseDouble(getAverageDamage()) < 2700) {
                averageDamageColor = ContextCompat.getColor(activity, R.color.green);
            } else if (Double.parseDouble(getAverageDamage()) < 3300) {
                averageDamageColor = ContextCompat.getColor(activity, R.color.blue);
            } else {
                averageDamageColor = ContextCompat.getColor(activity, R.color.violet);
            }
        } else if (Objects.equals(tier, "9")) {
            if (Double.parseDouble(getAverageDamage()) < 1700) {
                averageDamageColor = ContextCompat.getColor(activity, R.color.white);
            } else if (Double.parseDouble(getAverageDamage()) < 2200) {
                averageDamageColor = ContextCompat.getColor(activity, R.color.green);
            } else if (Double.parseDouble(getAverageDamage()) < 2600) {
                averageDamageColor = ContextCompat.getColor(activity, R.color.blue);
            } else {
                averageDamageColor = ContextCompat.getColor(activity, R.color.violet);
            }
        } else if (Objects.equals(tier, "8")) {
            if (Double.parseDouble(getAverageDamage()) < 1300) {
                averageDamageColor = ContextCompat.getColor(activity, R.color.white);
            } else if (Double.parseDouble(getAverageDamage()) < 1800) {
                averageDamageColor = ContextCompat.getColor(activity, R.color.green);
            } else if (Double.parseDouble(getAverageDamage()) < 2200) {
                averageDamageColor = ContextCompat.getColor(activity, R.color.blue);
            } else {
                averageDamageColor = ContextCompat.getColor(activity, R.color.violet);
            }
        } else {
            averageDamageColor = ContextCompat.getColor(activity, R.color.white);
        }

        int winRateColor;
        if (Double.parseDouble(getWinRate()) < 50) {
            winRateColor = ContextCompat.getColor(activity, R.color.white);
        } else if (Double.parseDouble(getWinRate()) < 60) {
            winRateColor = ContextCompat.getColor(activity, R.color.green);
        } else if (Double.parseDouble(getWinRate()) < 70) {
            winRateColor = ContextCompat.getColor(activity, R.color.blue);
        } else {
            winRateColor = ContextCompat.getColor(activity, R.color.violet);
        }

        int efficiencyColor;
        if (Double.parseDouble(getEfficiency()) < 1) {
            efficiencyColor = ContextCompat.getColor(activity, R.color.white);
        } else if (Double.parseDouble(getEfficiency()) < 1.5) {
            efficiencyColor = ContextCompat.getColor(activity, R.color.green);
        } else if (Double.parseDouble(getEfficiency()) < 2) {
            efficiencyColor = ContextCompat.getColor(activity, R.color.blue);
        } else {
            efficiencyColor = ContextCompat.getColor(activity, R.color.violet);
        }

        //

        LinearLayout mainLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        mainLayoutParams.setMargins(0, padding, 0, padding);
        mainLayout.setLayoutParams(mainLayoutParams);
        mainLayout.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_layout));
        mainLayout.setPadding(padding, padding, padding, padding);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        //

        AppCompatTextView nameVehicle = new AppCompatTextView(activity);
        nameVehicle.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 4)
        );
        nameVehicle.setTextColor(ContextCompat.getColor(activity, R.color.white));
        nameVehicle.setTypeface(ResourcesCompat.getFont(activity, R.font.inter), BOLD);
        nameVehicle.setGravity(CENTER);
        nameVehicle.setMaxLines(1);
        nameVehicle.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        nameVehicle.setText(name);

        //
        ////
        //

        LinearLayout columnsLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams columnsLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                padding * 24
        );
        columnsLayoutParams.setMargins(0, padding, 0, 0);
        columnsLayout.setLayoutParams(columnsLayoutParams);
        columnsLayout.setGravity(CENTER);
        columnsLayout.setOrientation(LinearLayout.HORIZONTAL);

        //
        ////
        //

        LinearLayout firstColumnLayout = new LinearLayout(activity);
        firstColumnLayout.setLayoutParams(new LinearLayout.LayoutParams(
                displayWidth / 100 * 45,
                LinearLayout.LayoutParams.MATCH_PARENT)
        );
        firstColumnLayout.setOrientation(LinearLayout.VERTICAL);

        //

        LinearLayout secondColumnLayout = new LinearLayout(activity);
        secondColumnLayout.setLayoutParams(new LinearLayout.LayoutParams(
                displayWidth / 100 * 45,
                LinearLayout.LayoutParams.MATCH_PARENT)
        );
        secondColumnLayout.setOrientation(LinearLayout.VERTICAL);

        //
        ////
        //////
        ////
        //

        AppCompatTextView battlesName = new AppCompatTextView(activity);
        battlesName.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 2)
        );
        battlesName.setTextColor(ContextCompat.getColor(activity, R.color.white));
        battlesName.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        battlesName.setGravity(CENTER);
        battlesName.setMaxLines(1);
        battlesName.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        battlesName.setText(activity.getString(R.string.battles));

        //

        AppCompatTextView battlesValue = new AppCompatTextView(activity);
        battlesValue.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 4)
        );
        battlesValue.setTextColor(battlesColor);
        battlesValue.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        battlesValue.setGravity(CENTER);
        battlesValue.setMaxLines(1);
        battlesValue.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        battlesValue.setText(getBattles());

        //
        ////
        //

        AppCompatTextView averageDamageName = new AppCompatTextView(activity);
        LinearLayout.LayoutParams averageDamageNameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 2
        );
        averageDamageNameParams.setMargins(0, padding * 2, 0, 0);
        averageDamageName.setLayoutParams(averageDamageNameParams);
        averageDamageName.setTextColor(ContextCompat.getColor(activity, R.color.white));
        averageDamageName.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        averageDamageName.setGravity(CENTER);
        averageDamageName.setMaxLines(1);
        averageDamageName.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        averageDamageName.setText(activity.getString(R.string.average_damage));

        //

        AppCompatTextView averageDamageValue = new AppCompatTextView(activity);
        averageDamageValue.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 4)
        );
        averageDamageValue.setTextColor(averageDamageColor);
        averageDamageValue.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        averageDamageValue.setGravity(CENTER);
        averageDamageValue.setMaxLines(1);
        averageDamageValue.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        averageDamageValue.setText(getAverageDamage());

        //
        ////
        //

        AppCompatTextView survivedName = new AppCompatTextView(activity);
        LinearLayout.LayoutParams survivedNameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 2
        );
        survivedNameParams.setMargins(0, padding * 2, 0, 0);
        survivedName.setLayoutParams(survivedNameParams);
        survivedName.setTextColor(ContextCompat.getColor(activity, R.color.white));
        survivedName.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        survivedName.setGravity(CENTER);
        survivedName.setMaxLines(1);
        survivedName.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        survivedName.setText(activity.getString(R.string.survived));

        //

        AppCompatTextView survivedValue = new AppCompatTextView(activity);
        survivedValue.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 4)
        );
        survivedValue.setTextColor(ContextCompat.getColor(activity, R.color.white));
        survivedValue.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        survivedValue.setGravity(CENTER);
        survivedValue.setMaxLines(1);
        survivedValue.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        survivedValue.setText(getSurvived());

        //
        ////
        //

        AppCompatTextView winRateName = new AppCompatTextView(activity);
        winRateName.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 2)
        );
        winRateName.setTextColor(ContextCompat.getColor(activity, R.color.white));
        winRateName.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        winRateName.setGravity(CENTER);
        winRateName.setMaxLines(1);
        winRateName.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        winRateName.setText(activity.getString(R.string.win_rate));

        //

        AppCompatTextView winRateValue = new AppCompatTextView(activity);
        winRateValue.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 4)
        );
        winRateValue.setTextColor(winRateColor);
        winRateValue.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        winRateValue.setGravity(CENTER);
        winRateValue.setMaxLines(1);
        winRateValue.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        winRateValue.setText(getWinRate());

        //
        ////
        //

        AppCompatTextView efficiencyName = new AppCompatTextView(activity);
        LinearLayout.LayoutParams efficiencyNameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 2
        );
        efficiencyNameParams.setMargins(0, padding * 2, 0, 0);
        efficiencyName.setLayoutParams(efficiencyNameParams);
        efficiencyName.setTextColor(ContextCompat.getColor(activity, R.color.white));
        efficiencyName.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        efficiencyName.setGravity(CENTER);
        efficiencyName.setMaxLines(1);
        efficiencyName.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        efficiencyName.setText(activity.getString(R.string.efficiency));

        //

        AppCompatTextView efficiencyValue = new AppCompatTextView(activity);
        efficiencyValue.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 4)
        );
        efficiencyValue.setTextColor(efficiencyColor);
        efficiencyValue.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        efficiencyValue.setGravity(CENTER);
        efficiencyValue.setMaxLines(1);
        efficiencyValue.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        efficiencyValue.setText(getEfficiency());

        //
        ////
        //

        AppCompatTextView hitsFromShotsName = new AppCompatTextView(activity);
        LinearLayout.LayoutParams hitsFromShotsNameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 2
        );
        hitsFromShotsNameParams.setMargins(0, padding * 2, 0, 0);
        hitsFromShotsName.setLayoutParams(hitsFromShotsNameParams);
        hitsFromShotsName.setTextColor(ContextCompat.getColor(activity, R.color.white));
        hitsFromShotsName.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        hitsFromShotsName.setGravity(CENTER);
        hitsFromShotsName.setMaxLines(1);
        hitsFromShotsName.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        hitsFromShotsName.setText(activity.getString(R.string.hits_from_shots));

        //

        AppCompatTextView hitsFromShotsValue = new AppCompatTextView(activity);
        hitsFromShotsValue.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 4)
        );
        hitsFromShotsValue.setTextColor(ContextCompat.getColor(activity, R.color.white));
        hitsFromShotsValue.setTypeface(ResourcesCompat.getFont(activity, R.font.inter));
        hitsFromShotsValue.setGravity(CENTER);
        hitsFromShotsValue.setMaxLines(1);
        hitsFromShotsValue.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        hitsFromShotsValue.setText(getHitsFromShots());

        //
        ////
        //////
        ////
        //

        AppCompatButton details = new AppCompatButton(activity);
        LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                padding * 5
        );
        details.setLayoutParams(detailsParams);
        details.setBackground(AppCompatResources.getDrawable(activity, R.drawable.background_button_insets));
        details.setTextColor(ContextCompat.getColor(activity, R.color.white));
        details.setTypeface(ResourcesCompat.getFont(activity, R.font.inter), BOLD);
        details.setGravity(CENTER);
        details.setMaxLines(1);
        details.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        details.setText(activity.getString(R.string.detailed_statistics));
        details.setPadding(padding, padding, padding, padding);
        details.setOnClickListener(l -> {

            ViewFlipper tanksLayoutsFlipper = activity.findViewById(R.id.tanks_layouts_flipper);

            TextView tanksWinsView = activity.findViewById(R.id.tanks_wins);
            TextView tanksLossesView = activity.findViewById(R.id.tanks_losses);
            TextView tanksWinAndSurviveView = activity.findViewById(R.id.tanks_win_and_survive);
            TextView tanksSurviveView = activity.findViewById(R.id.tanks_survive);
            TextView tanksXpView = activity.findViewById(R.id.tanks_xp);
            TextView tanksFragsView = activity.findViewById(R.id.tanks_frags);
            TextView tanksShotsView = activity.findViewById(R.id.tanks_shots);
            TextView tanksHitsView = activity.findViewById(R.id.tanks_hits);
            TextView tanksSpottedView = activity.findViewById(R.id.tanks_spotted);
            TextView tanksFrags8pView = activity.findViewById(R.id.tanks_frags8p);
            TextView tanksMaxXpView = activity.findViewById(R.id.tanks_max_xp);
            TextView tanksMaxFragsView = activity.findViewById(R.id.tanks_max_frags);
            TextView tanksCapturedPointsView = activity.findViewById(R.id.tanks_captured_points);
            TextView tanksDefendedView = activity.findViewById(R.id.tanks_defended);
            TextView tanksDamageDealtView = activity.findViewById(R.id.tanks_damage_dealt);
            TextView tanksDamageReceivedView = activity.findViewById(R.id.tanks_damage_received);

            tanksWinsView.setText(getWins());
            tanksLossesView.setText(getLosses());
            tanksWinAndSurviveView.setText(getWinAndSurvived());
            tanksSurviveView.setText(getSurvivedBattles());
            tanksXpView.setText(getXp());
            tanksFragsView.setText(getFrags());
            tanksShotsView.setText(getShots());
            tanksHitsView.setText(getHits());
            tanksSpottedView.setText(getSpotted());
            tanksFrags8pView.setText(getFrags8p());
            tanksMaxXpView.setText(getMaxXp());
            tanksMaxFragsView.setText(getMaxXp());
            tanksCapturedPointsView.setText(getCapturePoints());
            tanksDefendedView.setText(getDroppedCapturePoints());
            tanksDamageDealtView.setText(getDamageDealt());
            tanksDamageReceivedView.setText(getDamageReceived());

            tanksLayoutsFlipper.setDisplayedChild(1);

        });

        //
        ////
        //////
        ////
        //

        tanksListLayout.addView(mainLayout);
        mainLayout.addView(nameVehicle);
        mainLayout.addView(columnsLayout);
        mainLayout.addView(details);
        columnsLayout.addView(firstColumnLayout);
        columnsLayout.addView(secondColumnLayout);
        firstColumnLayout.addView(battlesName);
        firstColumnLayout.addView(battlesValue);
        firstColumnLayout.addView(averageDamageName);
        firstColumnLayout.addView(averageDamageValue);
        firstColumnLayout.addView(survivedName);
        firstColumnLayout.addView(survivedValue);
        secondColumnLayout.addView(winRateName);
        secondColumnLayout.addView(winRateValue);
        secondColumnLayout.addView(efficiencyName);
        secondColumnLayout.addView(efficiencyValue);
        secondColumnLayout.addView(hitsFromShotsName);
        secondColumnLayout.addView(hitsFromShotsValue);

    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @NonNull
    @Override
    public String toString() {
        String result = super.toString();

        result += "id: " + id + "\n";
        result += "name: " + name + "\n";
        result += "nation: " + nation + "\n";
        result += "tier: " + tier + "\n";
        result += "type: " + type + "\n";

        return result;
    }

}