package com.baolinetworktechnology.shejiquan.adapter;
import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.lidroid.xutils.BitmapUtils;
import android.content.Context;  
import android.graphics.Bitmap;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;  
import android.widget.ImageView;  
import android.widget.TextView;  
  
public class HorizontalListViewAdapter extends BaseAdapter{  
    private Context mContext;  
	private BitmapUtils mImageUtil;
    Bitmap iconBitmap;  
    private int selectIndex = -1;  
    String Positionstr;
    List<CaseClass> list=new ArrayList<CaseClass>();
    List<CaseClass> addlist=new ArrayList<CaseClass>();
    public HorizontalListViewAdapter(Context context,List<CaseClass> list){  
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
    public void setPosition(CaseClass caseclass) { 
    	Positionstr="";
    	for (int i = 0; i < addlist.size(); i++) {
			if (caseclass.id == addlist.get(i).id) {
				addlist.remove(i);
				for (int c = 0; c < addlist.size(); c++) {
					if(addlist.size()==1 || c==1){
						Positionstr +=addlist.get(c).title;
					}else{	    					
						Positionstr +=addlist.get(c).title+",";
					}
				}
				notifyDataSetChanged();
				return;
			}  
    	}
    	if(addlist.size()>=2){
    	   addlist.remove(0);
       	   addlist.add(caseclass);
    	}else{	
    	addlist.add(caseclass);
    	}
    	for (int i = 0; i < addlist.size(); i++) {
			if(addlist.size()==1 || i==1){
				Positionstr +=addlist.get(i).title;
			}else{	    					
				Positionstr +=addlist.get(i).title+",";
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
        CaseClass caseclass=list.get(position);
        mImageUtil.display(holder.image_amg, caseclass.getSmallImages("_300_300."));
        holder.mTitle.setText(caseclass.title);
        holder.image_ok.setBackgroundResource(R.drawable.con_icon_ydydg_def);
        for (int i = 0; i < addlist.size(); i++) {
        	if(caseclass.id==addlist.get(i).id){
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
    public List<CaseClass> getaddlist(){
  		return addlist;
    }
    public String getPostStr(){
    	Positionstr="";
    	for (int c = 0; c < addlist.size(); c++) {
			if(addlist.size()==1 || c==1){
				Positionstr +=addlist.get(c).title;
			}else{	    					
				Positionstr +=addlist.get(c).title+",";
			}
		}
  		return Positionstr;
    }
    public void setaddlist(List<CaseClass> cass){
    	addlist.addAll(cass);
        notifyDataSetChanged();
    }
}
