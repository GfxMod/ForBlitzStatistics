package ru.forblitz.statistics.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.ViewFlipper
import ru.forblitz.statistics.R
import ru.forblitz.statistics.dto.VehicleSpecs
import ru.forblitz.statistics.dto.VehiclesStatisticsResponse.VehicleStatistics
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.VehicleUtils.getAverageDamageColor
import ru.forblitz.statistics.utils.VehicleUtils.getBattlesColor
import ru.forblitz.statistics.utils.VehicleUtils.getEfficiencyColor
import ru.forblitz.statistics.utils.VehicleUtils.getWinRateColor
import ru.forblitz.statistics.widget.data.DetailsLayout

class VehicleAdapter(
    private val activityContext: Context,
    vehicles: ArrayList<Pair<VehicleSpecs, VehicleStatistics>>
) : ArrayAdapter<Pair<VehicleSpecs, VehicleStatistics>>(
    activityContext, R.layout.item_vehicle, vehicles
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val vehicleSpecs = getItem(position)!!.first
        val vehicleStat = getItem(position)!!.second
        val item = LayoutInflater.from(context).inflate(R.layout.item_vehicle, null)!!
        item.layoutParams = AbsListView.LayoutParams(
            AbsListView.LayoutParams.MATCH_PARENT, (parent.width * 0.85).toInt()
        )
        val name = item.findViewById<TextView>(R.id.name)
        val battles = item.findViewById<TextView>(R.id.battles)
        val winRate = item.findViewById<TextView>(R.id.win_rate)
        val averageDamage = item.findViewById<TextView>(R.id.average_damage)
        val efficiency = item.findViewById<TextView>(R.id.efficiency)
        val survived = item.findViewById<TextView>(R.id.survived)
        val hitsFromShots = item.findViewById<TextView>(R.id.hits_from_shots)
        val details = item.findViewById<Button>(R.id.details)
        battles.setTextColor(getBattlesColor(context, vehicleStat.statistics.battles))
        winRate.setTextColor(
            getWinRateColor(
                context,
                vehicleStat.statistics.winningPercentage!!.toDouble()
            )
        )
        averageDamage.setTextColor(
            getAverageDamageColor(
                context,
                vehicleStat.statistics.averageDamage!!.toDouble(),
                vehicleSpecs.tier
            )
        )
        efficiency.setTextColor(
            getEfficiencyColor(
                context,
                vehicleStat.statistics.efficiency!!.toDouble()
            )
        )
        name.text = vehicleSpecs.name
        battles.text = ParseUtils.splitByThousands(vehicleStat.statistics.battles.toString())
        winRate.text = vehicleStat.statistics.winningPercentage.toString()
        averageDamage.text = ParseUtils.splitByThousands(vehicleStat.statistics.averageDamage.toString())
        efficiency.text = vehicleStat.statistics.efficiency.toString()
        survived.text = vehicleStat.statistics.survivedPercentage.toString()
        hitsFromShots.text = vehicleStat.statistics.hitsOutOfShots.toString()
        details.setOnClickListener {
            ((activityContext as Activity).findViewById<DetailsLayout>(R.id.tanks_details_layout)).setData(vehicleStat.statistics)

            activityContext.findViewById<ViewFlipper>(R.id.tanks_layouts_flipper).displayedChild = 1
        }
        return item
    }
}