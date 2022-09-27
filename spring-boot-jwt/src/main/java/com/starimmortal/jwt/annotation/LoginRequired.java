package com.starimmortal.jwt.annotation;

import java.lang.annotation.*;

/**
 * 登录权限
 *
 * @author william@StarImmortal
 * @date 2022/09/24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {

}
