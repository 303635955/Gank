package stbvideocall.jhonelee.xyt.com.aini_app.actions;

import android.animation.AnimatorSet;
import android.util.ArrayMap;

import java.util.IllegalFormatCodePointException;

/**
 * Created by Administrator on 2017/3/9.
 */

public class RxAction {

    private final String type;
    private final ArrayMap<String,Object> data;

    public RxAction(String type, ArrayMap<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public static Builder type(String type){
        return new Builder().with(type);
    }

    public String getType(){
        return type;
    }

    public ArrayMap<String,Object> getData(){
        return data;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String tag){
        return (T) data.get(tag);
    }

    //使用静态内部类的方式来构造对象
    public static class Builder{
        private String type;
        private ArrayMap<String,Object> data;

        Builder with(String type){
            if(type == null){
                throw new IllegalArgumentException("Type may not be null");
            }
            this.type = type;
            this.data = new ArrayMap<>();
            return this;
        }

        public Builder bundle(String key, Object value) {
            if (key == null) {
                throw new IllegalArgumentException("Key may not be null.");
            }

            if (value == null) {
                throw new IllegalArgumentException("Value may not be null.");
            }
            data.put(key, value);
            return this;
        }

        public RxAction build(){
            if(type == null || type.isEmpty()){
                throw new IllegalArgumentException("At least one key is required.");
            }
            return new RxAction(type,data);
        }


    }
}
