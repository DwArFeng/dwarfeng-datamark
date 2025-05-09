package com.dwarfeng.datamark.service;

import com.dwarfeng.datamark.handler.DatamarkQosHandler;
import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
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
            throw ServiceExceptionHelper.logParse("列出所有处理器的名称时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public boolean updateAllowed(@Nullable String handlerName) throws ServiceException {
        try {
            return datamarkQosHandler.updateAllowed(handlerName);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("返回处理器是否允许更新时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public String get(@Nullable String handlerName) throws ServiceException {
        try {
            return datamarkQosHandler.get(handlerName);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("获取数据标记值时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public String refresh(@Nullable String handlerName) throws ServiceException {
        try {
            return datamarkQosHandler.refresh(handlerName);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("刷新数据标记值时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public String update(@Nullable String handlerName, String datamarkValue) throws ServiceException {
        try {
            return datamarkQosHandler.update(handlerName, datamarkValue);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("更新数据标记值时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
