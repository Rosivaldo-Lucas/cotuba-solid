package cotuba.application;

import cotuba.domain.Ebook;
import cotuba.epub.GeradorEPUBImpl;
import cotuba.pdf.GeradorPDFImpl;

import java.util.HashMap;
import java.util.Map;

public interface GeradorEbook {

  Map<String, GeradorEbook> GERADORES = new HashMap<>() {{
    put("pdf", new GeradorPDFImpl());
    put("epub", new GeradorEPUBImpl());
  }};

  void gera(final Ebook ebook);

  static GeradorEbook cria(final String formato) {
    final GeradorEbook geradorEbook = GERADORES.get(formato);

    if (geradorEbook == null) {
      throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
    }

    return geradorEbook;
  }

}
