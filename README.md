# RefreashTabView
## 头部悬停可刷新控件

>Demo effect

![Demo effect](https://github.com/FreeSunny/RefreashTabView/blob/master/assets/refreash.gif)

>version 1.0.0

1：该控件主要实现了在同一个页面中有多个左右滑动切换的子页面，每一个子页面包含有一个可下拉刷新的listview

2：同时多个子页面面顶部共同包含同一个header头部，在各个子页面上下滑动时，header头跟着滑动，当向上滑动到一定程度时，
header头部悬停。向下滑动时，头部展开。

>version 1.0.1

1: 修复了大家说编译不过的问题，其实是因为页面gradle脚本的版本，和页面有几处注释不是utf-8编码造成的

>version 1.1.0

1: 增加自适应头部高度，可以动态改变头部高度

2：升级编译配置，gradle 1.2.3 - > 2.3.0

>version 1.1.1

1： 修复头部太高展示不正确

2： 移除兼容2.3版本代码

>version 2.0

1: SwipeRefreshLayout + NestedScrollView + AppBarLayout + RecycleView 等重新实现了该效果


2: 项目地址， Amazing下的Nest Scrolling View
[link](https://github.com/FreeSunny/Amazing#nest-scrolling-view)

> future

* 以下看用户需求

1: 顶部文字拦截事件处理

*2：可能会改写成recyclerView*

3: 去掉refresh控件，采用原生处理

#LICENSE
----------

> Copyright 2016 freeSunny

> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at

>    http://www.apache.org/licenses/LICENSE-2.0

> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.