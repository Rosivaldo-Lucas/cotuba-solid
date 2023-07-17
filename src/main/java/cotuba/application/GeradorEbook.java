package cotuba.application;

import cotuba.domain.Ebook;
import cotuba.epub.GeradorEPUBImpl;
import cotuba.pdf.GeradorPDFImpl;

public interface GeradorEbook {

  void gera(final Ebook ebook);

  static GeradorEbook cria(final String formato) {
    GeradorEbook geradorEbook;

    if ("pdf".equals(formato)) {
      geradorEbook = new GeradorPDFImpl();
    } else if ("epub".equals(formato)) {
      geradorEbook = new GeradorEPUBImpl();
    } else {
      throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
    }

    return geradorEbook;
  }

}
