package ru.forblitz.statistics.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import ru.forblitz.statistics.dto.VehicleSpecs;
import ru.forblitz.statistics.dto.VehicleStat;
import ru.forblitz.statistics.utils.VehicleUtils;
import ru.forblitz.statistics.widget.data.DetailsLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import kotlin.Pair;

import java.util.ArrayList;

import ru.forblitz.statistics.R;

public class VehicleAdapter extends ArrayAdapter<Pair<VehicleSpecs, VehicleStat>> {

    final Context context;

    public VehicleAdapter(@NonNull Context context, ArrayList<Pair<VehicleSpecs, VehicleStat>> vehicles) {
        super(context, R.layout.item_vehicle, vehicles);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        VehicleSpecs vehicleSpecs = getItem(position).getFirst();
        VehicleStat vehicleStat = getItem(position).getSecond();

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

        battles.setTextColor(VehicleUtils.getBattlesColor(getContext(), Integer.parseInt(vehicleStat.all.getBattles())));
        winRate.setTextColor(VehicleUtils.getWinRateColor(getContext(), Double.parseDouble(vehicleStat.all.getWinRate())));
        averageDamage.setTextColor(VehicleUtils.getAverageDamageColor(getContext(), Double.parseDouble(vehicleStat.all.getAverageDamage()), vehicleSpecs.tier));
        efficiency.setTextColor(VehicleUtils.getEfficiencyColor(getContext(), Double.parseDouble(vehicleStat.all.getEfficiency())));

        name.setText(vehicleSpecs.name);
        battles.setText(vehicleStat.all.getBattles());
        winRate.setText(vehicleStat.all.getWinRate());
        averageDamage.setText(vehicleStat.all.getAverageDamage());
        efficiency.setText(vehicleStat.all.getEfficiency());
        survived.setText(vehicleStat.all.getSurvived());
        hitsFromShots.setText(vehicleStat.all.getHitsFromShots());

        details.setOnClickListener(l -> {
            ViewFlipper tanksLayoutsFlipper = ((Activity) context).findViewById(R.id.tanks_layouts_flipper);

            ((DetailsLayout) ((Activity) context).findViewById(R.id.tanks_details_layout)).setData(vehicleStat.all);

            tanksLayoutsFlipper.setDisplayedChild(1);
        });
        return convertView;

    }

}
