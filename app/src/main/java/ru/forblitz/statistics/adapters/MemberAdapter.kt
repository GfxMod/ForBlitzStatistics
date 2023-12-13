package ru.forblitz.statistics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.forblitz.statistics.R
import ru.forblitz.statistics.dto.Member
import ru.forblitz.statistics.utils.InterfaceUtils
import ru.forblitz.statistics.utils.ParseUtils

class MemberAdapter(private val activityContext: Context, members: Array<Member?>?) : ArrayAdapter<Member?>(
    activityContext, R.layout.item_member, members!!
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val member = getItem(position)
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_member, null)
        }
        itemView!!.layoutParams = AbsListView.LayoutParams(
            AbsListView.LayoutParams.MATCH_PARENT, (InterfaceUtils.getX() * 0.15).toInt()
        )
        itemView.setOnClickListener {
            InterfaceUtils.search(
                activityContext, member!!.accountName
            )
        }
        val name = member!!.accountName
        val details = ParseUtils.formatClanRole(
            context, member.role
        ) + "; " + context.resources.getString(R.string.joined_at) + ParseUtils.formatSecondsTimestampToDate(
            member.joinedAt
        )
        val memberName = itemView.findViewById<TextView>(R.id.member_name)
        val memberDetails = itemView.findViewById<TextView>(R.id.member_details)
        memberName.text = name
        memberDetails.text = details
        return itemView
    }
}