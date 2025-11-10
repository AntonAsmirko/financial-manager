package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.HelpCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.state.app.AppState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(HelpCommand.STR_REP)
@RequiredArgsConstructor
public class HelpProcessingAlg implements CLICommandProcessingAlg<HelpCommand> {

  private final AppState appState;

  @Override
  public FsmState process(HelpCommand cliCommand) {
    System.out.println(
        """
                        FinanceManager CLI — консольный менеджер личных финансов
                        ------------------------------------------------------------

                        Использование:
                          java -jar finance-manager.jar <команда> [опции] [аргументы]

                        Доступные команды:

                          signup [-n <имя>] <логин> <пароль>
                              Зарегистрировать нового пользователя.
                              Пример:
                                signup anton 123
                                signup -n Антон anton 123

                          login <логин> <пароль>
                              Авторизоваться под существующим пользователем.
                              Пример:
                                login anton 123

                          logout
                              Выйти из текущего аккаунта.

                          transaction [-c <категория>] [-t <время>] <сумма> <источник>
                              Добавить транзакцию (расход или доход).
                              Если сумма отрицательная — расход, положительная — доход.
                              Примеры:
                                transaction -c еда -30000 РосАл
                                transaction -c образование -5000 Университет
                                transaction -c ЗП 10000 Работа

                          transaction-list [опции]
                              Показать список транзакций.
                              Поддерживаемые фильтры:
                                -incoming         только поступления
                                -outcoming        только расходы
                                -c <категории>    через запятую (например: -c ЗП,еда)
                                -from <время>     начиная с указанной даты (yyyy-MM-dd'T'HH:mm)
                                -to <время>       до указанной даты (yyyy-MM-dd'T'HH:mm)
                              Примеры:
                                transaction-list
                                transaction-list -incoming
                                transaction-list -outcoming
                                transaction-list -c еда
                                transaction-list -c ЗП,еда
                                transaction-list -from 2025-12-01T00:00 -to 2025-12-10T23:59

                          limit [-n <название>] -c <категория> [-from <время>] [-to <время>] <сумма>
                              Создать новый лимит по категории расходов.
                              Примеры:
                                limit -n мой-первый-лимит -c еда 40000
                                limit -n мой-второй-лимит -c образование -from 2025-10-09T22:04 -to 2025-12-09T22:04 4000

                          limit-list [опции]
                              Показать список лимитов (можно фильтровать по дате и категории).
                              Пример:
                                limit-list
                                limit-list -c еда

                          stats [опции]
                              Вывести статистику по операциям и лимитам.
                              Поддерживает те же фильтры, что transaction-list:
                                -c <категории>
                                -from <время>
                                -to <время>
                              Примеры:
                                stats
                                stats -c еда
                                stats -from 2025-12-08T01:15

                          help
                              Показать эту справку.

                          \\q
                              Выйти из приложения.

                        ------------------------------------------------------------
                        Примеры сеанса работы:

                          signup anton 123
                          login anton 123
                          transaction -c еда -30000 РосАл
                          transaction -c образование -5000 Университет
                          transaction -c ЗП 10000 Работа
                          limit -n мой-первый-лимит -c еда 40000
                          stats
                          \\q
                        ------------------------------------------------------------
                        """);
    return appState.getFsmState();
  }
}
