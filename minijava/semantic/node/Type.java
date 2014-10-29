package minijava.semantic.node;

import java.util.HashMap;
import java.util.Map;

/**
 * User: kowa
 * Date: 10/28/14
 */
public class Type {
    private String id;
    private boolean primitive;

    private static final Map<String, Type> TYPES = new HashMap<String, Type>();

    private Type(String _id, Boolean _primitive) {
        id = _id;
        primitive = _primitive;
    }

    public static Type getType(String id) {
        if (TYPES.containsKey(id)) {
            return TYPES.get(id);
        } else {
            Type type = new Type(id, id.equals("int") || id.equals("boolean"));
            TYPES.put(id, type);
            return type;
        }
    }

    public String getName() {
        return id;
    }

    public Boolean isPrimitive() {
        return primitive;
    }
}
