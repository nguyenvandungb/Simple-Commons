package com.ts.commons.extensions

import android.content.Context
import com.ts.commons.models.FileDirItem

fun FileDirItem.isRecycleBinPath(context: Context): Boolean {
    return path.startsWith(context.recycleBinPath)
}
