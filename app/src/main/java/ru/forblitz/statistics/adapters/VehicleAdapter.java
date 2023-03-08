package ru.forblitz.statistics.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ru.forblitz.statistics.R.id;
import ru.forblitz.statistics.R.layout;
import ru.forblitz.statistics.data.Utils;
import ru.forblitz.statistics.data.Vehicle;

public class VehicleAdapter extends ArrayAdapter<Vehicle> {

    Activity activity;

    public VehicleAdapter(@NonNull Activity activity, ArrayList<Vehicle> vehicles) {
        super(activity, layout.item_vehicle, vehicles);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Vehicle vehicle = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout.item_vehicle, null);
        }

        convertView.setLayoutParams(new ListView.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT,
                (int) (Utils.getY(activity) * 0.4)
        ));

        TextView name = convertView.findViewById(id.name);
        TextView battles = convertView.findViewById(id.battles);
        TextView winRate = convertView.findViewById(id.win_rate);
        TextView averageDamage = convertView.findViewById(id.average_damage);
        TextView efficiency = convertView.findViewById(id.efficiency);
        TextView survived = convertView.findViewById(id.survived);
        TextView hitsFromShots = convertView.findViewById(id.hits_from_shots);
        Button details = convertView.findViewById(id.details);

        battles.setTextColor(vehicle.getBattlesColor(getContext()));
        winRate.setTextColor(vehicle.getWinRateColor(getContext()));
        averageDamage.setTextColor(vehicle.getAverageDamageColor(getContext()));
        efficiency.setTextColor(vehicle.getEfficiencyColor(getContext()));

        name.setText(vehicle.getName());
        battles.setText(vehicle.getBattles());
        winRate.setText(vehicle.getWinRate());
        averageDamage.setText(vehicle.getAverageDamage());
        efficiency.setText(vehicle.getEfficiency());
        survived.setText(vehicle.getSurvived());
        hitsFromShots.setText(vehicle.getHitsFromShots());

        convertView.setOnClickListener(l -> {});

        details.setOnClickListener(l -> {
            ViewFlipper tanksLayoutsFlipper = activity.findViewById(id.tanks_layouts_flipper);

            TextView tanksWins = activity.findViewById(id.tanks_wins);
            TextView tanksLosses = activity.findViewById(id.tanks_losses);
            TextView tanksWinAndSurvive = activity.findViewById(id.tanks_win_and_survive);
            TextView tanksSurvive = activity.findViewById(id.tanks_survive);
            TextView tanksXp = activity.findViewById(id.tanks_xp);
            TextView tanksFrags = activity.findViewById(id.tanks_frags);
            TextView tanksShots = activity.findViewById(id.tanks_shots);
            TextView tanksHits = activity.findViewById(id.tanks_hits);
            TextView tanksSpotted = activity.findViewById(id.tanks_spotted);
            TextView tanksFrags8p = activity.findViewById(id.tanks_frags8p);
            TextView tanksMaxXp = activity.findViewById(id.tanks_max_xp);
            TextView tanksMaxFrags = activity.findViewById(id.tanks_max_frags);
            TextView tanksCapturedPoints = activity.findViewById(id.tanks_captured_points);
            TextView tanksDropped = activity.findViewById(id.tanks_dropped);
            TextView tanksDamageDealt = activity.findViewById(id.tanks_damage_dealt);
            TextView tanksDamageReceived = activity.findViewById(id.tanks_damage_received);

            tanksWins.setText(vehicle.getData().getWins());
            tanksLosses.setText(vehicle.getData().getLosses());
            tanksWinAndSurvive.setText(vehicle.getData().getWinAndSurvived());
            tanksSurvive.setText(vehicle.getData().getSurvivedBattles());
            tanksXp.setText(vehicle.getData().getXp());
            tanksFrags.setText(vehicle.getData().getFrags());
            tanksShots.setText(vehicle.getData().getShots());
            tanksHits.setText(vehicle.getData().getHits());
            tanksSpotted.setText(vehicle.getData().getSpotted());
            tanksFrags8p.setText(vehicle.getData().getFrags8p());
            tanksMaxXp.setText(vehicle.getData().getMaxXp());
            tanksMaxFrags.setText(vehicle.getData().getMaxFrags());
            tanksCapturedPoints.setText(vehicle.getData().getCapturedPoints());
            tanksDropped.setText(vehicle.getData().getDroppedCapturePoints());
            tanksDamageDealt.setText(vehicle.getData().getDamageDealt());
            tanksDamageReceived.setText(vehicle.getData().getDamageReceived());


            tanksLayoutsFlipper.setDisplayedChild(1);
        });

        return convertView;

    }

}
