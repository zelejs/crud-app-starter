package com.jfeat.module.autoRender.util;

import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ParameterUtil {

    public static  <T> T parameterReplace(T old,T parameter){
        Class oldClass = old.getClass();
        Class parameterClass = parameter.getClass();
        Field[]  oldClassDeclaredFields= oldClass.getDeclaredFields();
        Field[] parameterClassDeclaredFields = parameterClass.getDeclaredFields();

        if (oldClassDeclaredFields.length!=parameterClassDeclaredFields.length){
            return null;
        }
        for (int i=0;i<oldClassDeclaredFields.length;i++){
            Field oldClassDeclaredField = oldClassDeclaredFields[i];
            for (int j=0;j<parameterClassDeclaredFields.length;j++){
                Field parameterClassDeclaredField = parameterClassDeclaredFields[j];
                if (oldClassDeclaredField.getName().equals(parameterClassDeclaredField.getName())){

                    try {
                        PropertyDescriptor oldPd = new PropertyDescriptor(oldClassDeclaredField.getName(), oldClass);
                        Method oldGetMethod = oldPd.getReadMethod();//获得get方法

                        PropertyDescriptor paramPd = new PropertyDescriptor(parameterClassDeclaredField.getName(), parameterClass);
                        Method paramGetMethod = oldPd.getReadMethod();//获得get方法

                        Object paramFieldValue = ReflectionUtils.invokeMethod(paramGetMethod,parameter);

                        if (paramFieldValue!=null){
                            oldClassDeclaredField.setAccessible(true);
                            oldClassDeclaredField.set(old,paramFieldValue);
                        }

                        Object fieldValue = ReflectionUtils.invokeMethod(oldGetMethod,old);
                        System.out.println(fieldValue);
                        if(fieldValue == null){
                            continue;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }


        }
        return old;
    }


}
