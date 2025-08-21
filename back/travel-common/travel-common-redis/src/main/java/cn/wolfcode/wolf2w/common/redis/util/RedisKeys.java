package cn.wolfcode.wolf2w.common.redis.util;

import lombok.Getter;

@Getter
public enum RedisKeys {

    BRUSH("brush",1*60),
    VERIFY_CODE("verify_code", 3 * 60),

    STRATEGY_STATIS_HASH("strategy_statis_hash", -1L),

    STRATEGY_USER_FAVOR("strategy_user_favor", -1L),
    STRATEGY_USER_THUMBSUP("strategy_user_thumbsup", -1L);

    private String prefix;//key的前缀
    private long time;//过期时间

    private RedisKeys(String prefix, long time) {
        this.prefix = prefix;
        this.time = time;
    }

    /**
     * key的拼接方法
     * 参数：
     * 要拼接的内容可以是多段
     * eg.  user_login_info: 11
     * 返回值
     * 拼接后得到的key
     *
     * @return
     */
    public String join(String... values) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.prefix);
        for (String value : values) {
            stringBuffer.append(":").append(value);
        }
        return stringBuffer.toString();
    }
}
