1、MyBatis Plus注解 @InterceptorIgnore --2023-11-22
@InterceptorIgnore 注解是MyBatis Plus提供的注解之一，用于标识在特定情况下是否忽略某些拦截器的执行。 
通过在实体类或字段上添加 @InterceptorIgnore 注解，开发者可以灵活地控制拦截器的行为，以适应不同的业务需求。

2、Shiro之@RequiresPermissions注解原理详解 --2023-11-22
就是在Controller的方法里，加上@RequiresPermissions注解，并写上权限标识。那么，有该权限标识的用户，才能访问到请求。

3、@AkaliHot(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 50, duration = 5) 热点数据限制
这就代表了，如果某个skuCode在5秒之内有超过50个线程正在运行，那么就提为热点，并用热点数据直接返回。

4、@AkaliFallback(grade = FlowGradeEnum.FLOW_GRADE_THREAD, count = 100)
当这个方法的同时运行的线程超过100个时，触发降级，降级会自动调用原方法名+Fallback方法名(并且参数要一致)，
当降级触发后会直接返回fallback str，当线程数小于100时，框架也会自动摘除降级，还是输出hi,xxxx。

5、disruptor单机内存使用的mq消息
https://juejin.cn/post/6844904020973191181

6、google guava学习
https://wizardforcel.gitbooks.io/guava-tutorial/content/1.html