package com.dwarfeng.datamark.api.integration.springtelqos;

import com.dwarfeng.datamark.stack.service.DatamarkQosService;
import com.dwarfeng.springtelqos.sdk.command.CliCommand;
import com.dwarfeng.springtelqos.sdk.configuration.TelqosCommand;
import com.dwarfeng.springtelqos.sdk.util.CliCommandUtil;
import com.dwarfeng.springtelqos.stack.command.CommandDescriptor;
import com.dwarfeng.springtelqos.stack.command.CommandExecutor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据标记指令。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@TelqosCommand
public class DatamarkCommand extends CliCommand {

    @SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
    private static final String IDENTITY = "datamark";

    // region 子指令选项

    private static final String COMMAND_OPTION_LIST_HANDLERS = "lh";
    private static final String COMMAND_OPTION_LIST_HANDLERS_LONG_OPT = "list-handlers";
    private static final String COMMAND_OPTION_UPDATE_ALLOWED = "ua";
    private static final String COMMAND_OPTION_UPDATE_ALLOWED_LONG_OPT = "update-allowed";
    private static final String COMMAND_OPTION_GET = "get";
    private static final String COMMAND_OPTION_REFRESH = "refresh";
    private static final String COMMAND_OPTION_UPDATE = "update";

    private static final String[] COMMAND_OPTION_ARRAY = new String[]{
            COMMAND_OPTION_LIST_HANDLERS,
            COMMAND_OPTION_UPDATE_ALLOWED,
            COMMAND_OPTION_GET,
            COMMAND_OPTION_REFRESH,
            COMMAND_OPTION_UPDATE
    };

    private static final String COMMAND_SUB_OPTION_HANDLER_NAME = "hn";
    private static final String COMMAND_SUB_OPTION_HANDLER_NAME_LONG_OPT = "handler-name";
    private static final String COMMAND_SUB_OPTION_DATAMARK_VALUE = "dv";
    private static final String COMMAND_SUB_OPTION_DATAMARK_VALUE_LONG_OPT = "datamark-value";

    // endregion

    private final DatamarkQosService datamarkQosService;

    public DatamarkCommand(DatamarkQosService datamarkQosService) {
        super(IDENTITY);
        this.datamarkQosService = datamarkQosService;
    }

    @Override
    protected DescriptionProvider provideDescriptionProvider() {
        return ctx -> "数据标记服务";
    }

    @Override
    protected CliSyntaxProvider provideCliSyntaxProvider() {
        return this::cliSyntaxProvider;
    }

    private String cliSyntaxProvider(CommandDescriptor.Context context) throws Exception {
        final String[] patterns = new String[]{
                context.getRuntimeIdentity() + " " + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_LIST_HANDLERS),
                context.getRuntimeIdentity() + " " + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_UPDATE_ALLOWED) +
                        " [" + CliCommandUtil.concatOptionPrefix(COMMAND_SUB_OPTION_HANDLER_NAME) + " handler-name]",
                context.getRuntimeIdentity() + " " + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_GET) +
                        " [" + CliCommandUtil.concatOptionPrefix(COMMAND_SUB_OPTION_HANDLER_NAME) + " handler-name]",
                context.getRuntimeIdentity() + " " + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_REFRESH) +
                        " [" + CliCommandUtil.concatOptionPrefix(COMMAND_SUB_OPTION_HANDLER_NAME) + " handler-name]",
                context.getRuntimeIdentity() + " " + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_UPDATE) +
                        " [" + CliCommandUtil.concatOptionPrefix(COMMAND_SUB_OPTION_HANDLER_NAME) + " handler-name] [" +
                        CliCommandUtil.concatOptionPrefix(COMMAND_SUB_OPTION_DATAMARK_VALUE) + " datamark-value]"
        };
        return CliCommandUtil.cliSyntax(patterns);
    }

    @Override
    protected List<Option> provideOptions() {
        List<Option> list = new ArrayList<>();
        list.add(
                Option.builder(COMMAND_OPTION_LIST_HANDLERS).longOpt(COMMAND_OPTION_LIST_HANDLERS_LONG_OPT)
                        .optionalArg(true).hasArg(false).desc("列出所有可用的数据标记处理器").build()
        );
        list.add(
                Option.builder(COMMAND_OPTION_UPDATE_ALLOWED).longOpt(COMMAND_OPTION_UPDATE_ALLOWED_LONG_OPT)
                        .optionalArg(true).hasArg(false).desc("返回处理器是否允许更新").build()
        );
        list.add(Option.builder(COMMAND_OPTION_GET).optionalArg(true).hasArg(false).desc("获取数据标记值").build());
        list.add(Option.builder(COMMAND_OPTION_REFRESH).optionalArg(true).hasArg(false).desc("刷新数据标记值").build());
        list.add(Option.builder(COMMAND_OPTION_UPDATE).optionalArg(true).hasArg(false).desc("更新数据标记值").build());
        list.add(
                Option.builder(COMMAND_SUB_OPTION_HANDLER_NAME).longOpt(COMMAND_SUB_OPTION_HANDLER_NAME_LONG_OPT)
                        .hasArg(true).type(String.class).desc("处理器名称").build()
        );
        list.add(
                Option.builder(COMMAND_SUB_OPTION_DATAMARK_VALUE).longOpt(COMMAND_SUB_OPTION_DATAMARK_VALUE_LONG_OPT)
                        .hasArg(true).type(String.class).desc("数据标记值").build()
        );
        return list;
    }

    @Override
    protected void executeWithCmd(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        Pair<String, Integer> pair = CliCommandUtil.analyseCommand(cmd, COMMAND_OPTION_ARRAY);
        if (pair.getRight() != 1) {
            context.sendMessage(CliCommandUtil.optionMismatchMessage(COMMAND_OPTION_ARRAY));
            context.sendMessage(context.getCommandManual(context.getRuntimeIdentity()));
            return;
        }
        switch (pair.getLeft()) {
            case COMMAND_OPTION_LIST_HANDLERS:
                handleListHandlers(context, cmd);
                break;
            case COMMAND_OPTION_UPDATE_ALLOWED:
                handleUpdateAllowed(context, cmd);
                break;
            case COMMAND_OPTION_GET:
                handleGet(context, cmd);
                break;
            case COMMAND_OPTION_REFRESH:
                handleRefresh(context, cmd);
                break;
            case COMMAND_OPTION_UPDATE:
                handleUpdate(context, cmd);
                break;
            default:
                throw new IllegalStateException("不应该执行到此处, 请联系开发人员");
        }
    }

    private void handleListHandlers(
            CommandExecutor.Context context,
            @SuppressWarnings("unused") CommandLine cmd
    ) throws Exception {
        // 调用服务，获取所有处理器的名称。
        List<String> handlerNames = datamarkQosService.listHandlerNames();

        // 输出结果。
        context.sendMessage("可用的处理器名称: ");
        if (handlerNames.isEmpty()) {
            context.sendMessage("  (Empty)");
            return;
        }
        for (int i = 0; i < handlerNames.size(); i++) {
            context.sendMessage(String.format("  %3d: %s", i + 1, handlerNames.get(i)));
        }
    }

    private void handleUpdateAllowed(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        // 获取处理器名称。
        String handlerName = parseHandlerName(context, cmd);

        // 调用服务，获取服务是否允许更新。
        boolean updateAllowed = datamarkQosService.updateAllowed(handlerName);

        // 输出结果。
        context.sendMessage(
                "处理器名称: " + normalizeHandlerNameForOutput(handlerName) + ", 允许更新: " + updateAllowed
        );
    }

    private void handleGet(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        // 获取处理器名称。
        String handlerName = parseHandlerName(context, cmd);

        // 调用服务，获取数据标记值。
        String datamark = datamarkQosService.get(handlerName);

        // 输出结果。
        context.sendMessage("处理器名称: " + normalizeHandlerNameForOutput(handlerName) + ", 数据标记值: " + datamark);
    }

    private void handleRefresh(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        // 获取处理器名称。
        String handlerName = parseHandlerName(context, cmd);

        // 调用服务，获取数据标记值。
        String datamark = datamarkQosService.refresh(handlerName);

        // 输出结果。
        context.sendMessage("刷新成功!");
        context.sendMessage(
                "处理器名称: " + normalizeHandlerNameForOutput(handlerName) + ", 刷新后的数据标记值: " + datamark
        );
    }

    private void handleUpdate(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        // 获取处理器名称和数据标记值。
        String handlerName = parseHandlerName(context, cmd);
        String datamark = parseDatamarkValue(context, cmd);

        // 调用服务，更新数据标记值。
        datamark = datamarkQosService.update(handlerName, datamark);

        // 输出结果。
        context.sendMessage("更新成功!");
        context.sendMessage(
                "处理器名称: " + normalizeHandlerNameForOutput(handlerName) + ", 更新的数据标记值: " + datamark
        );
    }

    private String parseHandlerName(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        // 如果有 COMMAND_SUB_OPTION_HANDLER_NAME 选项，则直接获取 handlerName。
        if (cmd.hasOption(COMMAND_SUB_OPTION_HANDLER_NAME)) {
            return StringUtils.trimToNull(cmd.getOptionValue(COMMAND_SUB_OPTION_HANDLER_NAME));
        }

        // 如果没有 COMMAND_SUB_OPTION_HANDLER_NAME 选项，则根据处理器数量决定行为。
        List<String> handlerNames = datamarkQosService.listHandlerNames();
        if (handlerNames.size() <= 1) {
            return null;
        }

        // 多处理器场景下，先输出处理器列表，再交互式输入。
        context.sendMessage("可用的处理器名称: ");
        for (int i = 0; i < handlerNames.size(); i++) {
            context.sendMessage(String.format("  %3d: %s", i + 1, handlerNames.get(i)));
        }
        context.sendMessage("请输入处理器名称:");
        return StringUtils.trimToNull(context.receiveMessage());
    }

    private String parseDatamarkValue(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        if (cmd.hasOption(COMMAND_SUB_OPTION_DATAMARK_VALUE)) {
            String datamarkValue = StringUtils.trim(
                    (String) cmd.getParsedOptionValue(COMMAND_SUB_OPTION_DATAMARK_VALUE)
            );
            if (StringUtils.isNotEmpty(datamarkValue)) {
                return datamarkValue;
            }
        }
        context.sendMessage("请输入新的数据标记值:");
        return context.receiveMessage();
    }

    private String normalizeHandlerNameForOutput(@Nullable String handlerName) {
        return StringUtils.defaultIfBlank(handlerName, "<default>");
    }
}
