package util;

import model.annotation.MaxLength;
import model.annotation.Positive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationValidator {

    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();
        Class<?> current = obj.getClass();

        while (current != null) {
            Field[] fields = current.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                try {
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {
                        if (value == null || ((Number) value).doubleValue() <= 0) {
                            Positive annotation = field.getAnnotation(Positive.class);
                            errors.add(field.getName() + " " + annotation.message());
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        MaxLength annotation = field.getAnnotation(MaxLength.class);
                        if (value != null && value.toString().length() > annotation.value()) {
                            errors.add(field.getName() + " must have at most " + annotation.value() + " characters");
                        }
                    }
                } catch (Exception e) {
                    errors.add("Error validating " + field.getName());
                }
            }

            current = current.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
