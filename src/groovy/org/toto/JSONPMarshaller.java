package org.toto;
import grails.converters.JSON;
import groovy.lang.GroovyObject;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException;
import org.codehaus.groovy.grails.web.converters.marshaller.ObjectMarshaller;
import org.codehaus.groovy.grails.web.json.JSONWriter;
import org.springframework.beans.BeanUtils;

public class JSONPMarshaller implements ObjectMarshaller<JSON> {

    public boolean supports(Object object) {
        return object instanceof JSONPWrapper;
    }

    public void marshalObject(Object o, JSON json) throws ConverterException {
        JSONWriter writer = json.getWriter();
        JSONPWrapper wrapper = (JSONPWrapper)o;
        try {
            writer.object();
            // deal with callback value
            writer.key("callback");
            writer.value(wrapper.getCallback());
            // do as usual
            for (PropertyDescriptor property : BeanUtils.getPropertyDescriptors(wrapper.getWrappedElement().getClass())) {
                String name = property.getName();
                Method readMethod = property.getReadMethod();
                if (readMethod != null && !(name.equals("metaClass"))) {
                    Object value = readMethod.invoke(wrapper.getWrappedElement(), (Object[]) null);
                    writer.key(name);
                    json.convertAnother(value);
                }
            }
            for (Field field : wrapper.getWrappedElement().getClass().getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (Modifier.isPublic(modifiers) && !(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers))) {
                    writer.key(field.getName());
                    json.convertAnother(field.get(wrapper.getWrappedElement()));
                }
            }
            writer.endObject();
        }
        catch (ConverterException ce) {
            throw ce;
        }
        catch (Exception e) {
            throw new ConverterException("Error converting Bean with class " + o.getClass().getName(), e);
        }
    }
}
