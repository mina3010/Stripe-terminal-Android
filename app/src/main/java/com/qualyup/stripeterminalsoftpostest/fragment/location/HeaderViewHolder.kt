package com.qualyup.stripeterminalsoftpostest.fragment.location

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.qualyup.stripeterminalsoftpostest.R
import com.qualyup.stripeterminalsoftpostest.databinding.ListItemHeaderBinding

/**
 * Bind Info about a location list to a header view.
 */
class HeaderViewHolder(
    parent: View,
) : RecyclerView.ViewHolder(parent) {
    private val resources = parent.resources
    private val binding = ListItemHeaderBinding.bind(parent)

    fun bind(size: Int, hasMore: Boolean) {
        val titleId = if (hasMore) R.string.select_location_list_title_more else R.string.select_location_list_title
        binding.listItemHeader.text = resources.getString(titleId, size)
    }
}
