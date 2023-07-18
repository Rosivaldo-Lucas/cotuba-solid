package cotuba.epub;

import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GeradorEPUB {

  public void gera(final Ebook ebook) {
    final Path arquivoDeSaida = ebook.getArquivoDeSaida();

    final Book epub = new Book();

    for (final Capitulo capitulo : ebook.getCapitulos()) {
      final String html = capitulo.getConteudoHTML();
      final String tituloDoCapitulo = capitulo.getTitulo();

      epub.addSection(tituloDoCapitulo, new Resource(html.getBytes(), MediatypeService.XHTML));
    }

    final EpubWriter epubWriter = new EpubWriter();

    try {
      epubWriter.write(epub, Files.newOutputStream(arquivoDeSaida));
    } catch (final IOException ex) {
      throw new IllegalStateException("Erro ao criar arquivo EPUB: " + arquivoDeSaida.toAbsolutePath(), ex);
    }
  }

}
