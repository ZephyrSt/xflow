package top.zephyrs.xflow.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean 工具类
 *
 * @author Zephy
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
    /**
     * Bean方法名中属性名开始的下标
     */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /**
     * 匹配getter方法的正则表达式
     */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /**
     * 匹配setter方法的正则表达式
     */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean属性复制工具方法。
     *
     * @param dest 目标对象
     * @param src  源对象
     */
    public static void copyBeanProp(Object dest, Object src) {
        try {
            BeanUtils.copyProperties(dest, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换bean的类型，注意：这里只会转换同名属性, 且非公开属性会被忽略，注意属性类型转换
     * @param src 待转换的数据
     * @param clazz 目标类型
     * @return 转换后的数据
     * @param <T> 转换后的类型
     */
    public static <T> T convertBean(Object src, Class<T> clazz) {
        try {
            T dest = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(dest, src);
            return dest;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 转换bean的类型，注意：这里只会转换同名属性, 且非公开属性会被忽略，注意属性类型转换
     * @param srcList 待转换的数据
     * @param clazz 目标类型
     * @return 转换后的数据
     * @param <T> 转换后的类型
     */
    public static <T> List<T> convertBean(List<?> srcList, Class<T> clazz) {
        try {
            List<T> destList = new ArrayList<>();
            for (Object src : srcList) {
                T dest = clazz.getDeclaredConstructor().newInstance();
                copyBeanProp(dest, src);
                destList.add(dest);
            }
            return destList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取对象的setter方法。
     *
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static List<Method> getSetterMethods(Object obj) {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<Method>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods) {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1)) {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }

    /**
     * 获取对象的getter方法。
     *
     * @param obj 对象
     * @return 对象的getter方法列表
     */

    public static List<Method> getGetterMethods(Object obj) {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<Method>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods) {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0)) {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

}
