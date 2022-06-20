package com.example.constellation.starfrag;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.constellation.R;
import com.example.constellation.bean.StarBean;

import java.util.ArrayList;
import java.util.List;

/*
 * 星座Fragment
 *   包括ViewPager和GridView
 * */
public class StarFragment extends Fragment {
    ViewPager starVp;
    GridView starGv;
    LinearLayout pointLayout;
    List<StarBean.StarinfoBean> mDatas;
    StarBaseAdapter starBaseAdapter;
    //声明图片数组
    int[] imgIds = {R.mipmap.pic_guanggao, R.mipmap.pic_star};
    //声明ViewPager的数据源
    List<ImageView> ivList;
    //声明管理指示器小圆点的集合
    List<ImageView> pointList;
    private StarPagerAdapter starPagerAdapter;
    //完成定时装置，实现自动滑动的效果
    Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                //获取当前ViewPager显示的页面
                int currentItem = starVp.getCurrentItem();
                if (currentItem == ivList.size() - 1) {
                    starVp.setCurrentItem(0);
                }else{
                    currentItem++;
                    starVp.setCurrentItem(currentItem);
                }

                //形成循环发送--接受消息的效果，在接受消息的同时，也要进行消息发送
                handler.sendEmptyMessageDelayed(1, 3000);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_star, container, false);
        initView(view);
        Bundle bundle = getArguments();
        StarBean infoBean = (StarBean) bundle.getSerializable("info");
        mDatas = infoBean.getStarinfo();//获取关于星座信息的集合数据
        //创建适配器
        starBaseAdapter = new StarBaseAdapter(getContext(), mDatas);
        starGv.setAdapter(starBaseAdapter);
        initPager();
        setVPListener();
        setGVListener();
        //延迟五秒钟发送一条消息，通知可以切换ViewPager的图片了
        handler.sendEmptyMessageDelayed(1, 3000);
        return view;
    }


    //设置ViewPager显示的页面
    private void initPager() {
        ivList = new ArrayList<>();
        pointList = new ArrayList<>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(imgIds[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置图片view的宽高
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            //将图片view加载到集合当中
            ivList.add(iv);
            //创建图片对应的指示器远点
            ImageView piv = new ImageView(getContext());
            piv.setImageResource(R.mipmap.point_normal);
            LinearLayout.LayoutParams plp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            plp.setMargins(20, 0, 0, 0);
            piv.setLayoutParams(plp);
            //将小圆点添加到布局当中
            pointLayout.addView(piv);
            //为了便于操作，将小圆点添加到统一管理的集合当中
            pointList.add(piv);
        }
        //默认第一个小圆点是获取焦点的状态
        pointList.get(0).setImageResource(R.mipmap.point_focus);
        starPagerAdapter = new StarPagerAdapter(getContext(), ivList);
        starVp.setAdapter(starPagerAdapter);
    }


    //设置ViewPager的监听器函数
    private void setVPListener() {
        starVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < pointList.size(); i++) {
                    pointList.get(i).setImageResource(R.mipmap.point_normal);
                }
                pointList.get(position).setImageResource(R.mipmap.point_focus);
            }
        });
    }

    //设置GridView的监听事件函数
    private void setGVListener() {
        starGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StarBean.StarinfoBean bean = mDatas.get(position);
                Intent intent = new Intent(getContext(), StarAnalysisActivity.class);
                intent.putExtra("star", bean);
                startActivity(intent);
            }
        });
    }

    private void initView(View view) {
        starVp = view.findViewById(R.id.starfrag_vp);
        starGv = view.findViewById(R.id.starfrag_gv);
        pointLayout = view.findViewById(R.id.starfrag_layout);
    }
}