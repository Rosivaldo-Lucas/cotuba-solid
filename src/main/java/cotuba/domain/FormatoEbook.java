package cotuba.domain;

import cotuba.application.GeradorEbook;
import cotuba.epub.GeradorEPUBImpl;
import cotuba.pdf.GeradorPDFImpl;

public enum FormatoEbook {

  PDF(new GeradorPDFImpl()),
  EPUB(new GeradorEPUBImpl());

  private final GeradorEbook geradorEbook;

  FormatoEbook(final GeradorEbook geradorEbook) {
    this.geradorEbook = geradorEbook;
  }

  public GeradorEbook getGeradorEbook() {
    return this.geradorEbook;
  }

}
