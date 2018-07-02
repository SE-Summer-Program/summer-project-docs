# Android编码规范

标签（空格分隔）： Android codingRules

---

> ## 1. 文件命名
> ### 1.1   Layout文件
> * 1.1.1 ***contentView*** 命名
> *activity_模块名_activity名*
  *fragment_模块名_activity名_fragment名*
> ######
> * 1.1.2 ***列表项*** 命名
> *listitem_模块名_所属的ListView的名称*
> ######
> * 1.1.3 ***Dialog*** 命名
> *dialog_模块名_方法*
> ######
> * 1.1.4 ***包含项*** 命名
> *Include_模块名_种类_方法*
Include_XXX_head_
Include_XXX_foot_

> ### 1.2 包名和文件夹命名
      一般来说文件夹里包括activity和相应取数据的工具，对于比较简单的文件夹退化成文件文件夹名用小写，文件名每个单词首字母大写


> ### 1.3 Java文件
> * 1.3.1	***Activity*** 文件
>
        所有单词的第一个字母大写，要以Activity结尾以包名的结尾开头，比如同去的活动详细信息页是TongquInfoActivity，大APP的登录页面是MainLoginActivity
> ######
> * 1.3.2	***Fragment*** 文件
>
        所有单词的第一个字母大写，要以Fragment结尾
		所属的Activity名+Fragment名+Fragment
		比如CenterTongquCreateFragment是我发起的活动的Fragment
> ######
> * 1.3.3	***Provider*** 文件
>
        包括提供数据或者其他什么的文件，比如说用GET或POST工具获得了json（String格式），provider文件用来把它转换成Java类，为Activity提供数据
	    所有单词的第一个字母大写，要以Provider结尾
        包名的结尾+Provider这个类的功能+Provider
				比如同去的列表页活动列表数据，TongquActListProvider
> ######
> * 1.3.4	***Adapter***文件
>
        适配器文件，所有单词的第一个字母大写，要以Adapter结尾
		以适配的类型开头，比如ListView、Pager、GridView、ExpandableList
		中间是这个Adapter的功能或者这个控件所属的Activity或Fragment
> ######
> * 1.3.5	***Info*** 文件
>
        用来保存某个类的各种属性
		比如ActivityInfo（新的应该改名成TongquActInfo）保存的是每一项活动的各种属性
		包名的结尾+功能+Info
> ######
> * 1.3.6	***Receiver*** 文件
>
        目前只有AlarmReceiver和BootReceiver
> ######
> * 1.3.7	***其它***
>
        再说吧。。。
> ######
> ### 1.4	资源文件
> * 1.4.1	**drawable** 图片
> * 1.4.2	**anim** 动画
> * 1.4.3	**menu** 菜单
> * 1.4.4	**其它**（基本是一些和界面有关的东西）

> ## 2	Java内部代码命名
> ### 2.1	资源
> * 2.1.1	ID命名——layout
						驼峰命名，除了第一个其它单词首字母大写
						以控件的类名开头，比如listView、button、imageButton
						跟着layout后面部分的名字再跟着这个控件的功能小写，下划线连接
> * 2.1.2	ID命名——menu

> * 2.1.3	String命名
						小写模块名（或者通用的就global）+下划线+出现的场合+功能
> * 2.1.4
> ### 2.2	变量

> ### 2.3	函数


> ## 3 Java文件代码结构
> ### 3.1	Activity文件
        变量定义
		构造函数
		（菜单）
		onCreate()函数
		其它重写函数
		onCreate()里的每一个部分拆分成一个函数
		自己的工具函数
> ### 3.2	Fragment文件
		变量定义
		传值的函数和构造函数
		onCreate()
		onCreateView()
		其它重写函数
		onCreateView()里的每一个部分拆分成一个函数
		自己的工具函数
> ### 3.3	类文件
		变量定义	属性名即可
		构造函数
		重写的函数
		自己的函数
		    getX…()
		    setX…()
		    initXXX()
		    isXXX()
		    checkXXX()
		    getXXX()
		    processXXX()
		    displayXXX()
		    saveXXX()
		    resetXXX()
		    clearXXX()
		    removeXXX()
> ### 3.4	Adapter文件
		变量定义
		构造函数
		重写函数
		自己的工具函数

> ## 4	注释
> ### 4.1	文件注释
		    /*
			    XXX
		    */
		写在class头顶，import下面，写清楚这个文件的功能
> ### 4.2	段落注释
			/*
			XXX------------------------------------------------------------------------
			*/
> ### 4.3	函数注释
			/*
			XXX
			*/
		写在每个函数的头顶
> ### 4.4	代码注释
			//
		不一定要另起一行
> ##5	Log输出记录



        Android编码规范建议（别人弄的觉得蛮有道理）
        1.　java代码中不出现中文，最多注释中可以出现中文
        2.　局部变量命名、静态成员变量命名
            只能包含字母，单词首字母除第一个外，都为大写，其他字母都为小写
        3.常量命名
            只能包含字母和_，字母全部大写，单词之间用_隔开
        4.图片尽量分拆成多个可重用的图片
        5.服务端可以实现的，就不要放在客户端
        6.引用第三方库要慎重，避免应用大容量的第三方库，导致客户端包非常大
        7.处理应用全局异常和错误，将错误以邮件的形式发送给服务端
        8.图片的.9处理
        9.使用静态变量方式实现界面间共享要慎重
        10.Log(系统名称模块名称接口名称，详细描述)
        11.单元测试（逻辑测试、界面测试）
        12.不要重用父类的handler，对应一个类的handler也不应该让其子类用到，否则会导致message.what冲突
        13.activity中在一个View.OnClickListener中处理所有的逻辑
        14.strings.xml中使用%1$s实现字符串的通配
        15.如果多个Activity中包含共同的UI处理，那么可以提炼一个CommonActivity，把通用部分叫由它来处理，其他activity只要继承它即可
        16.使用button+activitgroup实现tab效果时，使用Button.setSelected(true)，确保按钮处于选择状态，并使activitygroup的当前activity与该button对应
        17.如果所开发的为通用组件，为避免冲突，将drawable/layout/menu/values目录下的文件名增加前缀
        18.数据一定要效验，例如
            字符型转数字型，如果转换失败一定要有缺省值；
            服务端响应数据是否有效判断
