package cotuba.application;

import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;

import java.nio.file.Path;
import java.util.List;

public class Cotuba {

  public void executa(final ParametrosCotuba parametrosCotuba) {
    final String formato = parametrosCotuba.obtemFormato();
    final Path diretorioDosMD = parametrosCotuba.obtemDiretorioDosMD();
    final Path arquivoDeSaida = parametrosCotuba.obtemArquivoDeSaida();

    final RenderizadorMDParaHTML renderizador = RenderizadorMDParaHTML.cria();

    final List<Capitulo> capitulos = renderizador.renderiza(diretorioDosMD);

    final Ebook ebook = new Ebook();
    ebook.setFormato(formato);
    ebook.setArquivoDeSaida(arquivoDeSaida);
    ebook.setCapitulos(capitulos);

    if ("pdf".equals(formato)) {
      final GeradorPDF geradorPDF = GeradorPDF.cria();

      geradorPDF.gera(ebook);
    } else if ("epub".equals(formato)) {
      final GeradorEPUB geradorEPUB = GeradorEPUB.cria();

      geradorEPUB.gera(ebook);
    } else {
      throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
    }
  }

}
