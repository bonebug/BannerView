# Android图片轮播控件
# 由于本控件更多是从封装者角度出发，所以会存在一些易用上的问题，欢迎提出改进建议 
# 不建议直接使用 没为啥 就感觉写的不好  希望你看完代码能提出你的建议 完善你的思路 那么 万分感谢了

<br>

现在的绝大数app都有banner界面，实现欢迎页的正常轮播，或者广告图的无限轮播，这里简单封装了下控件。实现方式有两种：
一、常规的ViewPager
二、RecyclerView + SnapHelper
其中 ViewPager属于常规做法，RecyclerView + SnapHelper 方式理论上效率会高点内存会更节省点，自带了回收机制
这里推荐使用第二种

注意：两种实现均做了view不可见时 自动停止轮播


## 效果图

没。。。。


## 常量
|常量名称|描述|
|---|---|
|NavigatorLocationConstant.TOP_LEFT| 导航点位置在左上
|NavigatorLocationConstant.TOP_CENTER| 导航点位置在上中
|NavigatorLocationConstant.TOP_RIGHT| 导航点位置在右上
|NavigatorLocationConstant.BOTTOM_LEFT| 导航点位置在左下
|NavigatorLocationConstant.BOTTOM_CENTER| 导航点位置在底部剧中
|NavigatorLocationConstant.BOTTOM_RIGHT| 导航点位置在右下


## 动画常量类（RecyclerView + SnapHelper模式下的动画常量， viewpager模式下直接调用ViewPager自带的方法即可）
|常量名称|
|---|
|RecyclerViewPagerAdatper.ALPHAIN
|RecyclerViewPagerAdatper.SCALEIN
|RecyclerViewPagerAdatper.SLIDEIN_BOTTOM
|RecyclerViewPagerAdatper.SLIDEIN_LEFT
|RecyclerViewPagerAdatper.SLIDEIN_RIGHT


## 方法（BannerView---ViewPager）
|方法名|描述
|---|---|
|setNeedNavigator() |  设置是否需要导航点
|addItemType() | 添加item布局类型
|getAdapter() | 获取banner的adapter
|isAutoPlay() | 获取当前banner是否在自动轮播
|setAutoPlay() | 设置banner是否自动轮播
|startAutoPlay() | 开启自动轮播--- 需设置可自动轮播
|stopAutoPlay() | 关闭自动轮播
|setEndLoop() | 设置是否无限轮播
|setNavigatorParams() | 设置导航点的位置
|setNavigatorPointsBackgroundColors() | 设置导航点的背景色
|setNavigatorPointsBackgroundDrawable() | 设置导航点的背景图
|setNavigatorPointsGaps() | 设置导航点间距
|setNavigatorPointsRadius() | 设置导航点的半径
|setPageTransformer() | 设置页面切换动画
|setPageChangeListener() | 设置页面切换监听

## 方法（BannerView---RecyclerView + SnapHelper）
|方法名|描述
|---|---|
|setNeedNavigator() |  设置是否需要导航点
|addItemType() | 添加item布局类型
|getAdapter() | 获取banner的adapter
|isAutoPlay() | 获取当前banner是否在自动轮播
|setAutoPlay() | 设置banner是否自动轮播
|startAutoPlay() | 开启自动轮播--- 需设置可自动轮播
|stopAutoPlay() | 关闭自动轮播
|setEndLoop() | 设置是否无限轮播
|setNavigatorParams() | 设置导航点的位置
|setNavigatorPointsBackgroundColors() | 设置导航点的背景色
|setNavigatorPointsBackgroundDrawable() | 设置导航点的背景图
|setNavigatorPointsGaps() | 设置导航点间距
|setNavigatorPointsRadius() | 设置导航点的半径
|setPageTransformer() | 设置页面切换动画
|setPageChangeListener() | 设置页面切换监听




## 使用步骤

#### Step ... 看Demo

#### v1.0.0
* 添加基本功能

* 明显缺陷：recyclerview+snaphepler 中item切换动画效果不好，没有过程 后续完善，能说出来的就上面那些 由于着重点不一样 其他的 欢迎吐槽拍砖。。。。。




