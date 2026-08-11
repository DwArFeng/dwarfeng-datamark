package com.dwarfeng.datamark.impl.service;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;
import com.dwarfeng.datamark.stack.handler.DatamarkQosHandler;
import com.dwarfeng.datamark.stack.service.DatamarkQosService;
import com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatamarkQosServiceImpl implements DatamarkQosService {

    private final DatamarkQosHandler datamarkQosHandler;
    private final ServiceExceptionMapper sem;

    public DatamarkQosServiceImpl(DatamarkQosHandler datamarkQosHandler, ServiceExceptionMapper sem) {
        this.datamarkQosHandler = datamarkQosHandler;
        this.sem = sem;
    }

    @Override
    public List<String> listHandlerNames() throws ServiceException {
        try {
            return datamarkQosHandler.listHandlerNames();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse(
                    CoreMessages.message(CoreMessageKey.SERVICE_LIST_HANDLER_NAMES_FAILED), LogLevel.WARN, e, sem
            );
        }
    }

    @Override
    public boolean updateAllowed(@Nullable String handlerName) throws ServiceException {
        try {
            return datamarkQosHandler.updateAllowed(handlerName);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse(
                    CoreMessages.message(CoreMessageKey.SERVICE_UPDATE_ALLOWED_FAILED), LogLevel.WARN, e, sem
            );
        }
    }

    @Override
    public String get(@Nullable String handlerName) throws ServiceException {
        try {
            return datamarkQosHandler.get(handlerName);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse(
                    CoreMessages.message(CoreMessageKey.SERVICE_GET_FAILED), LogLevel.WARN, e, sem
            );
        }
    }

    @Override
    public String refresh(@Nullable String handlerName) throws ServiceException {
        try {
            return datamarkQosHandler.refresh(handlerName);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse(
                    CoreMessages.message(CoreMessageKey.SERVICE_REFRESH_FAILED), LogLevel.WARN, e, sem
            );
        }
    }

    @Override
    public String update(@Nullable String handlerName, String datamarkValue) throws ServiceException {
        try {
            return datamarkQosHandler.update(handlerName, datamarkValue);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse(
                    CoreMessages.message(CoreMessageKey.SERVICE_UPDATE_FAILED), LogLevel.WARN, e, sem
            );
        }
    }
}
