package cotuba.application;

import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;
import cotuba.epub.GeradorEPUB;
import cotuba.epub.GeradorEPUBImpl;
import cotuba.md.RenderizadorMDParaHTML;
import cotuba.md.RenderizadorMDParaHTMLImpl;
import cotuba.pdf.GeradorPDF;
import cotuba.pdf.GeradorPDFImpl;

import java.nio.file.Path;
import java.util.List;

public class Cotuba {

  public void executa(final String formato, final Path diretorioDosMD, final Path arquivoDeSaida) {
    final RenderizadorMDParaHTML renderizador = new RenderizadorMDParaHTMLImpl();

    final List<Capitulo> capitulos = renderizador.renderiza(diretorioDosMD);

    final Ebook ebook = new Ebook();
    ebook.setFormato(formato);
    ebook.setArquivoDeSaida(arquivoDeSaida);
    ebook.setCapitulos(capitulos);

    if ("pdf".equals(formato)) {
      final GeradorPDF geradorPDF = new GeradorPDFImpl();

      geradorPDF.gera(ebook);
    } else if ("epub".equals(formato)) {
      final GeradorEPUB geradorEPUB = new GeradorEPUBImpl();

      geradorEPUB.gera(ebook);
    } else {
      throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
    }
  }

}
