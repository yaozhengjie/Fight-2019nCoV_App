package com.odm.fight_2019ncov.ui.situation


import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.odm.fight_2019ncov.App
import com.odm.fight_2019ncov.R
import com.odm.fight_2019ncov.model.entity.GetAreaStat

//import com.chad.baserecyclerviewadapterhelper.R
//import com.chad.baserecyclerviewadapterhelper.adapter.node.tree.NodeTreeAdapter
//import com.chad.baserecyclerviewadapterhelper.entity.node.tree.FirstNode
//import com.chad.library.adapter.base.entity.node.BaseNode
//import com.chad.library.adapter.base.provider.BaseNodeProvider
//import com.chad.library.adapter.base.viewholder.BaseViewHolder
//import com.chad.baserecyclerviewadapterhelper.R
//import com.chad.baserecyclerviewadapterhelper.adapter.node.tree.NodeTreeAdapter
//import com.chad.baserecyclerviewadapterhelper.entity.node.tree.FirstNode

/**
 * @description: 省列表 树状子适配器第二层
 * @author: ODM
 * @date: 2020/1/27
 */

class ProvinceListAdapter : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1

    override val layoutId: Int
        get() = R.layout.item_situation_province

    override fun convert(
        helper: BaseViewHolder,
        data: BaseNode?
    ) {
        val entity: GetAreaStat ? = data as GetAreaStat?
        helper.setText(R.id.tv_provinceName_item_province_situation, entity?.provinceName?.subSequence(0,2))
        helper.setText(R.id.tv_provinceStatement_item_province_situation, "确诊${entity?.confirmedCount}例 " +
                "疑似${entity?.suspectedCount}例 治愈${entity?.curedCount}例 死亡${entity?.deadCount}例")
        helper.setImageResource(R.id.iv_switch_expand_item_province_situation, R.drawable.arrow_selected_16)
        when {
            entity?.confirmedCount!! >= 100 -> {
                helper.setImageDrawable(R.id.itemView_color_province_situation , App.CONTEXT.getDrawable(R.color.red))
            }
            entity?.confirmedCount!! >= 10 -> {
                helper.setImageDrawable(R.id.itemView_color_province_situation , App.CONTEXT.getDrawable(R.color.darkorange))
            }
            else -> {
                helper.setImageDrawable(R.id.itemView_color_province_situation , App.CONTEXT.getDrawable(R.color.lightsalmon))
            }
        }
        setArrowSpin(helper, data, false)
    }


    override fun convert(helper: BaseViewHolder, data: BaseNode?, payloads: List<Any>) {
        for (payload in payloads) {
            if (payload is Int && payload == AreaSituationAdapter.EXPAND_COLLAPSE_PAYLOAD) {
                // 增量刷新，使用动画变化箭头
                setArrowSpin(helper, data, true)
            }
        }
    }

    private fun setArrowSpin(
        helper: BaseViewHolder,
        data: BaseNode?,
        isAnimate: Boolean
    ) {
        val entity: GetAreaStat? = data as GetAreaStat?
        val imageView = helper.getView<ImageView>(R.id.iv_switch_expand_item_province_situation)
        if (entity?.isExpanded == true) {
            if (isAnimate)
                 ViewCompat.animate(imageView).setDuration(200)
                .setInterpolator(DecelerateInterpolator())
                .rotation(0f)
                .start()
            else {
                imageView.rotation = 0f
            }
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(90f)
                    .start()
            } else {
                imageView.rotation = 90f
            }
        }
    }

    override fun onClick(
        helper: BaseViewHolder,
        view: View,
        data: BaseNode,
        position: Int
    ) {
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        getAdapter()!!.expandOrCollapse(
            position,
            true,
            true,
            AreaSituationAdapter.EXPAND_COLLAPSE_PAYLOAD
        )
    }
}
