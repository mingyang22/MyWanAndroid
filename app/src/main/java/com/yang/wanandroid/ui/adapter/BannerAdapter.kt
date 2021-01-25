package com.yang.wanandroid.ui.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yang.wanandroid.model.bean.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

/**
 * @author ym on 1/20/21
 * 发现页 banner适配器
 * 只适用于图片
 */
class BannerAdapter(var banners: MutableList<Banner> = mutableListOf()) :
    BannerImageAdapter<Banner>(banners) {

    override fun onBindView(holder: BannerImageHolder?, data: Banner, position: Int, size: Int) {
        // 图片加载自己实现
        holder?.let {
            Glide.with(it.itemView)
                .load(data.imagePath)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(it.imageView);
        }
    }
}