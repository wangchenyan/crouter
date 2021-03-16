# crouter

[![](https://jitpack.io/v/wangchenyan/crouter.svg)](https://jitpack.io/#wangchenyan/crouter)

## 系列文章

- [手撸一个 Router 框架（上）：熟悉 APT](https://juejin.im/post/6844903923606618126)
- [手撸一个 Router 框架（下）：路由拦截机制](https://juejin.im/post/6844904193866596365)

## Features

1. 支持直接解析标准 URL 进行跳转，参数在目标页面通过 getIntent 获取
2. 支持多模块工程使用
3. 支持添加多个拦截器，自定义拦截顺序
4. Activity 自动注册（使用 [AutoRegister](https://github.com/luckybilly/AutoRegister) 实现自动注册）
5. 自动处理 `startActivityForResult` 回调
6. 支持获取 Fragment
7. 支持 Kotlin

## Change Log

`v2`
- 优化 Api 调用

`v1`
- 完成路由功能

## Dependency

1. Add gradle dependency

```
// root project build.gradle
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:3.6.4'
        classpath 'com.billy.android:autoregister:1.4.2'
    }
}
```

2. Add library dependency

```
// module build.gradle

...

dependencies {
    ...
    kapt 'com.github.wangchenyan.crouter:crouter-compiler:2.2'
    implementation 'com.github.wangchenyan.crouter:crouter-annotation:2.2'
    implementation 'com.github.wangchenyan.crouter:crouter-api:2.2'
}
```

3. Config

```
// module build.gradle
...

// 路由配置
kapt {
    arguments {
        // 模块名
        arg("moduleName", project.name)
    }
}

// 路由收集，使用默认配置
autoregister {
    registerInfo = [
            [
                    'scanInterface'        : 'me.wcy.router.annotation.RouterLoader',
                    'codeInsertToClassName': 'me.wcy.router.RouterSet',
                    'registerMethodName'   : 'register',
                    'include'              : ['me/wcy/router/annotation/loader/.*']
            ]
    ]
}

...
```

## Usage

1. 初始化，设置路由客户端。建议在 Application 的 onCreate，或第一个 Activity 的 onCreate 中执行

```
CRouter.setRouterClient(
    RouterClient.Builder()
        // 可选，设置登录拦截器
        .loginProvider { context, callback ->
            CRouter.with(context)
                .url("/login.html")
                .startForResult { requestCode, resultCode, data ->
                    if (resultCode == Activity.RESULT_OK) {
                        callback.invoke()
                    }
                }
        }
        .build()
)
```

2. 配置 Activity 路由注解

```
@Router("/target\\.html", needLogin = true)
class TargetActivity : BaseActivity() {
}
```

3. 尽情使用吧

```
// 不关心结果
CRouter.with(this)
    // url 可以传入完整链接，也可以只传 path: '/target.html'
    .url("https://host.com/target.html")
    .start()

// 关心结果
CRouter.with(this)
    .url("/target.html")
    .startForResult { requestCode, resultCode, data ->
        if (resultCode == Activity.RESULT_OK && data != null) {
            val value = data.extras?.getString("key")
            alert("跳转取值", value)
        }
    }
```

**更多用法请参考 [sample](https://github.com/wangchenyan/crouter/tree/master/sample) 代码**

## Interceptor

在 CRouter 中使用拦截器和 OkHttp 的用法一致，这里以添加一个 H5 页面拦截器为例，当识别到网页链接，用内置浏览器打开

```
class H5Interceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.uri()
        val response = chain.proceed(request)
        if (response.intent() == null && uri != null && (uri.scheme == "http" || uri.scheme == "https")) {
            val context = request.context()
            val intent = Intent(context, BrowserActivity::class.java)
            intent.putExtra(Extra.URL, uri.toString())
            return Response.Builder()
                    .context(context)
                    .request(request)
                    .intent(intent)
                    .build()
        }
        return response
    }
}
```

在初始化时添加拦截器

```
CRouter.setRouterClient(
    RouterClient.Builder()
        .addInterceptor(H5Interceptor())
        .build()
)
```

## ProGuard

无

## About me

掘金：https://juejin.im/user/2313028193754168

微博：https://weibo.com/wangchenyan1993

## License

    Copyright 2019 wangchenyan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
