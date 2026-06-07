package com.bank.common;

import com.bank.entity.SysUser;

public class UserContext {

    private static final ThreadLocal<SysUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(SysUser user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static SysUser getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}
