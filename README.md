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
####ObjectAnimator源码分析
#####创建ObjectAnimator
######1.ObjectAnimator.ofInt(Object obj,String property,int ... intVals)返回ObjectAnimator对象
ObjectorAnimation主要是个工厂类，可以创建Int,float,object,argb类型的属性动画。方法里面创建ObjectAnimator对象，ObjectorAnimator构造方法里面，初始化属性的方法android.animation.ObjectAnimator#setTarget和android.animation.ObjectAnimator#setPropertyName，mTarget是个参数obj弱引用类型的属性(WeakReference<Object>)，设置mTarget时候，如果和原先的obj不同则执行代码，暂停动画，重新初始化obj;setPropertyName方法，获取mValues数组的第一个值，该数组是个android.animation.PropertyValuesHolder类型数组，这个属性待会会详细说明，先看下setPropertyName将获取mValues数组第一个元素属性字符串，android.animation.ValueAnimator#mValuesMap移除属性字符串的key,设置成新的动画属性字符串，值是mValues的第一个元素。
 

######2接着ObjectAnimation.ofInt(Object obj,String property,int ... intVals)
执行完ObjectValue构造方法并调用其android.animation.ObjectAnimator#setIntValues(PropertyValuesHolder... values)，初始化mValues和mValuesMap

2.1intVals的数据封装为PropertyValuesHolder.ofInt(mProperty, values)的形式作为元素保存在属性mValues数组里面。mValues可以看出是保存一系列属性及其动画值的PropertyValuesHolder数组，
PropertyValuesHolder的ofInt只是简单创建了IntPropertyValuesHolder对象的一行代码；
IntPropertyValuesHolder继承了PropertyValuesHolder，构造方法调用父类的setIntValues，设置intVals以KeyframeSet.ofInt(int ... intVals)保存在android.animation.PropertyValuesHolder#mKeyframes中。

2.2mValuesMap存储property字符串为key，以PropertyValuesHolder为值的数据，方便根据属性字符串，索引动画值。

######3.android.animation.ValueAnimator#setInterpolator方法
设置android.animation.ValueAnimator#mInterpolator的属性，默认是android.view.animation.AccelerateDecelerateInterpolator，调用方法时，如果为null则设置为android.view.animation.LinearInterpolator。该属性是在android.animation.ValueAnimator#animateValue方法调用过。

######4.android.animation.ValueAnimator#setEvaluator方法
在Animator里只有ofApla方法和ofObject有用到，该方法根据设置mValues第一个元素PropertyValuesHolder的android.animation.PropertyValuesHolder#mEvaluator属性和步骤2.1的KeyframeSet的android.animation.KeyframeSet#mEvaluator属性。

#####启动ObjectAnimator动画
1.android.animation.ObjectAnimator#start方法
从android.animation.ValueAnimator#sAnimationHandler（java.lang.ThreadLocal）调用一个android.animation.ValueAnimator.Animatorndler（android.animation.ValueAnimator.AnimatorHandler）的类型的对象，保证每个线程有个对应的AnimationHandler，Animatiorndler用来循环动画的类。<br/>
如果没从ThreadLocal获取到AnimationHandler，则android.animation.ValueAnimator#start()。<br/>
如果有，则取消AnimatorHandler里面的所有Animator（android.animation.ValueAnimator.AnimationHandler#mAnimations，android.animation.ValueAnimator.AnimationHandler#mPendingAnimations，android.animation.ValueAnimator.AnimationHandler#mDelayedAnims）动画执行，调用Animator的cancel方法

接着调用android.animation.ValueAnimator#start()，方法里面只调用了方法，start(false)。这个方法初始化和启动android.animation.ValueAnimator.AnimationHandler start方法。

android.animation.ValueAnimator.AnimationHandler#scheduleAnimation调用android.animation.ValueAnimator.AnimationHandler#mChoreographer#postCallback方法。android.animation.ValueAnimator.AnimationHandler#doAnimationFrame,通知界面android.animation.ValueAnimator#startAnimation<br/>
1.该方法执行android.animation.ValueAnimator.AnimationHandler#mPendingAnimations的所有动画;<br/>
2.如果没有延迟执行startAnimation，否则加入的android.animation.ValueAnimator.AnimationHandler#mDelayedAnims里面。<br/>
3.接着遍历延迟动画列表，将可以准备好执行的动画加入mReadyAnims，并开始执行mReadyAnims的animstor,清空相关的延迟animator数据。<br/>
4.遍历所有激活的动画，判断是否结束（android.animation.ValueAnimator#doAnimationFrame方法判断是否结束动画），并把结束的animator做处理，处理为于android.animation.ValueAnimator#endAnimation方法，并将改animator移除AnimatorHandler，发送通知android.animation.Animator.AnimatorListener#onAnimationEnd<br/>
5.调用android.animation.ValueAnimator.AnimationHandler#commitAnimationFrame，调用激活的animator的commitAnimationFrame方法<br/>
6.如果有animator未执行，则继续调用scheduleAnimation方法。

#####取消ObjectAnimator动画
android.animation.ValueAnimator#cancel，如果没有启动的animator，先执行android.animation.Animator.AnimatorListener#onAnimationStart方法，然后发送通知给监听对象android.animation.Animator.AnimatorListener#onAnimationCancel，最后android.animation.ValueAnimator#endAnimation，清除当前AnimatorHandler的所有动画信息。

##案例结构
[AnimationFactory.java](https://github.com/rickgit/animation/blob/master/app/src/main/java/edu/ptu/androidanimation/animation/AnimationFactory.java)
```
app
|____main
| |____java
| | |____edu
| | | |____ptu
| | | | |____androidanimation
| | | | | |____AnimationActivity.java 
| | | | | |____animation
| | | | | | |____AnimationFactory.java 抽象工厂实现，可以返回ViewAnimation,AnimationDrawable,Animator动画类
```
