package com.dwarfeng.datamark.impl.handler;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;
import com.dwarfeng.datamark.sdk.util.DatamarkExceptionHelper;
import com.dwarfeng.datamark.sdk.util.DatamarkValueUtil;
import com.dwarfeng.datamark.stack.exception.*;
import com.dwarfeng.datamark.stack.handler.DatamarkHandler;
import com.dwarfeng.datamark.stack.struct.DatamarkConfig;
import com.dwarfeng.subgrade.aop.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class DatamarkHandlerImpl implements DatamarkHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatamarkHandlerImpl.class);

    private final ApplicationContext ctx;

    private final DatamarkConfig datamarkConfig;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private String cachedDatamarkValue;

    public DatamarkHandlerImpl(ApplicationContext ctx, DatamarkConfig datamarkConfig) {
        this.ctx = ctx;
        this.datamarkConfig = datamarkConfig;
    }

    @BehaviorAnalyse
    @Override
    public boolean updateAllowed() {
        return datamarkConfig.isUpdateAllowed();
    }

    @BehaviorAnalyse
    @Override
    public String get() throws HandlerException {
        lock.readLock().lock();
        try {
            if (isCached()) {
                return cachedDatamarkValue;
            }
        } catch (Exception e) {
            throw DatamarkExceptionHelper.parse(e);
        } finally {
            lock.readLock().unlock();
        }
        lock.writeLock().lock();
        try {
            if (isCached()) {
                return cachedDatamarkValue;
            }
            readAndUpdateCache();
            return cachedDatamarkValue;
        } catch (Exception e) {
            throw DatamarkExceptionHelper.parse(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @BehaviorAnalyse
    @Override
    public String refresh() throws HandlerException {
        lock.writeLock().lock();
        try {
            readAndUpdateCache();
            return cachedDatamarkValue;
        } catch (Exception e) {
            throw DatamarkExceptionHelper.parse(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @BehaviorAnalyse
    @Override
    public String update(String datamarkValue) throws HandlerException {
        lock.writeLock().lock();
        try {
            writeAndUpdateCache(datamarkValue);
            return cachedDatamarkValue;
        } catch (Exception e) {
            throw DatamarkExceptionHelper.parse(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isCached() {
        return Objects.nonNull(cachedDatamarkValue);
    }

    // 为了代码的可阅读性，此处不做简化。
    @SuppressWarnings({"ConstantValue"})
    private void readAndUpdateCache() throws HandlerException {
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_REFRESH_CACHE_START));
        String tempDatamark;
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_RESOURCE_READ));
        Resource resource = ctx.getResource(datamarkConfig.getResourceUrl());
        try (
                InputStream in = resource.getInputStream();
                Scanner scanner = new Scanner(in, datamarkConfig.getResourceCharset())
        ) {
            if (!scanner.hasNextLine()) {
                LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_RESOURCE_EMPTY));
                tempDatamark = StringUtils.EMPTY;
            } else {
                LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_RESOURCE_NON_EMPTY));
                tempDatamark = scanner.nextLine();
            }
        } catch (Exception e) {
            LOGGER.warn(CoreMessages.message(CoreMessageKey.HANDLER_REFRESH_FAILED), e);
            cachedDatamarkValue = null;
            LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_CURRENT, cachedDatamarkValue));
            throw new ResourceReadFailedException(e, datamarkConfig.getResourceUrl());
        }
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_TEMP_DATAMARK_VALIDATE));
        if (!DatamarkValueUtil.isDatamarkValueValid(tempDatamark)) {
            LOGGER.warn(CoreMessages.message(CoreMessageKey.HANDLER_TEMP_DATAMARK_INVALID));
            cachedDatamarkValue = null;
            LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_CURRENT, cachedDatamarkValue));
            throw new IllegalDatamarkValueException(tempDatamark);
        }
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_UPDATE_FROM_TEMP));
        cachedDatamarkValue = tempDatamark;
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_CURRENT, cachedDatamarkValue));
    }

    private void writeAndUpdateCache(String datamark) throws HandlerException {
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_WRITE_CACHE_START));
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_UPDATE_ALLOWED_VALIDATE));
        if (!datamarkConfig.isUpdateAllowed()) {
            LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_CURRENT, cachedDatamarkValue));
            throw new UpdateNotAllowedException();
        }
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_DATAMARK_VALIDATE));
        if (!DatamarkValueUtil.isDatamarkValueValid(datamark)) {
            LOGGER.warn(CoreMessages.message(CoreMessageKey.HANDLER_DATAMARK_INVALID));
            LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_CURRENT, cachedDatamarkValue));
            throw new IllegalDatamarkValueException(datamark);
        }
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_RESOURCE_WRITABLE_VALIDATE));
        Resource resource = ctx.getResource(datamarkConfig.getResourceUrl());
        if (!(resource instanceof WritableResource)) {
            LOGGER.warn(CoreMessages.message(CoreMessageKey.HANDLER_RESOURCE_NOT_WRITABLE));
            LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_CURRENT, cachedDatamarkValue));
            throw new ResourceNotWritableException(datamarkConfig.getResourceUrl());
        }
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_RESOURCE_WRITE));
        try (
                OutputStream out = ((WritableResource) resource).getOutputStream();
                PrintStream ps = new PrintStream(out, false, datamarkConfig.getResourceCharset())
        ) {
            ps.println(datamark);
        } catch (Exception e) {
            LOGGER.warn(CoreMessages.message(CoreMessageKey.HANDLER_WRITE_FAILED), e);
            LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_CURRENT, cachedDatamarkValue));
            throw new ResourceWriteFailedException(e, datamarkConfig.getResourceUrl());
        }
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_UPDATE_FROM_DATAMARK));
        cachedDatamarkValue = datamark;
        LOGGER.debug(CoreMessages.message(CoreMessageKey.HANDLER_CACHE_CURRENT, cachedDatamarkValue));
    }

    @Override
    public String toString() {
        return "DatamarkHandlerImpl{" +
                "ctx=" + ctx +
                ", datamarkConfig=" + datamarkConfig +
                ", lock=" + lock +
                ", cachedDatamarkValue='" + cachedDatamarkValue + '\'' +
                '}';
    }
}
