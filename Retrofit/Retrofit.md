### Retrofit



### 一、注解分类

#### 	1.请求方法注解

`DELETE`,`GET`,`POST`,`PUT`,`HEAD`,`PATCH`,`OPTIONS`,`HTTP`

HTTP特殊，可以替换其他七种。

#### 	2.标记注解

`FormUrlEncoded`、`Multipart`、`Streaming`

#### 	3.参数注解

`Path`、`Header`、`HeaderMap`、`Headers`、`Query`、`QueryMap`、`Body`、`Field`、`FieldMap`、`Part`、`PartMap`等

### 二、基本使用

#### 	1.GET、HEADERS、QUERY、QUERYMAP

​	定义API接口

```java
public interface LeoServiceForGet {
    @GET("get/getUser?id=leo")
    Call<Leo> getUserInfo();

    @GET("get/getUser")//请求入参可以由Query注解指定，参数传入即可
    @Headers({
            "User-Agent: Retrofit"
    })//多个Header添加
    Call<Leo> getUserInfoWithQuery(@Query("id") String id);

    @GET("get/getUser")//多个入参可以使用QueryMap，简洁
    //单个Header动态添加
    Call<Leo> getUserInfoWithQueryMap(@QueryMap Map<String,String> options,@Header("Location") String loacation);

    @GET("{apiPath}/getUser?id=leo")//{apiPath}的值要动态替换，由传入的值替换
    Call<Leo> getUserInfoWithPath(@Path("apiPath") String path);
}
```

​	创建Retrofit实例

```java
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
```

​	生成目标API

```java
LeoServiceForGet service = retrofit.create(LeoServiceForGet.class);
```

​	调用接口

```java
service.getUserInfo().enqueue(new Callback<Leo>() {
    @Override
    public void onResponse(Call<Leo> call, Response<Leo> response) {
        System.out.println(response.body());
    }
    @Override
    public void onFailure(Call<Leo> call, Throwable t) {
        System.out.println(t.getMessage());
    }
});
```

#### 	2.其他注解

```java
//POST
@FormUrlEncoded
@POST("post/getUser")
//Field必须和FormUrlEncoded一起使用，表名是一次表单提交
Call<Leo> getUserInfo(@Field("id") String id);
//POST
@POST("post/getUser")//Retrofit 会把ID转换为String
Call<Leo> getUserInfoWithBody(@Body Id id);
//上传
@Multipart
@POST("users/photo")
Call<String> uploadFile(@Part MultipartBody.Part file,@Part("description") RequestBody desc);
//下载
@Streaming
@GET
Call<ResponseBody> download();
```



### 三、工作流程分析

#### 1.默认流程

​	整个网络请求过程要搞明白的点有以下几个：

> 我们定义的API接口没有方法实现，Retrofit如何实现？
>
> 方法、参数的注解如何处理？
>
> 和OkHttp有什么关系？如何合作的？



先来看Retrofit的构建

```java
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
```



然后调用`create`方法就生成了我们的API接口，然后我们就能直接调用相关的具体方法了。

```java
LeoServiceForGet service = retrofit.create(LeoServiceForGet.class);
```

`LeoServiceForGet`是怎么生成的，肯定是有具体的接口实现才能调用方法的，所以先看的就是`create`方法：

```java
=======Retrofit=======
public <T> T create(final Class<T> service) {
  Utils.validateServiceInterface(service);
  if (validateEagerly) {
    eagerlyValidateMethods(service);
  }
  return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
      new InvocationHandler() {
        private final Platform platform = Platform.get();
        @Override public Object invoke(Object proxy, Method method, @Nullable Object[] args)
            throws Throwable {
          //Object的方法忽略
          if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
          }
          //default方法忽略
          if (platform.isDefaultMethod(method)) {
            return platform.invokeDefaultMethod(method, service, proxy, args);
          }
          //调用者自定义的类及方法
          ServiceMethod<Object, Object> serviceMethod =
              (ServiceMethod<Object, Object>) loadServiceMethod(method);
          OkHttpCall<Object> okHttpCall = new OkHttpCall<>(serviceMethod, args);
          return serviceMethod.adapt(okHttpCall);
        }
      });
}
```

`create`方法利用动态代理生成代理类，然后调用具体方法，我们定义的类是`LeoServiceForGet`，接口方法的返回值是`Call<T>`，方法最后生成`ServiceMethod`和`OkHttpCall`，然后调用`ServiceMethod`的`adapt`方法来返回`Call<T>`.

来看`serviceMethod.adapt(okHttpCall)`方法实现：

```java
=======ServiceMethod=======
T adapt(Call<R> call) {
  return callAdapter.adapt(call);
}
```

调用的是`callAdapter.adapt(call);`,`callAdapter`是什么？如何赋值？就得跟进`ServiceMethod`的构建过程了。

实例化`ServiceMethod`的方法是`loadServiceMethod`：

```java
=======ServiceMethod=======
ServiceMethod<?, ?> loadServiceMethod(Method method) {
  //缓存里面有就直接返回，注解的处理损耗性能
  ServiceMethod<?, ?> result = serviceMethodCache.get(method);
  if (result != null) return result;
  synchronized (serviceMethodCache) {
    result = serviceMethodCache.get(method);
    if (result == null) {
      //缓存取不到就实例化一个
      result = new ServiceMethod.Builder<>(this, method).build();
      serviceMethodCache.put(method, result);
    }
  }
  return result;
}
```

我们关注实例`ServiceMethod`的过程,也就是`build()`方法：

```java
=======ServiceMethod=======
	public ServiceMethod build() {
  		//生成callAdapter，用它生成了具体接口的返回值Call<T>
      callAdapter = createCallAdapter();
      responseType = callAdapter.responseType();
		 ......
      responseConverter = createResponseConverter();
		 //方法注解处理
      for (Annotation annotation : methodAnnotations) {
        parseMethodAnnotation(annotation);
      }
      ......
		 //参数注解获取及处理
      int parameterCount = parameterAnnotationsArray.length;
      parameterHandlers = new ParameterHandler<?>[parameterCount];
      for (int p = 0; p < parameterCount; p++) {
        Type parameterType = parameterTypes[p];
        Annotation[] parameterAnnotations = parameterAnnotationsArray[p];
        if (parameterAnnotations == null) {
          throw parameterError(p, "No Retrofit annotation found.");
        }
        parameterHandlers[p] = parseParameter(p, parameterType, parameterAnnotations);
      }
     ......//判断异常
      return new ServiceMethod<>(this);
    }
```

整个方法做的事情还真不少，不仅生成了我们想要的`callAdapter`,还把整个方法的注解和参数的注解都获取进行处理，这里关于注解的处理稍后看，先来看`createCallAdapter()`方法如何生成`callAdapter`:

```java
=======ServiceMethod=======
private CallAdapter<T, R> createCallAdapter() {
  Type returnType = method.getGenericReturnType();
	......
  Annotation[] annotations = method.getAnnotations();
  try {
    //调用retrofit
    return (CallAdapter<T, R>) retrofit.callAdapter(returnType, annotations);
  } catch (RuntimeException e) { // Wide exception range because factories are user code.
    throw methodError(e, "Unable to create call adapter for %s", returnType);
  }
}
```

再看Retrofit.callAdapter()的实现

```java
=======Retrofit=======
public CallAdapter<?, ?> callAdapter(Type returnType, Annotation[] annotations) {
  return nextCallAdapter(null, returnType, annotations);
}

public CallAdapter<?, ?> nextCallAdapter(@Nullable CallAdapter.Factory skipPast, Type returnType,
    Annotation[] annotations) {
  checkNotNull(returnType, "returnType == null");
  checkNotNull(annotations, "annotations == null");
	//对callAdapterFactories遍历，找到CallAdapter
  int start = callAdapterFactories.indexOf(skipPast) + 1;
  for (int i = start, count = callAdapterFactories.size(); i < count; i++) {
    CallAdapter<?, ?> adapter = callAdapterFactories.get(i).get(returnType, annotations, this);
    if (adapter != null) {
      return adapter;
    }
  }
	......//构建异常
  throw new IllegalArgumentException(builder.toString());
}
```

`callAdapterFactories`是从哪里来的？

流程如下：

->`Retrofit.Builder.build()`方法

->`platform.defaultCallAdapterFactory(callbackExecutor)`

->返回 `DefaultCallAdapterFactory.INSTANCE`

最终我们跟到`DefaultCallAdapterFactory`的`get`方法，这里返回我们想要的`CallAdapter`，并且看到`adapt`方法

```java
@Override 
public Call<Object> adapt(Call<Object> call) {
  return call;
}
```

并没有对参数做什么处理，直接返回了。

再回头看到Retrofit.create方法

```java
OkHttpCall<Object> okHttpCall = new OkHttpCall<>(serviceMethod, args);
return serviceMethod.adapt(okHttpCall);
```

结合接口定义：

```java
Call<Leo> getUserInfo();
```

也就是说这里方法的返回Call的具体实现就是`OkHttpCall`.看名字应该是和OKHTTP有关系。

到目前为止，我们大致了解了`Retrofit`生成API和相关方法返回值的过程。那么接下来就要看请求发起过程了：

```java
=======Demo=======
service.getUserInfo().enqueue(new Callback<Leo>() {
    @Override
    public void onResponse(Call<Leo> call, Response<Leo> response) {
        System.out.println(response.body());
    }
    @Override
    public void onFailure(Call<Leo> call, Throwable t) {
        System.out.println(t.getMessage());
    }
});
```

发起请求就是同步异步的方式，这里看异步的`enqueue`方法,`Call`是接口，根据上文，我们要看的是实现类`OkHttpCall`的具体实现：

```java
=======OkHttpCall=======
@Override 
public void enqueue(final Callback<T> callback) {
  //引用OKHTTP的call
  okhttp3.Call call;
  Throwable failure;
  synchronized (this) {
    if (executed) throw new IllegalStateException("Already executed.");
    executed = true;
    call = rawCall;
    failure = creationFailure;
    if (call == null && failure == null) {
      try {
        //okhttp3.Call实现
        call = rawCall = createRawCall();
      } catch (Throwable t) {
        throwIfFatal(t);
        failure = creationFailure = t;
      }
    }
  }
  if (failure != null) {
    callback.onFailure(this, failure);
    return;
  }

  call.enqueue(new okhttp3.Callback() {
    @Override public void onResponse(okhttp3.Call call, okhttp3.Response rawResponse) {
      Response<T> response;
      try {
        response = parseResponse(rawResponse);
      } catch (Throwable e) {
        callFailure(e);
        return;
      }
      try {
        callback.onResponse(OkHttpCall.this, response);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
    @Override public void onFailure(okhttp3.Call call, IOException e) {
      callFailure(e);
    }
    private void callFailure(Throwable e) {
      try {
        callback.onFailure(OkHttpCall.this, e);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
  });
}
```

过程就是内部使用`createRawCall()`方法准备`okhttp.Call`，然后剩下的过程就是交付给OKHTTP去操作了。

看下`createRawCall`方法

```java
=======OkHttpCall=======
private okhttp3.Call createRawCall() throws IOException {
  okhttp3.Call call = serviceMethod.toCall(args);
  return call;
}
```

再跟进`serviceMethod.toCall(args)`

```java
okhttp3.Call toCall(@Nullable Object... args) throws IOException {
  RequestBuilder requestBuilder = new RequestBuilder(httpMethod, baseUrl, relativeUrl, headers,
      contentType, hasBody, isFormEncoded, isMultipart);
 ......
  return callFactory.newCall(requestBuilder.build());
}
```

最后的`callFactory.newCall`方法似曾相识，`callFactory`是谁？其实它在`Retrofit`的`build`过程中已经实例化了。

```java
if (callFactory == null) {
  callFactory = new OkHttpClient();
}
```

其实也就是`OkHttpClient`

最终还是构建OKHTTP来发起网络请求了。

现在，我们算是搞清楚了这个流程的大概：

`retrofit.create()`动态代理生成我们的接口实现，`loadServiceMethod`内部处理方法和参数相关注解，并且为我们的接口方法的返回值`Call`具体到子实现类`OkHttpCall`，至此准备好了网络请求的相关参数，最终调用`enqueue`等方法的时候，内部实际构建了`OKHTTP`的`Call`对象来管理网络请求。

搞明白了`Retrofit`生成接口方法的过程和OKHTTP两者的关系，还剩下一个注解的处理细节没看。但是我们已经知道相关的处理是在`ServiceMethod`的`build`方法:

跟到方法先看下方法的注解处理：

```java
for (Annotation annotation : methodAnnotations) {
  parseMethodAnnotation(annotation);
}
```

```java
private void parseMethodAnnotation(Annotation annotation) {
  if (annotation instanceof DELETE) {
    parseHttpMethodAndPath("DELETE", ((DELETE) annotation).value(), false);
  } else if (annotation instanceof GET) {
    parseHttpMethodAndPath("GET", ((GET) annotation).value(), false);
  } else if (annotation instanceof POST) {
    parseHttpMethodAndPath("POST", ((POST) annotation).value(), true);
  } 
  ......省略部分代码
}
```

方法根据不同的注解类型来处理，都调用的是`parseHttpMethodAndPath`方法，再跟进，

```java
private void parseHttpMethodAndPath(String httpMethod, String value, boolean hasBody) {
  if (this.httpMethod != null) {
    throw methodError("Only one HTTP method is allowed. Found: %s and %s.",
        this.httpMethod, httpMethod);
  }
  this.httpMethod = httpMethod;
  this.hasBody = hasBody;
  if (value.isEmpty()) {
    return;
  }
  // 相对地址
  int question = value.indexOf('?');
  if (question != -1 && question < value.length() - 1) {
    // 在相对地址中不能含有参数的请求，也就是不能有?
    String queryParams = value.substring(question + 1);
    Matcher queryParamMatcher = PARAM_URL_REGEX.matcher(queryParams);
    if (queryParamMatcher.find()) {
      throw methodError("URL query string \"%s\" must not have replace block. "
          + "For dynamic query parameters use @Query.", queryParams);
    }
  }
  this.relativeUrl = value;
  this.relativeUrlParamNames = parsePathParameters(value);
}
```

方法的处理比较明确，在注解中声明的URL不能含有?及相关参数，如果有参数要使用Query注解。

看完方法的注解再来看参数的注解处理：

```java
int parameterCount = parameterAnnotationsArray.length;
parameterHandlers = new ParameterHandler<?>[parameterCount];
for (int p = 0; p < parameterCount; p++) {
  Type parameterType = parameterTypes[p];
  Annotation[] parameterAnnotations = parameterAnnotationsArray[p];
  parameterHandlers[p] = parseParameter(p, parameterType, parameterAnnotations);
}
```

根据方法参数的长度声明一个`ParameterHandler`数组，然后调用`parseParameter`方法为这个数组赋值：

```java
private ParameterHandler<?> parseParameter(
    int p, Type parameterType, Annotation[] annotations) {
  ParameterHandler<?> result = null;
  for (Annotation annotation : annotations) {
    ParameterHandler<?> annotationAction = parseParameterAnnotation(
        p, parameterType, annotations, annotation);
    if (annotationAction == null) {
      continue;
    }
    result = annotationAction;
  }
  return result;
}
```

最后看`parseParameterAnnotation`方法，此方法很长有几百行，主要内容就是根据不同的注解类型进行不同的判断，返回一个`ParameterHandler`的子类对象，我们可以截取看下`Path`注解的处理：

```java
 else if (annotation instanceof Path) {
  if (gotQuery) {
    throw parameterError(p, "A @Path parameter must not come after a @Query.");
  }
  if (gotUrl) {
    throw parameterError(p, "@Path parameters may not be used with @Url.");
  }
  if (relativeUrl == null) {
    throw parameterError(p, "@Path can only be used with relative url on @%s", httpMethod);
  }
  gotPath = true;

  Path path = (Path) annotation;
  String name = path.value();
  validatePathName(p, name);
  Converter<?, String> converter = retrofit.stringConverter(type, annotations);
  return new ParameterHandler.Path<>(name, converter, path.encoded());
}
```

最后返回了`ParameterHandler.Path`，其他的几个注解也是类似的实现，那么最终的返回值哪里用了呢？

搜索一下用到的地方，就只有`toCall`方法中有引用，这个方法是我们发起请求时候调用的：

```java
ParameterHandler<Object>[] handlers = (ParameterHandler<Object>[]) parameterHandlers;
for (int p = 0; p < argumentCount; p++) {
  handlers[p].apply(requestBuilder, args[p]);
}
```

遍历所有的`ParameterHandler`然后调用`apply`方法，这个方法在`ParameterHandler`中是抽象的，具体实现在子类，我们看下`Path`的实现：

```java
static final class Path<T> extends ParameterHandler<T> {
  private final String name;
  private final Converter<T, String> valueConverter;
  private final boolean encoded;
  Path(String name, Converter<T, String> valueConverter, boolean encoded) {
    this.name = checkNotNull(name, "name == null");
    this.valueConverter = valueConverter;
    this.encoded = encoded;
  }
  @Override void apply(RequestBuilder builder, @Nullable T value) throws IOException {
    builder.addPathParam(name, valueConverter.convert(value), encoded);
  }
}
```

```java
void addPathParam(String name, String value, boolean encoded) {
  if (relativeUrl == null) {
    throw new AssertionError();
  }
  relativeUrl = relativeUrl.replace("{" + name + "}", canonicalizeForPath(value, encoded));
}
```

`Path`的实现就是替换花括号{}中的字符串,`Path`的用法就基本清楚了，其他几个注解可参考源码。



#### 2.Rx流程

留在复习的时候写o(╯□╰)o



