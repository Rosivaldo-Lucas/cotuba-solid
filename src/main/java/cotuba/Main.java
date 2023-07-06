package cotuba;

import java.nio.file.Path;
import java.util.List;

public class Main {

  public static void main(String[] args) {

    final LeitorOpcoesCLI leitorOpcoesCLI = new LeitorOpcoesCLI(args);

    Path diretorioDosMD = leitorOpcoesCLI.getDiretorioDosMD();
    String formato = leitorOpcoesCLI.getFormato();
    Path arquivoDeSaida = leitorOpcoesCLI.getArquivoDeSaida();
    boolean modoVerboso = leitorOpcoesCLI.getModoVerboso();

    try {
      final RenderizadorMDParaHTML renderizador = new RenderizadorMDParaHTML();

      final List<Capitulo> capitulos = renderizador.renderiza(diretorioDosMD);

      final Ebook ebook = new Ebook();
      ebook.setFormato(formato);
      ebook.setArquivoDeSaida(arquivoDeSaida);
      ebook.setCapitulos(capitulos);

      if ("pdf".equals(formato)) {
        final GeradorPDF geradorPDF = new GeradorPDF();

        geradorPDF.gera(ebook);
      } else if ("epub".equals(formato)) {
        final GeradorEPUB geradorEPUB = new GeradorEPUB();

        geradorEPUB.gera(ebook);
      } else {
        throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
      }

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
