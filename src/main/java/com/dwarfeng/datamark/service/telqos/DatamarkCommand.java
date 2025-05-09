package com.dwarfeng.datamark.service.telqos;

import com.dwarfeng.datamark.service.DatamarkQosService;
import com.dwarfeng.springtelqos.node.config.TelqosCommand;
import com.dwarfeng.springtelqos.sdk.command.CliCommand;
import com.dwarfeng.springtelqos.stack.command.Context;
import com.dwarfeng.springtelqos.stack.exception.TelqosException;
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
 * <p>
 * 用于提供 <code>spring-telqos</code> 框架的指令注册。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@TelqosCommand
public class DatamarkCommand extends CliCommand {

    private static final String COMMAND_OPTION_LIST_HANDLERS = "lh";
    private static final String COMMAND_OPTION_LIST_HANDLERS_LONG_OPT = "list-handlers";
    private static final String COMMAND_OPTION_UPDATE_ALLOWED = "ua";
    private static final String COMMAND_OPTION_UPDATE_ALLOWED_LONG_OPT = "update-allowed";
    private static final String COMMAND_OPTION_GET = "get";
    private static final String COMMAND_OPTION_REFRESH = "refresh";
    private static final String COMMAND_OPTION_UPDATE = "update";

    private static final String[] COMMAND_OPTION_ARRAY = {
            COMMAND_OPTION_LIST_HANDLERS,
            COMMAND_OPTION_UPDATE_ALLOWED,
            COMMAND_OPTION_GET,
            COMMAND_OPTION_REFRESH,
            COMMAND_OPTION_UPDATE
    };

    private static final String COMMAND_OPTION_HANDLER_NAME = "hn";
    private static final String COMMAND_OPTION_DATAMARK_VALUE = "dv";

    @SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
    private static final String IDENTITY = "datamark";
    private static final String DESCRIPTION = "数据标记服务";

    private static final String CMD_LINE_SYNTAX_LIST_SERVICES = IDENTITY + " " +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_LIST_HANDLERS);
    private static final String CMD_LINE_SYNTAX_UPDATE_ALLOWED = IDENTITY + " " +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_UPDATE_ALLOWED) + " [" +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_HANDLER_NAME) + " handler-name]";
    private static final String CMD_LINE_SYNTAX_GET = IDENTITY + " " +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_GET) + " [" +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_HANDLER_NAME) + " handler-name]";
    private static final String CMD_LINE_SYNTAX_REFRESH = IDENTITY + " " +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_REFRESH) + " [" +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_HANDLER_NAME) + " handler-name]";
    private static final String CMD_LINE_SYNTAX_UPDATE = IDENTITY + " " +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_UPDATE) + " [" +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_HANDLER_NAME) + " handler-name] [" +
            CommandUtil.concatOptionPrefix(COMMAND_OPTION_DATAMARK_VALUE) + " datamark-value]";

    private static final String[] CMD_LINE_ARRAY = new String[]{
            CMD_LINE_SYNTAX_LIST_SERVICES,
            CMD_LINE_SYNTAX_UPDATE_ALLOWED,
            CMD_LINE_SYNTAX_GET,
            CMD_LINE_SYNTAX_REFRESH,
            CMD_LINE_SYNTAX_UPDATE
    };

    private static final String CMD_LINE_SYNTAX = CommandUtil.syntax(CMD_LINE_ARRAY);

    private final DatamarkQosService datamarkQosService;

    public DatamarkCommand(DatamarkQosService datamarkQosService) {
        super(IDENTITY, DESCRIPTION, CMD_LINE_SYNTAX);
        this.datamarkQosService = datamarkQosService;
    }

    @Override
    protected List<Option> buildOptions() {
        List<Option> list = new ArrayList<>();
        list.add(
                Option.builder(COMMAND_OPTION_LIST_HANDLERS).longOpt(COMMAND_OPTION_LIST_HANDLERS_LONG_OPT)
                        .desc("列出所有可用的数据标记处理器").build()
        );
        list.add(
                Option.builder(COMMAND_OPTION_UPDATE_ALLOWED).longOpt(COMMAND_OPTION_UPDATE_ALLOWED_LONG_OPT)
                        .desc("返回处理器是否允许更新").build()
        );
        list.add(Option.builder(COMMAND_OPTION_GET).desc("获取数据标记值").build());
        list.add(Option.builder(COMMAND_OPTION_REFRESH).desc("刷新数据标记值").build());
        list.add(Option.builder(COMMAND_OPTION_UPDATE).desc("更新数据标记值").build());
        list.add(Option.builder(COMMAND_OPTION_HANDLER_NAME).desc("数据服务 ID").hasArg().type(String.class).build());
        list.add(Option.builder(COMMAND_OPTION_DATAMARK_VALUE).desc("数据标记值").hasArg().type(String.class).build());
        return list;
    }

    @Override
    protected void executeWithCmd(Context context, CommandLine commandLine) throws TelqosException {
        try {
            Pair<String, Integer> pair = CommandUtil.analyseCommand(commandLine, COMMAND_OPTION_ARRAY);
            if (pair.getRight() != 1) {
                context.sendMessage(CommandUtil.optionMismatchMessage(COMMAND_OPTION_ARRAY));
                context.sendMessage(super.cmdLineSyntax);
                return;
            }
            switch (pair.getLeft()) {
                case COMMAND_OPTION_LIST_HANDLERS:
                    handleListServices(context, commandLine);
                    break;
                case COMMAND_OPTION_UPDATE_ALLOWED:
                    handleUpdateAllowed(context, commandLine);
                    break;
                case COMMAND_OPTION_GET:
                    handleGet(context, commandLine);
                    break;
                case COMMAND_OPTION_REFRESH:
                    handleRefresh(context, commandLine);
                    break;
                case COMMAND_OPTION_UPDATE:
                    handleUpdate(context, commandLine);
                    break;
            }
        } catch (Exception e) {
            throw new TelqosException(e);
        }
    }

    private void handleListServices(
            Context context,
            // 为了代码的可扩展性，此处不做简化
            @SuppressWarnings("unused") CommandLine commandLine
    ) throws Exception {
        // 调用服务，获取所有处理器的名称。
        List<String> handlerNames = datamarkQosService.listHandlerNames();

        // 输出结果。
        context.sendMessage("可用的处理器名称: ");
        if (handlerNames.isEmpty()) {
            context.sendMessage("  (Empty)");
        } else {
            for (int i = 0; i < handlerNames.size(); i++) {
                String handlerName = handlerNames.get(i);
                context.sendMessage(String.format("  %3d: %s", i + 1, handlerName));
            }
        }
    }

    private void handleUpdateAllowed(Context context, CommandLine commandLine) throws Exception {
        // 交互标记。
        boolean interactiveFlag = false;

        // 确定 handlerName。
        String handlerName = null;
        // 如果有 COMMAND_OPTION_HANDLER_NAME 选项，则直接获取 handlerName。
        if (commandLine.hasOption(COMMAND_OPTION_HANDLER_NAME)) {
            handlerName = commandLine.getOptionValue(COMMAND_OPTION_HANDLER_NAME);
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

    private void handleGet(Context context, CommandLine commandLine) throws Exception {
        // 交互标记。
        boolean interactiveFlag = false;

        // 确定 handlerName。
        String handlerName = null;
        // 如果有 COMMAND_OPTION_HANDLER_NAME 选项，则直接获取 handlerName。
        if (commandLine.hasOption(COMMAND_OPTION_HANDLER_NAME)) {
            handlerName = commandLine.getOptionValue(COMMAND_OPTION_HANDLER_NAME);
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

    private void handleRefresh(Context context, CommandLine commandLine) throws Exception {
        // 交互标记。
        boolean interactiveFlag = false;

        // 确定 handlerName。
        String handlerName = null;
        // 如果有 COMMAND_OPTION_HANDLER_NAME 选项，则直接获取 handlerName。
        if (commandLine.hasOption(COMMAND_OPTION_HANDLER_NAME)) {
            handlerName = commandLine.getOptionValue(COMMAND_OPTION_HANDLER_NAME);
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

    private void handleUpdate(Context context, CommandLine commandLine) throws Exception {
        // 交互标记。
        boolean interactiveFlag = false;

        // 确定 handlerName。
        String handlerName = null;
        // 如果有 COMMAND_OPTION_HANDLER_NAME 选项，则直接获取 handlerName。
        if (commandLine.hasOption(COMMAND_OPTION_HANDLER_NAME)) {
            handlerName = commandLine.getOptionValue(COMMAND_OPTION_HANDLER_NAME);
        }
        // 如果 handlerName 为 null，则使用交互式输入获取。
        if (Objects.isNull(handlerName)) {
            handlerName = interactiveGetHandlerName(context);
            interactiveFlag = true;
        }

        // 确定 datamark。
        String datamark = null;
        // 如果有 COMMAND_OPTION_DATAMARK_VALUE 选项，则直接获取 datamark。
        if (commandLine.hasOption(COMMAND_OPTION_DATAMARK_VALUE)) {
            datamark = StringUtils.trim((String) commandLine.getParsedOptionValue(COMMAND_OPTION_DATAMARK_VALUE));
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

    private String interactiveGetHandlerName(Context context) throws Exception {
        context.sendMessage("请输入数据标记处理器的名称:");
        return context.receiveMessage();
    }

    private String interactiveGetDatamarkValue(Context context) throws Exception {
        context.sendMessage("请输入新的数据标记值:");
        return context.receiveMessage();
    }
}
