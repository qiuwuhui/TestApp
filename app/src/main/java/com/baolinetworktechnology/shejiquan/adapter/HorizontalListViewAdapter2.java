package com.baolinetworktechnology.shejiquan.adapter;
import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.CaseClassJD;
import com.lidroid.xutils.BitmapUtils;
import android.content.Context;  
import android.graphics.Bitmap;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.ImageView;  
import android.widget.TextView;  
  
public class HorizontalListViewAdapter2 extends BaseAdapter{  
    private Context mContext;  
	private BitmapUtils mImageUtil;
    Bitmap iconBitmap;  
    private int selectIndex = -1;  
    String Positionstr;
    List<CaseClassJD> list=new ArrayList<CaseClassJD>();
    List<CaseClassJD> addlist=new ArrayList<CaseClassJD>();
    public HorizontalListViewAdapter2(Context context,List<CaseClassJD> list){  
        this.mContext = context;  
        this.list=list;
        
        mImageUtil = new BitmapUtils(mContext);
		//加载图片类
		mImageUtil.configDefaultLoadingImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.defualt_trans);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
    }  
    @Override  
    public int getCount() {
    	
		return list.size();  
    }  
    @Override  
    public Object getItem(int position) {  
        return position;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
    public void setPosition(CaseClassJD caseclass) { 
    	Positionstr="";
    	for (int i = 0; i < addlist.size(); i++) {
			if (caseclass.classID == addlist.get(i).classID) {                  
				addlist.remove(i);
				for (int c = 0; c < addlist.size(); c++) {
					if(addlist.size()==1 || c==1){
						Positionstr +=addlist.get(c).className;
					}else{	    					
						Positionstr +=addlist.get(c).className+",";
					}
				}
				notifyDataSetChanged();
				return;
			}  
    	}
    	addlist.clear();
    	addlist.add(caseclass);
    	for (int i = 0; i < addlist.size(); i++) {
			if(addlist.size()==1 || i==1){
				Positionstr +=addlist.get(i).className;
			}else{	    					
				Positionstr +=addlist.get(i).className+",";
			}
		}
    	notifyDataSetChanged();
    }  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
  
        ViewHolder holder;  
        if(convertView==null){  
            holder = new ViewHolder();  
            convertView = View.inflate(parent.getContext(),
            R.layout.horizonta_layout, null); 
            holder.image_amg=(ImageView)convertView.findViewById(R.id.image_amg);  
            holder.image_ok=(ImageView)convertView.findViewById(R.id.image_ok);  
            holder.mTitle=(TextView)convertView.findViewById(R.id.mTitle);           
            convertView.setTag(holder);  
        }else{  
            holder=(ViewHolder)convertView.getTag();  
        }  
        if(position == selectIndex){  
            convertView.setSelected(true);  
        }else{  
            convertView.setSelected(false);  
        }  
        CaseClassJD caseclass=list.get(position);
        mImageUtil.display(holder.image_amg, caseclass.getSmallImages("_300_300."));
        holder.mTitle.setText(caseclass.className);
        holder.image_ok.setBackgroundResource(R.drawable.con_icon_ydydg_def);
        for (int i = 0; i < addlist.size(); i++) {
        	if(caseclass.classID==addlist.get(i).classID){
        	holder.image_ok.setBackgroundResource(R.drawable.con_icon_ydydg_pre);
        	}
		}
        return convertView;  
    }  
  
    private static class ViewHolder {  
        private TextView mTitle;  
        private ImageView image_amg,image_ok;
        
    }  

    public void setSelectIndex(int i){  
        selectIndex = i;  
    }  
    public String getPositionstr(){
  		return Positionstr;
  	} 
    public String getPostStr(){
    	Positionstr="";
    	for (int i = 0; i < addlist.size(); i++) {
    		if(addlist.size()==1 || i==1){
				Positionstr +=addlist.get(i).className;
			}else{	    					
				Positionstr +=addlist.get(i).className+",";
			}
		}
  		return Positionstr;
    }
    public List<CaseClassJD> getaddlist(){
  		return addlist;
  	} 
    public void setaddlist(List<CaseClassJD> list){
  		addlist.addAll(list);
  		notifyDataSetChanged();
  	} 
}
