package anton.asmirko;

import anton.asmirko.app.FinanceManagerApp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
  public static void main(String[] args) {
    final ApplicationContext context = new AnnotationConfigApplicationContext("anton.asmirko.app");
    final FinanceManagerApp financeManagerApp = context.getBean(FinanceManagerApp.class);
    financeManagerApp.run(args);
  }
}
