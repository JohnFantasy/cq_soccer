package com.laofaner.cq_soccer.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 */
public class ObjectConvertor {

    public static <K,V> List<V> toAnotherListObj(List<K> listobj, Class<V> anotherClassObj)
    {
        List<V> vs=new ArrayList<V>();
        for  (K k:   listobj ) {
            vs.add(toAnotherObj(k,anotherClassObj));
        }
        return vs;
    }
    public static  <K, V> V toAnotherObj(K oneObj, Class<V> anotherClassObj) {
        return toAnotherObj(oneObj, anotherClassObj, null);
    }

    private static <K, V> V toAnotherObj(K oneObj, Class<V> anotherClassObj, ReflectionUtils.FieldFilter filter) {
        if (oneObj == null) {
            return null;
        }
        V anotherObj = null;
        try {
            anotherObj = anotherClassObj.newInstance();
            BeanUtils.copyProperties(oneObj,anotherObj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return anotherObj;
    }

    /**
     * 把一个对象转成另一个对象并且把属性值为空的属性赋值成空
     * @param oneObj
     * @param anotherClassObj
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V toAnotherObjAndBlankToNull(K oneObj, Class<V> anotherClassObj)   {
        if (oneObj == null) {
            return null;
        }
        V anotherObj = null;
        try {
            anotherObj = anotherClassObj.newInstance();
            BeanUtils.copyProperties(oneObj,anotherObj);
            Field[] field = anotherObj.getClass().getDeclaredFields();
            for(int j=0 ; j<field.length ; j++){     //遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = anotherObj.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(anotherObj); // 调用getter方法获取属性值
                    if (value!=null && value.equals("")) {
                        m = anotherObj.getClass().getMethod("set"+name,String.class);
                        Object[] parameters = { null };
                        m.invoke(anotherObj, parameters);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return anotherObj;
    }

/*    private static <K, V> V toAnotherObj(K oneObj, Class<V> anotherClassObj, ReflectionUtils.FieldFilter filter) {
        if (oneObj == null) {
            return null;
        }
        V anotherObj = null;
        try {
            anotherObj = anotherClassObj.newInstance();
            Class<?> oneClassObj = oneObj.getClass();
            Field[] fields = getMatchedFields(oneClassObj, filter);
            for (Field field : fields) {
                String fieldName = field.getName();
                String append = "";
                if(fieldName.length() > 1 && fieldName.substring(1,2).matches("^[A-Z]$")) {
                    append = fieldName;
                }else {
                    append = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                }
                String getMethodName = "get" + append;
                String setMethodName = "set" + append;
                Method getMethod = oneClassObj.getMethod(getMethodName);
                getMethod.setAccessible(true);
                Object attrForOneObj = getMethod.invoke(oneObj);
                Method setMethod = anotherClassObj.getMethod(setMethodName, field.getType());
                setMethod.setAccessible(true);
                setMethod.invoke(anotherObj, attrForOneObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return anotherObj;
    }*/
    private static Field[] getMatchedFields(Class<?> classObj, ReflectionUtils.FieldFilter filter)
    {
        List<Field> matchedFields = new ArrayList<Field>();
        Field[] fields = classObj.getDeclaredFields();
        for (Field field : fields)
        {
            if (null == filter || filter.matches(field))
            {
                matchedFields.add(field);
            }
        }

        return matchedFields.toArray(new Field[] {});
    }
}
