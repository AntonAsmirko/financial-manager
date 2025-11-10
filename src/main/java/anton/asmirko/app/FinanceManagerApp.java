package anton.asmirko.app;

import anton.asmirko.app.cli.CLIReader;
import anton.asmirko.app.model.commands.CLICommand;
import anton.asmirko.app.model.commands.QuitCommand;
import anton.asmirko.app.service.StateService;
import anton.asmirko.app.state.app.AppFsm;
import java.nio.file.Path;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinanceManagerApp {

  private final Scanner scanner = new Scanner(System.in);
  private final AppFsm appFsm;
  private final CLIReader cliReader;
  private final StateService stateService;

  public void run(final String[] args) {
    if (args.length != 0) {
      try {
        final Path state = Path.of(args[0]);
        stateService.loadState(state);
      } catch (Exception e) {
        System.err.println(e.getMessage());
        return;
      }
    }
    CLICommand cliCommand = null;
    do {
      try {
        cliCommand = cliReader.readArgs(getRawCommand());
        appFsm.handleCommand(cliCommand);
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    } while (!(cliCommand instanceof QuitCommand));
  }

  private String[] getRawCommand() {
    final String line = scanner.nextLine();
    return line.split("\\s+");
  }
}
