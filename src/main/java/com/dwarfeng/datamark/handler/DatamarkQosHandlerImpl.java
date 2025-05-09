package com.dwarfeng.datamark.handler;

import com.dwarfeng.datamark.exception.AmbiguousDatamarkHandlerException;
import com.dwarfeng.datamark.exception.DatamarkHandlerNotFoundException;
import com.dwarfeng.datamark.exception.NoDatamarkHandlerPresentException;
import com.dwarfeng.subgrade.sdk.exception.HandlerExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DatamarkQosHandlerImpl implements DatamarkQosHandler {

    private final Map<String, DatamarkHandler> datamarkHandlerMap;

    public DatamarkQosHandlerImpl(Map<String, DatamarkHandler> datamarkHandlerMap) {
        this.datamarkHandlerMap = Optional.ofNullable(datamarkHandlerMap).orElse(Collections.emptyMap());
    }

    @Override
    public List<String> listHandlerNames() throws HandlerException {
        try {
            List<String> handlerNames = datamarkHandlerMap.keySet().stream().sorted().collect(Collectors.toList());
            return Collections.unmodifiableList(handlerNames);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public boolean updateAllowed(@Nullable String handlerName) throws HandlerException {
        try {
            return determineHandler(handlerName).updateAllowed();
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public String get(@Nullable String handlerName) throws HandlerException {
        try {
            return determineHandler(handlerName).get();
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public String refresh(@Nullable String handlerName) throws HandlerException {
        try {
            return determineHandler(handlerName).refresh();
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        }
    }

    @Override
    public String update(@Nullable String handlerName, String datamarkValue) throws HandlerException {
        try {
            return determineHandler(handlerName).update(datamarkValue);
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
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
