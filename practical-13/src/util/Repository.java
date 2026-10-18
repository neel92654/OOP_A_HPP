package util;
import model.annotation.Id;import java.lang.reflect.Field;import java.util.ArrayList;import java.util.HashMap;
public class Repository<T> {
    private final HashMap<String,T> items=new HashMap<>();
    public T save(T entity){if(entity==null)throw new IllegalArgumentException("Entity cannot be null");items.put(readId(entity),entity);return entity;}
    @SafeVarargs public final void saveAll(T... values){for(T value:values)save(value);}
    public T findById(String id){return items.get(id);}
    public ArrayList<T> findAll(){return new ArrayList<>(items.values());}
    public boolean delete(String id){return items.remove(id)!=null;}
    private String readId(T entity){Class<?> type=entity.getClass();while(type!=null){for(Field field:type.getDeclaredFields()){if(field.isAnnotationPresent(Id.class)){try{field.setAccessible(true);Object value=field.get(entity);if(value==null)throw new IllegalArgumentException("@Id field cannot be null");return value.toString();}catch(ReflectiveOperationException e){throw new IllegalStateException("Cannot read @Id field",e);}}}type=type.getSuperclass();}throw new IllegalArgumentException("No field annotated with @Id on "+entity.getClass().getName());}
}
