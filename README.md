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
view.setBackgroundResource(R.drawable.animlist);

####DrawableAnimation源码分析
1.构造方法,默认构造方法没有state,res为null<br/>
```
    private AnimationDrawable(AnimationState state, Resources res) {
        final AnimationState as = new AnimationState(state, this, res);
        setConstantState(as);
        if (state != null) {
            setFrame(0, true, false);
        }
    }
```
android.graphics.drawable.AnimationDrawable.AnimationState 继承android.graphics.drawable.DrawableContainer.DrawableContainerState
AnimationState构造方法主要是对内部对象android.graphics.drawable.DrawableContainer.DrawableContainerState#mOwner的拷贝
```
    public abstract static class DrawableContainerState extends ConstantState {
        final DrawableContainer mOwner;
        
```
setConStatState方法将as保持到对象mAnimationState。

2.addFrame方法，将当前的frame保存到mAnimationState。并把frame设置为invisable
```
    public void addFrame(@NonNull Drawable frame, int duration) {
        mAnimationState.addFrame(frame, duration);
        if (!mRunning) {
            setFrame(0, true, false);
        }
    }
```
android.graphics.drawable.AnimationDrawable.AnimationState#addFrame
```
        public void addFrame(Drawable dr, int dur) {
            int pos = super.addChild(dr);
            mDurations[pos] = dur;
        }
```
super.addChild(dr)，保存了drawable并设置为不可见，保存drawable是个数组，每次容器大小不够，用System.arraycopy(mDrawables, 0, newDrawables, 0, oldSize);增长10个单位。<br/>
接下来看下AnimationDrawable的setFrame方法
```
    private void setFrame(int frame, boolean unschedule, boolean animate) {
        if (frame >= mAnimationState.getChildCount()) {
            return;
        }
        mAnimating = animate;
        mCurFrame = frame;
        selectDrawable(frame);
        if (unschedule || animate) {
            unscheduleSelf(this);
        }
        if (animate) {
            // Unscheduling may have clobbered these values; restore them
            mCurFrame = frame;
            mRunning = true;
            scheduleSelf(this, SystemClock.uptimeMillis() + mAnimationState.mDurations[frame]);
        }
    }
```
selectDrawable根据上面的addFrame方法，可知frame为0，unscheduleSelf方法调用Drawable的消息回调接口。

3.start方法
```
    public void start() {
        mAnimating = true;
        if (!isRunning()) {
            // Start from 0th frame.
            setFrame(0, false, mAnimationState.getChildCount() > 1
                    || !mAnimationState.mOneShot);
        }
    }
```
setFrame可以看，步骤2的分析。调用了android.graphics.drawable.Drawable#scheduleSelf方法,最终执行android.graphics.drawable.AnimationDrawable#run方法。run方法只有一行代码nextFrame(false),如下代码块。nextFrame增加1，再循环下一帧。
```
    private void nextFrame(boolean unschedule) {
        int nextFrame = mCurFrame + 1;
        final int numFrames = mAnimationState.getChildCount();
        final boolean isLastFrame = mAnimationState.mOneShot && nextFrame >= (numFrames - 1);

        // Loop if necessary. One-shot animations should never hit this case.
        if (!mAnimationState.mOneShot && nextFrame >= numFrames) {
            nextFrame = 0;
        }

        setFrame(nextFrame, unschedule, !isLastFrame);
    }
```

###视图动画(ViewAnimation)
官方定义：使用视图动画系统（view animation system）执行View的补间动画。补间通过计算start point, end point, size, rotation的信息执行动画

xml:
``` <alpha>, <scale>, <translate>, <rotate>,<set> ```

java:
android.view.animation.AnimationSet, android.view.animation.Animation, ,android.view.animation.AnimationUtils

####Animation源码分析
android.view.animation.Animation<br/>
1. AlphaAnimation构造方法
```
    public AlphaAnimation(float fromAlpha, float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
    }
    
```
2. start方法<br/>
调用android.view.animation.Animation#setStartTime方法
```
    /**
     * When this animation should start. When the start time is set to
     * {@link #START_ON_FIRST_FRAME}, the animation will start the first time
     * {@link #getTransformation(long, Transformation)} is invoked. The time passed
     * to this method should be obtained by calling
     * {@link AnimationUtils#currentAnimationTimeMillis()} instead of
     * {@link System#currentTimeMillis()}.
     *
     * @param startTimeMillis the start time in milliseconds
     */
    public void setStartTime(long startTimeMillis) {
        mStartTime = startTimeMillis;
        mStarted = mEnded = false;
        mCycleFlip = false;
        mRepeated = 0;
        mMore = true;
    }
```
注释提示会调用#getTransformation(long, Transformation)，在View.Draw时候的执行android.view.View#applyLegacyAnimation会调用到
```
draw()绘制的元素
         *      1. Draw the background
         *      2. If necessary, save the canvas' layers to prepare for fading
         *      3. Draw view's content
         *      4. Draw children
         *      5. If necessary, draw the fading edges and restore layers
         *      6. Draw decorations (scrollbars for instance)
applyLegacyAnimation方法
    private boolean applyLegacyAnimation(ViewGroup parent, long drawingTime,
            Animation a, boolean scalingRequired) {
        Transformation invalidationTransform;
        final int flags = parent.mGroupFlags;
        final boolean initialized = a.isInitialized();
        if (!initialized) {
            a.initialize(mRight - mLeft, mBottom - mTop, parent.getWidth(), parent.getHeight());
            a.initializeInvalidateRegion(0, 0, mRight - mLeft, mBottom - mTop);
            if (mAttachInfo != null) a.setListenerHandler(mAttachInfo.mHandler);
            onAnimationStart();
        }

        final Transformation t = parent.getChildTransformation();
        boolean more = a.getTransformation(drawingTime, t, 1f);
        if (scalingRequired && mAttachInfo.mApplicationScale != 1f) {
            if (parent.mInvalidationTransformation == null) {
                parent.mInvalidationTransformation = new Transformation();
            }
            invalidationTransform = parent.mInvalidationTransformation;
            a.getTransformation(drawingTime, invalidationTransform, 1f);
        } else {
            invalidationTransform = t;
        }
```
a.setListenerHandler(mAttachInfo.mHandler);设置了Animation的handler，进行事件处理<br/>
可以看出是运用Transform进行对象的设置改变的<br/>
android.view.animation.AlphaAnimation#applyTransformation
```
    /**
     * Changes the alpha property of the supplied {@link Transformation}
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float alpha = mFromAlpha;
        t.setAlpha(alpha + ((mToAlpha - alpha) * interpolatedTime));
    }
```
先看下android.view.animation.Transformation含有两个重要属性
```
    protected Matrix mMatrix;
    protected float mAlpha;
```
这两个属性就是属性动画包换的视图转换信息

3. cancel方法
```
    public void cancel() {
        if (mStarted && !mEnded) {
            fireAnimationEnd();
            mEnded = true;
            guard.close();
        }
        // Make sure we move the animation to the end
        mStartTime = Long.MIN_VALUE;
        mMore = mOneMoreTime = false;
    }

```
####AnimationUtils将xml动画文件转换为Animation
```
 public static Animation loadAnimation(Context context, @AnimRes int id)
            throws NotFoundException {

        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            return createAnimationFromXml(context, parser);
```
最终调用android.view.animation.AnimationUtils#createAnimationFromXml(android.content.Context, org.xmlpull.v1.XmlPullParser, android.view.animation.AnimationSet, android.util.AttributeSet)，转化为Animation对象
```
    private static Animation createAnimationFromXml(Context c, XmlPullParser parser,
            AnimationSet parent, AttributeSet attrs) throws XmlPullParserException, IOException {

        Animation anim = null;

        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();

        while (((type=parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth)
               && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            String  name = parser.getName();

            if (name.equals("set")) {
                anim = new AnimationSet(c, attrs);
                createAnimationFromXml(c, parser, (AnimationSet)anim, attrs);
            } else if (name.equals("alpha")) {
                anim = new AlphaAnimation(c, attrs);
            } else if (name.equals("scale")) {
                anim = new ScaleAnimation(c, attrs);
            }  else if (name.equals("rotate")) {
                anim = new RotateAnimation(c, attrs);
            }  else if (name.equals("translate")) {
                anim = new TranslateAnimation(c, attrs);
            } else {
                throw new RuntimeException("Unknown animation name: " + parser.getName());
            }

            if (parent != null) {
                parent.addAnimation(anim);
            }
        }

        return anim;

    }
```

###属性动画(PropertyAnimation Android 3.0,API level 11)
官方定义：通过一段时间改变任何Object的属性形成动画
TODO 插值器（Interpolator）和估值器（TypeEvaluator）分析
```
java:
android.animation.ObjectAnimator
android.animation.Animator
android.animation.AnimatorSet
android.animation.ValueAnimator
TimeInterpolator
android.view.ViewPropertyAnimator 封装了ValueAnimator

Duration:一个动画的时长，默认300ms
Time interpolation: 一段代码块计算当前的属性的值
Frame refresh delay：刷新的时间，默认10ms
```
####ValueAnimator
通过指定一系列类型（int, float, or color）的值，使这些类型的值动态变动
####ObjectAnimator
ObjectAnimator是ValueAnimator的子类，包含分时引擎和动画值计算，使目标对象的相应属性产生动画变动
####ObjectAnimator源码分析
#####创建ObjectAnimator
1.ObjectAnimator.ofInt(Object obj,String property,int ... intVals)返回ObjectAnimator对象
ObjectorAnimation主要是个工厂类，可以创建Int,float,object,argb类型的属性动画。
```
    public static ObjectAnimator ofInt(Object target, String propertyName, int... values) {
        ObjectAnimator anim = new ObjectAnimator(target, propertyName);
        anim.setIntValues(values);
        return anim;
    }
```
方法里面创建ObjectAnimator对象，
```
    private ObjectAnimator(Object target, String propertyName) {
        setTarget(target);
        setPropertyName(propertyName);
    }
```
ObjectorAnimator构造方法里面，初始化属性的方法android.animation.ObjectAnimator#setTarget和android.animation.ObjectAnimator#setPropertyName，mTarget是个参数obj弱引用类型的属性(WeakReference<Object>)，设置mTarget时候，如果和原先的obj不同则执行代码，暂停动画，重新初始化obj;setPropertyName方法，获取mValues数组的第一个值，该数组是个android.animation.PropertyValuesHolder类型数组，这个属性待会会详细说明，先看下setPropertyName将获取mValues数组第一个元素属性字符串，android.animation.ValueAnimator#mValuesMap移除属性字符串的key,设置成新的动画属性字符串，值是mValues的第一个元素。
 
<br/>
2接着ObjectAnimation.ofInt(Object obj,String property,int ... intVals)
执行完ObjectValue构造方法并调用其android.animation.ObjectAnimator#setIntValues(PropertyValuesHolder... values)，初始化mValues和mValuesMap
```
    @Override
    public void setIntValues(int... values) {
        if (mValues == null || mValues.length == 0) {
            // No values yet - this animator is being constructed piecemeal. Init the values with
            // whatever the current propertyName is
            if (mProperty != null) {
                setValues(PropertyValuesHolder.ofInt(mProperty, values));
            } else {
                setValues(PropertyValuesHolder.ofInt(mPropertyName, values));
            }
        } else {
            super.setIntValues(values);
        }
    }
```
2.1 intVals的数据封装为PropertyValuesHolder.ofInt(mProperty, values)的形式作为元素保存在属性mValues数组里面。mValues可以看出是保存一系列属性及其动画值的PropertyValuesHolder数组，
PropertyValuesHolder的ofInt只是简单创建了IntPropertyValuesHolder对象的一行代码；
IntPropertyValuesHolder继承了PropertyValuesHolder，构造方法调用父类的setIntValues，设置intVals以KeyframeSet.ofInt(int ... intVals)保存在android.animation.PropertyValuesHolder#mKeyframes中。
```
    public void setValues(PropertyValuesHolder... values) {
        int numValues = values.length;
        mValues = values;
        mValuesMap = new HashMap<String, PropertyValuesHolder>(numValues);
        for (int i = 0; i < numValues; ++i) {
            PropertyValuesHolder valuesHolder = values[i];
            mValuesMap.put(valuesHolder.getPropertyName(), valuesHolder);
        }
        // New property/values/target should cause re-initialization prior to starting
        mInitialized = false;
    }

```

2.2 mValuesMap存储property字符串为key，以PropertyValuesHolder为值的数据，方便根据属性字符串，索引动画值。
<br/>
3.android.animation.ValueAnimator#setInterpolator方法
设置android.animation.ValueAnimator#mInterpolator的属性，默认是android.view.animation.AccelerateDecelerateInterpolator，调用方法时，如果为null则设置为android.view.animation.LinearInterpolator。该属性是在android.animation.ValueAnimator#animateValue方法调用过。

<br/>
4.android.animation.ValueAnimator#setEvaluator方法
在Animator里只有ofApla方法和ofObject有用到，该方法根据设置mValues第一个元素PropertyValuesHolder的android.animation.PropertyValuesHolder#mEvaluator属性和步骤2.1的KeyframeSet的android.animation.KeyframeSet#mEvaluator属性。

#####启动ObjectAnimator动画
1.android.animation.ObjectAnimator#start方法
从android.animation.ValueAnimator#sAnimationHandler（java.lang.ThreadLocal）调用一个android.animation.ValueAnimator.AnimatorHandler（android.animation.ValueAnimator.AnimatorHandler）的类型的对象，保证每个线程有个对应的AnimationHandler，Animatiorndler用来循环动画的类。<br/>
如果没从ThreadLocal获取到AnimationHandler，则android.animation.ValueAnimator#start()。<br/>
如果有，则取消AnimatorHandler里面的所有Animator（android.animation.ValueAnimator.AnimationHandler#mAnimations，android.animation.ValueAnimator.AnimationHandler#mPendingAnimations，android.animation.ValueAnimator.AnimationHandler#mDelayedAnims）动画执行，调用Animator的cancel方法

接着调用android.animation.ValueAnimator#start()，方法里面只调用了方法，start(false)。这个方法初始化和启动android.animation.ValueAnimator.AnimationHandler start方法。
```
        void doAnimationFrame(long frameTime) {
            mLastFrameTime = frameTime;

            // mPendingAnimations holds any animations that have requested to be started
            // We're going to clear mPendingAnimations, but starting animation may
            // cause more to be added to the pending list (for example, if one animation
            // starting triggers another starting). So we loop until mPendingAnimations
            // is empty.
            while (mPendingAnimations.size() > 0) {
                ArrayList<ValueAnimator> pendingCopy =
                        (ArrayList<ValueAnimator>) mPendingAnimations.clone();
                mPendingAnimations.clear();
                int count = pendingCopy.size();
                for (int i = 0; i < count; ++i) {
                    ValueAnimator anim = pendingCopy.get(i);
                    // If the animation has a startDelay, place it on the delayed list
                    if (anim.mStartDelay == 0) {
                        anim.startAnimation(this);
                    } else {
                        mDelayedAnims.add(anim);
                    }
                }
            }
```
android.animation.ValueAnimator.AnimationHandler#scheduleAnimation调用android.animation.ValueAnimator.AnimationHandler#mChoreographer#postCallback方法。android.animation.ValueAnimator.AnimationHandler#doAnimationFrame,通知界面android.animation.ValueAnimator#startAnimation<br/>,执行android.animation.ValueAnimator#animationFrame，调用android.animation.ValueAnimator#animateValue更新target的数据。
1.该方法执行android.animation.ValueAnimator.AnimationHandler#mPendingAnimations的所有动画;<br/>

2.如果没有延迟执行startAnimation，否则加入的android.animation.ValueAnimator.AnimationHandler#mDelayedAnims里面。<br/>
```
            // Next, process animations currently sitting on the delayed queue, adding
            // them to the active animations if they are ready
            int numDelayedAnims = mDelayedAnims.size();
            for (int i = 0; i < numDelayedAnims; ++i) {
                ValueAnimator anim = mDelayedAnims.get(i);
                if (anim.delayedAnimationFrame(frameTime)) {
                    mReadyAnims.add(anim);
                }
            }
            int numReadyAnims = mReadyAnims.size();
            if (numReadyAnims > 0) {
                for (int i = 0; i < numReadyAnims; ++i) {
                    ValueAnimator anim = mReadyAnims.get(i);
                    anim.startAnimation(this);
                    anim.mRunning = true;
                    mDelayedAnims.remove(anim);
                }
                mReadyAnims.clear();
            }
```
3.接着遍历延迟动画列表，将可以准备好执行的动画加入mReadyAnims，并开始执行mReadyAnims的animstor,清空相关的延迟animator数据。<br/>
```
            // Now process all active animations. The return value from animationFrame()
            // tells the handler whether it should now be ended
            int numAnims = mAnimations.size();
            for (int i = 0; i < numAnims; ++i) {
                mTmpAnimations.add(mAnimations.get(i));
            }
            for (int i = 0; i < numAnims; ++i) {
                ValueAnimator anim = mTmpAnimations.get(i);
                if (mAnimations.contains(anim) && anim.doAnimationFrame(frameTime)) {
                    mEndingAnims.add(anim);
                }
            }
            mTmpAnimations.clear();
            if (mEndingAnims.size() > 0) {
                for (int i = 0; i < mEndingAnims.size(); ++i) {
                    mEndingAnims.get(i).endAnimation(this);
                }
                mEndingAnims.clear();
            }
```
4.遍历所有激活的动画，判断是否结束（android.animation.ValueAnimator#doAnimationFrame方法判断是否结束动画），并把结束的animator做处理，处理为于android.animation.ValueAnimator#endAnimation方法，并将改animator移除AnimatorHandler，发送通知android.animation.Animator.AnimatorListener#onAnimationEnd。通过android.animation.ValueAnimator#animationFrame更新界面。<br/>
```
            // Schedule final commit for the frame.
            mChoreographer.postCallback(Choreographer.CALLBACK_COMMIT, mCommit, null);
```
5.调用android.animation.ValueAnimator.AnimationHandler#commitAnimationFrame，调用激活的animator的commitAnimationFrame方法<br/>
```
            // If there are still active or delayed animations, schedule a future call to
            // onAnimate to process the next frame of the animations.
            if (!mAnimations.isEmpty() || !mDelayedAnims.isEmpty()) {
                scheduleAnimation();
            }
```
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
