package cotuba.application;

import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
public class Cotuba {

  private final GeradorPDF geradorPDF;
  private final GeradorEPUB geradorEPUB;
  private final RenderizadorMDParaHTML renderizadorMDParaHTML;

  public Cotuba(final GeradorPDF geradorPDF, final GeradorEPUB geradorEPUB, final RenderizadorMDParaHTML renderizadorMDParaHTML) {
    this.geradorPDF = geradorPDF;
    this.geradorEPUB = geradorEPUB;
    this.renderizadorMDParaHTML = renderizadorMDParaHTML;
  }

  public void executa(final ParametrosCotuba parametrosCotuba) {
    final String formato = parametrosCotuba.obtemFormato();
    final Path diretorioDosMD = parametrosCotuba.obtemDiretorioDosMD();
    final Path arquivoDeSaida = parametrosCotuba.obtemArquivoDeSaida();

    final List<Capitulo> capitulos = this.renderizadorMDParaHTML.renderiza(diretorioDosMD);

    final Ebook ebook = new Ebook();
    ebook.setFormato(formato);
    ebook.setArquivoDeSaida(arquivoDeSaida);
    ebook.setCapitulos(capitulos);

    if ("pdf".equals(formato)) {
      this.geradorPDF.gera(ebook);
    } else if ("epub".equals(formato)) {
      this.geradorEPUB.gera(ebook);
    } else {
      throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
    }
  }

}
