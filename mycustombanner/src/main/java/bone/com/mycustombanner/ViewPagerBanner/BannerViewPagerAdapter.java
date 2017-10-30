package bone.com.mycustombanner.ViewPagerBanner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bone.com.mycustombanner.RecyclerViewBanner.adapter.BaseViewHolder;
import bone.com.mycustombanner.entity.ItemTypeBeanInterface;
import bone.com.mycustombanner.itemtype.BaseItemType;

/**
 * 功能：
 * ＊创建者：赵然 on 2017/10/9 14:46
 * ＊
 */

public class BannerViewPagerAdapter<T> extends PagerAdapter {

    private List<T> datas;

    private Context context;

    private SparseArray<BaseItemType> itemTypeSparseArray;
    /**
     * 是否无限循环
     */
    private boolean isEndLoop = true;
    /**
     * 当无限循环时  item的最大值
     */
    private int maxItemCount = Integer.MAX_VALUE;

    public BannerViewPagerAdapter(Context context) {

        this.context = context;
        itemTypeSparseArray = new SparseArray<>();
        datas = new ArrayList<>();
    }

    @Override
    public int getCount() {
        int   loopItemCount = datas.size() == 0 ? 0 :maxItemCount;
        return isEndLoop ? loopItemCount : datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        int index = getRealPosition(position);
        Log.d("adapter" ,"position:"+position +" index:"+index);
        ItemTypeBeanInterface t = (ItemTypeBeanInterface) datas.get(index);

        if (itemTypeSparseArray.get(t.getItemType()) != null) {
            BaseItemType itemType = itemTypeSparseArray.get(t.getItemType());
            View itemView = LayoutInflater.from(context).inflate(itemType.getItemViewLay(),null);
            itemType.instantiateItem(new BaseViewHolder(itemView), datas.get(index));
            container.addView(itemView);
            return itemView;
        }

        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void addItemType(BaseItemType<ItemTypeBeanInterface> itemType) {
        itemTypeSparseArray.put(itemType.getItemType(), itemType);

    }

    public List<T> getDatas() {

        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }


    public boolean isEndLoop() {
        return isEndLoop;
    }

    public void setEndLoop(boolean endLoop) {
        isEndLoop = endLoop;
        notifyDataSetChanged();
    }

    /**
     * 获取UI展示item对应角标位置 需 position > 0
     *
     * @param position
     * @return
     */
    public int getRealPosition(int position) {

        if (datas.size() == 0)  return 0;

        return isEndLoop ? position % datas.size() : position;

    }

    public int getStartPosition() {
        int startPosition = 0;

        if (getCount() ==  maxItemCount){
            startPosition = maxItemCount / 2 - ( (maxItemCount / 2) %  datas.size() );
        }
        return startPosition;
    }
}
