package ru.forblitz.statistics.adapters;

import android.app.Activity;
import android.content.Context;
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

import ru.forblitz.statistics.R;
import ru.forblitz.statistics.data.Vehicle;

public class VehicleAdapter extends ArrayAdapter<Vehicle> {

    final Context context;

    public VehicleAdapter(@NonNull Context context, ArrayList<Vehicle> vehicles) {
        super(context, R.layout.item_vehicle, vehicles);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Vehicle vehicle = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_vehicle, null);

        convertView.setLayoutParams(new ListView.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT,
                (int) (parent.getWidth() * 0.85)
        ));

        TextView name = convertView.findViewById(R.id.name);
        TextView battles = convertView.findViewById(R.id.battles);
        TextView winRate = convertView.findViewById(R.id.win_rate);
        TextView averageDamage = convertView.findViewById(R.id.average_damage);
        TextView efficiency = convertView.findViewById(R.id.efficiency);
        TextView survived = convertView.findViewById(R.id.survived);
        TextView hitsFromShots = convertView.findViewById(R.id.hits_from_shots);
        Button details = convertView.findViewById(R.id.details);

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

        details.setOnClickListener(l -> {
            ViewFlipper tanksLayoutsFlipper = ((Activity) context).findViewById(R.id.tanks_layouts_flipper);

            TextView tanksWins = ((Activity) context).findViewById(R.id.tanks_wins);
            TextView tanksLosses = ((Activity) context).findViewById(R.id.tanks_losses);
            TextView tanksWinAndSurvive = ((Activity) context).findViewById(R.id.tanks_win_and_survive);
            TextView tanksSurvive = ((Activity) context).findViewById(R.id.tanks_survive);
            TextView tanksXp = ((Activity) context).findViewById(R.id.tanks_xp);
            TextView tanksFrags = ((Activity) context).findViewById(R.id.tanks_frags);
            TextView tanksShots = ((Activity) context).findViewById(R.id.tanks_shots);
            TextView tanksHits = ((Activity) context).findViewById(R.id.tanks_hits);
            TextView tanksSpotted = ((Activity) context).findViewById(R.id.tanks_spotted);
            TextView tanksFrags8p = ((Activity) context).findViewById(R.id.tanks_frags8p);
            TextView tanksMaxXp = ((Activity) context).findViewById(R.id.tanks_max_xp);
            TextView tanksMaxFrags = ((Activity) context).findViewById(R.id.tanks_max_frags);
            TextView tanksCapturedPoints = ((Activity) context).findViewById(R.id.tanks_captured_points);
            TextView tanksDropped = ((Activity) context).findViewById(R.id.tanks_dropped);
            TextView tanksDamageDealt = ((Activity) context).findViewById(R.id.tanks_damage_dealt);
            TextView tanksDamageReceived = ((Activity) context).findViewById(R.id.tanks_damage_received);

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
