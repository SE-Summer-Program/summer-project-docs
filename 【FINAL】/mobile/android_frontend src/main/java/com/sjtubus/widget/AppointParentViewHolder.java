package com.sjtubus.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.utils.StringCalendarUtils;

public class AppointParentViewHolder extends BaseViewHolder{

    private Context context;
    private View view;

    private ImageView expand;

    AppointParentViewHolder(Context context, View view){
        super(view);
        this.context = context;
        this.view = view;
    }

    public void bindView(final AppointInfo bean, final int pos, final AppointItemClickListener appointItemClickListener){
        LinearLayout container = view.findViewById(R.id.container);

       // TextView shiftid = view.findViewById(R.id.appointitem_shiftid);
        TextView departure_place = view.findViewById(R.id.appointitem_departureplace);
        TextView arrive_place = view.findViewById(R.id.appointitem_arriveplace);
        TextView departure_time = view.findViewById(R.id.appointitem_departuretime);
        TextView arrive_time = view.findViewById(R.id.appointitem_arrivetime);
        TextView remain_seat = view.findViewById(R.id.appointitem_remainseat);
        expand = view.findViewById(R.id.appointitem_expand);

       // parentDashedView = view.findViewById(R.id.parent_dashed_view);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)expand.getLayoutParams();
        expand.setLayoutParams(params);

        departure_place.setText(bean.getDeparture_place());
        arrive_place.setText(bean.getArrive_place());
        departure_time.setText(StringCalendarUtils.HHmmssToHHmm(bean.getDeparture_time()));
        String arrive_time_text = "约" + StringCalendarUtils.HHmmssToHHmm(bean.getArrive_time());
        arrive_time.setText(arrive_time_text);

        String remain_text = "";
        if (bean.getRemain_seat() == 0){
            remain_text = "已无座";
        } else {
            remain_text = "剩余"+bean.getRemain_seat()+"座";
        }
        remain_seat.setText(remain_text);

        if (bean.isExpand()) {
            expand.setRotation(90);
        } else {
            expand.setRotation(0);
        }

        //父布局OnClick监听
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appointItemClickListener != null){
                    if (bean.isExpand()) {
                        appointItemClickListener.onHideChildItem(bean);
                      //  parentDashedView.setVisibility(View.VISIBLE);
                        bean.setExpand(false);
                        rotationExpandIcon(90, 0);
                    } else {
                        appointItemClickListener.onExpandChildItem(bean);
                      //  parentDashedView.setVisibility(View.INVISIBLE);
                        bean.setExpand(true);
                        rotationExpandIcon(0, 90);
                    }
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon(float from, float to) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                expand.setRotation((Float) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }
}