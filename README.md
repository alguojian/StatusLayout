### StatusLayout 多种加载view状态的管理viewGroup

> 使用如下 :

#### 1.Add it in your root build.gradle at the end of repositories:
```
allprojects {
            repositories {
            ...
            maven { url 'https://www.jitpack.io' }
            }
}
```

#### 2.Add the dependency
```
dependencies {
                implementation 'com.github.ALguojian:StatusLayout:0.0.1'
        }
```

##### 支持在activity，和fragment中使用，支持包裹任意view使用，只需要在使用的view中使用--id--statusLayout即可

```
    <TextView
            android:id="@+id/statusLayout"
            android:gravity="center"
            android:background="#ff0"
            android:layout_width="300dp"
            android:layout_height="299dp"
            android:text="Hello World!"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>
```


#### 初始化可以放在application或者app第一个activity中初始化
```

//设置默认布局，DefaultStatusAdapter()为提供的默认加载布局，如果需要自定义，往下看
StatusLayout.getInstance().setDefaultAdapter(DefaultStatusAdapter())

//开启debug日志，默认关闭
StatusLayout.setDebug()


//activity中使用
StatusLayout.getInstance()//获得默认构造对象
.attachView(this)//绑定activity
.onRetryClick(View.OnClickListener {//加载失败，点击重试的回调
        
})

//fragment中使用，如果id-statusLayout，在fragment中为xml的根节点时，这是通过view的getParent()，是找不到父view的，
所以新建了一个新的FramLayout()，进行状态管理，所以，在onCreateView中需要返回StatusLayout.getInstance().getRootView()

val inflate: ViewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_simple, container, false)

StatusLayout.getInstance()
.attachView(inflate.root)//直接绑定引入的根view
.onRetryClick(View.OnClickListener {  })

//需要返回管理view
return StatusLayout.getInstance().getRootView()


//更新状态
StatusLayout.getInstance().showLoading()
StatusLayout.getInstance().showSuccess()
StatusLayout.getInstance().showFailed()
StatusLayout.getInstance().showEmpty()

//设置一个新的adapter,后续操作都需要使用新的StatusLayout对象
StatusLayout statusLayout = StatusLayout.setNewAdapter(DefaultStatusAdapter())
statusLayout.attachView(this).onRetryClick(View.OnClickListener {
           
})

```

#### 设置适配器,只需要集成StatusAdapter，即可，返回自定义的加载view

```
class DefaultStatusAdapter : StatusAdapter {
    override fun getView(statusLayout: StatusLayout, statusView: View?, status: Int): View? {

        var defaultLoadingView: DefaultLoadingView? = null
        if (statusView != null && statusView is DefaultLoadingView) defaultLoadingView = statusView
        if (defaultLoadingView == null) {
            defaultLoadingView = DefaultLoadingView(statusLayout.getContext(), statusLayout.getRetryClick())
        }
        defaultLoadingView.setStatus(status)
        return defaultLoadingView
    }
}

```

#### 提供的默认加载view
```
class DefaultLoadingView(context: Context, private val mOnClickListener: View.OnClickListener?) :
    LinearLayout(context) {

    private val mTextView: TextView
    private val mImageView: ImageView
    private val errorBoldView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.status_loading_view, this, true)
        mImageView = findViewById(R.id.image)
        mTextView = findViewById(R.id.text)
        errorBoldView = findViewById(R.id.errorBoldView)
        setBackgroundColor(-0xf0f10)
    }

    /**
     * 设置是否显示图示文案
     *
     * @param visible
     */
    fun setTextVisibility(visible: Boolean) {
        mTextView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 设置新状态的view信息
     *
     * @param status
     */
    fun setStatus(status: Int) {
        var show = true
        var image = R.drawable.status_loading
        var str = ""
        errorBoldView.visibility = if (status == StatusLayout.STATUS_LAYOUT_STATUS_FAIL) View.VISIBLE else View.GONE

        when (status) {
            StatusLayout.STATUS_LAYOUT_STATUS_SUCCESS -> show = false
            StatusLayout.STATUS_LAYOUT_STATUS_LOADING -> {
                str = "加载中..."
            }
            StatusLayout.STATUS_LAYOUT_STATUS_FAIL -> {
                str = "点击空白重试"
                image = R.drawable.status_ic_state_error
                setOnClickListener(mOnClickListener)
            }
            StatusLayout.STATUS_LAYOUT_STATUS_EMPTY -> {
                str = "暂无数据"
                image = R.drawable.status_ic_state_empty
            }
        }
        mImageView.setImageResource(image)
        mTextView.text = str
        val layoutParams = mImageView.layoutParams as LinearLayout.LayoutParams
        layoutParams.bottomMargin = if (status == StatusLayout.STATUS_LAYOUT_STATUS_LOADING) 30 else -90
        visibility = if (show) View.VISIBLE else View.GONE

    }
}
```