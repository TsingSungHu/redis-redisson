package com.qs.redisson.controller;

import com.qs.redisson.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于注解的方式 加锁
 * 
 * @author huqingsong
 */
@Slf4j
@RestController
public class AnnotationLockController {

    /**
     * 模拟这个是商品库存
     */
    public static volatile Integer TOTAL = 10;

    @GetMapping("annotationLockDecreaseStock")
    @DistributedLock(value="goods", leaseTime=5)
    public String lockDecreaseStock() {
        if (TOTAL > 0) {
            TOTAL--;
        }
        log.info("===注解模式=== 减完库存后,当前库存===" + TOTAL);
        return "=================================";
    }
}
