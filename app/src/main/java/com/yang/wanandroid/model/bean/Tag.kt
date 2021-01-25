package com.yang.wanandroid.model.bean

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * @author ym on 1/7/21
 * 文章标签信息
 */
@Keep
@Parcelize
@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "article_id")
    var articleId: Long,
    var name: String?,
    var url: String?
) : Parcelable