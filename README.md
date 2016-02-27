# 动画
##定义
[指由许多帧静止的画面，以一定的速度（如每秒16张）连续播放时，肉眼因视觉残象产生错觉，而误以为画面活动的作品。](https://zh.wikipedia.org/wiki/动画)。这是wiki百科的定义，但在实际开发中，手机是以每秒60帧进行绘制元素，保证动画的流畅度。

##Android 2D动画
###补间动画(DrawableAnimation)
官方定义：加载一系列的Drawable，一个接着另一个，组成动画

xml:
``` <animation-list> ```
item 元素的android:duration属性设置播放的时间，单位为毫秒(ms)

java:
android.graphics.drawable.AnimationDrawable

###视图动画(ViewAnimation)
官方定义：使用视图动画系统（view animation system）执行View的补间动画。补间通过计算start point, end point, size, rotation的信息执行动画

xml:
``` <alpha>, <scale>, <translate>, <rotate>,<set> ```

java:
android.view.animation.AnimationSet,android.view.animation.Animation

###属性动画(PropertyAnimation Android 3.0,API level 11)
官方定义：通过一段时间改变任何Object的属性形成动画

java:
android.animation.ValueAnimator，android.animation.ObjectAnimator，android.animation.Animator，TimeInterpolator
Duration:一个动画的时长，默认300ms
Time interpolation: 一段代码块计算当前的属性的值
Frame refresh delay：刷新的时间，默认10ms
####ValueAnimator
通过指定一系列类型（int, float, or color）的值，使这些类型的值动态变动
####ObjectAnimator
ObjectAnimator是ValueAnimator的子类，包含分时引擎和动画值计算，使目标对象的相应属性产生动画变动


##类结构
<pre>
app
|____main
| |____java
| | |____edu
| | | |____ptu
| | | | |____androidanimation
| | | | | |____AnimationActivity.java 
| | | | | |____animation
| | | | | | |____[AnimationFactory.java](https://github.com/rickgit/animation/blob/master/app/src/main/java/edu/ptu/androidanimation/animation/AnimationFactory.java) 抽象工程可以返回ViewAnimation,AnimationDrawable,Animator动画类
| |____AndroidManifest.xml
| |____res
| | |____drawable
| | | |____ic_launcher.png
| | |____mipmap-xhdpi
| | | |____ic_launcher.png
| | |____values
| | | |____styles.xml
| | | |____strings.xml
| | | |____dimens.xml
| | |____menu
| | | |____menu_animation.xml
| | |____layout
| | | |____activity_animation.xml
| | |____anim
|____androidTest
| |____java
| | |____edu
| | | |____ptu
| | | | |____androidanimation
| | | | | |____ApplicationTest.java
</pre>
