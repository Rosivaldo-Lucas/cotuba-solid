package cotuba;

import java.nio.file.Path;

public class Main {

  public static void main(String[] args) {

    final LeitorOpcoesCLI leitorOpcoesCLI = new LeitorOpcoesCLI(args);

    Path diretorioDosMD = leitorOpcoesCLI.getDiretorioDosMD();
    String formato = leitorOpcoesCLI.getFormato();
    Path arquivoDeSaida = leitorOpcoesCLI.getArquivoDeSaida();
    boolean modoVerboso = leitorOpcoesCLI.getModoVerboso();

    try {
      if ("pdf".equals(formato)) {
        final GeradorPDF geradorPDF = new GeradorPDF();

        geradorPDF.gera(diretorioDosMD, arquivoDeSaida);
      } else if ("epub".equals(formato)) {
        final GeradorEPUB geradorEPUB = new GeradorEPUB();

        geradorEPUB.gera(diretorioDosMD, arquivoDeSaida);
      } else {
        throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
      }

      System.out.println("Arquivo gerado com sucesso: " + arquivoDeSaida);

    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      if (modoVerboso) {
        ex.printStackTrace();
      }
      System.exit(1);
    }
  }

}
