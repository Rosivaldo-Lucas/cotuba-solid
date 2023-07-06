package cotuba.application;

import cotuba.domain.Ebook;
import cotuba.epub.GeradorEPUBImpl;

public interface GeradorEPUB {

  void gera(final Ebook ebook);

  static GeradorEPUB cria() {
    return new GeradorEPUBImpl();
  }

}
