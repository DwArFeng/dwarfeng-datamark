package com.dwarfeng.datamark.service.telqos;

import com.dwarfeng.datamark.service.DatamarkQosService;
import com.dwarfeng.springtelqos.node.configuration.TelqosCommand;
import com.dwarfeng.springtelqos.sdk.command.CliCommand;
import com.dwarfeng.springtelqos.sdk.util.CliCommandUtil;
import com.dwarfeng.springtelqos.stack.command.CommandDescriptor;
import com.dwarfeng.springtelqos.stack.command.CommandExecutor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    // region 指令选项

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

    private static final String COMMAND_OPTION_HANDLER_NAME = "hn";
    private static final String COMMAND_OPTION_DATAMARK_VALUE = "dv";

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
                        " [" + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_HANDLER_NAME) + " handler-name]",
                context.getRuntimeIdentity() + " " + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_GET) +
                        " [" + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_HANDLER_NAME) + " handler-name]",
                context.getRuntimeIdentity() + " " + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_REFRESH) +
                        " [" + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_HANDLER_NAME) + " handler-name]",
                context.getRuntimeIdentity() + " " + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_UPDATE) +
                        " [" + CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_HANDLER_NAME) + " handler-name] [" +
                        CliCommandUtil.concatOptionPrefix(COMMAND_OPTION_DATAMARK_VALUE) + " datamark-value]"
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
        list.add(Option.builder(COMMAND_OPTION_HANDLER_NAME).hasArg(true).type(String.class).desc("数据服务 ID").build());
        list.add(Option.builder(COMMAND_OPTION_DATAMARK_VALUE).hasArg(true).type(String.class).desc("数据标记值").build());
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
            // 为了代码的可扩展性，此处不做简化。
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
            String handlerName = handlerNames.get(i);
            context.sendMessage(String.format("  %3d: %s", i + 1, handlerName));
        }
    }

    private void handleUpdateAllowed(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        // 交互标记。
        boolean interactiveFlag = false;

        // 确定 handlerName。
        String handlerName = null;
        // 如果有 COMMAND_OPTION_HANDLER_NAME 选项，则直接获取 handlerName。
        if (cmd.hasOption(COMMAND_OPTION_HANDLER_NAME)) {
            handlerName = cmd.getOptionValue(COMMAND_OPTION_HANDLER_NAME);
        }
        // 如果 handlerName 为 null，则使用交互式输入获取。
        if (Objects.isNull(handlerName)) {
            handlerName = interactiveGetHandlerName(context);
            interactiveFlag = true;
        }

        // 调用服务，获取服务是否允许更新。
        boolean updateAllowed = datamarkQosService.updateAllowed(handlerName);

        // 信息输出。
        if (interactiveFlag) {
            context.sendMessage(StringUtils.EMPTY);
        }
        context.sendMessage("处理器名称: " + handlerName + ", 允许更新: " + updateAllowed);
    }

    private void handleGet(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        // 交互标记。
        boolean interactiveFlag = false;

        // 确定 handlerName。
        String handlerName = null;
        // 如果有 COMMAND_OPTION_HANDLER_NAME 选项，则直接获取 handlerName。
        if (cmd.hasOption(COMMAND_OPTION_HANDLER_NAME)) {
            handlerName = cmd.getOptionValue(COMMAND_OPTION_HANDLER_NAME);
        }
        // 如果 handlerName 为 null，则使用交互式输入获取。
        if (Objects.isNull(handlerName)) {
            handlerName = interactiveGetHandlerName(context);
            interactiveFlag = true;
        }

        // 调用服务，获取数据标记值。
        String datamark = datamarkQosService.get(handlerName);

        // 信息输出。
        if (interactiveFlag) {
            context.sendMessage(StringUtils.EMPTY);
        }
        context.sendMessage("处理器名称: " + handlerName + ", 数据标记值: " + datamark);
    }

    private void handleRefresh(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        // 交互标记。
        boolean interactiveFlag = false;

        // 确定 handlerName。
        String handlerName = null;
        // 如果有 COMMAND_OPTION_HANDLER_NAME 选项，则直接获取 handlerName。
        if (cmd.hasOption(COMMAND_OPTION_HANDLER_NAME)) {
            handlerName = cmd.getOptionValue(COMMAND_OPTION_HANDLER_NAME);
        }
        // 如果 handlerName 为 null，则使用交互式输入获取。
        if (Objects.isNull(handlerName)) {
            handlerName = interactiveGetHandlerName(context);
            interactiveFlag = true;
        }

        // 调用服务，获取数据标记值。
        String datamark = datamarkQosService.refresh(handlerName);

        // 信息输出。
        if (interactiveFlag) {
            context.sendMessage(StringUtils.EMPTY);
        }
        context.sendMessage("刷新成功!");
        context.sendMessage("处理器名称: " + handlerName + ", 刷新后的数据标记值: " + datamark);
    }

    private void handleUpdate(CommandExecutor.Context context, CommandLine cmd) throws Exception {
        // 交互标记。
        boolean interactiveFlag = false;

        // 确定 handlerName。
        String handlerName = null;
        // 如果有 COMMAND_OPTION_HANDLER_NAME 选项，则直接获取 handlerName。
        if (cmd.hasOption(COMMAND_OPTION_HANDLER_NAME)) {
            handlerName = cmd.getOptionValue(COMMAND_OPTION_HANDLER_NAME);
        }
        // 如果 handlerName 为 null，则使用交互式输入获取。
        if (Objects.isNull(handlerName)) {
            handlerName = interactiveGetHandlerName(context);
            interactiveFlag = true;
        }

        // 确定 datamark。
        String datamark = null;
        // 如果有 COMMAND_OPTION_DATAMARK_VALUE 选项，则直接获取 datamark。
        if (cmd.hasOption(COMMAND_OPTION_DATAMARK_VALUE)) {
            datamark = StringUtils.trim((String) cmd.getParsedOptionValue(COMMAND_OPTION_DATAMARK_VALUE));
        }
        // 如果 datamark 为 null，则使用交互式输入获取。
        if (Objects.isNull(datamark)) {
            datamark = interactiveGetDatamarkValue(context);
            interactiveFlag = true;
        }

        // 调用服务，获取数据标记值。
        datamark = datamarkQosService.update(handlerName, datamark);

        // 信息输出。
        if (interactiveFlag) {
            context.sendMessage(StringUtils.EMPTY);
        }
        context.sendMessage("更新成功!");
        context.sendMessage("处理器名称: " + handlerName + ", 更新的数据标记值: " + datamark);
    }

    private String interactiveGetHandlerName(CommandExecutor.Context context) throws Exception {
        context.sendMessage("请输入数据标记处理器的名称:");
        return context.receiveMessage();
    }

    private String interactiveGetDatamarkValue(CommandExecutor.Context context) throws Exception {
        context.sendMessage("请输入新的数据标记值:");
        return context.receiveMessage();
    }
}
