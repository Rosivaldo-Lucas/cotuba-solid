package cotuba.application;

import cotuba.domain.Ebook;
import cotuba.pdf.GeradorPDFImpl;

public interface GeradorPDF {

  void gera(final Ebook ebook);

  static GeradorPDF cria() {
    return new GeradorPDFImpl();
  }

}
