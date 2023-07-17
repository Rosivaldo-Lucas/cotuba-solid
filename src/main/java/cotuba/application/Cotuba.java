package cotuba.application;

import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;
import cotuba.md.RenderizadorMDParaHTMLImpl;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
public class Cotuba {

  public void executa(final ParametrosCotuba parametrosCotuba) {
    final String formato = parametrosCotuba.obtemFormato();
    final Path diretorioDosMD = parametrosCotuba.obtemDiretorioDosMD();
    final Path arquivoDeSaida = parametrosCotuba.obtemArquivoDeSaida();

    final RenderizadorMDParaHTMLImpl renderizadorMDParaHTML = new RenderizadorMDParaHTMLImpl();

    final List<Capitulo> capitulos = renderizadorMDParaHTML.renderiza(diretorioDosMD);

    final Ebook ebook = new Ebook();
    ebook.setFormato(formato);
    ebook.setArquivoDeSaida(arquivoDeSaida);
    ebook.setCapitulos(capitulos);

    final GeradorEbook geradorEbook = GeradorEbook.cria(formato);

    if ("pdf".equals(formato)) {
      geradorEbook.gera(ebook);
    } else if ("epub".equals(formato)) {
      geradorEbook.gera(ebook);
    } else {
      throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
    }
  }

}
