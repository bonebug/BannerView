package com.test.banner.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.test.banner.R;
import com.test.banner.itemtype.ImageItemTypeImp;
import com.test.banner.databean.ItemData;
import com.test.banner.itemtype.TextItemTypeImp;

import java.util.ArrayList;
import java.util.List;

import bone.com.mycustombanner.RecyclerViewBanner.RecyclerViewPagerAdatper;
import bone.com.mycustombanner.RecyclerViewBanner.RecyclerVieBannerView;
import bone.com.mycustombanner.utils.DensityUtil;
import bone.com.mycustombanner.ViewPagerBanner.NavigatorLocationConstant;

public class RecyclerViewBannerActivity extends AppCompatActivity {

    private RecyclerVieBannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_recyclerbanner_show);
        bannerView =  findViewById(R.id.bv);
        //默认item类型
//        bannerView.addItemType(new DefaultItemTypeImp());
        //多种类型
        bannerView.addItemType(new ImageItemTypeImp());
        bannerView.addItemType(new TextItemTypeImp());

        bannerView.getAdapter().setNewData(getData());

        initNavigator();
        bannerView.setNeedNavigator(true);


        bannerView.setAutoPlay(true);
//        bannerView.setEndLoop(false);

        setListener();
        bannerView.startAutoPlay();

        bannerView.setPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("bannerView","onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("bannerView","onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("bannerView","onPageScrollStateChanged");
            }
        });
        bannerView.setPageTransformer(RecyclerViewPagerAdatper.SCALEIN);
    }

    /**
     * 设置点击时间
     */
    private void setListener() {

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerView.setAutoPlay(true);
                bannerView.startAutoPlay();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerView.stopAutoPlay();
            }
        });
    }

    /**
     * 初始化导航点
     */
    private void initNavigator() {
        int margin = DensityUtil.dip2px(RecyclerViewBannerActivity.this, 15);
        bannerView.setNavigatorParams(0, 0, margin, margin, NavigatorLocationConstant.BOTTOM_RIGHT);
//        bannerView.setNavigatorPointsBackgroundColors(R.color.color_green,R.color.color_write);
        bannerView.setNavigatorPointsBackgroundDrawable(R.drawable.icon_chengweidaren, R.drawable.icon_daren);
        bannerView.setNavigatorPointsGaps(DensityUtil.dip2px(RecyclerViewBannerActivity.this, 5));
        bannerView.setNavigatorPointsRadius(DensityUtil.dip2px(RecyclerViewBannerActivity.this, 10));

    }


    private List<ItemData> getData() {


        List<ItemData> datas = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ItemData data = new ItemData();
            data.url = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1507536679&di=b76f0379a417444846c30d6241d66297&src=http://img15.3lian.com/2015/f2/104/d/88.jpg";
//            data.TYPE = "DefaultItemType";
            data.type = ImageItemTypeImp.TYPE;
            datas.add(data);
        }
        for (int i = 0; i < 1; i++) {
            ItemData data = new ItemData();
//            data.url = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1507536679&di=b76f0379a417444846c30d6241d66297&src=http://img15.3lian.com/2015/f2/104/d/88.jpg";
//            data.TYPE = "DefaultItemType";
            data.text = "哈哈" + i;
            data.type = TextItemTypeImp.TYPE;
            datas.add(data);
        }

        for (int i = 0; i < 1; i++) {
            ItemData data = new ItemData();
            data.url = "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=182598671,2920807851&fm=173&s=B4B0779158731D9EE02571080300E0D0&w=640&h=397&img.JPEG";
//            data.TYPE = "DefaultItemType";
            data.type = ImageItemTypeImp.TYPE;
            datas.add(data);
        }

        for (int i = 0; i < 1; i++) {
            ItemData data = new ItemData();
//            data.url = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1507536679&di=b76f0379a417444846c30d6241d66297&src=http://img15.3lian.com/2015/f2/104/d/88.jpg";
//            data.TYPE = "DefaultItemType";
            data.text = "嘿嘿" + i;
            data.type = TextItemTypeImp.TYPE;
            datas.add(data);
        }
        return datas;
    }
}
