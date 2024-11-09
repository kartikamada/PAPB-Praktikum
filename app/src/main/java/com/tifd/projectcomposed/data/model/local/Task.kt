package com.tifd.projectcomposed.data.model.local

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
class Task (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "subject")
    var subject: String,

    @ColumnInfo(name = "task_details")
    var taskDetails: String,

    @ColumnInfo(name = "done")
    var done: Boolean = false,

    // Store as String in database
    @ColumnInfo(name = "image_uri")
    var imageUri: Uri? = null,
) : Parcelable