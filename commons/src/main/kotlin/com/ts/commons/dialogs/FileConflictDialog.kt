package com.ts.commons.dialogs

import android.app.Activity
import com.ts.commons.R
import com.ts.commons.R.id.conflict_dialog_radio_keep_both
import com.ts.commons.R.id.conflict_dialog_radio_merge
import com.ts.commons.R.id.conflict_dialog_radio_skip
import com.ts.commons.extensions.baseConfig
import com.ts.commons.extensions.beVisibleIf
import com.ts.commons.extensions.getAlertDialogBuilder
import com.ts.commons.extensions.setupDialogStuff
import com.ts.commons.helpers.CONFLICT_KEEP_BOTH
import com.ts.commons.helpers.CONFLICT_MERGE
import com.ts.commons.helpers.CONFLICT_OVERWRITE
import com.ts.commons.helpers.CONFLICT_SKIP
import com.ts.commons.models.FileDirItem
import kotlinx.android.synthetic.main.dialog_file_conflict.view.*

class FileConflictDialog(
    val activity: Activity, val fileDirItem: FileDirItem, val showApplyToAllCheckbox: Boolean,
    val callback: (resolution: Int, applyForAll: Boolean) -> Unit
) {
    val view = activity.layoutInflater.inflate(R.layout.dialog_file_conflict, null)!!

    init {
        view.apply {
            val stringBase = if (fileDirItem.isDirectory) R.string.folder_already_exists else R.string.file_already_exists
            conflict_dialog_title.text = String.format(activity.getString(stringBase), fileDirItem.name)
            conflict_dialog_apply_to_all.isChecked = activity.baseConfig.lastConflictApplyToAll
            conflict_dialog_apply_to_all.beVisibleIf(showApplyToAllCheckbox)
            conflict_dialog_divider.beVisibleIf(showApplyToAllCheckbox)
            conflict_dialog_radio_merge.beVisibleIf(fileDirItem.isDirectory)

            val resolutionButton = when (activity.baseConfig.lastConflictResolution) {
                CONFLICT_OVERWRITE -> conflict_dialog_radio_overwrite
                CONFLICT_MERGE -> conflict_dialog_radio_merge
                else -> conflict_dialog_radio_skip
            }
            resolutionButton.isChecked = true
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok) { dialog, which -> dialogConfirmed() }
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(view, this)
            }
    }

    private fun dialogConfirmed() {
        val resolution = when (view.conflict_dialog_radio_group.checkedRadioButtonId) {
            conflict_dialog_radio_skip -> CONFLICT_SKIP
            conflict_dialog_radio_merge -> CONFLICT_MERGE
            conflict_dialog_radio_keep_both -> CONFLICT_KEEP_BOTH
            else -> CONFLICT_OVERWRITE
        }

        val applyToAll = view.conflict_dialog_apply_to_all.isChecked
        activity.baseConfig.apply {
            lastConflictApplyToAll = applyToAll
            lastConflictResolution = resolution
        }

        callback(resolution, applyToAll)
    }
}
