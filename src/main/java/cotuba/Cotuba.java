package cotuba;

import java.nio.file.Path;
import java.util.List;

public class Cotuba {

  public void executa(final String formato, final Path diretorioDosMD, final Path arquivoDeSaida) {
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
  }

}
