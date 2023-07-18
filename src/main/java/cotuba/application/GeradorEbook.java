package cotuba.application;

import cotuba.domain.Ebook;
import cotuba.domain.FormatoEbook;

public interface GeradorEbook {

  void gera(final Ebook ebook);

  static GeradorEbook cria(final FormatoEbook formatoEbook) {
    return formatoEbook.getGeradorEbook();
  }

}
