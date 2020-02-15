### StatusLayout 多种加载view状态的管理viewGroup

#### 使用kotlin编写，最新版本为 ,使用如下
[![](https://jitpack.io/v/alguojian/StatusLayout.svg)](https://jitpack.io/#alguojian/StatusLayout)


#### 1.Add it in your root build.gradle at the end of repositories:
```
allprojects {
        repositories {
             maven { url 'https://www.jitpack.io' }
        }
}
```

#### 2.Add the dependency
```
dependencies {
        implementation 'com.github.ALguojian:StatusLayout:1.0.6'
}
```

#### 3.如果项目没有添加kotlin支持，需要在项目build文件以及app添加添加，我现在使用1.3.21版本
```
dependencies {
        classpath 'com.android.tools.build:gradle:3.3.2'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
}
    
dependencies {
        implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
}  
```


#### 支持在activity，和fragment中使用，支持包裹任意view使用，只需要在使用的view中使用--id--statusLayout即可

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
  StatusLayout.setDefaultAdapter(StatusLayoutDefaultAdapter())

//开启debug日志，默认关闭
StatusLayout.setDebug()

```

##### 1.activity中使用
```
var statusHelper=StatusLayout.attachView(this)//绑定activity                                                   
                                                     .onRetryClick { }//加载失败，点击重试的回调   
```

##### 2.fragment中使用
**注意：：如果id-statusLayout，在fragment中为xml的根节点时，这是通过view的getParent()，是找不到父view的，
所以新建了一个新的FramLayout()，进行状态管理，所以，在onCreateView中需要返回StatusLayout.getInstance().getRootView()**

```
val inflate: ViewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_simple, container, false)

StatusLayout
.attachView(inflate.root)//直接绑定引入的根view
.onRetryClick {  }

//需要返回管理view
return StatusLayout.getRootView()
```

##### 3.更新状态
```
statusHelper.showLoading()
statusHelper.showSuccess()
statusHelper.showFailed()
statusHelper.showEmpty()

//设置一个新的adapter,后续操作都需要使用新的StatusLayout对象
StatusLayout statusLayout = StatusLayout.setNewAdapter(DefaultStatusAdapter())
statusLayout.attachView(this).onRetryClick {
           
}

//如果为某一个页面设置了新的适配器，需要添加

override fun onDestroy() {
        super.onDestroy()
        StatusLayout.clearNewAdapter()
    }

```

##### 4.为某一个页面设置新的适配器,只需要继承StatusAdapter，即可，返回自定义的加载view

```
class StatusLayoutDefaultAdapter : StatusAdapter {
    override fun getView(statusHelper: StatusLayout.StatusHelper, statusView: View?, status: Int): View? {
        var defaultLoadingView: DefaultLoadingView? = null
        if (statusView != null && statusView is DefaultLoadingView) defaultLoadingView = statusView
        if (defaultLoadingView == null) {
            defaultLoadingView = DefaultLoadingView(statusHelper.context!!, statusHelper.click)
        }
        defaultLoadingView.setStatus(status)
        return defaultLoadingView

    }

}


```