package cotuba.cli;

import cotuba.application.Cotuba;

import java.nio.file.Path;

public class Main {

  public static void main(String[] args) {

    final LeitorOpcoesCLI leitorOpcoesCLI = new LeitorOpcoesCLI(args);

    Path diretorioDosMD = leitorOpcoesCLI.getDiretorioDosMD();
    String formato = leitorOpcoesCLI.getFormato();
    Path arquivoDeSaida = leitorOpcoesCLI.getArquivoDeSaida();
    boolean modoVerboso = leitorOpcoesCLI.getModoVerboso();

    try {
      final Cotuba cotuba = new Cotuba();

      cotuba.executa(formato, diretorioDosMD, arquivoDeSaida);

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
