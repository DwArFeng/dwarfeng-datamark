package com.dwarfeng.datamark.impl.handler;

import com.dwarfeng.datamark.sdk.util.DatamarkQosExceptionHelper;
import com.dwarfeng.datamark.stack.exception.AmbiguousDatamarkHandlerException;
import com.dwarfeng.datamark.stack.exception.DatamarkHandlerNotFoundException;
import com.dwarfeng.datamark.stack.exception.NoDatamarkHandlerPresentException;
import com.dwarfeng.datamark.stack.handler.DatamarkHandler;
import com.dwarfeng.datamark.stack.handler.DatamarkQosHandler;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DatamarkQosHandlerImpl implements DatamarkQosHandler {

    private final Map<String, DatamarkHandler> datamarkHandlerMap;

    public DatamarkQosHandlerImpl(Map<String, DatamarkHandler> datamarkHandlerMap) {
        this.datamarkHandlerMap = Optional.ofNullable(datamarkHandlerMap).orElse(Collections.emptyMap());
    }

    @Override
    public List<String> listHandlerNames() throws HandlerException {
        try {
            return datamarkHandlerMap.keySet().stream().sorted().toList();
        } catch (Exception e) {
            throw DatamarkQosExceptionHelper.parse(e);
        }
    }

    @Override
    public boolean updateAllowed(@Nullable String handlerName) throws HandlerException {
        try {
            return determineHandler(handlerName).updateAllowed();
        } catch (Exception e) {
            throw DatamarkQosExceptionHelper.parse(e);
        }
    }

    @Override
    public String get(@Nullable String handlerName) throws HandlerException {
        try {
            return determineHandler(handlerName).get();
        } catch (Exception e) {
            throw DatamarkQosExceptionHelper.parse(e);
        }
    }

    @Override
    public String refresh(@Nullable String handlerName) throws HandlerException {
        try {
            return determineHandler(handlerName).refresh();
        } catch (Exception e) {
            throw DatamarkQosExceptionHelper.parse(e);
        }
    }

    @Override
    public String update(@Nullable String handlerName, String datamarkValue) throws HandlerException {
        try {
            return determineHandler(handlerName).update(datamarkValue);
        } catch (Exception e) {
            throw DatamarkQosExceptionHelper.parse(e);
        }
    }

    private DatamarkHandler determineHandler(@Nullable String handlerName) throws Exception {
        if (datamarkHandlerMap.isEmpty()) {
            throw new NoDatamarkHandlerPresentException();
        }
        if (handlerName == null) {
            if (datamarkHandlerMap.size() == 1) {
                return datamarkHandlerMap.values().iterator().next();
            } else {
                throw new AmbiguousDatamarkHandlerException();
            }
        } else {
            if (!datamarkHandlerMap.containsKey(handlerName)) {
                throw new DatamarkHandlerNotFoundException(handlerName);
            }
            return datamarkHandlerMap.get(handlerName);
        }
    }

    @Override
    public String toString() {
        return "DatamarkQosHandlerImpl{" +
                "datamarkHandlerMap=" + datamarkHandlerMap +
                '}';
    }
}
