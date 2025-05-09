package com.dwarfeng.datamark.handler;

import com.dwarfeng.datamark.exception.*;
import com.dwarfeng.datamark.struct.DatamarkConfig;
import com.dwarfeng.datamark.util.DatamarkValueUtil;
import com.dwarfeng.subgrade.sdk.exception.HandlerExceptionHelper;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
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
            throw HandlerExceptionHelper.parse(e);
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
            throw HandlerExceptionHelper.parse(e);
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
            throw HandlerExceptionHelper.parse(e);
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
            throw HandlerExceptionHelper.parse(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isCached() {
        return Objects.nonNull(cachedDatamarkValue);
    }

    // 为了代码的可阅读性，此处不做简化。
    @SuppressWarnings("ConstantValue")
    private void readAndUpdateCache() throws HandlerException {
        LOGGER.debug("刷新并更新缓存...");
        String tempDatamark;
        LOGGER.debug("读取资源中的内容...");
        Resource resource = ctx.getResource(datamarkConfig.getResourceUrl());
        try (
                InputStream in = resource.getInputStream();
                Scanner scanner = new Scanner(in, datamarkConfig.getResourceCharset())
        ) {
            if (!scanner.hasNextLine()) {
                LOGGER.debug("资源中没有下一行内容, 将 tempDatamark 设置为空字符串...");
                tempDatamark = StringUtils.EMPTY;
            } else {
                LOGGER.debug("资源中有下一行内容, 将 tempDatamark 设置为下一行的内容...");
                tempDatamark = scanner.nextLine();
            }
        } catch (Exception e) {
            LOGGER.warn("刷新数据标记值时发生异常, 将清除缓存并抛出异常, 异常信息如下: ", e);
            cachedDatamarkValue = null;
            LOGGER.debug("最新缓存内容为: {}", cachedDatamarkValue);
            throw new ResourceReadFailedException(e, datamarkConfig.getResourceUrl());
        }
        LOGGER.debug("校验 tempDatamark 内容...");
        if (!DatamarkValueUtil.isDatamarkValueValid(tempDatamark)) {
            LOGGER.warn("数据标记值不合法, 将清除缓存并抛出异常");
            cachedDatamarkValue = null;
            LOGGER.debug("最新缓存内容为: {}", cachedDatamarkValue);
            throw new IllegalDatamarkValueException(tempDatamark);
        }
        LOGGER.debug("更新缓存内容为 tempDatamark...");
        cachedDatamarkValue = tempDatamark;
        LOGGER.debug("最新缓存内容为: {}", cachedDatamarkValue);
    }

    private void writeAndUpdateCache(String datamark) throws HandlerException {
        LOGGER.debug("写入并更新缓存...");
        LOGGER.debug("确认服务允许更新...");
        if (!datamarkConfig.isUpdateAllowed()) {
            LOGGER.debug("最新缓存内容为: {}", cachedDatamarkValue);
            throw new UpdateNotAllowedException();
        }
        LOGGER.debug("校验 datamark 内容...");
        if (!DatamarkValueUtil.isDatamarkValueValid(datamark)) {
            LOGGER.warn("数据标记值不合法, 将抛出异常");
            LOGGER.debug("最新缓存内容为: {}", cachedDatamarkValue);
            throw new IllegalDatamarkValueException(datamark);
        }
        LOGGER.debug("验证资源是否可写...");
        Resource resource = ctx.getResource(datamarkConfig.getResourceUrl());
        if (!(resource instanceof WritableResource)) {
            LOGGER.warn("资源不可写, 将抛出异常");
            LOGGER.debug("最新缓存内容为: {}", cachedDatamarkValue);
            throw new ResourceNotWritableException(datamarkConfig.getResourceUrl());
        }
        LOGGER.debug("向资源中写入内容...");
        try (
                OutputStream out = ((WritableResource) resource).getOutputStream();
                PrintStream ps = new PrintStream(out, false, datamarkConfig.getResourceCharset())
        ) {
            ps.println(datamark);
        } catch (Exception e) {
            LOGGER.warn("写入数据标记值时发生异常, 将抛出异常, 异常信息如下: ", e);
            LOGGER.debug("最新缓存内容为: {}", cachedDatamarkValue);
            throw new ResourceWriteFailedException(e, datamarkConfig.getResourceUrl());
        }
        LOGGER.debug("更新缓存内容为 datamark...");
        cachedDatamarkValue = datamark;
        LOGGER.debug("最新缓存内容为: {}", cachedDatamarkValue);
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
