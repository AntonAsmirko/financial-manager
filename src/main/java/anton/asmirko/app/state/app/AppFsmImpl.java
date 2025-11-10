package anton.asmirko.app.state;

import anton.asmirko.app.model.commands.CLICommand;
import anton.asmirko.app.model.commands.LoginCommand;
import anton.asmirko.app.model.fsm.AuthState;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.fsm.WorkingState;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.service.UserService;
import anton.asmirko.app.state.algorithms.CLICommandProcessingAlg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppFsmImpl implements AppFsm {

    private FsmState fsmState = AuthState.getInstance();
    private final Map<String, CLICommandProcessingAlg<?>> algorithms;


    @Override
    @SuppressWarnings("unchecked")
    public <T extends CLICommand> void handleCommand(T cliCommand) {
        if (!fsmState.acceptsCommand(cliCommand)) {
            throw new IllegalStateException(String.format("Команда %s не допустима в состоянии %s", cliCommand.getStrRep(), fsmState));
        }
        var alg = (CLICommandProcessingAlg<CLICommand>) algorithms.get(cliCommand.getStrRep());
        fsmState = Optional.ofNullable(alg)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        String.format("Команда %s не поддердивается", cliCommand.getStrRep())))
                .process(cliCommand);
    }
}
