package com.dwarfeng.datamark.api.integration.springtelqos;

import com.dwarfeng.datamark.base.sdk.i18n.MessageContext;
import com.dwarfeng.datamark.stack.service.DatamarkQosService;
import com.dwarfeng.springtelqos.stack.command.CommandDescriptor;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Datamark Telqos 指令测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkCommandTest {

    @Test
    public void localizesDescriptionAndOptionsWithoutChangingProtocolIds() throws Exception {
        TestDatamarkCommand command = new TestDatamarkCommand(new StubDatamarkQosService());

        assertEquals("Datamark service", command.description(Locale.ENGLISH));
        assertEquals("数据标记服务", command.description(Locale.SIMPLIFIED_CHINESE));

        List<Option> options = command.options(Locale.SIMPLIFIED_CHINESE);
        assertEquals(7, options.size());
        assertEquals("lh", options.getFirst().getOpt());
        assertEquals("list-handlers", options.getFirst().getLongOpt());
        assertEquals("列出所有可用的数据标记处理器", options.getFirst().getDescription());
    }

    @Test
    public void localizesSyntaxPlaceholders() throws Exception {
        TestDatamarkCommand command = new TestDatamarkCommand(new StubDatamarkQosService());

        String syntax = command.syntax(Locale.SIMPLIFIED_CHINESE);

        assertTrue(syntax.contains("datamark"));
        assertTrue(syntax.contains("处理器名称"));
        assertTrue(syntax.contains("数据标记值"));
    }

    private static final class TestDatamarkCommand extends DatamarkCommand {

        private TestDatamarkCommand(DatamarkQosService datamarkQosService) {
            super(datamarkQosService);
        }

        private String description(Locale locale) throws Exception {
            return MessageContext.call(
                    locale, () -> provideDescriptionProvider().provideDescription(null)
            );
        }

        @SuppressWarnings("SameParameterValue")
        private List<Option> options(Locale locale) {
            return MessageContext.call(locale, this::provideOptions);
        }

        @SuppressWarnings("SameParameterValue")
        private String syntax(Locale locale) throws Exception {
            CommandDescriptor.Context context = new CommandDescriptor.Context() {
                @Override
                public String getIdentity() {
                    return "datamark";
                }

                @Override
                public String getRuntimeIdentity() {
                    return "datamark";
                }
            };
            return MessageContext.call(
                    locale, () -> provideCliSyntaxProvider().provideCliSyntax(context)
            );
        }
    }

    private static final class StubDatamarkQosService implements DatamarkQosService {

        @Override
        public List<String> listHandlerNames() {
            return List.of();
        }

        @Override
        public boolean updateAllowed(String handlerName) {
            return false;
        }

        @Override
        public String get(String handlerName) {
            return "";
        }

        @Override
        public String refresh(String handlerName) {
            return "";
        }

        @Override
        public String update(String handlerName, String datamarkValue) {
            return datamarkValue;
        }
    }
}
