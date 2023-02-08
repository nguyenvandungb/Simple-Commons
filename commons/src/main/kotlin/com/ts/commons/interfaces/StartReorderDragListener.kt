package com.ts.commons.interfaces

import androidx.recyclerview.widget.RecyclerView

interface StartReorderDragListener {
    fun requestDrag(viewHolder: RecyclerView.ViewHolder)
}
