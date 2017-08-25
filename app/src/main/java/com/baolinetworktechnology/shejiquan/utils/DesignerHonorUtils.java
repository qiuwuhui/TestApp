package com.baolinetworktechnology.shejiquan.utils;

import android.view.View;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;

public class DesignerHonorUtils {
	public void setIcon(ImageView ivHonor, int IsCertification,
			ImageView ivSer, int ServiceStatus) {
	
		switch (IsCertification) {
		case 0:
		case 1:// 未认证
		case 2:// 审核中
			ivHonor.setImageResource(R.drawable.weishop_admin_unhonor);
			break;
		case 3:// 审核通过
			ivHonor.setImageResource(R.drawable.weishop_admin_honor);
			break;

		}
		
		switch (ServiceStatus) {
		case 0:// 非付费
			if (ivSer.getVisibility() != View.GONE)
				ivSer.setVisibility(View.GONE);
			break;
		case 1:// 精英
			if (ivSer.getVisibility() != View.VISIBLE)
				ivSer.setVisibility(View.VISIBLE);
			ivSer.setImageResource(R.drawable.weishop_grad_1);
			break;
		case 2:// 资深
			if (ivSer.getVisibility() != View.VISIBLE)
				ivSer.setVisibility(View.VISIBLE);
			ivSer.setImageResource(R.drawable.weishop_grad_2);
			break;
		case 3:// 首席
			if (ivSer.getVisibility() != View.VISIBLE)
				ivSer.setVisibility(View.VISIBLE);
			ivSer.setImageResource(R.drawable.weishop_grad_3);
			break;
		default:
			break;
		}
	}

	public void setHonor(ImageView ivHonor, int IsCertification) {

		if (IsCertification == 0) {

			ivHonor.setImageResource(R.drawable.weishop_admin_unhonor);
		} else {
			ivHonor.setImageResource(R.drawable.weishop_admin_honor);
		}
	}

	public void setServiceStatus(ImageView ivSer, String ServiceStatus) {

		int statu = Integer.getInteger(ServiceStatus, 0);
		if (statu > 0) {
			if (ivSer.getVisibility() != View.VISIBLE)
				ivSer.setVisibility(View.VISIBLE);
		} else {
			if (ivSer.getVisibility() != View.GONE)
				ivSer.setVisibility(View.GONE);
		}

	}
}
