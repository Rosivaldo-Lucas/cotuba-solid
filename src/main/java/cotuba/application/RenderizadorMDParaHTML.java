package cotuba.application;

import cotuba.domain.Capitulo;
import cotuba.md.RenderizadorMDParaHTMLImpl;

import java.nio.file.Path;
import java.util.List;

public interface RenderizadorMDParaHTML {

  List<Capitulo> renderiza(final Path diretorioDosMD);

  static RenderizadorMDParaHTML cria() {
    return new RenderizadorMDParaHTMLImpl();
  }

}
