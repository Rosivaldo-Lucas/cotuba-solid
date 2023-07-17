package cotuba.cli;

import cotuba.CotubaConfig;
import cotuba.application.Cotuba;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.file.Path;

public class Main {

  public static void main(String[] args) {

    final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(CotubaConfig.class);
    final Cotuba cotuba = applicationContext.getBean(Cotuba.class);

    final LeitorOpcoesCLI leitorOpcoesCLI = new LeitorOpcoesCLI(args);

    final Path arquivoDeSaida = leitorOpcoesCLI.obtemArquivoDeSaida();
    final boolean modoVerboso = leitorOpcoesCLI.obtemModoVerboso();

    try {
      cotuba.executa(leitorOpcoesCLI);

      System.out.println("Arquivo gerado com sucesso: " + arquivoDeSaida);
    } catch (final Exception ex) {
      System.err.println(ex.getMessage());

      if (modoVerboso) {
        ex.printStackTrace();
      }

      System.exit(1);
    }
  }

}
