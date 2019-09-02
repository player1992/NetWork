## OkHttp使用及分析

### 一、API使用

#### 1.Get请求

```java
public class GetApi {
    private static final String url = "http://10.1.133.96/integral-api/park_coupon/v1/benefit_set";
    public static void main(String [] args){
        getTest();
    }
    public static void getTest(){
        Request.Builder builder = new Request.Builder().url(url);
        builder.method("GET",null);//相当于 builder.get();
        //构建请求
        Request request = builder.build();
        //OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient();
        //请求的网络包装
        Call call = okHttpClient.newCall(request);
        //异步执行
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println(result);
            }
        });
    }
}
```

#### 2.Post请求

```java
public class PostApi {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MD = MediaType.parse("text/x-markdown; charset=utf-8");
    static String url = "http://10.1.133.96/leo/post/getUser";
  
    public static void main(String [] args){
        postTest();
    }

    private static void postTest() {
        String postData = "json markdown";
        //明确POST的数据类型
        RequestBody requestBody = RequestBody.create(JSON, postData);
        //POST请求的builder
        FormBody formBody = new FormBody.Builder()
                .add("id", "leo")
                .build();
        //封装请求头 请求方法 URL 缓存策略
        Request request = new Request.Builder()
                //请求地址
                .url(url)
                //请求头
                .header("User-Agent","leoHttp")
                .header("CacheControl","no-cache")
                //请求方法
                .post(formBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Date:"+response.header("Date"));
                System.out.println("Content-Length:"+response.header("Content-Length"));
                System.out.println("Connection:"+response.header("Connection"));
                System.out.println("Content-Type:"+response.header("Content-Type"));
                System.out.println(response.body().string());
            }
        });
    }
}
```

#### 3.上传

```java
public class UploadApi {
    public static void main(String[] args) {
        upload();
    }
    private static void upload() {
        File file = new File("/Users/xxx/Desktop","flist.md");
        Request request = new Request.Builder().url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MediaType.parse("text/x-markdown;charset=utf-8"), file))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }
}
```

#### 4.下载

```java
public class DownloadApi {

    public static void main(String[] args) {
        download();
    }

    private static void download() {
        String url = "http://localhost:8088/demo.gif";
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                File file = new File("/Users/xxx/Desktop", "download.gif");
                    FileOutputStream fileOutputStream;
                if (file != null){
                    fileOutputStream = new FileOutputStream(file);
                    byte[] bytes = new byte[2048];
                    int len =0;
                    while ((len = inputStream.read(bytes))!= -1){
                        fileOutputStream.write(bytes,0,len);
                    }
                    fileOutputStream.flush();
                }
            }
        });
    }
}
```

#### 5.认证处理

```java
public class Authorization {
		        //处理401未验证,
    public static void main(String [] args){
        OkHttpClient httpClient;
        httpClient = new OkHttpClient.Builder().authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                //如果不处理会一直重试
                System.out.println("authenticate ： " + response);
                System.out.println("challenges ： " + response.challenges());
                // 可以使用 response.priorResponse() 计算次数 或者直接返回null处理
                if (responseCount(response) > 3) {
                    return null;
                }
                //authenticate ： Response{protocol=http/1.1, code=401, message=Unauthorized, url=https://publicobject.com/secrets/hellosecret.txt}
                //challenges ：[Basic realm="OkHttp Secrets" charset="ISO-8859-1"]
                String basic = Credentials.basic("jesse", "password1");
                return response
                        .request()
                        .newBuilder()
                        .header("Authorization", basic)
                        .build();
            }
        }).build();
        Request request = new Request.Builder()
                .url("http://publicobject.com/secrets/hellosecret.txt")
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("----------");
                System.out.println(response.body().string());
            }
        });
    }

  	//计数方式决定是否取消请求
    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
```

#### 6.其他设置

##### 连接超时时间/读写超时时间

```java
OkHttpClient.Builder builder = new OkHttpClient.Builder();
builder .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .cache(new Cache(externalCache, cacheSize));
OkHttpClient okHttpClient = builder.build();
```

##### 取消请求

```java
final Call call = okHttpClient.newCall(request);
executorService.schedule(new Runnable() {
    @Override
    public void run() {
        call.cancel();//按照一定的规则取消请求
    }
}, 150, TimeUnit.MILLISECONDS);
```



### 二、流程分析

#### 1.关键类

##### > Request

​	网络请求的相关信息，通过`Request.Builder`构建，内部包含`url`,`method`,`header`,`body`相关请求信息。

##### > OkHttpClient

​	核心类，一般调用`okHttpClient.newCall(request);`方法来生成`Call`对象。

##### > Call

​	`OkHttpClient`将请求的`Request`进一步封装，实现类是`RealCall`。

##### > Response

​	请求结果，`body`、`code`、`message`等信息。	

#### 2.处理过程

##### > 概述流程

​	从入口来看，`Call.enqueue(Callback)`方法做了什么，`Call`的实现类是`RealCall`

```java
@Override 
public void enqueue(Callback responseCallback) {
  synchronized (this) {
    if (executed) throw new IllegalStateException("Already Executed");
    executed = true;
  }
  captureCallStackTrace();
  eventListener.callStart(this);
  //  client.dispatcher()的enqueue
  client.dispatcher().enqueue(new AsyncCall(responseCallback));
}
```

​	每个请求都只能执行一次，否则抛出异常，然后调用了`Dispatcher`的`enqueue`方法，`Dispatcher`先不谈，继续跟进看：

```java
synchronized void enqueue(AsyncCall call) {
  if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
    runningAsyncCalls.add(call);
    executorService().execute(call);
  } else {
    readyAsyncCalls.add(call);
  }
}
```

​	内部一个判断，如果满足什么条件就用线程池执行任务，否则添加到一个队列，直接看`AsyncCall`的实现：

```java
@Override protected void execute() {
    boolean signalledCallback = false;
    try {
      Response response = getResponseWithInterceptorChain();
			......
    } catch (IOException e) {
      ......
    } finally {
      //任务最终调用dispatcher()的finish
      client.dispatcher().finished(this);
    }
  }
}
```

​	获取到`Response`的是`getResponseWithInterceptorChain`方法,最后又调用了`Dispatcher`的`finish`方法

```java
Response getResponseWithInterceptorChain() throws IOException {
  List<Interceptor> interceptors = new ArrayList<>();
  interceptors.addAll(client.interceptors());
  interceptors.add(retryAndFollowUpInterceptor);
  interceptors.add(new BridgeInterceptor(client.cookieJar()));
  interceptors.add(new CacheInterceptor(client.internalCache()));
  interceptors.add(new ConnectInterceptor(client));
  if (!forWebSocket) {
    interceptors.addAll(client.networkInterceptors());
  }
  interceptors.add(new CallServerInterceptor(forWebSocket));
  Interceptor.Chain chain = new c(interceptors, null, null, null, 0,
      originalRequest, this, eventListener, client.connectTimeoutMillis(),
      client.readTimeoutMillis(), client.writeTimeoutMillis());
  return chain.proceed(originalRequest);
}
```

​	？？？？？？

​	添加了几个intercepor,new了一个Chain，就把Response返回了？！

​	看下`proceed`方法

```java
public Response proceed(Request request, StreamAllocation streamAllocation, HttpCodec httpCodec,
    RealConnection connection) throws IOException {
  if (index >= interceptors.size()) throw new AssertionError();
  calls++;
	......
  // 调用当前拦截器的intercept方法
  RealInterceptorChain next = new RealInterceptorChain(interceptors, streamAllocation, httpCodec,
      connection, index + 1, request, call, eventListener, connectTimeout, readTimeout,
      writeTimeout);
  Interceptor interceptor = interceptors.get(index);
  Response response = interceptor.intercept(next);
	.....
  return response;
}
```

​	主要任务是从`interceptors`取出当前的`Interceptor`并调用`intercept(next)`方法，同时实例化了一个`RealInterceptorChain`传给该方法作为参数。

​	取出来的第一个Interceptor是谁呢？就是刚才`getResponseWithInterceptorChain`方法中的若干`Interceptor`:

> `RetryAndFollowUpInterceptor` 负责重连和重定向
>
> `BridgeInterceptor` 应用程序到网络链接的友好封装，请求头响应头相关
>
> `CacheInterceptor` 缓存处理
>
> `ConnectInterceptor`打开网络连接，不返回Response
>
> `CallServerInterceptor` 执行真正任务

​	OkHttp就是通过责任链的调用方式，依次调用各个拦截器的intercept方法，整个网络流程的各部分任务就分摊到了各个拦截器中，并得到最终的结果返回。

​	各个拦截器内部实现暂且不谈。

##### > `Dispatcher`

​	之前谈到每个任务的执行都涉及到了`Dispatcher`

```java
try {
  client.dispatcher().executed(this);
	......
} catch (IOException e) {
	......
} finally {
  client.dispatcher().finished(this);
}
```

这个类是干什么的呢？

```java
/**
 * Policy on when async requests are executed.
 * <p>Each dispatcher uses an {@link ExecutorService} to run calls internally. If you supply your
 * own executor, it should be able to run {@linkplain #getMaxRequests the configured maximum} number
 * of calls concurrently.
 */
```

文档上来看，这是一个决定什么时候执行某个任务的调度器

主要参数如下：

```java
private int maxRequests = 64;//最大请求数
private int maxRequestsPerHost = 5;//一台主机的最大请求数
private @Nullable ExecutorService executorService;//线程池
//待执行的任务队列
private final Deque<AsyncCall> readyAsyncCalls = new ArrayDeque<>();
//正在执行的异步任务队列
private final Deque<AsyncCall> runningAsyncCalls = new ArrayDeque<>();
//正在执行的同步任务队列
private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();
```

然后我们回头看

```java
synchronized void enqueue(AsyncCall call) {
  //如果正在执行的任务数小于最大请求数量，并且满足每台主机的请求数限制就执行任务
  if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
    runningAsyncCalls.add(call);
    executorService().execute(call);
  } else {
    //否则添加到等待队列
    readyAsyncCalls.add(call);
  }
}
```

`finish`方法做了什么？这里主要看异步任务

```java
private <T> void finished(Deque<T> calls, T call, boolean promoteCalls) {
  int runningCallsCount;
  Runnable idleCallback;
  synchronized (this) {
    if (!calls.remove(call)) throw new AssertionError("Call wasn't in-flight!");
    if (promoteCalls) promoteCalls();
    runningCallsCount = runningCallsCount();
    idleCallback = this.idleCallback;
  }
  if (runningCallsCount == 0 && idleCallback != null) {
    idleCallback.run();
  }
}
```

异步任务会执行`promoteCalls`方法

```java
private void promoteCalls() {
  if (runningAsyncCalls.size() >= maxRequests) return; // Already running max capacity.
  if (readyAsyncCalls.isEmpty()) return; // No ready calls to promote.
	
  for (Iterator<AsyncCall> i = readyAsyncCalls.iterator(); i.hasNext(); ) {
    AsyncCall call = i.next();

    if (runningCallsForHost(call) < maxRequestsPerHost) {
      i.remove();
      runningAsyncCalls.add(call);
      executorService().execute(call);
    }
    if (runningAsyncCalls.size() >= maxRequests) return; // Reached max capacity.
  }
}
```

主要任务就是在待执行队列中取出下一个任务执行。

##### > 连接池复用机制

`ConnectionPool`

