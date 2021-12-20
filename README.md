# Redisson实现分布式锁

使用SpringBoot2.1.5 + Maven3.5.4 + Redisson3.16.4 + lombok(插件)实现分布式锁。

## 1、加锁方式

支持 `自定义注解加锁` 和 ` 常规加锁` 两种模式

**自定义注解加锁**

```java
 @DistributedLock(value="goods", leaseTime=5)
  public String lockDecreaseStock(){
    //业务逻辑
  }
```

**常规加锁**

```java
 //1、加锁
 redissonLock.lock("redisson", 10);
 //2、业务逻辑
 //3、解锁
 redissonLock.unlock("redisson");
```

## 2、Redis部署方式

支持四种Redis部署方式

```
1、单机模式部署
2、集群模式部署
3、主从模式部署
4、哨兵模式部署
```

已经实现支持上面四种模式，你要采用哪种只需要修改配置文件`application.properties`，项目代码不需要做任何修改。

<br>


