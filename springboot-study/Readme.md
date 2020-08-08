# 背景

springboot1.5.9集成JavaMelody的时候， [https://github.com/javamelody/javamelody/blob/without-spring-boot-starter/javamelody-for-spring-boot/src/main/java/hello/JavaMelodyConfiguration.java#L110](https://github.com/javamelody/javamelody/blob/without-spring-boot-starter/javamelody-for-spring-boot/src/main/java/hello/JavaMelodyConfiguration.java#L110)  发现这种集成方式的配置采用的硬编码方式，不能根据配置文件进行配置。因此我就想将配置方式修改为从配置文件读取，我的第一想法是采用@Value注解，先把一个log参数读取进来，但是发现该参数一直不能生效。

第一步，将monitoringFilter以下的bean都注释掉，该配置就生效了。

第二步，逐步增加注释掉的bean，当去掉SpringDataSourceBeanPostProcessor 和 SpringRestTemplateBeanPostProcessor这两个bean的加载，log参数就加载进来了。

那是什么原因呢？

# 原因探析

### 1.表象的深入分析

1.经过查看源码发现，SpringDataSourceBeanPostProcessor和SpringRestTemplateBeanPostProcessor实现了BeanPostProcessor, PriorityOrdered这两个接口。

2.然后我就把这种实现方式抽离出来简化了数据结构和实现方便调试，代码在[这里]( https://github.com/zzyymaggie/springboot-demo/tree/master/springboot-study/dependency-injection-example/src/main/java/com/example/issue )。

经过调试发现 实现了BeanPostProcessor, PriorityOrdered这两个接口的bean影响@Value的bean的加载。

而且，还多打出来一行信息： 

```
020-08-08 20:50:32.412  INFO 2700 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'myConfiguration1' of type [com.example.issue.MyConfiguration1$$EnhancerBySpringCGLIB$$ed2d2a7f] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
```

 据此推断，可能是MyConfiguration1启动时机过早，导致实现@Value注解的AutowiredAnnotationBeanPostProcessor没来得及实例化及注册呢。 

### 2. BeanPostProcessor启动阶段对其依赖的Bean造成的影响 

BeanPostProcessor的启动阶段包括四个阶段

-  第一阶段applicationContext内置阶段 
- 第二阶段priorityOrdered阶段
- 第三阶段Ordered阶段
- 第四阶段nonOrdered阶段

第一阶段的代码在AbstractApplicationContext.refresh()的 prepareBeanFactory()方法里，这里就不展示代码了。

第二阶段到第四阶段实例化和注册过程如下：

```
// 启动 Spring 应用上下文
AbstractApplicationContext.refresh()
//注册BeanPostProcessors
-> AbstractApplicationContext.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory)
//调用委托执行post processors任务的工具类PostProcessorRegistrationDelegate去执行注册逻辑
-> PostProcessorRegistrationDelegate.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext)
```

然后分成三个阶段依次实例化并注册实现了PriorityOrdered的BeanPostProcessor、实现了Ordered的BeanPostProcessor、普通的BeanPostProcessor(没实现Ordered)，代码如下：

```java
	// Separate between BeanPostProcessors that implement PriorityOrdered,
	// Ordered, and the rest.
	List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
	List<BeanPostProcessor> internalPostProcessors = new ArrayList<BeanPostProcessor>();
	List<String> orderedPostProcessorNames = new ArrayList<String>();
	List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
	for (String ppName : postProcessorNames) {
		if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			priorityOrderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
			orderedPostProcessorNames.add(ppName);
		}
		else {
			nonOrderedPostProcessorNames.add(ppName);
		}
	}

	// First, register the BeanPostProcessors that implement PriorityOrdered.
	sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
	registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);
	
	// Next, register the BeanPostProcessors that implement Ordered.
	List<BeanPostProcessor> orderedPostProcessors = new ArrayList<BeanPostProcessor>();
	for (String ppName : orderedPostProcessorNames) {
		BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
		orderedPostProcessors.add(pp);
		if (pp instanceof MergedBeanDefinitionPostProcessor) {
			internalPostProcessors.add(pp);
		}
	}
	sortPostProcessors(orderedPostProcessors, beanFactory);
	registerBeanPostProcessors(beanFactory, orderedPostProcessors);
	
	// Now, register all regular BeanPostProcessors.
	List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
	for (String ppName : nonOrderedPostProcessorNames) {
		BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
		nonOrderedPostProcessors.add(pp);
		if (pp instanceof MergedBeanDefinitionPostProcessor) {
			internalPostProcessors.add(pp);
		}
	}
	registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);
	// Finally, re-register all internal BeanPostProcessors.
	sortPostProcessors(internalPostProcessors, beanFactory);
	registerBeanPostProcessors(beanFactory, internalPostProcessors);
	// Re-register post-processor for detecting inner beans as ApplicationListeners,
	// moving it to the end of the processor chain (for picking up proxies etc).
	beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
```

从上面调试与源代码分析，同一批的BeanPostProcesser全部实例化完成后，再全部注册。而在实例化的时候其依赖的Bean同样要先实例化。

因此导致一个结果就是，被PriorityOrderedBeanPostProcessor所依赖的Bean其初始化时无法享受到PriorityOrdered、Ordered、和nonOrdered的BeanPostProcessor的服务。而被OrderedBeanPostProcessor所依赖的Bean无法享受Ordered、和nonOrdered的BeanPostProcessor的服务。最后被nonOrderedBeanPostProcessor所依赖的Bean无法享受到nonOrderedBeanPostProcessor的服务。



这个场景里就是MyPriorityPost开始实例化时会导致MyConfiguration1实例化，而此时是无法享受到同是实现了PriorityOrdered的AutowiredAnnotationBeanPostProcessor服务，导致@Value参数注入失败。



# 解决方案

### 1.注入参数采用构造器参数注入

将参数封装到@ConfigurationProperties里，然后采用@Bean实例化，再通过构造器参数注入。

```java
@Configuration
public class MyConfiguration2 {

    @Bean
    public HelloProperties helloProperties() {
        return new HelloProperties();
    }

    @Bean
    public MyPost2 myPost2(HelloProperties helloProperties){
        System.out.println("MyPost2:" + helloProperties.isLog());
        return new MyPost2();
    }

    @Bean
    public MyPriorityPost2 myPriorityPost2(){
        return new MyPriorityPost2();
    }
}
```

但是如果只是一个参数，用这种方式有些太重了。所以有了第二种一劳永逸的办法。

### 2.将实现了 PriorityOrdered的BeanPostProcessor单独放到一个配置类

```java
@Configuration
public class MyConfiguration3Seperator {
    @Bean
    public MyPriorityPost3 myPriorityPost3(){
        return new MyPriorityPost3();
    }
}
```

参考文章：[https://blog.csdn.net/m0_37962779/article/details/78605478](https://blog.csdn.net/m0_37962779/article/details/78605478)

代码库：
[https://github.com/zzyymaggie/springboot-demo/tree/master/springboot-study](https://github.com/zzyymaggie/springboot-demo/tree/master/springboot-study)